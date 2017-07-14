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
package org.n52.svalbard.decode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.n52.shetland.ogc.sos.delobs.DeleteObservationRequest;
import org.n52.svalbard.decode.exception.DecodingException;

import net.opengis.sosdo.x20.DeleteObservationDocument;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 * @since 1.0.0
 */
public class DeleteObservationV20DecoderTest {

    private static String observationId = "test_obs_id";
    private static XmlObject incorrectXmlObject;
    private static DeleteObservationDocument correctXmlObject;
    private DeleteObservationV20Decoder decoder;

    @Before
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
    @BeforeClass
    public static void initFixtures() {
        incorrectXmlObject = XmlObject.Factory.newInstance();
    }

    @Test
    public void constructorReturnsdecoder() {
        String className = DeleteObservationV20Decoder.class.getName();
        assertNotNull("decoder is null. Constructor failed", decoder);
        assertTrue("decoder of constructed object is not of class" + className,
                decoder.getClass().getName().equals(className));
    }

    @Test
    public void testGetDecoderKeyTypes() {
        assertNotNull("DecoderKey is null", decoder.getKeys());
    }

    @Test
    public void getSupportedTypesReturnsEmptyList() {
        assertNotNull("Supported Types is null", decoder.getSupportedTypes());
        assertEquals("Supported Types size ", 0, decoder.getSupportedTypes().size());
    }

    @Test(expected = DecodingException.class)
    public void decodeNullThrowsDecodingException() throws DecodingException {
        decoder.decode(null);
    }

    @Test(expected = DecodingException.class)
    public void decodingIncorrectXmlObjectThrowsDecodingException() throws DecodingException {
        decoder.decode(incorrectXmlObject);
    }

    @Test
    public void decodingCorrectXmlObjectReturnsCorrectServiceRequest() throws DecodingException {
        String className = DeleteObservationRequest.class.getName();
        assertNotNull("Decoding of correct XmlObject returned null", decoder.decode(correctXmlObject));
        assertEquals("Class of Result ", className, decoder.decode(correctXmlObject).getClass().getName());
    }

    @Test(expected = DecodingException.class)
    public void should_throw_DecodingException_when_receving_invalid_DeleteObservationDocument()
            throws DecodingException {
        correctXmlObject = DeleteObservationDocument.Factory.newInstance();
        decoder.decode(correctXmlObject);
    }

}
