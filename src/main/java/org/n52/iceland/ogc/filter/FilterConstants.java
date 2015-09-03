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
package org.n52.iceland.ogc.filter;

import com.google.common.base.Joiner;
import javax.xml.namespace.QName;

import org.n52.iceland.ogc.OGCConstants;
import org.n52.iceland.w3c.SchemaLocation;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumBiMap;
import com.google.common.collect.Sets;
import java.util.Set;
import org.n52.iceland.ogc.gml.GmlConstants;

/**
 * Constants interface for <a
 * href="http://www.opengeospatial.org/standards/filter">OGC Filter Encoding</a>
 *
 * @since 1.0.0
 */
public interface FilterConstants {

    String NS_FES_2 = "http://www.opengis.net/fes/2.0";

    String NS_FES_110 = "http://www.opengis.net/ogc";

    String NS_FES_2_PREFIX = "fes";

    String NS_FES_110_PREFIX = "fes";

    String SCHEMA_LOCATION_URL_FES_20 = "http://schemas.opengis.net/filter/2.0/filterAll.xsd";

    String SCHEMA_LOCATION_URL_FES_110 = "http://schemas.opengis.net/filter/1.1.0/filter.xsd";

    SchemaLocation FES_20_SCHEMA_LOCATION = new SchemaLocation(NS_FES_2, SCHEMA_LOCATION_URL_FES_20);

    SchemaLocation FES_110_SCHEMA_LOCATION = new SchemaLocation(NS_FES_110, SCHEMA_LOCATION_URL_FES_110);

    String FILTER_LANGUAGE_FES_FILTER = OGCConstants.QUERY_LANGUAGE_PREFIX + "OGC-FES:Filter";

    /*
     * element names
     */
    String EN_TEQUALS = "TEquals";
    String EN_VALUE_REFERENCE = "ValueReference";
    String EN_LITERAL = "Literal";
    String EN_PROPERTY_IS_EQUAL_TO = "PropertyIsEqualTo";
    String EN_FILTER = "Filter";

    /*
     * QNames
     */
    QName QN_FILTER = new QName(NS_FES_2, EN_FILTER, NS_FES_2_PREFIX);
    QName QN_VALUE_REFERENCE = new QName(NS_FES_2, EN_VALUE_REFERENCE, NS_FES_2_PREFIX);
    QName QN_LITERAL = new QName(NS_FES_2, EN_LITERAL, NS_FES_2_PREFIX);
    QName QN_PROPERTY_IS_EQUAL_TO = new QName(NS_FES_2, EN_PROPERTY_IS_EQUAL_TO, NS_FES_2_PREFIX);

    /**
     * Enumeration for conformance class constraint names
     */
    enum ConformanceClassConstraintNames {

        ImplementsQuery,
        ImplementsAdHocQuery,
        ImplementsFunctions,
        ImplementsResourceld,
        ImplementsMinStandardFilter,
        ImplementsStandardFilter,
        ImplementsMinSpatialFilter,
        ImplementsSpatialFilter,
        ImplementsMinTemporalFilter,
        ImplementsTemporalFilter,
        ImplementsVersionNav,
        ImplementsSorting,
        ImplementsExtendedOperators,
        ImplementsMinimumXPath,
        ImplementsSchemaElementFunc
    }

    class TimeOperatorMapping {

        private static final BiMap<TimeOperator, TimeOperator2> bimap
                = EnumBiMap.create(TimeOperator.class, TimeOperator2.class);

        static {
            bimap.put(TimeOperator.TM_Before, TimeOperator2.Before);
            bimap.put(TimeOperator.TM_After, TimeOperator2.After);
            bimap.put(TimeOperator.TM_Begins, TimeOperator2.Begins);
            bimap.put(TimeOperator.TM_Ends, TimeOperator2.Ends);
            bimap.put(TimeOperator.TM_EndedBy, TimeOperator2.EndedBy);
            bimap.put(TimeOperator.TM_BegunBy, TimeOperator2.BegunBy);
            bimap.put(TimeOperator.TM_During, TimeOperator2.During);
            bimap.put(TimeOperator.TM_Equals, TimeOperator2.TEquals);
            bimap.put(TimeOperator.TM_Contains, TimeOperator2.TContains);
            bimap.put(TimeOperator.TM_Overlaps, TimeOperator2.TOverlaps);
            bimap.put(TimeOperator.TM_Meets, TimeOperator2.Meets);
            bimap.put(TimeOperator.TM_MetBy, TimeOperator2.MetBy);
            bimap.put(TimeOperator.TM_OverlappedBy, TimeOperator2.OverlappedBy);
        }

        public static TimeOperator get(TimeOperator2 to) {
            return bimap.inverse().get(to);
        }

        public static TimeOperator2 get(TimeOperator to) {
            return bimap.get(to);
        }
    }

    /**
     * Enumeration for temporal operators
     */
    enum TimeOperator {

        TM_Before,
        TM_After,
        TM_Begins,
        TM_Ends,
        TM_EndedBy,
        TM_BegunBy,
        TM_During,
        TM_Equals,
        TM_Contains,
        TM_Overlaps,
        TM_Meets,
        TM_MetBy,
        TM_OverlappedBy;

