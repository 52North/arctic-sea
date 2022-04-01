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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.n52.janmayen.Producer;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.DeleteSensorResponse;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import com.google.common.collect.Maps;

import net.opengis.swes.x20.DeleteSensorResponseDocument;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
//@Deprecated
public class DeleteSensorResponseEncoderTest {

    private static DeleteSensorResponseEncoder instance;

    @BeforeAll
    public static void setup() {
        instance = new DeleteSensorResponseEncoder();
        Producer<XmlOptions> options = () -> new XmlOptions();
        instance.setXmlOptions(options);

        EncoderRepository encoderRepository = new EncoderRepository();
        encoderRepository.setEncoders(Arrays.asList(instance));
        encoderRepository.init();

        SchemaRepository schemaRepository = new SchemaRepository();
        schemaRepository.setEncoderRepository(encoderRepository);
        schemaRepository.init();

        instance.setSchemaRepository(schemaRepository);
        instance.setEncoderRepository(encoderRepository);
        instance.setXmlOptions(options);
        schemaRepository.init();
    }

    @Test
    public void should_return_correct_encoder_keys() {
        Set<EncoderKey> returnedKeySet = instance.getKeys();
        assertEquals(returnedKeySet.size(), 3);
        assertTrue(returnedKeySet.contains(new XmlEncoderKey(SwesConstants.NS_SWES_20, DeleteSensorResponse.class)));
        assertTrue(returnedKeySet.contains(new OperationResponseEncoderKey(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                                                                           Sos2Constants.Operations.DeleteSensor, MediaTypes.TEXT_XML)));
        assertTrue(returnedKeySet.contains(new OperationResponseEncoderKey(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                                                                           Sos2Constants.Operations.DeleteSensor, MediaTypes.APPLICATION_XML)));
    }

    @Test
    public void should_return_emptyMap_for_supportedTypes() {
        assertNotNull(instance.getSupportedTypes());
        assertTrue(instance.getSupportedTypes().isEmpty());
    }

    @Test
    public void should_return_emptySet_for_conformanceClasses() {
        assertNotNull(instance.getConformanceClasses(SosConstants.SOS, Sos2Constants.SERVICEVERSION));
        assertTrue(instance.getConformanceClasses(SosConstants.SOS, Sos2Constants.SERVICEVERSION)
                .isEmpty());
    }

    @Test
    public void should_add_own_prefix_to_prefixMap() {
        Map<String, String> prefixMap = Maps.newHashMap();
        instance.addNamespacePrefixToMap(prefixMap);
        assertFalse(prefixMap.isEmpty());
        assertTrue(prefixMap.containsKey(SwesConstants.NS_SWES_20));
        assertTrue(prefixMap.containsValue(SwesConstants.NS_SWES_PREFIX));
    }

    @Test
    public void should_not_fail_if_prefixMap_is_null() {
        instance.addNamespacePrefixToMap(null);
    }

    @Test
    public void should_return_contentType_xml() {
        assertEquals(instance.getContentType(), MediaTypes.TEXT_XML);
    }

    @Test
    public void should_return_correct_schema_location() {
        assertEquals(instance.getSchemaLocations().size(), 1);
        SchemaLocation schemLoc = instance.getSchemaLocations().iterator().next();
        assertEquals(schemLoc.getNamespace(), "http://www.opengis.net/swes/2.0");
        assertEquals(schemLoc.getSchemaFileUrl(), "http://schemas.opengis.net/swes/2.0/swes.xsd");
    }

    @Test
    public void should_return_exception_if_received_null() throws EncodingException {
        Assertions.assertThrows(UnsupportedEncoderInputException.class, () -> {
            instance.encode(null);
        });
        Assertions.assertThrows(UnsupportedEncoderInputException.class, () -> {
            instance.encode(null, new ByteArrayOutputStream());
        });
        Assertions.assertThrows(UnsupportedEncoderInputException.class, () -> {
            instance.encode(null, EncodingContext.empty());
        });



    }

    @Test
    public void should_encode_DeleteSensor_response() throws EncodingException {
        final DeleteSensorResponse response = new DeleteSensorResponse();
        final String deletedProcedure = "deletedProcedure";
        response.setDeletedProcedure(deletedProcedure);
        final XmlObject encodedResponse = instance.encode(response);
        assertTrue(encodedResponse instanceof DeleteSensorResponseDocument);
        final DeleteSensorResponseDocument doc = (DeleteSensorResponseDocument) encodedResponse;
        assertFalse(doc.isNil());
        assertEquals(doc.getDeleteSensorResponse().getDeletedProcedure(), deletedProcedure);
        assertTrue(doc.validate());
    }
}
