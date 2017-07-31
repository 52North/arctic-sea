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
package org.n52.svalbard.write;

import static java.util.stream.Collectors.toSet;

import java.io.OutputStream;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.om.ObservationStream;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.Sos2StreamingConstants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.ObservationEncoder;
import org.n52.svalbard.encode.SchemaAwareEncoder;
import org.n52.svalbard.encode.StreamingEncoder;
import org.n52.svalbard.encode.StreamingEncoderFlags;
import org.n52.svalbard.encode.XmlBeansEncodingFlags;
import org.n52.svalbard.encode.XmlEncoderFlags;
import org.n52.svalbard.encode.XmlEncoderKey;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * Implementatio of {@link XmlStreamWriter} for {@link GetObservationResponse}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class GetObservationResponseXmlStreamWriter extends XmlStreamWriter<GetObservationResponse> {
    public GetObservationResponseXmlStreamWriter(EncodingContext context, OutputStream outputStream,
                                                 GetObservationResponse element) throws XMLStreamException {
        super(context, outputStream, element);
    }

    @Override
    public void write()
            throws XMLStreamException, EncodingException {
        try {
            start();
            writeGetObservationResponseDoc();
            end();
            finish();
        } catch (XMLStreamException xmlse) {
            throw new EncodingException(xmlse);
        }
    }

    private void writeGetObservationResponseDoc()
            throws XMLStreamException, EncodingException {
        start(Sos2StreamingConstants.GET_OBSERVATION_RESPONSE);
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        namespace(SosConstants.NS_SOS_PREFIX, Sos2Constants.NS_SOS_20);
        GetObservationResponse response = getElement();
        // get observation encoder
        ObservationEncoder<XmlObject, OmObservation> encoder = findObservationEncoder(response.getResponseFormat());
        // write schemaLocation
        schemaLocation(getSchemaLocation(encoder));
        EncodingContext ctx = getContext()
                .with(XmlEncoderFlags.ENCODE_NAMESPACE, response.getResponseFormat())
                .with(XmlBeansEncodingFlags.DOCUMENT)
                .with(StreamingEncoderFlags.EMBEDDED)
                .without(XmlBeansEncodingFlags.PROPERTY_TYPE)
                .without(XmlBeansEncodingFlags.TYPE);
        try {
            ObservationStream stream = response.getObservationCollection();
            if (encoder.shouldObservationsWithSameXBeMerged()) {
                stream = stream.merge();
            }
            while (stream.hasNext()) {
                OmObservation o = stream.next();
                if (o.getValue() instanceof ObservationStream) {
                    ObservationStream value = (ObservationStream) o.getValue();
                    if (encoder.supportsResultStreamingForMergedValues()) {
                        writeObservationData(ctx, o, encoder);
                    } else {
                        while (value.hasNext()) {
                            writeObservationData(ctx, value.next(), encoder);
                        }
                    }
                } else {
                    writeObservationData(ctx, o, encoder);
                }
            }
        } catch (OwsExceptionReport owse) {
            throw new EncodingException(owse);
        }
        end(Sos2StreamingConstants.GET_OBSERVATION_RESPONSE);
    }

    @SuppressWarnings("unchecked")
    private void writeObservationData(EncodingContext ctx,
                                      OmObservation observation,
                                      ObservationEncoder<XmlObject, OmObservation> encoder)
            throws XMLStreamException, EncodingException {
        start(Sos2StreamingConstants.OBSERVATION_DATA);
        if (encoder instanceof StreamingEncoder) {
            ((StreamingEncoder<XmlObject, OmObservation>) encoder).encode(observation, getOutputStream(), ctx);
        } else {
            rawText(encoder.encode(observation, ctx).xmlText(getXmlOptions().setSaveNoXmlDecl()));
        }
        end(Sos2StreamingConstants.OBSERVATION_DATA);
    }

    private Set<SchemaLocation> getSchemaLocation(ObservationEncoder<XmlObject, OmObservation> encoder) {
        return Stream.of(
                getEncoder()
                        .filter(e -> e instanceof SchemaAwareEncoder)
                        .map(e -> (SchemaAwareEncoder<?, ?>) e)
                        .map(SchemaAwareEncoder::getSchemaLocations)
                        .orElseGet(() -> Collections.singleton(Sos2Constants.SOS_GET_OBSERVATION_SCHEMA_LOCATION)),
                Optional.ofNullable(encoder)
                        .filter(e -> e instanceof SchemaAwareEncoder)
                        .map(e -> (SchemaAwareEncoder<?, ?>) e)
                        .map(SchemaAwareEncoder::getSchemaLocations).orElseGet(Collections::emptySet)
        ).flatMap(Set::stream).collect(toSet());
    }

    /**
     * Finds a O&Mv2 compatible {@link ObservationEncoder}
     *
     * @param responseFormat the response format
     *
     * @return the encoder or {@code null} if none is found
     *
     * @throws EncodingException if the found encoder is not a {@linkplain ObservationEncoder}
     */
    private ObservationEncoder<XmlObject, OmObservation> findObservationEncoder(String responseFormat)
            throws EncodingException {
        Optional<Encoder<XmlObject, OmObservation>> encoder = this
                .<XmlObject, OmObservation>tryGetEncoder(new XmlEncoderKey(responseFormat, OmObservation.class));
        if (!encoder.isPresent()) {
            return null;
        } else if (encoder.get() instanceof ObservationEncoder) {
            ObservationEncoder<XmlObject, OmObservation> oe = (ObservationEncoder<XmlObject, OmObservation>) encoder
                    .get();
            return oe.isObservationAndMeasurmentV20Type() ? oe : null;
        } else {
            throw new EncodingException("Error while encoding response, encoder is not of type ObservationEncoder!");
        }
    }

}
