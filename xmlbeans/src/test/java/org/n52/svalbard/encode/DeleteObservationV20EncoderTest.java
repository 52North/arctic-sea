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
package org.n52.sos.ext.deleteobservation.v20;

import static java.lang.Boolean.TRUE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.n52.sos.ext.deleteobservation.DeleteObservationConstants.NS_SOSDO_2_0;
import static org.n52.sos.ext.deleteobservation.DeleteObservationConstants.NS_SOSDO_PREFIX;
import static org.n52.sos.ogc.sos.SosConstants.SOS;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.n52.sos.encode.EncoderKey;
import org.n52.sos.encode.VersionedOperationEncoderKey;
import org.n52.sos.encode.XmlEncoderKey;
import org.n52.sos.exception.ows.concrete.UnsupportedEncoderInputException;
import org.n52.sos.ext.deleteobservation.DeleteObservationConstants;
import org.n52.sos.ext.deleteobservation.DeleteObservationResponse;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.util.http.MediaTypes;
import org.n52.sos.w3c.SchemaLocation;

import net.opengis.sosdo.x20.DeleteObservationResponseDocument;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 * @since 4.0.0
 */
public class DeleteObservationEncoderTest {

    private static final String observationId = "test_observation_id";

    private static DeleteObservationEncoder instance;

    private static DeleteObservationResponse incorrectCoreResponseMissingAttributes;

    /*
     * <?xml version="1.0" encoding="UTF-8"?> <sosdo:DeleteObservationResponse
     * version="2.0" service="SOS"
     * xmlns:sosdo="http://www.opengis.net/sosdo/2.0">
     * <sosdo:deletedObservation>
     * test-observation-identifier</sosdo:deletedObservation>
     * </sosdo:DeleteObservationResponse>
     */
    private static DeleteObservationResponse correctCoreResponse;

    @BeforeClass
    public static void initInstance() {
        instance = new DeleteObservationEncoder();
        incorrectCoreResponseMissingAttributes = new DeleteObservationResponse(NS_SOSDO_2_0);
        correctCoreResponse = new DeleteObservationResponse(NS_SOSDO_2_0);
        correctCoreResponse.setObservationId(observationId);
        correctCoreResponse.setService(SOS);
        correctCoreResponse.setVersion(Sos2Constants.SERVICEVERSION);
    }

    @Test
    public void constructorReturnsInstance() {
        final String className = DeleteObservationEncoder.class.getName();
        assertNotNull("Instance is null. Constructor failed", instance);
        assertTrue("Instance of constructed object is not of class" + className,
                instance.getClass().getName().equals(className));
    }

    @Test
    public void testGetEncoderKey() {
        assertNotNull("DecoderKeyTypes is null", instance.getEncoderKeyType());
        EncoderKey key = new XmlEncoderKey(NS_SOSDO_2_0, DeleteObservationResponse.class);
        assertTrue("DecoderKeyTypes does NOT contain " + key, instance.getEncoderKeyType().contains(key));
        key =
                new VersionedOperationEncoderKey(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                        DeleteObservationConstants.Operations.DeleteObservation, MediaTypes.TEXT_XML,
                        DeleteObservationConstants.NS_SOSDO_2_0);
        assertTrue("DecoderKeyTypes does NOT contain " + key, instance.getEncoderKeyType().contains(key));
        key =
                new VersionedOperationEncoderKey(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                        DeleteObservationConstants.Operations.DeleteObservation, MediaTypes.APPLICATION_XML,
                        DeleteObservationConstants.NS_SOSDO_2_0);
        assertTrue("DecoderKeyTypes does NOT contain " + key, instance.getEncoderKeyType().contains(key));
    }

    @Test
    public void testGetConformanceClasses() {
        final Set<String> conformanceClasses = new HashSet<String>(1);
        conformanceClasses.add(DeleteObservationConstants.CONFORMANCE_CLASS_10);
        conformanceClasses.add(DeleteObservationConstants.CONFORMANCE_CLASS_20);
        assertNotNull("ConformanceClasses is null", instance.getConformanceClasses());
        assertEquals("ConformanceClasses size", conformanceClasses.size(), instance.getConformanceClasses().size());
        assertTrue("ConformanceClasses contains " + DeleteObservationConstants.CONFORMANCE_CLASS_20, instance
                .getConformanceClasses().contains(DeleteObservationConstants.CONFORMANCE_CLASS_20));
    }

    @Test
    public void getSupportedTypesReturnsEmptyList() {
        assertNotNull("Supported Types is null", instance.getSupportedTypes());
        assertEquals("Supported Types size ", 0, instance.getSupportedTypes().size());
    }

    @Test(expected = UnsupportedEncoderInputException.class)
    public void encodeNullReturnsNull() throws OwsExceptionReport {
        instance.encode(null);
    }

    @Test(expected = OwsExceptionReport.class)
    public void encodingCoreResponseWithMissingAttributesReturnsNull() throws OwsExceptionReport {
        instance.encode(incorrectCoreResponseMissingAttributes);
    }

    @Test
    public void encodingCorrectXmlObjectReturnsCorrectServiceRequest() throws OwsExceptionReport {
        assertNotNull("Decoding of correct XmlObject returned null", instance.encode(correctCoreResponse));
        assertTrue("Class of Result ",
                instance.encode(correctCoreResponse) instanceof DeleteObservationResponseDocument);
    }

    @Test
    public void should_return_correct_content_type_txtXml() {
        assertThat(instance.getContentType(), is(MediaTypes.TEXT_XML));
    }

    @Test
    public void should_add_correct_namespace_and_prefix_to_given_map() {
        final Map<String, String> givenMap = new HashMap<String, String>(1);

        instance.addNamespacePrefixToMap(givenMap);

        assertThat(givenMap.containsKey(NS_SOSDO_2_0), is(TRUE));
        assertThat(givenMap.containsValue(NS_SOSDO_PREFIX), is(TRUE));
        assertThat(givenMap.get(NS_SOSDO_2_0), is(NS_SOSDO_PREFIX));
    }

    @Test
    public void should_not_throw_exception_when_receiving_invalid_namespace_prefix_maps() {
        instance.addNamespacePrefixToMap(null);
    }

    @Test
    public void should_return_correct_schema_location() {
        assertThat(instance.getSchemaLocations().size(), is(1));
        final SchemaLocation schemLoc = instance.getSchemaLocations().iterator().next();
        assertThat(schemLoc.getNamespace(), is("http://www.opengis.net/sosdo/2.0"));
        assertThat(
                schemLoc.getSchemaFileUrl(),
                is("http://52north.org/schema/sosdo/2.0/sosdo.xsd"));
    }

}
