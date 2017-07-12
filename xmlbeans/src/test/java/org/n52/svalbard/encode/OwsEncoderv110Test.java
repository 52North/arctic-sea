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

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.util.Arrays;

import net.opengis.ows.x11.ExceptionDocument;
import net.opengis.ows.x11.ExceptionReportDocument;
import net.opengis.ows.x11.ServiceIdentificationDocument.ServiceIdentification;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Before;
import org.junit.Test;

import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsServiceIdentification;
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.svalbard.SosHelperValues;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 * J&uuml;rrens</a>
 *
 * @since 1.0.0
 *
 */
public class OwsEncoderv110Test {

    private final OwsEncoderv110 encoder = new OwsEncoderv110();

    private EncoderRepository encoderRepository = new EncoderRepository();

    @Before
    public void init() {
        encoder.setXmlOptions(XmlOptions::new);
        encoder.setEncoderRepository(encoderRepository);
        encoderRepository.setEncoders(Arrays.asList(encoder));
        encoderRepository.init();
    }

    @Test
    public final void should_encode_Exception_into_owsExceptionReport_by_default() throws EncodingException {
        assertThat(encoder.encode(generateException()), instanceOf(ExceptionReportDocument.class));
    }

    @Test
    public void should_encode_Exception_into_owsException_when_using_flag() throws EncodingException {
        assertThat(encoder.encode(generateException(), encodeInObservationMap()), instanceOf(ExceptionDocument.class));
    }

    @SuppressWarnings("unchecked")
    // see
    // http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ300
    // for more details
    private EncodingContext encodeInObservationMap() {
        return EncodingContext.of(SosHelperValues.ENCODE_OWS_EXCEPTION_ONLY);
    }

    private NoApplicableCodeException generateException() {
        final NoApplicableCodeException nace = new NoApplicableCodeException();
        nace.setVersion(Sos2Constants.SERVICEVERSION);
        return nace;
    }

    @Test
    public void should_encode_service_identification_without_service_type_codespace() throws EncodingException {
        String serviceTypeValue = "serviceType";
        OwsServiceIdentification serviceId = new OwsServiceIdentification(new OwsCode(serviceTypeValue), null, null, null, null, null, null, null);
        XmlObject xbEncoded = encodeObjectToXml(OWSConstants.NS_OWS, serviceId);
        assertThat(xbEncoded, instanceOf(ServiceIdentification.class));
        ServiceIdentification xbServiceId = (ServiceIdentification) xbEncoded;
        assertThat(xbServiceId.getServiceType().getStringValue(), equalTo(serviceTypeValue));
        assertThat(xbServiceId.getServiceType().getCodeSpace(), nullValue());
    }

    @Test
    public void should_encode_service_identification_with_service_type_codespace() throws EncodingException {
        String serviceTypeValue = "serviceType";
        String serviceTypeCodeSpaceValue = "codeSpace";
        OwsServiceIdentification serviceId = new OwsServiceIdentification(new OwsCode(serviceTypeValue, URI.create(serviceTypeCodeSpaceValue)), null, null, null, null, null, null, null);
        XmlObject xbEncoded = encodeObjectToXml(OWSConstants.NS_OWS, serviceId);
        assertThat(xbEncoded, instanceOf(ServiceIdentification.class));
        ServiceIdentification xbServiceId = (ServiceIdentification) xbEncoded;
        assertThat(xbServiceId.getServiceType().getStringValue(), equalTo(serviceTypeValue));
        assertThat(xbServiceId.getServiceType().getCodeSpace(), equalTo(serviceTypeCodeSpaceValue));
    }

    private XmlObject encodeObjectToXml(String namespace, OwsServiceIdentification o) throws EncodingException {
        return (XmlObject)encoderRepository.getEncoder(CodingHelper.getEncoderKey(namespace, o)).encode(o);
    }
}
