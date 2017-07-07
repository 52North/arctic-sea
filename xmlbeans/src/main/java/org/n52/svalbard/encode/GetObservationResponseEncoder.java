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

import java.io.OutputStream;
import java.util.Set;
import java.util.function.Supplier;

import javax.xml.stream.XMLStreamException;

import net.opengis.sos.x20.GetObservationResponseDocument;
import net.opengis.sos.x20.GetObservationResponseType;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import org.n52.shetland.ogc.om.ObservationStream;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.XmlHelper;
import org.n52.svalbard.write.GetObservationResponseXmlStreamWriter;

import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GetObservationResponseEncoder extends AbstractObservationResponseEncoder<GetObservationResponse> {
    public GetObservationResponseEncoder() {
        super(SosConstants.Operations.GetObservation.name(), GetObservationResponse.class);
    }

    @Override
    public Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(Sos2Constants.SOS_GET_OBSERVATION_SCHEMA_LOCATION);
    }

    @Override
    protected XmlObject createResponse(ObservationEncoder<XmlObject, OmObservation> encoder,
                                       GetObservationResponse response) throws EncodingException {
        try {
            GetObservationResponseDocument doc = GetObservationResponseDocument.Factory.newInstance(getXmlOptions());
            GetObservationResponseType xbResponse = doc.addNewGetObservationResponse();
            ObservationStream observationCollection = response.getObservationCollection();
            while (observationCollection.hasNext()) {
                xbResponse.addNewObservationData()
                        .addNewOMObservation()
                        .set(encoder.encode(observationCollection.next()));
            }
            // in a single observation the gml:ids must be unique
            XmlHelper.makeGmlIdsUnique(doc.getDomNode());
            return doc;
        } catch (OwsExceptionReport ex) {
            throw new EncodingException(ex);
        }
    }

    @Override
    protected void createResponse(ObservationEncoder<XmlObject, OmObservation> encoder,
                                  GetObservationResponse response, OutputStream outputStream, EncodingContext ctx)
            throws EncodingException {
        try {
            EncodingContext context = ctx.with(EncoderFlags.ENCODER_REPOSITORY, getEncoderRepository())
                    .with(XmlEncoderFlags.XML_OPTIONS, (Supplier<XmlOptions>) this::getXmlOptions)
                    .with(StreamingEncoderFlags.ENCODER, this);
            new GetObservationResponseXmlStreamWriter(context, outputStream, response).write();
        } catch (XMLStreamException xmlse) {
            throw new EncodingException(xmlse);
        }
    }

}
