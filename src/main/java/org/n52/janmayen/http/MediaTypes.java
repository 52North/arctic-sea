/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.janmayen.http;

import com.google.common.collect.ImmutableSetMultimap;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public final class MediaTypes {

    @Deprecated
    public static final MediaType WILD_CARD = MediaType.any();
    @Deprecated
    public static final String APPLICATION = MediaType.APPLICATION_TYPE;
    @Deprecated
    public static final String TEXT = MediaType.TEXT_TYPE;
    private static final String SUBTYPE_XML = "xml";
    private static final String SUBTYPE_PLAIN = "plain";
    private static final String SUBTYPE_ZIP = "zip";
    private static final String SUBTYPE_JSON = "json";
    private static final String SUBTYPE_EXI = "exi";
    private static final String SUBTYPE_KVP = "x-kvp";
    private static final String SUBTYPE_SOAP = "soap+xml";
    private static final String SUBTYPE_NETCDF = "netcdf";
    private static final String SUBTYPE_CSV = "csv";
    private static final String SUBTYPE_RDATA = "rData";
    private static final String SUBTYPE_X_RDATA = "x-rData";
    private static final String SUBTYPE_GML = "gml+xml";
    private static final String SUBTYPE_OM = "om+xml";
    private static final String SUBTYPE_HTML = "html";
    private static final String SUBTYPE_GEOTIFF = "geotiff";
    private static final String SUBTYPE_X_GEOTIFF = "x-geotiff";
    private static final String SUBTYPE_X_NETCDF = "x-netcdf";
    private static final String SUBTYPE_X_ZIPPED_SHAPE = "x-zipped-shp";
    private static final String PARAM_VERSION = "version";
    @Deprecated
    public static final String XML = SUBTYPE_XML;


    public static final MediaType APPLICATION_X_GEOTIFF = MediaType.application(SUBTYPE_X_GEOTIFF);
    public static final MediaType APPLICATION_GEOTIFF = MediaType.application(SUBTYPE_GEOTIFF);
    public static final MediaType APPLICATION_X_NETCDF = MediaType.application(SUBTYPE_X_NETCDF);
    public static final MediaType TEXT_HTML = MediaType.text(SUBTYPE_HTML);
    public static final MediaType TEXT_CSV = MediaType.text(SUBTYPE_CSV);
    public static final MediaType TEXT_PLAIN = MediaType.text(SUBTYPE_PLAIN);
    public static final MediaType TEXT_XML = MediaType.text(SUBTYPE_XML);
    public static final MediaType APPLICATION_XML = MediaType.application(SUBTYPE_XML);
    public static final MediaType APPLICATION_ZIP = MediaType.application(SUBTYPE_ZIP);
    public static final MediaType APPLICATION_JSON = MediaType.application(SUBTYPE_JSON);
    public static final MediaType APPLICATION_EXI = MediaType.application(SUBTYPE_EXI);
    public static final MediaType APPLICATION_KVP = MediaType.application(SUBTYPE_KVP);
    public static final MediaType APPLICATION_SOAP_XML = MediaType.application(SUBTYPE_SOAP);
    public static final MediaType APPLICATION_NETCDF = MediaType.application(SUBTYPE_NETCDF);
    public static final MediaType APPLICATION_GML_32 = MediaType.application(SUBTYPE_GML, PARAM_VERSION, "3.2");
    public static final MediaType APPLICATION_OM_20 = MediaType.application(SUBTYPE_OM, PARAM_VERSION, "2.0");
    public static final MediaType APPLICATION_RDATA = MediaType.application(SUBTYPE_RDATA);
    public static final MediaType APPLICATION_X_RDATA = MediaType.application(SUBTYPE_X_RDATA);
    public static final MediaType APPLICATION_X_ZIPPED_SHP = MediaType.application(SUBTYPE_X_ZIPPED_SHAPE);

    public static final ImmutableSetMultimap<MediaType, MediaType> COMPATIBLE_TYPES
            = new ImmutableSetMultimap.Builder<MediaType, MediaType>()
                    .putAll(TEXT_XML, TEXT_XML, APPLICATION_XML)
                    .putAll(APPLICATION_XML, APPLICATION_XML, TEXT_XML)
                    .putAll(APPLICATION_GEOTIFF, APPLICATION_GEOTIFF, APPLICATION_X_GEOTIFF)
                    .putAll(APPLICATION_X_GEOTIFF, APPLICATION_X_GEOTIFF, APPLICATION_GEOTIFF)
                    .putAll(APPLICATION_NETCDF, APPLICATION_NETCDF, APPLICATION_X_NETCDF)
                    .putAll(APPLICATION_X_NETCDF, APPLICATION_X_NETCDF, APPLICATION_NETCDF)
                    .putAll(APPLICATION_RDATA, APPLICATION_RDATA, APPLICATION_X_RDATA)
                    .putAll(APPLICATION_X_RDATA, APPLICATION_X_RDATA, APPLICATION_RDATA)
                    .build();

    private MediaTypes() {
    }
}
