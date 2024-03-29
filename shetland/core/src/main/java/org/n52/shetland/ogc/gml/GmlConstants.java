/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.gml;

import javax.xml.namespace.QName;

import org.n52.shetland.w3c.SchemaLocation;

/**
 * Interface for OGC GML constants
 *
 * @since 1.0.0
 *
 */
public interface GmlConstants {

    /* namespaces and schema locations */
    String NS_GML = "http://www.opengis.net/gml";

    String NS_GML_32 = "http://www.opengis.net/gml/3.2";

    String NS_GML_33_CE = "http://www.opengis.net/gml/3.3/ce";

    String NS_GML_PREFIX = "gml";

    String SCHEMA_LOCATION_URL_GML_311 = "http://schemas.opengis.net/gml/3.1.1/base/gml.xsd";

    String SCHEMA_LOCATION_URL_GML_32 = "http://schemas.opengis.net/gml/3.2.1/gml.xsd";

    String SCHEMA_LOCATION_URL_GML_33_CE = "http://schemas.opengis.net/gml/3.3/geometryCompact.xsd";

    SchemaLocation GML_311_SCHEMAL_LOCATION = new SchemaLocation(NS_GML, SCHEMA_LOCATION_URL_GML_311);

    SchemaLocation GML_32_SCHEMAL_LOCATION = new SchemaLocation(NS_GML_32, SCHEMA_LOCATION_URL_GML_32);

    String AN_ID = "id";

    String GML_ID_WITH_PREFIX = new StringBuilder().append(NS_GML_PREFIX).append(':').append(AN_ID).toString();

    /* element names used in GML */

    String AN_REMOTE_SCHEMA = "remoteSchema";

    String EN_DESCRIPTION = "description";

    String EN_TIME_INSTANT = "TimeInstant";

    String EN_TIME_PERIOD = "TimePeriod";

    String EN_TIME_BEGIN = "beginTime";

    String EN_TIME_END = "endTime";

    String EN_TIME_POSITION = "timePosition";

    String EN_BEGIN_POSITION = "beginPosition";

    String EN_END_POSITION = "endPosition";

    String GML_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    // nil values from GML 3.2 Section 8.2.3.1
    String NIL_INAPPLICABLE = "urn:ogc:def:nil:OGC:inapplicable";

    String NIL_MISSING = "urn:ogc:def:nil:OGC:missing";

    String NIL_TEMPLATE = "urn:ogc:def:nil:OGC:template";

    String NIL_UNKNOWN = "urn:ogc:def:nil:OGC:unknown";

    String NIL_WITHHELD = "urn:ogc:def:nil:OGC:withheld";

    String EN_ENVELOPE = "Envelope";

    String EN_ABSTRACT_TIME_OBJECT = "_TimeObject";

    String EN_ABSTRACT_TIME_OBJECT_32 = "AbstractTimeObject";

    String EN_ABSTRACT_GEOMETRY_32 = "AbstractGeometry";

    String EN_ABSTRACT_ENCODING = "_Encoding";

    String EN_ABSTRACT_OBSERVATION = "AbstractObservation";

    String EN_ABSTRACT_FEATURE = "_Feature";

    String EN_ABSTRACT_FEATURE_32 = "AbstractFeature";

    String EN_ABSTRACT_FEATURE_COLLECTION = "_FeatureCollection";

    String EN_FEATURE_COLLECTION = "FeatureCollection";

    String EN_ABSTRACT_GEOMETRY = "_Geometry";

    String EN_ABSTRACT_SURFACE = "_Surface";

    String EN_ABSTRACT_TIME_GEOM_PRIM = "_TimeGeometricPrimitive";

    String EN_ABSTRACT_RING_311 = "_Ring";

    String EN_ABSTRACT_RING_32 = "AbstractRing";

    String EN_PART_TYPE = "Type";

    String EN_LINEAR_RING = "LinearRing";

    String EN_LINE_STRING = "LineString";

    String EN_POINT = "Point";

    String EN_POLYGON = "Polygon";

    String EN_MULTI_POINT = "MultiPoint";

    String EN_MULTI_CURVE = "MultiCurve";

