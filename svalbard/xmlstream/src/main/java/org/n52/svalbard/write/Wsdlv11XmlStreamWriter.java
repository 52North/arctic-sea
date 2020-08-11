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

import java.io.OutputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.shetland.w3c.wsdl.AbstractAddress;
import org.n52.shetland.w3c.wsdl.AbstractWsdl;
import org.n52.shetland.w3c.wsdl.Binding;
import org.n52.shetland.w3c.wsdl.BindingFault;
import org.n52.shetland.w3c.wsdl.BindingMessage;
import org.n52.shetland.w3c.wsdl.BindingOperation;
import org.n52.shetland.w3c.wsdl.Definitions;
import org.n52.shetland.w3c.wsdl.ExtensibilityElement;
import org.n52.shetland.w3c.wsdl.Fault;
import org.n52.shetland.w3c.wsdl.Include;
import org.n52.shetland.w3c.wsdl.Message;
import org.n52.shetland.w3c.wsdl.Operation;
import org.n52.shetland.w3c.wsdl.Param;
import org.n52.shetland.w3c.wsdl.Part;
import org.n52.shetland.w3c.wsdl.Port;
import org.n52.shetland.w3c.wsdl.PortType;
import org.n52.shetland.w3c.wsdl.Schema;
import org.n52.shetland.w3c.wsdl.Service;
import org.n52.shetland.w3c.wsdl.Types;
import org.n52.shetland.w3c.wsdl.WSDLConstants;
import org.n52.shetland.w3c.wsdl.http.HttpBinding;
import org.n52.shetland.w3c.wsdl.http.HttpOperation;
import org.n52.shetland.w3c.wsdl.http.HttpUrlEncoded;
import org.n52.shetland.w3c.wsdl.mime.MimeXml;
import org.n52.shetland.w3c.wsdl.soap.SoapBinding;
import org.n52.shetland.w3c.wsdl.soap.SoapBody;
import org.n52.shetland.w3c.wsdl.soap.SoapFault;
import org.n52.shetland.w3c.wsdl.soap.SoapOperation;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.exception.EncodingException;

public class Wsdlv11XmlStreamWriter extends XmlStreamWriter<Definitions> {

    public Wsdlv11XmlStreamWriter(EncodingContext context, OutputStream outputStream, Definitions element)
            throws XMLStreamException {
        super(context, outputStream, element);
    }

    @Override
    public void write() throws XMLStreamException, EncodingException {
        Definitions definitions = getElement();
        start(definitions.getQName());
        for (Entry<String, String> entry : definitions.getNamespaces().entrySet()) {
            namespace(entry.getKey(), entry.getValue());
        }
        if (definitions.isSetTargetNamespace()) {
            attr(WSDLConstants.AN_XSD_TARGET_NAMESPACE, definitions.getTargetNamespace());
        }
        encodeTypes(definitions.getTypes());
        encodeMessages(definitions.getMessages());
        encodePortTypes(definitions.getPortTypes());
        encodeBindings(definitions.getBindings());
        encodeServices(definitions.getServices());
        end(definitions.getQName());
    }

    private void encodeTypes(Collection<Types> types) throws XMLStreamException {
        if (types != null && !types.isEmpty()) {
            for (Types type : types) {
                encodeTypes(type);
            }
        }
    }

    private void encodeTypes(Types types) throws XMLStreamException {
        start(types.getQName());
        encodeExtensibilityElements(types);
        end(types.getQName());
    }

    private void encodeMessages(Collection<Message> messages) throws XMLStreamException {
        if (messages != null && !messages.isEmpty()) {
            for (Message message : messages) {
                encodeMessage(message);
            }
        }
    }

    private void encodeMessage(Message message) throws XMLStreamException {
        start(message.getQName());
        addName(message);
        encodeParts(message.getParts());
        end(message.getQName());
    }

    private void encodeParts(Collection<Part> parts) throws XMLStreamException {
        if (parts != null && !parts.isEmpty()) {
            for (Part part : parts) {
                encodePart(part);
            }
        }
    }

    private void encodePart(Part part) throws XMLStreamException {
        empty(part.getQName());
        addName(part);
        if (part.isSetElement()) {
            attrElement(part.getElement());
        }
    }

    private void encodePortTypes(Collection<PortType> portTypes) throws XMLStreamException {
        if (portTypes != null && !portTypes.isEmpty()) {
            for (PortType portType : portTypes) {
                encodePortType(portType);
            }
        }
    }

    private void encodePortType(PortType portType) throws XMLStreamException {
        start(portType.getQName());
        addName(portType);
        encodeOperations(portType.getOperations());
        end(portType.getQName());
    }

