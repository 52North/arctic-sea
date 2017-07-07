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

import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;

import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.GetResultResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import net.opengis.sos.x20.GetResultResponseDocument;
import net.opengis.sos.x20.GetResultResponseType;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GetResultResponseEncoder extends AbstractSosResponseEncoder<GetResultResponse> {
    public GetResultResponseEncoder() {
        super(SosConstants.Operations.GetResult.name(), GetResultResponse.class);
    }

    @Override
    protected XmlObject create(GetResultResponse response) throws EncodingException {
        GetResultResponseDocument doc = GetResultResponseDocument.Factory.newInstance(getXmlOptions());
        GetResultResponseType gtr = doc.addNewGetResultResponse();
        XmlObject resultValues = gtr.addNewResultValues();
        if (response.hasResultValues()) {
            XmlString xmlString = XmlString.Factory.newInstance();
            xmlString.setStringValue(response.getResultValues());
            resultValues.set(xmlString);
        }
        return doc;
    }

    @Override
    public Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(Sos2Constants.SOS_GET_RESULT_SCHEMA_LOCATION);
    }
}
