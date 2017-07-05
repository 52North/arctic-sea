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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.n52.sos.ext.deleteobservation.DeleteObservationConstants.CONFORMANCE_CLASSES;
import static org.n52.sos.ext.deleteobservation.DeleteObservationConstants.NS_SOSDO_2_0;
import static org.n52.sos.ogc.sos.SosConstants.SOS;
import static org.n52.sos.util.CodingHelper.decoderKeysForElements;
import static org.n52.sos.util.CodingHelper.xmlDecoderKeysForOperation;
import static org.n52.sos.util.CollectionHelper.union;

import java.util.Set;

import net.opengis.sosdo.x20.DeleteObservationDocument;

import org.apache.xmlbeans.XmlObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.n52.sos.decode.DecoderKey;
import org.n52.sos.ext.deleteobservation.DeleteObservationConstants;
import org.n52.sos.ext.deleteobservation.DeleteObservationRequest;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.Sos2Constants;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 * @since 4.0.0
 */
public class DeleteObservationDecoderTest {

    private static DeleteObservationDecoder instance;

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

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void initFixtures() {
        incorrectXmlObject = XmlObject.Factory.newInstance();
        dkt =
                union(decoderKeysForElements(NS_SOSDO_2_0, DeleteObservationDocument.class),
                        xmlDecoderKeysForOperation(SOS, Sos2Constants.SERVICEVERSION,
                                DeleteObservationConstants.Operations.DeleteObservation));
    }

    @Before
    public void initInstance() {
        instance = new DeleteObservationDecoder();
        correctXmlObject = DeleteObservationDocument.Factory.newInstance();
        correctXmlObject.addNewDeleteObservation();
    }

    @Test
    public void constructorReturnsInstance() {
        String className = DeleteObservationDecoder.class.getName();
        assertNotNull("Instance is null. Constructor failed", instance);
        assertTrue("Instance of constructed object is not of class" + className,
                instance.getClass().getName().equals(className));
    }

    @Test
    public void testGetDecoderKeyTypes() {
        assertNotNull("DecoderKey is null", instance.getDecoderKeyTypes());
        assertTrue("DecoderKey does NOT equal " + dkt, instance.getDecoderKeyTypes().equals(dkt));
    }

    @Test
    public void testGetConformanceClasses() {
        assertNotNull("ConformanceClasses is null", instance.getConformanceClasses());
        assertTrue("ConformanceClasses contains " + CONFORMANCE_CLASSES,
                instance.getConformanceClasses().equals(CONFORMANCE_CLASSES));
    }

    @Test
    public void getSupportedTypesReturnsEmptyList() {
        assertNotNull("Supported Types is null", instance.getSupportedTypes());
        assertEquals("Supported Types size ", 0, instance.getSupportedTypes().size());
    }

    @Test(expected = OwsExceptionReport.class)
    public void decodeNullThrowsOwsExceptionReport() throws OwsExceptionReport {
        instance.decode(null);
    }

    @Test(expected = OwsExceptionReport.class)
    public void decodingIncorrectXmlObjectThrowsOwsExceptionReport() throws OwsExceptionReport {
        instance.decode(incorrectXmlObject);
    }

    @Test
    public void decodingCorrectXmlObjectReturnsCorrectServiceRequest() throws OwsExceptionReport {
        String className = DeleteObservationRequest.class.getName();
        assertNotNull("Decoding of correct XmlObject returned null", instance.decode(correctXmlObject));
        assertEquals("Class of Result ", className, instance.decode(correctXmlObject).getClass().getName());
    }

    @Test(expected = OwsExceptionReport.class)
    public void should_throw_OwsExceptionReport_when_receving_invalid_DeleteObservationDocument()
            throws OwsExceptionReport {
        correctXmlObject = DeleteObservationDocument.Factory.newInstance();
        instance.decode(correctXmlObject);
    }

}