    private void encodeOperations(Collection<Operation> operations) throws XMLStreamException {
        if (operations != null && !operations.isEmpty()) {
            for (Operation operation : operations) {
                encodeOperation(operation);
            }
        }
    }

    private void encodeOperation(Operation operation) throws XMLStreamException {
        start(operation.getQName());
        addName(operation);
        if (operation.isSetInput()) {
            encodeParam(operation.getInput());
        }
        if (operation.isSetOutput()) {
            encodeParam(operation.getOutput());
        }
        if (operation.isSetFaults()) {
            encodeFaults(operation.getFaults());
        }
        end(operation.getQName());
    }

    private void encodeParam(Param param) throws XMLStreamException {
        start(param.getQName());
        addName(param);
        if (param.isSetMessage()) {
            attrMessage(param.getMessage());
        }
        if (param.isSetAction()) {
            attrAction(param.getAction());
        }
        end(param.getQName());
    }

    private void encodeFaults(Collection<Fault> faults) throws XMLStreamException {
        if (faults != null && !faults.isEmpty()) {
            for (Fault fault : faults) {
                encodeFault(fault);
            }
        }
    }

    private void encodeFault(Fault fault) throws XMLStreamException {
        start(fault.getQName());
        addName(fault);
        attrMessage(fault.getMessage());
        attrAction(fault.getAction());
        end(fault.getQName());
    }

    private void encodeBindings(Collection<Binding> bindings) throws XMLStreamException {
        if (bindings != null && !bindings.isEmpty()) {
            for (Binding binding : bindings) {
                encodeBinding(binding);
            }
        }
    }

    private void encodeBinding(Binding binding) throws XMLStreamException {
        start(binding.getQName());
        addName(binding);
        attrType(binding.getType());
        encodeExtensibilityElements(binding);
        encodeBindingOperations(binding.getBindingOperations());
        end(binding.getQName());
    }

    private void encodeBindingOperations(Collection<BindingOperation> operations) throws XMLStreamException {
        if (operations != null && !operations.isEmpty()) {
            for (BindingOperation operation : operations) {
                encodeBindingOperation(operation);
            }
        }
    }

    private void encodeBindingOperation(BindingOperation operation) throws XMLStreamException {
        start(operation.getQName());
        addName(operation);
        encodeExtensibilityElements(operation);
        if (operation.isSetInput()) {
            encodeBindingMessage(operation.getInput());
        }
        if (operation.isSetOutput()) {
            encodeBindingMessage(operation.getOutput());
        }
        encodeBindingsFaults(operation.getBindingFaults());
        end(operation.getQName());
    }

    private void encodeBindingMessage(BindingMessage message) throws XMLStreamException {
        start(message.getQName());
        addName(message);
        encodeExtensibilityElements(message);
        end(message.getQName());
    }

    private void encodeServices(Collection<Service> services) throws XMLStreamException {
        if (services != null && !services.isEmpty()) {
            for (Service service : services) {
                encodeService(service);
            }
        }
    }

    private void encodeBindingsFaults(Collection<BindingFault> faults) throws XMLStreamException {
        if (faults != null && !faults.isEmpty()) {
            for (BindingFault fault : faults) {
                encodeBindingsFault(fault);
            }
        }
    }

    private void encodeBindingsFault(BindingFault fault) throws XMLStreamException {
        start(fault.getQName());
        addName(fault);
        encodeExtensibilityElements(fault);
        end(fault.getQName());
    }

    private void encodeService(Service service) throws XMLStreamException {
        start(service.getQName());
        addName(service);
        encodePorts(service.getPorts());
        end(service.getQName());
    }

    private void encodePorts(Collection<Port> ports) throws XMLStreamException {
        if (ports != null && !ports.isEmpty()) {
            for (Port port : ports) {
                encodePort(port);
            }
        }
    }

    private void encodePort(Port port) throws XMLStreamException {
        start(port.getQName());
        addName(port);
        if (port.isSetBinding()) {
            attrBinding(port.getBinding());
        }
        encodeExtensibilityElements(port);
        end(port.getQName());
    }

