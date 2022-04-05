/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.om.series.wml;

/**
 * @since 1.0.0
 *
 */
public interface ConformanceClassesWML2 {

    String UML_TIMESERIES_OBSERVATION = "http://www.opengis.net/spec/waterml/2.0/conf/uml-timeseries-observation";

    String UML_TIMESERIES_TVP_OBSERVATION =
            "http://www.opengis.net/spec/waterml/2.0/conf/uml-timeseries-tvp-observation";

    String UML_MEASUREMENT_TIMESERIES_TVP_OBSERVATION =
            "http://www.opengis.net/spec/waterml/2.0/conf/uml-measurement-timeries-tvp-observation";

    String XSD_XML_RULES = "http://www.opengis.net/spec/waterml/2.0/conf/xsd-xml-rules";

    String XSD_TIMESERIES_OBSERVATION = "http://www.opengis.net/spec/waterml/2.0/conf/xsd-timeseries-observation";

    String XSD_TIMESERIES_TVP_OBSERVATION =
            "http://www.opengis.net/spec/waterml/2.0/conf/xsd-timeseries-tvp-observation";

    String XSD_MEASUREMENT_TIMESERIES_TVP =
            "http://www.opengis.net/spec/waterml/2.0/conf/xsd-measurement-timeseries-tvp";

}
