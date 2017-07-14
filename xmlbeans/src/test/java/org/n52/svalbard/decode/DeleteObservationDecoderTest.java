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
import static org.n52.shetland.ogc.sos.SosConstants.SOS;
import static org.n52.shetland.ogc.sos.delobs.DeleteObservationConstants.NS_SOSDO_1_0;
import static org.n52.shetland.util.CollectionHelper.union;
import static org.n52.svalbard.util.CodingHelper.decoderKeysForElements;
import static org.n52.svalbard.util.CodingHelper.xmlDecoderKeysForOperation;

import java.util.Arrays;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
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
public class DeleteObservationDecoderTest {

    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    private DeleteObservationDecoder decoder;

    @Before
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
    }

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

    @Before
    public void initdecoder() {
        decoder = new DeleteObservationDecoder();
        correctXmlObject = DeleteObservationDocument.Factory.newInstance();
        correctXmlObject.addNewDeleteObservation().setObservation(observationId);
    }

    @Test
    public void constructorReturnsdecoder() {
        String className = DeleteObservationDecoder.class.getName();
        assertNotNull("decoder is null. Constructor failed", decoder);
        assertTrue("decoder of constructed object is not of class" + className,
                decoder.getClass().getName().equals(className));
    }

    @Test
    public void testGetDecoderKeyTypes() {
        assertNotNull("DecoderKey is null", decoder.getKeys());
        assertTrue("DecoderKey does NOT equal " + dkt, decoder.getKeys().equals(dkt));
    }

    @Test
    public void getSupportedTypesReturnsEmptyList() {
        assertNotNull("Supported Types is null", decoder.getSupportedTypes());
        assertEquals("Supported Types size ", 0, decoder.getSupportedTypes().size());
    }

    @Test(expected = DecodingException.class)
    public void decodeNullThrowsOwsExceptionReport() throws DecodingException {
        decoder.decode(null);
    }

    @Test(expected = DecodingException.class)
    public void decodingIncorrectXmlObjectThrowsOwsExceptionReport() throws DecodingException {
        decoder.decode(incorrectXmlObject);
    }

    @Test
    public void decodingCorrectXmlObjectReturnsCorrectServiceRequest() throws DecodingException {
        String className = DeleteObservationRequest.class.getName();
        assertNotNull("Decoding of correct XmlObject returned null", decoder.decode(correctXmlObject));
        assertEquals("Class of Result ", className, decoder.decode(correctXmlObject).getClass().getName());
        assertEquals("Id of observation to delete", observationId,
                decoder.decode(correctXmlObject).getObservationIdentifiers().iterator().next());
    }

    @Test(expected = DecodingException.class)
    public void should_throw_OwsExceptionReport_when_receving_invalid_DeleteObservationDocument()
            throws DecodingException {
        correctXmlObject = DeleteObservationDocument.Factory.newInstance();
        decoder.decode(correctXmlObject);
    }

    @BeforeClass
    public static void initFixtures() {
        incorrectXmlObject = XmlObject.Factory.newInstance();
        dkt = union(decoderKeysForElements(NS_SOSDO_1_0, DeleteObservationDocument.class), xmlDecoderKeysForOperation(
                SOS, Sos2Constants.SERVICEVERSION, DeleteObservationConstants.Operations.DeleteObservation));
    }

}
