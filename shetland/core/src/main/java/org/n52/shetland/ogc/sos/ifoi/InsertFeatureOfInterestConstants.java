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
package org.n52.shetland.ogc.sos.ifoi;

public interface InsertFeatureOfInterestConstants {

    /**
     * The operation name.
     */
    String OPERATION_NAME = "InsertFeatureOfInterest";

    /*
     * GDA v10
     */
    String NS_IFOI = "http://www.opengis.net/ifoi/1.0";

    String NS_IFOI_PREFIX = "ifoi";

    String SCHEMA_LOCATION_URL_INSERT_FEATURE_OF_INTEREST =
            "http://52north.org/schema/ifoi/1.0/InsertFeatureOfInterest.xsd";

    String CONFORMANCE_CLASS = "http://www.opengis.net/spec/SOS/2.0/conf/foi";

    public interface InsertFeatureOfInterestParams {

    }

}
