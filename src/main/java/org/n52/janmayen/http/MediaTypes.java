/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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

import static org.n52.janmayen.http.MediaType.application;
import static org.n52.janmayen.http.MediaType.text;

import com.google.common.collect.ImmutableSetMultimap;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public interface MediaTypes {

    MediaType APPLICATION_X_GEOTIFF = application("x-geotiff");
    MediaType APPLICATION_GEOTIFF = application("geotiff");
    MediaType APPLICATION_X_NETCDF = application("x-netcdf");
    MediaType TEXT_HTML = text("html");
    MediaType TEXT_CSV = text("csv");
    MediaType TEXT_PLAIN = text("plain");
    MediaType TEXT_XML = text("xml");
    MediaType APPLICATION_XML = application("xml");
    MediaType APPLICATION_ZIP = application("zip");
    MediaType APPLICATION_JSON = application("json");
    MediaType APPLICATION_EXI = application("exi");
    MediaType APPLICATION_KVP = application("x-kvp");
    MediaType APPLICATION_SOAP_XML = application("soap+xml");
    MediaType APPLICATION_NETCDF = application("netcdf");
    MediaType APPLICATION_GML_32 = application("gml+xml", "version", "3.2");
    MediaType APPLICATION_OM_20 = application("om+xml", "version", "2.0");
    MediaType APPLICATION_RDATA = application("rData");
    MediaType APPLICATION_X_RDATA = application("x-rData");
    MediaType APPLICATION_X_ZIPPED_SHP = application("x-zipped-shp");

    ImmutableSetMultimap<MediaType, MediaType> COMPATIBLE_TYPES
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



    @Deprecated String XML = "xml";
    @Deprecated MediaType WILD_CARD = MediaType.any();
    @Deprecated String APPLICATION = MediaType.APPLICATION_TYPE;
    @Deprecated String TEXT = MediaType.TEXT_TYPE;

}
