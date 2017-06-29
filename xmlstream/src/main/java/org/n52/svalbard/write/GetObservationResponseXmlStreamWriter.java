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

import java.io.OutputStream;
import java.util.Set;

import javax.inject.Inject;
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
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.EncodingValues;
import org.n52.svalbard.encode.ObservationEncoder;
import org.n52.svalbard.encode.SchemaAwareEncoder;
import org.n52.svalbard.encode.StreamingDataEncoder;
import org.n52.svalbard.encode.StreamingEncoder;
import org.n52.svalbard.encode.XmlEncoderKey;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.XmlOptionsHelper;

import com.google.common.collect.Sets;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Implementatio of {@link XmlStreamWriter} for {@link GetObservationResponse}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.1.0
 *
 */
public class GetObservationResponseXmlStreamWriter extends XmlStreamWriter<GetObservationResponse>
        implements StreamingDataEncoder {

    private GetObservationResponse response;

    private EncoderRepository encoderRepository;

    /**
     * constructor
     */
    public GetObservationResponseXmlStreamWriter() {
    }

    /**
     * constructor
     *
     * @param response {@link GetObservationResponse} to write to stream
     */
    public GetObservationResponseXmlStreamWriter(GetObservationResponse response) {
        this.response = response;
    }

    /**
     * @param encoderRepository the encoderRepository to set
     */
    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    @Override
    public void write(OutputStream out) throws XMLStreamException, EncodingException {
        write(getResponse(), out);
    }

    @Override
    public void write(OutputStream out, EncodingValues encodingValues) throws XMLStreamException, EncodingException {
        write(getResponse(), out, encodingValues);
    }

    @Override
    public void write(GetObservationResponse response, OutputStream out) throws XMLStreamException, EncodingException {
        write(response, out, new EncodingValues());
    }

    @Override
    public void write(GetObservationResponse response, OutputStream out, EncodingValues encodingValues)
            throws XMLStreamException, EncodingException {
        try {
            init(out, encodingValues);
            start(encodingValues.isEmbedded());
            writeGetObservationResponseDoc(response, encodingValues);
            end();
            finish();
        } catch (XMLStreamException xmlse) {
            throw new EncodingException(xmlse);
        }
    }

    /**
     * Set the {@link GetObservationResponse} to be written to stream
     *
     * @param response {@link GetObservationResponse} to write to stream
     */
    protected void setResponse(GetObservationResponse response) {
        this.response = response;
    }

    /**
     * Get the {@link GetObservationResponse} to write to stream
     *
     * @return {@link GetObservationResponse} to write
     */
    protected GetObservationResponse getResponse() {
        return response;
    }

    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED")
    private void writeGetObservationResponseDoc(GetObservationResponse response, EncodingValues encodingValues)
            throws XMLStreamException, EncodingException {
        start(Sos2StreamingConstants.GET_OBSERVATION_RESPONSE);
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        namespace(SosConstants.NS_SOS_PREFIX, Sos2Constants.NS_SOS_20);
        // get observation encoder
        ObservationEncoder<XmlObject, OmObservation> encoder = findObservationEncoder(response.getResponseFormat());
        encodingValues.setAsDocument(true);
        encodingValues.setEncodingNamespace(response.getResponseFormat());
        // write schemaLocation
        schemaLocation(getSchemaLocation(encodingValues, encoder));
        writeNewLine();
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
                        writeObservationData(o, encoder, encodingValues);
                        writeNewLine();
                    } else {
                        ObservationStream merged = value.merge();
                        while (merged.hasNext()) {
                            writeObservationData(merged.next(), encoder, encodingValues);
                            writeNewLine();
                        }
                    }
                } else {
                    writeObservationData(o, encoder, encodingValues);
                    writeNewLine();
                }
            }
        } catch (OwsExceptionReport owse) {
            throw new EncodingException(owse);
        }
        indent--;
        end(Sos2StreamingConstants.GET_OBSERVATION_RESPONSE);
    }

    private Set<SchemaLocation> getSchemaLocation(EncodingValues encodingValue,
                                                  ObservationEncoder<XmlObject, OmObservation> encoder) {
        Set<SchemaLocation> schemaLocations = Sets.newHashSet();

        if (encodingValue.isSetEncoder() && encodingValue.getEncoder() instanceof SchemaAwareEncoder) {
            schemaLocations.addAll(((SchemaAwareEncoder<?, ?>) encodingValue.getEncoder()).getSchemaLocations());
        } else {
            schemaLocations.add(Sos2Constants.SOS_GET_OBSERVATION_SCHEMA_LOCATION);
        }
        if (encoder != null && encoder instanceof SchemaAwareEncoder) {
            schemaLocations.addAll(((SchemaAwareEncoder<?, ?>) encoder).getSchemaLocations());
        }
        return schemaLocations;
    }

    @SuppressWarnings("unchecked")
    private void writeObservationData(OmObservation observation, ObservationEncoder<XmlObject, OmObservation> encoder,
                                      EncodingValues encodingValues) throws XMLStreamException, EncodingException {
        start(Sos2StreamingConstants.OBSERVATION_DATA);
        writeNewLine();
        if (encoder instanceof StreamingEncoder) {
            ((StreamingEncoder<XmlObject, OmObservation>) encoder)
                    .encode(observation, getOutputStream(), encodingValues.setAsDocument(true)
                            .setEmbedded(true).setIndent(indent));
        } else {
            rawText(encoder.encode(observation, encodingValues.getAdditionalValues())
                    .xmlText(XmlOptionsHelper.getInstance().getXmlOptions()));
        }
        indent--;
        writeNewLine();
        end(Sos2StreamingConstants.OBSERVATION_DATA);
        indent++;
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
        Encoder<XmlObject, OmObservation> encoder = encoderRepository
                .getEncoder(new XmlEncoderKey(responseFormat, OmObservation.class));
        if (encoder == null) {
            return null;
        } else if (encoder instanceof ObservationEncoder) {
            ObservationEncoder<XmlObject, OmObservation> oe = (ObservationEncoder<XmlObject, OmObservation>) encoder;
            return oe.isObservationAndMeasurmentV20Type() ? oe : null;
        } else {
            throw new EncodingException("Error while encoding response, encoder is not of type ObservationEncoder!");
        }
    }

}
