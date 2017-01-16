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
package org.n52.svalbard;

import java.io.OutputStream;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.StreamingValue;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.AbstractStreaming;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.ObservationEncoder;
import org.n52.svalbard.encode.StreamingDataEncoder;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import com.google.common.collect.Sets;

import net.opengis.sos.x20.GetObservationResponseDocument;
import net.opengis.sos.x20.GetObservationResponseType;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 4.0.0
 */
public class GetObservationResponseEncoder extends AbstractObservationResponseEncoder<GetObservationResponse>
        implements StreamingDataEncoder {
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
        GetObservationResponseDocument doc = GetObservationResponseDocument.Factory.newInstance(getXmlOptions());
        GetObservationResponseType xbResponse = doc.addNewGetObservationResponse();
        if (!response.isSetMergeObservation()) {
            response.setMergeObservations(encoder.shouldObservationsWithSameXBeMerged());
        }
        // TODO iterate over observation collection and remove processed
        // observation
        for (OmObservation o : response.getObservationCollection()) {
            if (encoder instanceof StreamingDataEncoder) {
                xbResponse.addNewObservationData().addNewOMObservation().set(encoder.encode(o));
            } else {
                if (o.getValue() instanceof AbstractStreaming) {
                    processAbstractStreaming(xbResponse, (AbstractStreaming) o.getValue(), encoder,
                            response.isSetMergeObservation());
                } else {
                    xbResponse.addNewObservationData().addNewOMObservation().set(encoder.encode(o));
                }
            }
        }
        // in a single observation the gml:ids must be unique
        if (response.getObservationCollection().size() > 1) {
            XmlHelper.makeGmlIdsUnique(doc.getDomNode());
        }
        return doc;
    }

    private void processAbstractStreaming(GetObservationResponseType xbResponse, AbstractStreaming value,
            ObservationEncoder<XmlObject, OmObservation> encoder, boolean merge) throws EncodingException {
        if (value instanceof StreamingValue) {
            processStreamingValue(xbResponse, (StreamingValue<?>) value, encoder, merge);
        } else {
            throw new UnsupportedEncoderInputException(this, value);
        }
    }

    private void processStreamingValue(GetObservationResponseType xbResponse, StreamingValue<?> streamingValue,
            ObservationEncoder<XmlObject, OmObservation> encoder, boolean merge) throws EncodingException {
        try {
            if (streamingValue.hasNextValue()) {
                if (merge) {
                    for (OmObservation obs : streamingValue.mergeObservation()) {
                        xbResponse.addNewObservationData().addNewOMObservation().set(encoder.encode(obs));
                    }
                } else {
                    do {
                        xbResponse.addNewObservationData().addNewOMObservation()
                                .set(encoder.encode(streamingValue.nextSingleObservation()));
                    } while (streamingValue.hasNextValue());
                }
            } else if (streamingValue.getValue() != null) {
                xbResponse.addNewObservationData().addNewOMObservation()
                        .set(encoder.encode(streamingValue.getValue().getValue()));
            }
        } catch (OwsExceptionReport owse) {
            throw new EncodingException(owse);
        }
    }

    @Override
    protected void createResponse(ObservationEncoder<XmlObject, OmObservation> encoder,
            GetObservationResponse response, OutputStream outputStream, EncodingValues encodingValues)
            throws EncodingException {
        try {
            encodingValues.setEncoder(this);
            new GetObservationResponseXmlStreamWriter().write(response, outputStream, encodingValues);
        } catch (XMLStreamException xmlse) {
            throw new EncodingException(xmlse);
        }
    }

}