    String EN_LOWER_CORNER = "lowerCorner";

    String EN_UPPER_CORNER = "upperCorner";

    String EN_FEATURE_MEMBER = "featureMember";

    String EN_IDENTIFIER = "identifier";

    String EN_NAME = "name";

    String EN_META_DATA_PROPERTY = "metaDataProperty";

    /* attribute names in GML */

    String AN_INDETERMINATE_POSITION = "indeterminatePosition";

    /* QNames for elements */

    QName QN_DESCRIPTION = new QName(NS_GML, EN_DESCRIPTION, NS_GML_PREFIX);

    QName QN_ENVELOPE = new QName(NS_GML, EN_ENVELOPE, NS_GML_PREFIX);

    QName QN_POINT = new QName(NS_GML, EN_POINT, NS_GML_PREFIX);

    QName QN_MULTIPOINT = new QName(NS_GML, EN_MULTI_POINT, NS_GML_PREFIX);

    QName QN_LINESTRING = new QName(NS_GML, EN_LINE_STRING, NS_GML_PREFIX);

    QName QN_POLYGON = new QName(NS_GML, EN_POLYGON, NS_GML_PREFIX);

    QName QN_ENVELOPE_32 = new QName(NS_GML_32, EN_ENVELOPE, NS_GML_PREFIX);

    QName QN_POINT_32 = new QName(NS_GML_32, EN_POINT, NS_GML_PREFIX);

    QName QN_LINESTRING_32 = new QName(NS_GML_32, EN_LINE_STRING, NS_GML_PREFIX);

    QName QN_MULTI_CURVE_32 = new QName(NS_GML_32, EN_MULTI_CURVE, NS_GML_PREFIX);

    QName QN_POLYGON_32 = new QName(NS_GML_32, EN_POLYGON, NS_GML_PREFIX);

    QName QN_MULTI_POINT_32 = new QName(NS_GML_32, EN_MULTI_POINT, NS_GML_PREFIX);

    QName QN_TIME_INSTANT = new QName(NS_GML, EN_TIME_INSTANT, NS_GML_PREFIX);

    QName QN_TIME_PERIOD = new QName(NS_GML, EN_TIME_PERIOD, NS_GML_PREFIX);

    QName QN_TIME_INSTANT_32 = new QName(NS_GML_32, EN_TIME_INSTANT, NS_GML_PREFIX);

    /**
     * The {@code QName} for {@code gml:TimePeriod}.
     */
    QName QN_TIME_PERIOD_32 = new QName(NS_GML_32, EN_TIME_PERIOD, NS_GML_PREFIX);

    QName QN_ABSTRACT_FEATURE_COLLECTION = new QName(NS_GML, EN_ABSTRACT_FEATURE_COLLECTION, NS_GML_PREFIX);

    QName QN_FEATURE_COLLECTION = new QName(NS_GML, EN_FEATURE_COLLECTION, NS_GML_PREFIX);

    QName QN_FEATURE_COLLECTION_32 = new QName(NS_GML_32, EN_FEATURE_COLLECTION, NS_GML_PREFIX);

    QName QN_FEATURE_MEMBER = new QName(NS_GML, EN_FEATURE_MEMBER, NS_GML_PREFIX);

    QName QN_FEATURE_MEMBER_32 = new QName(NS_GML_32, EN_FEATURE_MEMBER, NS_GML_PREFIX);

    QName QN_ABSTRACT_RING = new QName(NS_GML, EN_ABSTRACT_RING_311, NS_GML_PREFIX);

    QName QN_LINEAR_RING = new QName(NS_GML, EN_LINEAR_RING, NS_GML_PREFIX);

    QName QN_ABSTRACT_RING_32 = new QName(NS_GML_32, EN_ABSTRACT_RING_32, NS_GML_PREFIX);

    QName QN_LINEAR_RING_32 = new QName(NS_GML_32, EN_LINEAR_RING, NS_GML_PREFIX);

    QName QN_ABSTRACT_TIME_OBJECT = new QName(NS_GML, EN_ABSTRACT_TIME_OBJECT, NS_GML_PREFIX);

