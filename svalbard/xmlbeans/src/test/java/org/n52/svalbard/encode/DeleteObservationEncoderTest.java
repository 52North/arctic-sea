/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.n52.janmayen.Producer;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.delobs.DeleteObservationConstants;
import org.n52.shetland.ogc.sos.delobs.DeleteObservationResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import net.opengis.sosdo.x10.DeleteObservationResponseDocument;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 * @since 1.0.0
 */
public class DeleteObservationEncoderTest {

    private static final String observationId = "test_observation_id";

    private static DeleteObservationEncoder instance;

    /*
     * <?xml version="1.0" encoding="UTF-8"?> <sosdo:DeleteObservationResponse
     * version="2.0" service="SOS"
     * xmlns:sosdo="http://www.opengis.net/sosdo/1.0">
     * <sosdo:deletedObservation>
     * test-observation-identifier</sosdo:deletedObservation>
     * </sosdo:DeleteObservationResponse>
     */
    private static DeleteObservationResponse correctCoreResponse;

    @BeforeAll
    public static void initInstance() {
        instance = new DeleteObservationEncoder();
        Producer<XmlOptions> options = () -> new XmlOptions();

        EncoderRepository encoderRepository = new EncoderRepository();
        encoderRepository.setEncoders(Arrays.asList(instance));
        encoderRepository.init();

        SchemaRepository schemaRepository = new SchemaRepository();
        schemaRepository.setEncoderRepository(encoderRepository);
        schemaRepository.init();

        instance.setSchemaRepository(schemaRepository);
        instance.setEncoderRepository(encoderRepository);
        instance.setXmlOptions(options);

        correctCoreResponse = new DeleteObservationResponse(DeleteObservationConstants.NS_SOSDO_1_0);
        correctCoreResponse.setObservationId(observationId);
        correctCoreResponse.setService(SosConstants.SOS);
        correctCoreResponse.setVersion(Sos2Constants.SERVICEVERSION);
    }

    @Test
    public void constructorReturnsInstance() {
        final String className = DeleteObservationEncoder.class.getName();
        assertNotNull(instance, "Instance is null. Constructor failed");
        assertTrue(instance.getClass().getName().equals(className), "Instance of constructed object is not of class" + className);
    }

    @Test
    public void testGetEncoderKey() {
        assertNotNull(instance.getKeys(), "DecoderKeyTypes is null");
        EncoderKey key = new XmlEncoderKey(DeleteObservationConstants.NS_SOSDO_1_0, DeleteObservationResponse.class);
        assertTrue(instance.getKeys().contains(key), "DecoderKeyTypes does NOT contain " + key);
        key =
                new OperationResponseEncoderKey(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                        DeleteObservationConstants.Operations.DeleteObservation, MediaTypes.TEXT_XML);
        assertTrue(instance.getKeys().contains(key), "DecoderKeyTypes does NOT contain " + key);
        key =
                new OperationResponseEncoderKey(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                        DeleteObservationConstants.Operations.DeleteObservation, MediaTypes.APPLICATION_XML);
        assertTrue(instance.getKeys().contains(key), "DecoderKeyTypes does NOT contain " + key);
    }

    @Test
    public void getSupportedTypesReturnsEmptyList() {
        assertNotNull(instance.getSupportedTypes(), "Supported Types is null");
        assertEquals(instance.getSupportedTypes().size(), 0, "Supported Types size ");
    }

    @Test
    public void encodeNullReturnsNull() throws EncodingException {
        Assertions.assertThrows(UnsupportedEncoderInputException.class, () -> {
            instance.encode(null);
        });
    }

    @Test
    public void encodeCorrectCoreResponse() throws EncodingException {
        final DeleteObservationResponse correctCoreResponse = new DeleteObservationResponse(DeleteObservationConstants.NS_SOSDO_1_0);
        correctCoreResponse.setService("SOS");
        correctCoreResponse.setVersion("2.0.0");
        correctCoreResponse.setObservationId(observationId);
        assertNotNull(instance.encode(correctCoreResponse), "Encoding correct core response returns null");
        assertTrue(
                instance.encode(correctCoreResponse).xmlText().indexOf(observationId) != -1, "Observation ID NOT contained in response");
    }

    @Test
    public void encodingCorrectXmlObjectReturnsCorrectServiceRequest() throws EncodingException {
        XmlObject encoded = instance.encode(correctCoreResponse);
        assertNotNull(encoded, "Decoding of correct XmlObject returned null");
        assertTrue(encoded instanceof DeleteObservationResponseDocument, "Class of Result ");
        assertEquals(observationId,
                ((DeleteObservationResponseDocument) encoded).getDeleteObservationResponse().getDeletedObservation(),
                "Id of observation to delete");
    }

    @Test
    public void should_return_correct_content_type_txtXml() {
        assertEquals(MediaTypes.TEXT_XML, instance.getContentType());
    }

    @Test
    public void should_add_correct_namespace_and_prefix_to_given_map() {
        final Map<String, String> givenMap = new HashMap<>(1);

        instance.addNamespacePrefixToMap(givenMap);

        assertTrue(givenMap.containsKey(DeleteObservationConstants.NS_SOSDO_1_0));
        assertTrue(givenMap.containsValue(DeleteObservationConstants.NS_SOSDO_PREFIX));
        assertEquals(givenMap.get(DeleteObservationConstants.NS_SOSDO_1_0), DeleteObservationConstants.NS_SOSDO_PREFIX);
    }

    @Test
    public void should_not_throw_exception_when_receiving_invalid_namespace_prefix_maps() {
        instance.addNamespacePrefixToMap(null);
    }

    @Test
    public void should_return_correct_schema_location() {
        assertEquals(instance.getSchemaLocations().size(), 1);
        final SchemaLocation schemLoc = instance.getSchemaLocations().iterator().next();
        assertEquals(schemLoc.getNamespace(), DeleteObservationConstants.NS_SOSDO_1_0);
        assertEquals(schemLoc.getSchemaFileUrl(), DeleteObservationConstants.NS_SOSDO_1_0_SCHEMA_LOCATION);
    }
}
