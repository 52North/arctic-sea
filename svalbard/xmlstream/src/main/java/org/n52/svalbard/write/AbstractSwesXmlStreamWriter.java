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
package org.n52.svalbard.write;


import java.io.OutputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.Extensions;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swes.SwesStreamingConstants;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.XmlBeansEncodingFlags;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.base.Strings;

import net.opengis.swes.x20.ExtensibleResponseType;


public abstract class AbstractSwesXmlStreamWriter<T> extends XmlStreamWriter<T> {

    public AbstractSwesXmlStreamWriter(EncodingContext context, OutputStream outputStream, T element)
            throws XMLStreamException {
        super(context, outputStream, element);
    }

    protected void writeExtensions(Extensions extensions) throws XMLStreamException, EncodingException {
        for (Extension<?> extension : extensions.getExtensions()) {
            if (extension.getValue() instanceof SweAbstractDataComponent) {
                writeExtension((SweAbstractDataComponent) extension.getValue());
            }
        }
    }

    protected void writeExtension(SweAbstractDataComponent sweAbstractDataComponent)
            throws EncodingException, XMLStreamException {
        EncodingContext ctx = EncodingContext.of(XmlBeansEncodingFlags.PROPERTY_TYPE, "true");
        XmlObject extension = encodeSwe(ctx, sweAbstractDataComponent);
        if (extension.xmlText().contains(XML_FRAGMENT)) {
            XmlObject set =
                    ExtensibleResponseType.Factory.newInstance(getXmlOptions())
                            .addNewExtension().set(extension);
            writeXmlObject(set, SwesStreamingConstants.QN_EXTENSION);
        } else {
            if (checkExtension(extension)) {
                QName name = extension.schemaType().getName();
                String prefix = name.getPrefix();
                if (Strings.isNullOrEmpty(prefix)) {
                    XmlCursor newCursor = extension.newCursor();
                    prefix = newCursor.prefixForNamespace(name.getNamespaceURI());
                    newCursor.setAttributeText(W3CConstants.QN_XSI_TYPE,
                            prefix + ":" + name.getLocalPart());
                    newCursor.dispose();
                }
                writeXmlObject(extension, SwesStreamingConstants.QN_EXTENSION);
            } else {
                start(SwesStreamingConstants.QN_EXTENSION);
                writeXmlObject(extension, SwesStreamingConstants.QN_EXTENSION);
                end(SwesStreamingConstants.QN_EXTENSION);
            }
        }
    }

    private boolean checkExtension(XmlObject extension) {
        if (extension.schemaType() != null) {
            SchemaType schemaType = extension.schemaType();
            if (schemaType.getName() != null) {
                QName name = schemaType.getName();
                if (name.getLocalPart() != null && name.getLocalPart().toLowerCase().contains("propertytype")) {
                    return true;
                }
            }
        }
        return false;
    }

    protected XmlObject encodeSwe(EncodingContext ctx, Object o) throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o, ctx);
    }

    protected XmlObject encodeGml(EncodingContext ctx, Object o) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o, ctx);
    }

    private XmlObject encodeObjectToXml(String namespace, Object o, EncodingContext ctx) throws EncodingException {
        Encoder<XmlObject, Object> encoder = getEncoder(namespace, o);
        if (encoder != null) {
            return encoder.encode(o, ctx);
        }
        return null;
    }

}
