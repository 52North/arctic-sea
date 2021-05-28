/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.svalbard.decode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.sos.delobs.DeleteObservationRequest;
import org.n52.svalbard.decode.exception.DecodingException;

import net.opengis.sosdo.x20.DeleteObservationDocument;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 * @since 1.0.0
 */
public class DeleteObservationV20DecoderTest implements DeleteDecoderTest {

    private static String observationId = "test_obs_id";
    private static XmlObject incorrectXmlObject;
    private static DeleteObservationDocument correctXmlObject;
    private DeleteObservationV20Decoder decoder;

    @BeforeEach
    public void setup() {
        DecoderRepository decoderRepository = new DecoderRepository();

        decoder = new DeleteObservationV20Decoder();
        decoder.setDecoderRepository(decoderRepository);
        decoder.setXmlOptions(XmlOptions::new);

        decoderRepository.setDecoders(Arrays.asList(decoder));
        decoderRepository.init();

        correctXmlObject = DeleteObservationDocument.Factory.newInstance();
        correctXmlObject.addNewDeleteObservation().addObservation(observationId);
    }

    /*
     * <?xml version="1.0" encoding="UTF-8"?> <sosdo:DeleteObservation
     * version="2.0" service="SOS"
     * xmlns:sosdo="http://www.opengis.net/sosdo/1.0">
     * <sosdo:observation>test-observation-identifier</sosdo:observation>
     * </sosdo:DeleteObservation>
     */
    @BeforeAll
    public static void initFixtures() {
        incorrectXmlObject = XmlObject.Factory.newInstance();
    }

    @Test
    public void constructorReturnsdecoder() {
        String className = DeleteObservationV20Decoder.class.getName();
        assertNotNull(decoder, "decoder is null. Constructor failed");
        assertTrue(
                decoder.getClass().getName().equals(className), "decoder of constructed object is not of class" + className);
    }

    @Test
    public void testGetDecoderKeyTypes() {
        assertNotNull(decoder.getKeys(), "DecoderKey is null");
    }

    @Test
    public void getSupportedTypesReturnsEmptyList() {
        assertNotNull(decoder.getSupportedTypes(), "Supported Types is null");
        assertEquals(0, decoder.getSupportedTypes().size(), "Supported Types size ");
    }

    @Test
    public void decodingCorrectXmlObjectReturnsCorrectServiceRequest() throws DecodingException {
        String className = DeleteObservationRequest.class.getName();
        assertNotNull(decoder.decode(correctXmlObject), "Decoding of correct XmlObject returned null");
        assertEquals(className, decoder.decode(correctXmlObject).getClass().getName(), "Class of Result ");
    }

    @Override
    public Decoder getDecoder() {
        return decoder;
    }

    @Override
    public XmlObject getIncorrectXmlObject() {
        return incorrectXmlObject;
    }

    @Override
    public XmlObject getCorrectXmlObject() {
        return DeleteObservationDocument.Factory.newInstance();
    }

}
