/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.iceland.statistics.impl.geolocation;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Singleton;
import javax.security.auth.Destroyable;

import org.elasticsearch.common.geo.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.shetland.ogc.ows.service.OwsServiceRequestContext;
import org.n52.iceland.statistics.api.StatisticsLocationUtilSettingsKeys;
import org.n52.iceland.statistics.api.interfaces.geolocation.IAdminStatisticsLocation;
import org.n52.iceland.statistics.api.interfaces.geolocation.IStatisticsLocationUtil;
import org.n52.iceland.statistics.api.parameters.ObjectEsParameterFactory;
import org.n52.iceland.statistics.api.utils.FileDownloader;
import org.n52.iceland.statistics.api.utils.GeoLiteFileDownloader;
import org.n52.janmayen.net.IPAddress;
import org.n52.janmayen.lifecycle.Constructable;

import com.maxmind.db.Reader.FileMode;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;

/**
 * Utility class for mapping objects to Elasticsearch specific Geolocation type
 * objects
 *
 */

@Singleton
@Configurable
public class StatisticsLocationUtil implements IStatisticsLocationUtil, IAdminStatisticsLocation, Constructable, Destroyable {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsLocationUtil.class);

    private boolean enabled = false;
    private boolean isAutoDownload;
    private String downloadFolderPath;
    private String cityDbLoc;
    private String countryDbLoc;
    private LocationDatabaseType dbType;
    private DatabaseReader reader;

    public StatisticsLocationUtil() {
    }

    @Override
    public Map<String, Object> ip2SpatialData(IPAddress ip) {
        if (ip == null) {
            return null;
        }

        return ip2SpatialData(ip.asInetAddress());
    }

    @Override
    public Map<String, Object> ip2SpatialData(String host) {
        try {
            return ip2SpatialData(InetAddress.getByName(host));
        } catch (UnknownHostException e) {
            logger.warn("Not a valid IPv4 address", e);
        }
        return null;
    }

    private Map<String, Object> ip2SpatialData(InetAddress ip) {
        if (!enabled) {
            return null;
        }
        if (reader == null) {
            logger.warn("Location database is not initialized. Exiting.");
            return null;
        }
        try {
            Map<String, Object> holder = new HashMap<>();
            if (dbType == LocationDatabaseType.COUNTRY) {
                Country country = reader.country(ip).getCountry();
                holder.put(ObjectEsParameterFactory.GEOLOC_COUNTRY_CODE.getName(), country.getIsoCode());
            } else {
                CityResponse city = reader.city(ip);
                Location loc = city.getLocation();
                holder.put(ObjectEsParameterFactory.GEOLOC_COUNTRY_CODE.getName(), city.getCountry().getIsoCode());
                holder.put(ObjectEsParameterFactory.GEOLOC_CITY_NAME.getName(), city.getCity().getName());
                holder.put(ObjectEsParameterFactory.GEOLOC_GEO_POINT.getName(), new GeoPoint(loc.getLatitude(), loc.getLongitude()));
            }
            return holder;
        } catch (Throwable e) {
            logger.warn("Can't convert IP to GeoIp", e);
        }
        return null;
    }

    /**
     * Resolves source {@link IPAddress} if there were a proxy get the original
     * address
     *
     * @param ctx
     *            holder of the address
     * @return caller source address
     */
    @Override
    public IPAddress resolveOriginalIpAddress(OwsServiceRequestContext ctx) {
        if (ctx == null) {
            return null;
        }
        if (ctx.getForwardedForChain().isPresent()) {
            return ctx.getForwardedForChain().get().getOrigin();
        } else {
            return ctx.getIPAddress().orNull();
        }
    }

    @Override
    public void initDatabase(LocationDatabaseType type, String pathToDatabase) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(pathToDatabase);
        logger.info("Init {} as type {} with file {}", getClass().toString(), type.toString(), pathToDatabase);
        dbType = type;
        try {
            File f = new File(pathToDatabase);
            reader = new DatabaseReader.Builder(f).fileMode(FileMode.MEMORY_MAPPED).build();
            // mismatch
            if (!type.getGeoLite2Name().equals(reader.getMetadata().getDatabaseType())) {
                logger.error("DatabaseType {} not match with the databasefile {}. Exiting.", type.toString(), pathToDatabase);
                destroy();
                return;
            }
        } catch (Throwable e) {
            logger.error("Couldn't initation geolocation database ", e);
            reader = null;
        }
    }

    @Override
    public void destroy() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            logger.error("Error during closing GeoLite reader", e);
        } finally {
            enabled = false;
            reader = null;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Setting(StatisticsLocationUtilSettingsKeys.ENABLED)
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAutoDownload() {
        return isAutoDownload;
    }

    @Setting(StatisticsLocationUtilSettingsKeys.DATABASE_DOWNLOADER)
    public void setAutoDownload(String choice) {
        this.isAutoDownload = choice.equalsIgnoreCase(StatisticsLocationUtilSettingsKeys.DATABASE_DOWNLOADER_AUTO);
    }

    public String getDownloadFolderPath() {
        return downloadFolderPath;
    }

    @Setting(StatisticsLocationUtilSettingsKeys.DOWNLOAD_FOLDERPATH)
    public void setDownloadFolderPath(String downloadFolderPath) {
        // strip end slash
        this.downloadFolderPath = downloadFolderPath;
        if (this.downloadFolderPath != null) {
            while (this.downloadFolderPath.endsWith("/") || this.downloadFolderPath.endsWith("\\")) {
                this.downloadFolderPath = this.downloadFolderPath.substring(0, this.downloadFolderPath.length() + 1);
            }
        }
    }

    public String getCityDbLoc() {
        return cityDbLoc;
    }

    @Setting(StatisticsLocationUtilSettingsKeys.MANUAL_CITY_LOC)
    public void setCityDbLoc(String cityDbLoc) {
        this.cityDbLoc = cityDbLoc;
    }

    public String getCountryDbLoc() {
        return countryDbLoc;
    }

    @Setting(StatisticsLocationUtilSettingsKeys.MANUAL_COUNTRY_LOC)
    public void setCountryDbLoc(String countryDbLoc) {
        this.countryDbLoc = countryDbLoc;
    }

    public LocationDatabaseType getDbType() {
        return dbType;
    }

    @Setting(StatisticsLocationUtilSettingsKeys.DATABASE_TYPE)
    public void setDbType(String dbType) {
        if (dbType.equalsIgnoreCase(StatisticsLocationUtilSettingsKeys.DATABASE_TYPE_CITY)) {
            this.dbType = LocationDatabaseType.CITY;
        } else {
            this.dbType = LocationDatabaseType.COUNTRY;
        }
    }

    @Override
    public void init() {
        logger.info("Statistics Geolocation module is {}", enabled);
        if (enabled) {
            // downloader auto - manual
            if (isAutoDownload) {
                String countryPath = downloadFolderPath + "/" + GeoLiteFileDownloader.COUNTRY_FILE_NAME;
                String cityPath = downloadFolderPath + "/" + GeoLiteFileDownloader.CITY_FILE_NAME;
                if (!FileDownloader.isPathExists(countryPath) || !FileDownloader.isPathExists(cityPath)) {
                    GeoLiteFileDownloader.downloadDefaultDatabases(downloadFolderPath);
                } else {
                    logger.info("GeoLite2 databases on paths {} and {} already exists", countryPath, cityPath);
                }
            }
            // db type
            String pathToDatabase = null;
            if (dbType == LocationDatabaseType.CITY) {
                if (isAutoDownload) {
                    pathToDatabase = downloadFolderPath + "/" + GeoLiteFileDownloader.CITY_FILE_NAME;
                } else {
                    pathToDatabase = cityDbLoc;
                }
            } else {
                if (isAutoDownload) {
                    pathToDatabase = downloadFolderPath + "/" + GeoLiteFileDownloader.COUNTRY_FILE_NAME;
                } else {
                    pathToDatabase = countryDbLoc;
                }
            }
            if (pathToDatabase == null) {
                logger.error("Path to type {} database can't be empty. Check your location database type or the path", dbType.name());
                return;
            }
            initDatabase(dbType, pathToDatabase);
        }
    }

}
