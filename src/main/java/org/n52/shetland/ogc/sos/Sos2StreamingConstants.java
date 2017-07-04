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
package org.n52.shetland.ogc.sos;

import javax.xml.namespace.QName;

public interface Sos2StreamingConstants {

    String EN_GET_OBSERVATION_RESPONSE = "GetObservationResponse";

    String EN_OBSERVATION_DATA = "observationData";

    QName GET_OBSERVATION_RESPONSE =
            new QName(Sos2Constants.NS_SOS_20, EN_GET_OBSERVATION_RESPONSE, SosConstants.NS_SOS_PREFIX);

    QName OBSERVATION_DATA = new QName(Sos2Constants.NS_SOS_20, EN_OBSERVATION_DATA, SosConstants.NS_SOS_PREFIX);

    String EN_GET_FEATURE_OF_INTEREST_RESPONSE = "GetFeatureOfInterestResponse";

    String EN_FEATURE_MEMBER = "featureMember";

    QName QN_GET_FEATURE_OF_INTEREST_RESPONSE =
            new QName(Sos2Constants.NS_SOS_20, EN_GET_FEATURE_OF_INTEREST_RESPONSE, Sos2Constants.NS_SOS_PREFIX);

    QName QN_FEATURE_MEMBER = new QName(Sos2Constants.NS_SOS_20, EN_FEATURE_MEMBER, Sos2Constants.NS_SOS_PREFIX);
}
