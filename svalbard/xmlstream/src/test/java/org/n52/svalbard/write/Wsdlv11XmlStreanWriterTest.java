/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.write;

import static org.n52.shetland.ogc.ows.OWSConstants.NS_OWS;
import static org.n52.shetland.ogc.ows.OWSConstants.NS_OWS_PREFIX;
import static org.n52.shetland.ogc.sos.Sos2Constants.NS_SOS_20;
import static org.n52.shetland.ogc.sos.SosConstants.NS_SOS_PREFIX;
import static org.n52.shetland.ogc.swes.SwesConstants.NS_SWES_20;
import static org.n52.shetland.ogc.swes.SwesConstants.NS_SWES_PREFIX;
import static org.n52.shetland.w3c.wsdl.WSDLConstants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlObject;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.w3c.wsdl.Binding;
import org.n52.shetland.w3c.wsdl.BindingFault;
import org.n52.shetland.w3c.wsdl.BindingInput;
import org.n52.shetland.w3c.wsdl.BindingOperation;
import org.n52.shetland.w3c.wsdl.BindingOutput;
import org.n52.shetland.w3c.wsdl.Definitions;
import org.n52.shetland.w3c.wsdl.Fault;
import org.n52.shetland.w3c.wsdl.Include;
import org.n52.shetland.w3c.wsdl.Input;
import org.n52.shetland.w3c.wsdl.Message;
import org.n52.shetland.w3c.wsdl.Operation;
import org.n52.shetland.w3c.wsdl.Output;
import org.n52.shetland.w3c.wsdl.Part;
import org.n52.shetland.w3c.wsdl.Port;
import org.n52.shetland.w3c.wsdl.PortType;
import org.n52.shetland.w3c.wsdl.Schema;
import org.n52.shetland.w3c.wsdl.Service;
import org.n52.shetland.w3c.wsdl.Types;
import org.n52.shetland.w3c.wsdl.http.HttpAddress;
import org.n52.shetland.w3c.wsdl.http.HttpBinding;
import org.n52.shetland.w3c.wsdl.mime.MimeXml;
import org.n52.shetland.w3c.wsdl.soap.SoapOperation;
import org.n52.svalbard.encode.EncoderFlags;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.StreamingEncoderFlags;
import org.n52.svalbard.encode.exception.EncodingException;

public class Wsdlv11XmlStreanWriterTest {

    @Test
    public void encode_service() throws EncodingException {
        Definitions definitions = new Definitions();
        definitions.addNamespace(NS_HTTP_PREFIX, NS_HTTP);
        definitions.addNamespace(NS_MIME_PREFIX, NS_MIME);
        definitions.addNamespace(NS_SOAP_PREFIX, NS_SOAP);
        definitions.addNamespace(NS_SOAP_12_PREFIX, NS_SOAP_12);
        definitions.addNamespace(NS_SOSW_PREFIX, NS_SOSW);
        definitions.addNamespace(NS_WSAM_PREFIX, NS_WSAM);
        definitions.addNamespace(NS_WSDL_PREFIX, NS_WSDL);
        definitions.addNamespace(NS_XSD_PREFIX, NS_XSD);
        definitions.addNamespace(NS_OWS_PREFIX, NS_OWS);
        definitions.addNamespace(NS_SOS_PREFIX, NS_SOS_20);
        definitions.addNamespace(NS_SWES_PREFIX, NS_SWES_20);

        Types types = new Types();
        types.addExtensibilityElement(new Schema("qualified", "http://www.opengis.net/sos/2.0",
                new Include("http://www.opengis.net/sos/2.0", "http://schemas.opengis.net/sos/2.0/sos.xsd")));
        types.addExtensibilityElement(new Schema("qualified", "http://www.opengis.net/ows/1.1",
                new Include("http://www.opengis.net/ows/1.1", "http://schemas.opengis.net/ows/1.1.0/owsAll.xsd")));
        types.addExtensibilityElement(new Schema("qualified", "http://www.opengis.net/swes/2.0",
                new Include("http://www.opengis.net/swes/2.0", "http://schemas.opengis.net/swes/2.0/swes.xsd")));
        definitions.addType(types);

        Message message = new Message("GetResultTemplate200RequestMessage");
        definitions.addMessage(message);
        Part part = new Part("body");
        part.setElement(new QName("", "GetResultTemplate", "sos"));
        message.addPart(part);

        PortType portType = new PortType("SosGetPortType");
        definitions.addPortType(portType);
        Operation operation = new Operation("DescribeSensor200");
        portType.addOperation(operation);
        operation.setInput(new Input("DescribeSensor200RequestMessage", SoapRequestActionUris.DESCRIBE_SENSOR,
                new QName("", "DescribeSensor200RequestMessage", "sosw")));
        operation.setOutput(new Output("DescribeSensor200ResponseMessage", SoapResponseActionUris.DESCRIBE_SENSOR,
                new QName("", "DescribeSensor200ResponseMessage", "sosw")));
        operation.addFault(new Fault("ExceptionMessage", URI.create("http://www.opengis.net/ows/1.1/Exception"),
                new QName("", "ExceptionMessage", "sosw")));

        Binding binding = new Binding("SosPoxBinding", QN_SOSW_POST_PORT_TYPE);
        binding.addExtensibilityElement(new HttpBinding(POX_HTTP_VERB));
        BindingOperation bindingOperation = new BindingOperation("UpdateSensorDescription200");
        binding.addBindingOperation(bindingOperation);
        bindingOperation.addExtensibilityElement(
                new SoapOperation("document", URI.create("http://www.opengis.net/swes/2.0/UpdateSensorDescription")));
        BindingInput bindingInput = new BindingInput();
        bindingOperation.setInput(bindingInput);
        bindingInput.addExtensibilityElement(new MimeXml());
        BindingOutput bindingOutput = new BindingOutput();
        bindingOperation.setOutput(bindingOutput);
        bindingOutput.addExtensibilityElement(new MimeXml());
        BindingFault bindingFault = new BindingFault("ExceptionMessage");
        bindingOperation.addBindingFault(bindingFault);

        definitions.addBinding(binding);

        Port port = new Port();
        port.setBinding(binding);
        port.setName("SosKvpPort");
        port.addExtensibilityElement(new HttpAddress(URI.create("http://localhost:8080/52n-sos-webapp/service")));

        Service service = new Service(SosConstants.SOS);
        service.addPort(port);
        definitions.addService(service);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            new Wsdlv11XmlStreamWriter(EncodingContext.of(EncoderFlags.ENCODER_REPOSITORY, new EncoderRepository())
                    .with(StreamingEncoderFlags.EMBEDDED, false), baos, definitions).write();
            ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
            XmlObject.Factory.parse(in);
        } catch (Exception e) {
            throw new EncodingException(e);
        }

    }

}
