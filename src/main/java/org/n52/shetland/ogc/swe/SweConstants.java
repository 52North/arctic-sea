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
package org.n52.shetland.ogc.swe;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import org.n52.shetland.w3c.SchemaLocation;

import com.google.common.base.Joiner;

/**
 * Constants interface for OGC SWE 1.0.1 and 2.0
 *
 * @since 1.0.0
 */
public interface SweConstants {

    // namespaces and schema locations
    String NS_SWE_101 = "http://www.opengis.net/swe/1.0.1";

    String NS_SWE_20 = "http://www.opengis.net/swe/2.0";

    String NS_SWE_PREFIX = "swe";

    String SCHEMA_LOCATION_URL_SWE_101 = "http://schemas.opengis.net/sweCommon/1.0.1/swe.xsd";

    String SCHEMA_LOCATION_URL_SWE_20 = "http://schemas.opengis.net/sweCommon/2.0/swe.xsd";

    SchemaLocation SWE_101_SCHEMA_LOCATION = new SchemaLocation(NS_SWE_101, SCHEMA_LOCATION_URL_SWE_101);

    SchemaLocation SWE_20_SCHEMA_LOCATION = new SchemaLocation(NS_SWE_20, SCHEMA_LOCATION_URL_SWE_20);

    String X_AXIS = "x";

    String Y_AXIS = "y";

    String Z_AXIS = "z";

    String ENCODING_TEXT = "http://www.opengis.net/swe/2.0/TextEncoding";

    String EN_BOOLEAN = "Boolean";

    String EN_CATEGORY = "Category";

    String EN_COUNT = "Count";

    String EN_COUNT_RANGE = "CountRange";

    String EN_DATA_ARRAY = "DataArray";

    String EN_DATA_ARRAY_PROPERTY_TYPE = "DataArrayPropertyType";

    String EN_DATA_CHOICE = "DataChoice";

    String EN_DATA_RECORD = "DataRecord";

    String EN_ENVELOPE = "Envelope";

    String EN_FIELD = "field";

    String EN_OBSERVABLE_PROPERTY = "ObservableProperty";

    String EN_POSITION = "Position";

    String EN_QUALITY = "Quality";

    String EN_QUANTITY = "Quantity";

    String EN_QUANTITY_RANGE = "QuantityRange";

    String EN_SIMPLEDATARECORD = "SimpleDataRecord";

    String EN_TEXT = "Text";

    String EN_TEXT_ENCODING = "TextEncoding";

    String EN_ENCODING_TYPE = "encodingType";

    String EN_TIME = "Time";

    String EN_TIME_RANGE = "TimeRange";

    String EN_UOM = "uom";

    String EN_VECTOR = "Vector";

    String EN_VALUE = "value";

    Joiner COLON_JOINER = Joiner.on(":");

    String VT_BOOLEAN = COLON_JOINER.join(NS_SWE_PREFIX, EN_BOOLEAN);

    String VT_CATEGORY = COLON_JOINER.join(NS_SWE_PREFIX, EN_CATEGORY);

    String VT_COUNT = COLON_JOINER.join(NS_SWE_PREFIX, EN_COUNT);

    String VT_COUNT_RANGE = COLON_JOINER.join(NS_SWE_PREFIX, EN_COUNT_RANGE);

    String VT_OBSERVABLE_PROPERTY = COLON_JOINER.join(NS_SWE_PREFIX, EN_OBSERVABLE_PROPERTY);

    String VT_QUANTITY = COLON_JOINER.join(NS_SWE_PREFIX, EN_QUANTITY);

    String VT_QUANTITY_RANGE = COLON_JOINER.join(NS_SWE_PREFIX, EN_QUANTITY_RANGE);

    String VT_TEXT = COLON_JOINER.join(NS_SWE_PREFIX, EN_TEXT);

    String VT_TIME = COLON_JOINER.join(NS_SWE_PREFIX, EN_TIME);

    String VT_TIME_RANGE = COLON_JOINER.join(NS_SWE_PREFIX, EN_TIME_RANGE);

    String VT_DATA_ARRAY = COLON_JOINER.join(NS_SWE_PREFIX, EN_DATA_ARRAY);

    String VT_DATA_RECORD = COLON_JOINER.join(NS_SWE_PREFIX, EN_DATA_RECORD);

    QName QN_BOOLEAN_SWE_101 = new QName(NS_SWE_101, EN_BOOLEAN, NS_SWE_PREFIX);

    QName QN_BOOLEAN_SWE_200 = new QName(NS_SWE_20, EN_BOOLEAN, NS_SWE_PREFIX);

    QName QN_CATEGORY_SWE_101 = new QName(NS_SWE_101, EN_CATEGORY, NS_SWE_PREFIX);

    QName QN_CATEGORY_SWE_200 = new QName(NS_SWE_20, EN_CATEGORY, NS_SWE_PREFIX);

    QName QN_COUNT_SWE_101 = new QName(NS_SWE_101, EN_COUNT, NS_SWE_PREFIX);

    QName QN_COUNT_SWE_200 = new QName(NS_SWE_20, EN_COUNT, NS_SWE_PREFIX);

    QName QN_COUNT_RANGE_SWE_200 = new QName(NS_SWE_20, EN_COUNT_RANGE, NS_SWE_PREFIX);

