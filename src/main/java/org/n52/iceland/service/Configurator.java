/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.service;


import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.n52.iceland.cache.ContentCache;
import org.n52.iceland.cache.ContentCacheController;
import org.n52.iceland.ds.ConnectionProvider;
import org.n52.iceland.ds.DataConnectionProvider;
import org.n52.iceland.ds.FeatureConnectionProvider;
import org.n52.iceland.ds.FeatureQueryHandler;
import org.n52.iceland.ds.HibernateDatasourceConstants;
import org.n52.iceland.event.ServiceEventBus;
import org.n52.iceland.event.events.ConfiguratorInitializedEvent;
import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.ogc.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsServiceIdentification;
import org.n52.iceland.ogc.ows.OwsServiceProvider;
import org.n52.iceland.ogc.ows.ServiceIdentificationFactory;
import org.n52.iceland.ogc.ows.ServiceProviderFactory;
import org.n52.iceland.util.LocalizedProducer;
import org.n52.iceland.util.Producer;

import com.google.common.collect.Sets;

/**
 * Singleton class reads the configFile and builds the RequestOperator and DAO;
 * configures the logger.
 *
 * @since 4.0.0
 */
@Deprecated
public class Configurator implements Constructable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Configurator.class);

    /**
     * instance attribute, due to the singleton pattern.
     */
    private static Configurator instance = null;
    @Inject
    private ServletContext servletContext;

    /**
     * base path for configuration files.
     */
    @Deprecated
    private String basepath;
    private Properties dataConnectionProviderProperties;
    @Inject
    @Deprecated
    private FeatureQueryHandler featureQueryHandler;
    @Deprecated

    private ConnectionProvider dataConnectionProvider;
    @Deprecated
    private ConnectionProvider featureConnectionProvider;
    @Inject
    @Deprecated
    private ContentCacheController contentCacheController;
    @Deprecated
    @Inject
    private ServiceIdentificationFactory serviceIdentificationFactory;
    @Deprecated
    @Inject
    private ServiceProviderFactory serviceProviderFactory;
    private Set<String> providedJdbcDrivers = Sets.newHashSet();
    @Deprecated
    private String connectionProviderIdentificator;
    @Deprecated
    private String datasourceDaoIdentificator;

    public Configurator() {
        // ugly hack for singleton access
        Configurator.instance = this;
    }

    @Autowired(required = false)
    public void setFeatureConnectionProvider(FeatureConnectionProvider featureConnectionProvider) {
        this.featureConnectionProvider = featureConnectionProvider;
    }

    @Inject
    public void setDataConnectionProvider(DataConnectionProvider dataConnectionProvider) {
        this.dataConnectionProvider = dataConnectionProvider;
    }

    @Override
    public void init() throws ConfigurationException {
        this.basepath = this.servletContext.getRealPath("/");
        checkForProvidedJdbc();
        if (featureConnectionProvider == null) {
            featureConnectionProvider = dataConnectionProvider;
        }
        ServiceEventBus.fire(new ConfiguratorInitializedEvent());
    }

    /**
     * @return Returns the service identification
     *         <p/>
     * @throws OwsExceptionReport
     */
    @Deprecated
    public OwsServiceIdentification getServiceIdentification() throws OwsExceptionReport {
        return get(serviceIdentificationFactory);
    }

    /**
     * @return Returns the service identification for the specific language
     *         <p/>
     * @throws OwsExceptionReport
     */
    @Deprecated
    public OwsServiceIdentification getServiceIdentification(Locale lanugage) throws OwsExceptionReport {
        return get(serviceIdentificationFactory, lanugage);
    }

    @Deprecated
    public ServiceIdentificationFactory getServiceIdentificationFactory() throws OwsExceptionReport {
        return serviceIdentificationFactory;
    }

    /**
     * @return Returns the service provider
     *         <p/>
     * @throws OwsExceptionReport
     */
    @Deprecated
    public OwsServiceProvider getServiceProvider() throws OwsExceptionReport {
        return get(serviceProviderFactory);
    }

    /**
     * @return the base path for configuration files
     */
    @Deprecated
    public String getBasePath() {
        return basepath;
    }

    /**
     * @return the current contentCacheController
     */
    @Deprecated
    public ContentCache getCache() {
        return getCacheController().getCache();
    }

    /**
     * @return the current contentCacheController
     */
    @Deprecated
    public ContentCacheController getCacheController() {
        return contentCacheController;
    }

    /**
     * @return the implemented data connection provider
     */
    @Deprecated
    public ConnectionProvider getDataConnectionProvider() {
        return dataConnectionProvider;
    }

    /**
     * @return the implemented feature connection provider
     */
    @Deprecated
    public ConnectionProvider getFeatureConnectionProvider() {
        return featureConnectionProvider;
    }

    /**
     * @return the implemented feature query handler
     */
    @Deprecated
    public FeatureQueryHandler getFeatureQueryHandler() {
        return featureQueryHandler;
    }

    public void addProvidedJdbcDriver(String providedJdbcDriver) {
        this.providedJdbcDrivers.add(providedJdbcDriver);
    }

    public Set<String> getProvidedJdbcDriver() {
        return this.providedJdbcDrivers;
    }

    /**
     * Check method if JDBC driver is provided.
     */
    private void checkForProvidedJdbc() {
        String key = HibernateDatasourceConstants.PROVIDED_JDBC;
        if (!dataConnectionProviderProperties.containsKey(key)
                || (dataConnectionProviderProperties.containsKey(key) &&
                    dataConnectionProviderProperties.getProperty(key).equals("true"))) {
            addProvidedJdbcDriver(dataConnectionProviderProperties
                    .getProperty(HibernateDatasourceConstants.HIBERNATE_DRIVER_CLASS));
        }
    }

    /**
     * @return the connectionProviderIdentificator
     */
    @Deprecated
    public String getConnectionProviderIdentificator() {
        return connectionProviderIdentificator;
    }

    /**
     * @return the datasourceDaoIdentificator
     */
    @Deprecated
    public String getDatasourceDaoIdentificator() {
        return datasourceDaoIdentificator;
    }

    /**
     * @return Returns the instance of the Configurator. <tt>null</tt> will be
     *         returned if the parameterized
     *         {@link #createInstance(Properties, String)} method was not
     *         invoked before. Usually this will be done in the SOS.
     *         <p/>
     * @see Configurator#createInstance(Properties, String)
     */
    @Deprecated
    public static Configurator getInstance() {
        return instance;
    }

    /**
     * @param connectionProviderConfig
     * @param basepath
     * @return Returns an instance of the SosConfigurator. This method is used
     *         to implement the singelton pattern
     *
     * @throws ConfigurationException
     *             if the initialization failed
     */
    @Deprecated
    public static Configurator createInstance( Properties connectionProviderConfig, String basepath) {
        return getInstance();
    }

    private static <T> T get(Producer<T> factory) throws OwsExceptionReport {
        try {
            return factory.get();
        } catch (Exception e) {
            if (e instanceof OwsExceptionReport) {
                throw (OwsExceptionReport) e;
            } else if (e.getCause() instanceof OwsExceptionReport) {
                throw (OwsExceptionReport) e.getCause();
            } else {
                throw new NoApplicableCodeException()
                        .causedBy(e).withMessage("Could not request object from %s", factory);
            }
        }
    }

    private static <T> T get(LocalizedProducer<T> factory, Locale language)
            throws OwsExceptionReport {
        try {
            return factory.get(language);
        } catch (Exception e) {
            if (e instanceof OwsExceptionReport) {
                throw (OwsExceptionReport) e;
            } else if (e.getCause() instanceof OwsExceptionReport) {
                throw (OwsExceptionReport) e.getCause();
            } else {
                throw new NoApplicableCodeException()
                        .causedBy(e).withMessage("Could not request object from %s", factory);
            }
        }
    }
}
