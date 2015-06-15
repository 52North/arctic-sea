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

import javax.xml.namespace.QName;

import org.n52.iceland.ogc.OGCConstants;
import org.n52.iceland.w3c.SchemaLocation;

/**
 * Constants interface for <a
 * href="http://www.opengeospatial.org/standards/filter">OGC Filter Encoding</a>
 *
 * @since 1.0.0
 */
public interface FilterConstants {
    String NS_FES_2 = "http://www.opengis.net/fes/2.0";

    String NS_FES_2_PREFIX = "fes";

    String SCHEMA_LOCATION_URL_FES_20 = "http://schemas.opengis.net/filter/2.0/filterAll.xsd";

    SchemaLocation FES_20_SCHEMA_LOCATION = new SchemaLocation(NS_FES_2, SCHEMA_LOCATION_URL_FES_20);

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

    /**
     * Enumeration for temporal operators
     */
    enum TimeOperator {
        TM_Before(TimeOperator2.Before),
        TM_After(TimeOperator2.After),
        TM_Begins(TimeOperator2.Begins),
        TM_Ends(TimeOperator2.Ends),
        TM_EndedBy(TimeOperator2.EndedBy),
        TM_BegunBy(TimeOperator2.BegunBy),
        TM_During(TimeOperator2.During),
        TM_Equals(TimeOperator2.TEquals),
        TM_Contains(TimeOperator2.TContains),
        TM_Overlaps(TimeOperator2.TOverlaps),
        TM_Meets(TimeOperator2.Meets),
        TM_MetBy(TimeOperator2.MetBy),
        TM_OverlappedBy(TimeOperator2.OverlappedBy);

        private final TimeOperator2 equivalent;

        private TimeOperator(TimeOperator2 equivalent) {
            this.equivalent = equivalent;
        }

        public TimeOperator2 getEquivalent() {
            return equivalent;
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
        Before(TimeOperator.TM_Before),
        After(TimeOperator.TM_After),
        Begins(TimeOperator.TM_Begins),
        Ends(TimeOperator.TM_Ends),
        EndedBy(TimeOperator.TM_EndedBy),
        BegunBy(TimeOperator.TM_BegunBy),
        During(TimeOperator.TM_During),
        TEquals(TimeOperator.TM_Equals),
        TContains(TimeOperator.TM_Contains),
        TOverlaps(TimeOperator.TM_Overlaps),
        Meets(TimeOperator.TM_Meets),
        MetBy(TimeOperator.TM_MetBy),
        OverlappedBy(TimeOperator.TM_OverlappedBy);

        private final TimeOperator equivalent;

        private TimeOperator2(TimeOperator equivalent) {
            this.equivalent = equivalent;
        }

        public TimeOperator getEquivalent() {
            return equivalent;
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
        BBOX
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
        PropertyIsBetween
    }

    /**
     * Enumeration for binary logic operators
     *
     * @since 4.0.0
     *
     */
    enum BinaryLogicOperator {
        And,
        Or
    }

    /**
     * Enumeration for unary logic operators
     *
     * @since 4.0.0
     *
     */
    enum UnaryLogicOperator {
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
}