        public TimeOperator2 getEquivalent() {
            return TimeOperatorMapping.get(this);
        }

        public static TimeOperator from(String s) {
            for (TimeOperator to : TimeOperator.values()) {
                if (to.name().equalsIgnoreCase(s)) {
                    return to;
                }
            }
            throw new IllegalArgumentException(s);
        }

        public static TimeOperator from(TimeOperator2 to) {
            return to.getEquivalent();
        }
    }

    /**
     * Enumeration for FES 2.0 temporal operators
     */
    enum TimeOperator2 {

        Before,
        After,
        Begins,
        Ends,
        EndedBy,
        BegunBy,
        During,
        TEquals,
        TContains,
        TOverlaps,
        Meets,
        MetBy,
        OverlappedBy;

        public TimeOperator getEquivalent() {
            return TimeOperatorMapping.get(this);
        }

        public static TimeOperator2 from(String s) {
            for (TimeOperator2 to : TimeOperator2.values()) {
                if (to.name().equalsIgnoreCase(s)) {
                    return to;
                }
            }
            throw new IllegalArgumentException(s);
        }

        public static TimeOperator2 from(TimeOperator to) {
            return to.getEquivalent();
        }
    }

    /**
     * Enumeration for spatial operators
     */
    enum SpatialOperator {

        Equals,
        Disjoint,
        Touches,
        Within,
        Overlaps,
        Crosses,
        Intersects,
        Contains,
        DWithin,
        Beyond,
        BBOX;
    }

    /**
     * Enumeration for geometry types
     */
    enum GeometryOperand {

        Envelope,
        Point,
        LineString,
        Polygon,
        ArcByCenterPoint,
        CircleByCenterPoint,
        Arc,
        Circle,
        ArcByBulge,
        Bezier,
        Clothoid,
        CubicSpline,
        Geodesic,
        OffsetCurve,
        Triangle,
        PolyhedralSurface,
        TriangulatedSurface,
        Tin,
        Solid;

        public static String asString(GeometryOperand go) {
            return Joiner.on(":").join(GmlConstants.NS_GML_PREFIX, go.name());
        }
    }

    /**
     * Enumeration for comparison operators
     */
    enum ComparisonOperator {

        PropertyIsEqualTo,
        PropertyIsNotEqualTo,
        PropertyIsLessThan,
        PropertyIsGreaterThan,
        PropertyIsLessThanOrEqualTo,
        PropertyIsGreaterThanOrEqualTo,
        PropertyIsLike,
        PropertyIsNil,
        PropertyIsNull,
        PropertyIsBetween;

        public static String asString(ComparisonOperator co) {
            switch (co) {
                case PropertyIsEqualTo:
                    return "EqualTo";
                case PropertyIsNotEqualTo:
                    return "NotEqualTo";
                case PropertyIsLessThan:
                    return "LessThan";
                case PropertyIsGreaterThan:
                    return "GreaterThan";
                case PropertyIsLessThanOrEqualTo:
                    return "LessThanOrEqualTo";
                case PropertyIsGreaterThanOrEqualTo:
                    return "GreaterThanEqualTo";
                case PropertyIsLike:
                    return "Like";
                case PropertyIsNil:
                    return "Nil";
                case PropertyIsNull:
                    return "NullCheck";
                case PropertyIsBetween:
                    return "Between";
                default:
                    throw new IllegalArgumentException(String.format("Operators %s is not supported.", co));
            }
        }
    }

    interface LogicOperator {

        public static final Set<LogicOperator> ALL = Sets.newHashSet(
                BinaryLogicOperator.And,
                BinaryLogicOperator.Or,
                UnaryLogicOperator.Not);

    }

    /**
     * Enumeration for binary logic operators
     *
     * @since 4.0.0
     *
     */
    enum BinaryLogicOperator implements LogicOperator {

        And,
        Or
    }

    /**
     * Enumeration for unary logic operators
     *
     * @since 4.0.0
     *
     */
    enum UnaryLogicOperator implements LogicOperator {

        Not
    }

    /**
     * Enumeration for AdHoc query parameter
     *
     * @since 4.0.0
     *
     */
    enum AdHocQueryParams {

        TypeNames,
        Aliases,
        PropertyName,
        Filter,
        Filter_Language,
        ResourceId,
        BBox,
        SortBy
        /*
         * TypeNames is mandatory but "Standards that reference this
         * International Standard may change the requirement for the TYPENAME
         * parameter. In such cases, the referencing standard shall document
         * whether the TYPENAME parameter is mandatory, optional or mandatory in
         * some cases and optional in others."
         */
    }

    /**
     * Enumeration for sort order
     *
     * @since 4.0.0
     *
     */
    enum SortOrder {

        ASC,
        DESC
    }

    enum Expression {

        ValueReference,
        Function
    }

    enum SimpleArithmeticOperator {

        Add,
        Sub,
        Mul,
        Div
    }

    enum Id {

        EID,
        FID
    }
}
