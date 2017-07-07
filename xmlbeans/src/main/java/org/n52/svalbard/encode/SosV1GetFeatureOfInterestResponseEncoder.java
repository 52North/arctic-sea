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
package org.n52.svalbard.encode;

import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.GetFeatureOfInterestResponse;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class SosV1GetFeatureOfInterestResponseEncoder
        extends AbstractSosV1ResponseEncoder<GetFeatureOfInterestResponse> {
    public SosV1GetFeatureOfInterestResponseEncoder() {
        super(SosConstants.Operations.GetFeatureOfInterest.name(), GetFeatureOfInterestResponse.class);
    }

    @Override
    protected XmlObject create(GetFeatureOfInterestResponse response) throws EncodingException {
        return getEncoder(GmlConstants.NS_GML, response.getAbstractFeature()).encode(response.getAbstractFeature());

    }
}
