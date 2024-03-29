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
package org.n52.svalbard.encode;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.UpdateSensorResponse;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Maps;

import net.opengis.swes.x20.UpdateSensorDescriptionResponseDocument;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class UpdateSensorResponseEncoderTest {

    private UpdateSensorResponseEncoder encoder;

    @BeforeEach
    public void setup() {

        EncoderRepository encoderRepository = new EncoderRepository();

        SchemaRepository schemaRepository = new SchemaRepository();
        schemaRepository.setEncoderRepository(encoderRepository);

        encoder = new UpdateSensorResponseEncoder();
        encoder.setEncoderRepository(encoderRepository);
        encoder.setXmlOptions(XmlOptions::new);
        encoder.setSchemaRepository(schemaRepository);

        encoderRepository.setEncoders(Arrays.asList(encoder));
        encoderRepository.init();
        schemaRepository.init();
    }

    @Test
    public void should_return_correct_encoder_keys() {
        Set<EncoderKey> returnedKeySet = encoder.getKeys();
        assertThat(returnedKeySet.size(), is(3));
        assertThat(returnedKeySet, hasItem(new XmlEncoderKey(SwesConstants.NS_SWES_20, UpdateSensorResponse.class)));
        assertThat(returnedKeySet, hasItem(new OperationResponseEncoderKey(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                Sos2Constants.Operations.UpdateSensorDescription, MediaTypes.TEXT_XML)));
        assertThat(returnedKeySet, hasItem(new OperationResponseEncoderKey(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                Sos2Constants.Operations.UpdateSensorDescription, MediaTypes.APPLICATION_XML)));

    }

    @Test
    public void should_return_emptyMap_for_supportedTypes() {
        assertThat(encoder.getSupportedTypes(), is(not(nullValue())));
        assertThat(encoder.getSupportedTypes().isEmpty(), is(TRUE));
    }

    @Test
    public void should_return_emptySet_for_conformanceClasses() {
        assertThat(encoder.getConformanceClasses(SosConstants.SOS, Sos2Constants.SERVICEVERSION), is(not(nullValue())));
        assertThat(encoder.getConformanceClasses(SosConstants.SOS, Sos2Constants.SERVICEVERSION).isEmpty(), is(TRUE));
    }

    @Test
    public void should_add_own_prefix_to_prefixMap() {
        Map<String, String> prefixMap = Maps.newHashMap();
        encoder.addNamespacePrefixToMap(prefixMap);
        assertThat(prefixMap.isEmpty(), is(FALSE));
        assertThat(prefixMap.containsKey(SwesConstants.NS_SWES_20), is(TRUE));
        assertThat(prefixMap.containsValue(SwesConstants.NS_SWES_PREFIX), is(TRUE));
    }

    @Test
    public void should_not_fail_if_prefixMap_is_null() {
        encoder.addNamespacePrefixToMap(null);
    }

    @Test
    public void should_return_contentType_xml() {
        assertThat(encoder.getContentType(), is(MediaTypes.TEXT_XML));
    }

    @Test
    public void should_return_correct_schema_location() {
        assertThat(encoder.getSchemaLocations().size(), is(1));
        SchemaLocation schemLoc = encoder.getSchemaLocations().iterator().next();
        assertThat(schemLoc.getNamespace(), is("http://www.opengis.net/swes/2.0"));
        assertThat(schemLoc.getSchemaFileUrl(), is("http://schemas.opengis.net/swes/2.0/swes.xsd"));
    }

    @Test
    public void should_return_exception_if_received_null() throws EncodingException {
        assertThrows(EncodingException.class, () -> {
            encoder.encode(null);
        });
        assertThrows(EncodingException.class, () -> {
            encoder.encode(null, new ByteArrayOutputStream());
        });
        assertThrows(EncodingException.class, () -> {
            encoder.encode(null, EncodingContext.empty());
        });
    }

    @Test
    public void should_encode_UpdateSensor_response() throws EncodingException {
        final UpdateSensorResponse response = new UpdateSensorResponse();
        final String updatedProcedure = "updatedProcedure";
        response.setUpdatedProcedure(updatedProcedure);



        final XmlObject encodedResponse = encoder.encode(response);

        assertThat(encodedResponse, is(instanceOf(UpdateSensorDescriptionResponseDocument.class)));
        final UpdateSensorDescriptionResponseDocument doc = (UpdateSensorDescriptionResponseDocument) encodedResponse;
        assertThat(doc.isNil(), is(FALSE));
        assertThat(doc.getUpdateSensorDescriptionResponse().getUpdatedProcedure(), is(updatedProcedure));
        assertThat(doc.validate(), is(TRUE));
    }

}
