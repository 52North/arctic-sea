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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.XmlHelper;
import org.n52.svalbard.write.GetDataAvailabilityStreamWriter;
import org.n52.svalbard.write.GetDataAvailabilityV20StreamWriter;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

/**
 * {@code Encoder} to handle {@link GetDataAvailabilityResponse}s.
 *
 * @author Christian Autermann
 *
 * @since 1.0.0
 */
public class GetDataAvailabilityXmlEncoder
        extends AbstractResponseEncoder<GetDataAvailabilityResponse> {

    public GetDataAvailabilityXmlEncoder() {
        super(SosConstants.SOS, Sos2Constants.SERVICEVERSION, GetDataAvailabilityConstants.OPERATION_NAME,
                Sos2Constants.NS_SOS_20, SosConstants.NS_SOS_PREFIX, GetDataAvailabilityResponse.class, false);
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return Collections.emptySet();
    }

    protected Set<SchemaLocation> getConcreteSchemaLocations(String namespace) {
        if (!Strings.isNullOrEmpty(namespace)) {
            switch (namespace) {
                case GetDataAvailabilityConstants.NS_GDA:
                    return Sets.newHashSet(GetDataAvailabilityConstants.GET_DATA_AVAILABILITY_SCHEMA_LOCATION);
                case GetDataAvailabilityConstants.NS_GDA_20:
                    return Sets.newHashSet(GetDataAvailabilityConstants.GET_DATA_AVAILABILITY_20_SCHEMA_LOCATION);
                default:
                    return getConcreteSchemaLocations();
            }
        }
        return getConcreteSchemaLocations();
    }

    @Override
    protected XmlObject create(GetDataAvailabilityResponse response)
            throws EncodingException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            EncodingContext ctx = EncodingContext.empty()
                    .with(EncoderFlags.ENCODER_REPOSITORY, getEncoderRepository())
                    .with(XmlEncoderFlags.XML_OPTIONS, (Supplier<XmlOptions>) this::getXmlOptions);
            if (GetDataAvailabilityConstants.NS_GDA.equals(response.getResponseFormat())) {
                new GetDataAvailabilityStreamWriter(ctx, baos, response.getDataAvailabilities()).write();
            } else if (GetDataAvailabilityConstants.NS_GDA_20.equals(response.getResponseFormat())) {
                new GetDataAvailabilityV20StreamWriter(ctx, baos, response.getDataAvailabilities()).write();
            }
            XmlObject encodedObject = XmlObject.Factory.parse(baos.toString("UTF8"));
            XmlHelper.validateDocument(encodedObject, EncodingException::new);
            return encodedObject;
        } catch (XMLStreamException | XmlException | UnsupportedEncodingException ex) {
            throw new EncodingException("Error encoding response", ex);
        }
    }
}
