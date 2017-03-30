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

    public static final String EN_GET_OBSERVATION_RESPONSE = "GetObservationResponse";

    public static final String EN_OBSERVATION_DATA = "observationData";

    public static final QName GET_OBSERVATION_RESPONSE
            = new QName(Sos2Constants.NS_SOS_20, EN_GET_OBSERVATION_RESPONSE, SosConstants.NS_SOS_PREFIX);

    public static final QName OBSERVATION_DATA
            = new QName(Sos2Constants.NS_SOS_20, EN_OBSERVATION_DATA, SosConstants.NS_SOS_PREFIX);
}
