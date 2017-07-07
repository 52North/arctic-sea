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

import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.sos.response.AbstractObservationResponse;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @param <T> the response type
 *
 * @since 1.0.0
 */
public abstract class AbstractObservationResponseEncoder<T extends AbstractObservationResponse>
        extends AbstractSosResponseEncoder<T> {

    public AbstractObservationResponseEncoder(String operation, Class<T> responseType) {
        super(operation, responseType);
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
        Encoder<XmlObject, OmObservation> encoder = getEncoder(new XmlEncoderKey(responseFormat, OmObservation.class));
        if (encoder == null) {
            return null;
        } else if (encoder instanceof ObservationEncoder) {
            ObservationEncoder<XmlObject, OmObservation> oe = (ObservationEncoder<XmlObject, OmObservation>) encoder;
            return oe.isObservationAndMeasurmentV20Type() ? oe : null;
        } else {
            throw new EncodingException("Error while encoding response, encoder is not of type ObservationEncoder!");
        }
    }

    /**
     * Finds a compatible response encoder to delegate to.
     *
     * @param responseFormat the response format
     *
     * @return the encoder
     *
     * @throws org.n52.svalbard.encode.exception.EncodingException if no encoder is found
     */
    private Encoder<XmlObject, T> findResponseEncoder(String responseFormat) throws EncodingException {
        return getEncoder(responseFormat, getResponseType());
    }

    @Override
    protected XmlObject create(T response) throws EncodingException {
        final String responseFormat = response.getResponseFormat();
        // search for an O&M2 encoder for this response format
        ObservationEncoder<XmlObject, OmObservation> encoder = findObservationEncoder(responseFormat);
        if (encoder != null) {
            // encode the response as a GetObservationResponseDocument
            return createResponse(encoder, response);
        }
        // there is no O&M2 compatible observation encoder:
        // search for a encoder for the response and delegate
        return findResponseEncoder(responseFormat).encode(response);
    }

    @Override
    protected void create(T response, OutputStream outputStream, EncodingContext encodingValues)
            throws EncodingException {
        final String responseFormat = response.getResponseFormat();
        // search for an O&M2 encoder for this response format
        ObservationEncoder<XmlObject, OmObservation> encoder = findObservationEncoder(responseFormat);
        if (encoder != null) {
            // encode the response as a GetObservationResponseDocument
            createResponse(encoder, response, outputStream, encodingValues);
        }
    }

    /**
     * Create a response using the provided O&M2 compatible observation encoder.
     *
     * @param encoder  the encoder
     * @param response the response
     *
     * @return the encoded response
     *
     * @throws EncodingException if an error occurs
     */
    protected abstract XmlObject createResponse(ObservationEncoder<XmlObject, OmObservation> encoder, T response)
            throws EncodingException;

    /**
     * Override this method in concrete response encoder if streaming is supported for this operations.
     *
     * @param encoder        the encoder
     * @param response       the response
     * @param outputStream   the output stream
     * @param encodingValues the encoding context
     *
     * @throws EncodingException if the encoding fails
     */
    protected void createResponse(ObservationEncoder<XmlObject, OmObservation> encoder, T response,
                                  OutputStream outputStream, EncodingContext encodingValues) throws EncodingException {
        super.create(response, outputStream, encodingValues);
    }

}