    QName QN_ABSTRACT_TIME_GEOM_PRIM = new QName(NS_GML, EN_ABSTRACT_TIME_GEOM_PRIM, NS_GML_PREFIX);

    QName QN_ABSTRACT_FEATURE_GML = new QName(NS_GML, EN_ABSTRACT_FEATURE, NS_GML_PREFIX);

    QName QN_ABSTRACT_FEATURE_GML_32 = new QName(NS_GML_32, EN_ABSTRACT_FEATURE_32, NS_GML_PREFIX);

    QName QN_ABSTRACT_TIME_32 = new QName(NS_GML_32, EN_ABSTRACT_TIME_OBJECT_32, NS_GML_PREFIX);

    QName QN_ABSTRACT_GEOMETRY_32 = new QName(NS_GML_32, EN_ABSTRACT_GEOMETRY_32, NS_GML_PREFIX);

    QName QN_REMOTE_SCHEMA = new QName(GmlConstants.NS_GML_32, AN_REMOTE_SCHEMA, NS_GML_PREFIX);

    /**
     * The {@code QName} for {@code gml:id}.
     */
    QName QN_ID_32 = new QName(NS_GML_32, AN_ID, NS_GML_PREFIX);

    QName QN_DESCRIPTION_32 = new QName(NS_GML_32, EN_DESCRIPTION, NS_GML_PREFIX);

    QName QN_ID = new QName(NS_GML, AN_ID, NS_GML_PREFIX);

    QName QN_BEGIN_POSITION_32 =
            new QName(GmlConstants.NS_GML_32, GmlConstants.EN_BEGIN_POSITION, GmlConstants.NS_GML_PREFIX);

    /**
     * The {@code QName} for {@code gml:endPosition}.
     */
    QName QN_END_POSITION_32 =
            new QName(GmlConstants.NS_GML_32, GmlConstants.EN_END_POSITION, GmlConstants.NS_GML_PREFIX);

    /**
     * The {@code QName} for {@code gml:timePosition}.
     */
    QName QN_TIME_POSITION_32 =
            new QName(GmlConstants.NS_GML_32, GmlConstants.EN_TIME_POSITION, GmlConstants.NS_GML_PREFIX);

    /**
     * The {@code QName} for {@code gml:identifier}.
     */
    QName QN_IDENTIFIER_32 = new QName(NS_GML_32, EN_IDENTIFIER, NS_GML_PREFIX);

    QName QN_NAME_32 = new QName(NS_GML_32, EN_NAME, NS_GML_PREFIX);

    QName QN_INDETERMINATE_POSITION_32 = new QName(NS_GML_32, AN_INDETERMINATE_POSITION, NS_GML_PREFIX);

    QName QN_OM_20_META_DATA_PROPERTY_32 = new QName(NS_GML_32, EN_META_DATA_PROPERTY, NS_GML_PREFIX);

    /** string constant for ascending sorting order */
    String SORT_ORDER_ASC = SortingOrder.ASC.name();

    /** Constant for result model of common observations */
    String SORT_ORDER_DESC = SortingOrder.DESC.name();

    String VALUE_REF_GML_DESCRIPTION = "gml:description";

    /**
     * Enumeration of the possible values for indeterminate Time attribute of eventtime in GetObservation
     * request
     *
     * @since 1.0.0
     *
     */
    enum IndetTimeValues {
        after,
        before,
        now,
        unknown
    }

    /**
     * enumeration of the possible sorting orders
     *
     * @since 1.0.0
     */
    enum SortingOrder {
        ASC,
        DESC
    }

    /**
     * inapplicable there is no value missing the correct value is not readily available to the sender of this
     * data. Furthermore, a correct value may not exist template the value will be available later unknown the
     * correct value is not known to, and not computable by, the sender of this data. However, a correct value
     * probably exists withheld the value is not divulged Not supported: other:text other brief explanation,
     * where text is a string of two or more characters with no included spaces
     *
     * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
     *
     * @since 1.0.0
     *
     */
    enum NilReason {
        inapplicable,
        missing,
        template,
        unknown,
        withheld;
    }

}