    private void encodeExtensibilityElements(AbstractWsdl wsdl) throws XMLStreamException {
        if (wsdl.isSetExtensibilityElements()) {
            for (ExtensibilityElement extensibilityElement : wsdl.getExtensibilityElements()) {
                if (extensibilityElement instanceof AbstractAddress) {
                    encodeAddress((AbstractAddress) extensibilityElement);
                } else if (extensibilityElement instanceof Schema) {
                    encodeSchema((Schema) extensibilityElement);
                } else if (extensibilityElement instanceof HttpBinding) {
                    encodeHttpBinding((HttpBinding) extensibilityElement);
                } else if (extensibilityElement instanceof HttpOperation) {
                    encodeHttpOperation((HttpOperation) extensibilityElement);
                } else if (extensibilityElement instanceof HttpUrlEncoded) {
                    encodeHttpUrlEncoded((HttpUrlEncoded) extensibilityElement);
                } else if (extensibilityElement instanceof MimeXml) {
                    encodeMimeXml((MimeXml) extensibilityElement);
                } else if (extensibilityElement instanceof SoapBinding) {
                    encodeSoapBinding((SoapBinding) extensibilityElement);
                } else if (extensibilityElement instanceof SoapBody) {
                    encodeSoapBody((SoapBody) extensibilityElement);
                } else if (extensibilityElement instanceof SoapFault) {
                    encodeSoapFault((SoapFault) extensibilityElement);
                } else if (extensibilityElement instanceof SoapOperation) {
                    encodeSoapOperation((SoapOperation) extensibilityElement);
                }
            }
        }
    }

    private void encodeAddress(AbstractAddress address) throws XMLStreamException {
        empty(address.getQName());
        attrLocation(address.getLocation().toString());
    }

    private void encodeSchema(Schema schema) throws XMLStreamException {
        start(schema.getQName());
        attr("elementFormDefault", schema.getElementFormDefault());
        attr("targetNamespace", schema.getTargetNamespace());
        encodeInclude(schema.getInclude());
        end(schema.getQName());
    }

    private void encodeInclude(Include include) throws XMLStreamException {
        empty(include.getQName());
        attr(WSDLConstants.AN_XSD_SCHEMA_LOCATION, include.getSchemaLocation());
    }

    private void encodeHttpBinding(HttpBinding httpBinding) throws XMLStreamException {
        empty(httpBinding.getQName());
        attrVerb(httpBinding.getVerb());
    }

    private void encodeHttpOperation(HttpOperation httpOperation) throws XMLStreamException {
        empty(httpOperation.getQName());
        attrLocation(httpOperation.getLocation());
    }

    private void encodeHttpUrlEncoded(HttpUrlEncoded httpUrlEncoded) throws XMLStreamException {
        empty(httpUrlEncoded.getQName());
    }

    private void encodeMimeXml(MimeXml mimeXml) throws XMLStreamException {
        empty(mimeXml.getQName());
    }

    private void encodeSoapBinding(SoapBinding soapBinding) throws XMLStreamException {
        empty(soapBinding.getQName());
        attrStyle(soapBinding.getStyle());
        attrTransport(soapBinding.getTransport());
    }

    private void encodeSoapBody(SoapBody soapBody) throws XMLStreamException {
        empty(soapBody.getQName());
        attrUse(soapBody.getUse());
    }

    private void encodeSoapFault(SoapFault soapFault) throws XMLStreamException {
        empty(soapFault.getQName());
        attrName(soapFault.getName());
        attrUse(soapFault.getUse());
    }

    private void encodeSoapOperation(SoapOperation soapOperation) throws XMLStreamException {
        empty(soapOperation.getQName());
        attrStyle(soapOperation.getStyle());
        attrSoapAction(soapOperation.getAction());
    }

    private void addName(AbstractWsdl wsdl) throws XMLStreamException {
        if (wsdl.isSetName()) {
            attrName(wsdl.getName());
        }
    }


    private void attrName(String value) throws XMLStreamException {
        attr("name", value);
    }

    private void attrBinding(QName value) throws XMLStreamException {
        attr("binding", toPrefixLocal(value));
    }

    private void attrElement(QName value) throws XMLStreamException {
        attr("element", toPrefixLocal(value));
    }

    private void attrMessage(QName value) throws XMLStreamException {
        attr("message", toPrefixLocal(value));
    }

    private void attrType(QName value) throws XMLStreamException {
        attr("type", toPrefixLocal(value));
    }

    private void attrVerb(String value) throws XMLStreamException {
        attr("verb", value);
    }

    private void attrStyle(String value) throws XMLStreamException {
        attr("style", value);
    }

    private void attrTransport(String value) throws XMLStreamException {
        attr("transport", value);
    }

    private void attrUse(String value) throws XMLStreamException {
        attr("use", value);
    }

    private void attrAction(URI value) throws XMLStreamException {
        attr(WSDLConstants.QN_WSAM_ACTION, value.toString());
    }

    private void attrSoapAction(URI value) throws XMLStreamException {
        attr("soapAction", value.toString());
    }

    private String toPrefixLocal(QName qName) {
        return qName.getPrefix() != null && !qName.getPrefix()
                .isEmpty() ? qName.getPrefix() + ":" + qName.getLocalPart() : qName.getLocalPart();
    }

    private void attrLocation(String value) throws XMLStreamException {
        attr("location", value);
    }

}
