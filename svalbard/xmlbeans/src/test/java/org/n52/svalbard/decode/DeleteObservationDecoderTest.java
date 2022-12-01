/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
import static org.n52.shetland.ogc.sos.SosConstants.SOS;
import static org.n52.shetland.ogc.sos.delobs.DeleteObservationConstants.NS_SOSDO_1_0;
import static org.n52.shetland.util.CollectionHelper.union;
import static org.n52.svalbard.util.CodingHelper.decoderKeysForElements;
import static org.n52.svalbard.util.CodingHelper.xmlDecoderKeysForOperation;

import java.util.Arrays;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.delobs.DeleteObservationConstants;
import org.n52.shetland.ogc.sos.delobs.DeleteObservationRequest;
import org.n52.svalbard.decode.exception.DecodingException;

import net.opengis.sosdo.x10.DeleteObservationDocument;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 * @since 1.0.0
 */
public class DeleteObservationDecoderTest implements DeleteDecoderTest{

    private DeleteObservationDecoder decoder;

    private static Set<DecoderKey> dkt;

    private static XmlObject incorrectXmlObject;

    /*
     * <?xml version="1.0" encoding="UTF-8"?> <sosdo:DeleteObservation
     * version="2.0" service="SOS"
     * xmlns:sosdo="http://www.opengis.net/sosdo/1.0">
     * <sosdo:observation>test-observation-identifier</sosdo:observation>
     * </sosdo:DeleteObservation>
     */
    private static DeleteObservationDocument correctXmlObject;

    private static String observationId = "test_obs_id";

    @BeforeEach
    public void setup() {
        DecoderRepository decoderRepository = new DecoderRepository();

        decoder = new DeleteObservationDecoder();
        decoder.setDecoderRepository(decoderRepository);
        decoder.setXmlOptions(XmlOptions::new);

        // GmlDecoderv321 gmlDecoderv321 = new GmlDecoderv321();
        // gmlDecoderv321.setXmlOptions(XmlOptions::new);
        // gmlDecoderv321.setDecoderRepository(decoderRepository);
        //
        // SweCommonDecoderV20 sweCommonDecoderV20 = new SweCommonDecoderV20();
        // sweCommonDecoderV20.setXmlOptions(XmlOptions::new);
        // sweCommonDecoderV20.setDecoderRepository(decoderRepository);

        decoderRepository.setDecoders(Arrays.asList(decoder));
        decoderRepository.init();

        correctXmlObject = DeleteObservationDocument.Factory.newInstance();
        correctXmlObject.addNewDeleteObservation().setObservation(observationId);
    }

    @Test
    public void constructorReturnsdecoder() {
        String className = DeleteObservationDecoder.class.getName();
        assertNotNull(decoder, "decoder is null. Constructor failed");
        assertTrue(decoder.getClass().getName().equals(className),
                "decoder of constructed object is not of class" + className);
    }

    @Test
    public void testGetDecoderKeyTypes() {
        assertNotNull(decoder.getKeys(), "DecoderKey is null");
        assertTrue(decoder.getKeys().equals(dkt), "DecoderKey does NOT equal " + dkt);
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
        assertEquals(observationId,
                decoder.decode(correctXmlObject).getObservationIdentifiers().iterator().next(),"Id of observation to delete");
    }

    @BeforeAll
    public static void initFixtures() {
        incorrectXmlObject = XmlObject.Factory.newInstance();
        dkt = union(decoderKeysForElements(NS_SOSDO_1_0, DeleteObservationDocument.class), xmlDecoderKeysForOperation(
                SOS, Sos2Constants.SERVICEVERSION, DeleteObservationConstants.Operations.DeleteObservation));
    }

    @Override
    public Decoder<?, XmlObject> getDecoder() {
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