    QName QN_DATA_ARRAY_SWE_200 = new QName(NS_SWE_20, EN_DATA_ARRAY, NS_SWE_PREFIX);

    QName QN_DATA_RECORD_SWE_200 = new QName(NS_SWE_20, EN_DATA_RECORD, NS_SWE_PREFIX);

    QName QN_DATA_ARRAY_PROPERTY_TYPE_SWE_200 = new QName(NS_SWE_20, EN_DATA_ARRAY_PROPERTY_TYPE, NS_SWE_PREFIX);

    QName QN_FIELD_200 = new QName(NS_SWE_20, EN_FIELD, NS_SWE_PREFIX);

    QName QN_QUANTITY_SWE_101 = new QName(NS_SWE_101, EN_QUANTITY, NS_SWE_PREFIX);

    QName QN_QUANTITY_SWE_200 = new QName(NS_SWE_20, EN_QUANTITY, NS_SWE_PREFIX);

    QName QN_QUANTITY_RANGE_SWE_200 = new QName(NS_SWE_20, EN_QUANTITY_RANGE, NS_SWE_PREFIX);

    QName QN_SIMPLEDATARECORD_SWE_101 = new QName(NS_SWE_101, EN_SIMPLEDATARECORD, NS_SWE_PREFIX);

    QName QN_DATA_RECORD_SWE_101 = new QName(NS_SWE_101, EN_DATA_RECORD, NS_SWE_PREFIX);

    QName QN_TEXT_ENCODING_SWE_101 = new QName(NS_SWE_101, EN_TEXT_ENCODING, NS_SWE_PREFIX);

    QName QN_TEXT_ENCODING_SWE_200 = new QName(NS_SWE_20, EN_TEXT_ENCODING, NS_SWE_PREFIX);

    QName QN_TEXT_SWE_101 = new QName(NS_SWE_101, EN_TEXT, NS_SWE_PREFIX);

    QName QN_TEXT_SWE_200 = new QName(NS_SWE_20, EN_TEXT, NS_SWE_PREFIX);

    QName QN_TIME_RANGE_SWE_101 = new QName(NS_SWE_101, EN_TIME_RANGE, NS_SWE_PREFIX);

    QName QN_TIME_RANGE_SWE_200 = new QName(NS_SWE_20, EN_TIME_RANGE, NS_SWE_PREFIX);

    QName QN_TIME_SWE_101 = new QName(NS_SWE_101, EN_TIME, NS_SWE_PREFIX);

    QName QN_ENVELOPE_SWE_101 = new QName(NS_SWE_101, EN_ENVELOPE, NS_SWE_PREFIX);

    QName QN_TIME_SWE_200 = new QName(NS_SWE_20, EN_TIME, NS_SWE_PREFIX);

    QName QN_UOM_SWE_200 = new QName(NS_SWE_20, EN_UOM, NS_SWE_PREFIX);

    QName QN_VECTOR_SWE_200 = new QName(NS_SWE_20, EN_VECTOR, NS_SWE_PREFIX);

    QName QN_VALUE_SWE_200 = new QName(NS_SWE_20, EN_VALUE, NS_SWE_PREFIX);

    QName QN_DATA_ARRAY_SWE_101 = new QName(NS_SWE_101, EN_DATA_ARRAY, NS_SWE_PREFIX);

    /**
     * Enum for SensorML types
     */
    enum SensorMLType {
        System,
        Component,
        ProcessModel,
        ProcessChain
    }

    /**
     * Enum for SWE aggregate types
     */
    enum SweAggregateType {
        SimpleDataRecord,
        DataRecord
    }

    final class SweCoordinateNames {
        public static final String EASTING = "easting";
        public static final String NORTHING = "northing";
        public static final String ALTITUDE = "altitude";
        public static final String HEIGHT = "height";
        public static final String SOUTHING = "southing";
        public static final String WESTING = "westing";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final String DEPTH = "depth";

        private static final Set<String> X_NAMES = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        private static final Set<String> Y_NAMES = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        private static final Set<String> Z_NAMES = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);


        static {
            Collections.addAll(X_NAMES, EASTING, WESTING, LONGITUDE);
            Collections.addAll(Y_NAMES, NORTHING, SOUTHING, LATITUDE);
            Collections.addAll(Z_NAMES, ALTITUDE, HEIGHT, DEPTH);
        }

        private SweCoordinateNames() {}

        public static boolean isX(String name) {
            return X_NAMES.contains(name);
        }

        public static boolean isY(String name) {
            return Y_NAMES.contains(name);
        }

        public static boolean isZ(String name) {
            return Z_NAMES.contains(name);
        }
    }

    /**
     * Enum for sensor descriptions
     */
    enum SosSensorDescription {
        XmlStringDescription,
        SosDescription
    }

    /**
     * Enum for SWE DataComponent types
     */
    enum SweDataComponentType {
        DataArray,
        DataRecord,
        SimpleDataRecord,
        Envelope,
        Field,
        Vector,
        Position,
        Boolean,
        Category,
        CategoryRange,
        Count,
        CountRange,
        Quantity,
        QuantityRange,
        Text,
        Time,
        TimeRange,
        ObservableProperty
    }
}
