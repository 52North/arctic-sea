/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.swes;

import javax.xml.namespace.QName;

import org.n52.shetland.w3c.SchemaLocation;

/**
 * Constants for OGC SWES 2.0
 *
 * @since 1.0.0
 *
 */
public interface SwesConstants {

    String EN_SOS_INSERTION_METADATA = "SosInsertionMetadata";

    String NS_SWES_20 = "http://www.opengis.net/swes/2.0";

    String NS_SWES_PREFIX = "swes";

    SchemaLocation SWES_20_SCHEMA_LOCATION =
            new SchemaLocation(NS_SWES_20, "http://schemas.opengis.net/swes/2.0/swes.xsd");

    SchemaLocation SWES_20_DESCRIBE_SENSOR_SCHEMA_LOCATION =
            new SchemaLocation(NS_SWES_20, "http://schemas.opengis.net/swes/2.0/swesDescribeSensor.xsd");

    SchemaLocation SWES_20_INSERT_SENSOR_SCHEMA_LOCATION =
            new SchemaLocation(NS_SWES_20, "http://schemas.opengis.net/swes/2.0/swesInsertSensor.xsd");

    SchemaLocation SWES_20_UPDATE_SENSOR_DESCRIPTION_SCHEMA_LOCATION =
            new SchemaLocation(NS_SWES_20, "http://schemas.opengis.net/swes/2.0/swesUpdateSensorDescription.xsd");

    SchemaLocation SWES_20_DELETE_SENSOR_SCHEMA_LOCATION =
            new SchemaLocation(NS_SWES_20, "http://schemas.opengis.net/swes/2.0/swesDeleteSensor.xsd");

    @Deprecated
    String SCHEMA_LOCATION_URL_SWES_20 = SWES_20_SCHEMA_LOCATION.getSchemaFileUrl();

    @Deprecated
    String SCHEMA_LOCATION_URL_SWES_20_DESCRIBE_SENSOR = SWES_20_DESCRIBE_SENSOR_SCHEMA_LOCATION.getSchemaFileUrl();

    @Deprecated
    String SCHEMA_LOCATION_URL_SWES_20_DELETE_SENSOR = SWES_20_DELETE_SENSOR_SCHEMA_LOCATION.getSchemaFileUrl();

    @Deprecated
    String SCHEMA_LOCATION_URL_SWES_20_INSERT_SENSOR = SWES_20_INSERT_SENSOR_SCHEMA_LOCATION.getSchemaFileUrl();

    @Deprecated
    String SCHEMA_LOCATION_URL_SWES_20_UPDATE_SENSOR_DESCRIPTION =
            SWES_20_UPDATE_SENSOR_DESCRIPTION_SCHEMA_LOCATION.getSchemaFileUrl();

    // element names
    String EN_EXTENSION = "extension";

    String EN_ABSTRACT_OFFERING = "AbstractOffering";

    String EN_DELETE_SENSOR_RESPONSE = "DeleteSensorResponse";

    String EN_DESCRIBE_SENSOR = "DescribeSensor";

    String EN_DELETE_SENSOR = "DeleteSensor";

    String EN_DESCRIBE_SENSOR_RESPONSE = "DescribeSensorResponse";

    String EN_INSERT_SENSOR = "InsertSensor";

    String EN_INSERT_SENSOR_RESPONSE = "InsertSensorResponse";

    String EN_INSERTION_METADATA = "InsertionMetadata";

    String EN_METADATA = "metadata";

    String EN_OFFERING = "offering";

    String EN_UPDATE_SENSOR_DESCRIPTION = "UpdateSensorDescription";

    String EN_UPDATE_SENSOR_DESCRIPTION_RESPONSE = "UpdateSensorDescriptionResponse";

    QName QN_INSERTION_METADATA =
            new QName(SwesConstants.NS_SWES_20, SwesConstants.EN_INSERTION_METADATA, SwesConstants.NS_SWES_PREFIX);

    // QNames for elements
    QName QN_ABSTRACT_OFFERING = new QName(NS_SWES_20, EN_ABSTRACT_OFFERING, NS_SWES_PREFIX);

    QName QN_DELETE_SENSOR = new QName(NS_SWES_20, EN_DELETE_SENSOR, NS_SWES_PREFIX);

    QName QN_DELETE_SENSOR_RESPONSE = new QName(NS_SWES_20, EN_DELETE_SENSOR_RESPONSE, NS_SWES_PREFIX);

    QName QN_DESCRIBE_SENSOR = new QName(NS_SWES_20, EN_DESCRIBE_SENSOR, NS_SWES_PREFIX);

    QName QN_DESCRIBE_SENSOR_RESPONSE = new QName(NS_SWES_20, EN_DESCRIBE_SENSOR_RESPONSE, NS_SWES_PREFIX);

    QName QN_INSERT_SENSOR = new QName(NS_SWES_20, EN_INSERT_SENSOR, NS_SWES_PREFIX);

    QName QN_INSERT_SENSOR_RESPONSE = new QName(NS_SWES_20, EN_INSERT_SENSOR_RESPONSE, NS_SWES_PREFIX);

    QName QN_METADATA = new QName(NS_SWES_20, EN_METADATA, NS_SWES_PREFIX);

    QName QN_OFFERING = new QName(NS_SWES_20, EN_OFFERING, NS_SWES_PREFIX);

    QName QN_UPDATE_SENSOR_DESCRIPTION = new QName(NS_SWES_20, EN_UPDATE_SENSOR_DESCRIPTION, NS_SWES_PREFIX);

    QName QN_UPDATE_SENSOR_DESCRIPTION_RESPONSE =
            new QName(NS_SWES_20, EN_UPDATE_SENSOR_DESCRIPTION_RESPONSE, NS_SWES_PREFIX);

    String SOAP_REASON_INVALID_REQUEST = "The request did not conform to its XML Schema definition.";

    // FIXME emtpy constant
    String SOAP_REASON_REQUEST_EXTENSION_NOT_SUPPORTED = "";

}
