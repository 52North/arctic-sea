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

import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
import org.n52.sos.decode.AbstractXmlDecoder;
import org.n52.sos.exception.ows.concrete.XmlDecodingException;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.swe.SweAbstractDataComponent;
import org.n52.sos.ogc.swes.SwesConstants;
import org.n52.sos.ogc.swes.SwesExtension;
import org.n52.sos.ogc.swes.SwesExtensionImpl;
import org.n52.sos.ogc.swes.SwesExtensions;
import org.n52.sos.util.CodingHelper;
import org.n52.sos.util.XmlHelper;

public abstract class AbstractGetDataAvailabilityXmlDecoder extends AbstractXmlDecoder<GetDataAvailabilityRequest> {

    public abstract GetDataAvailabilityRequest parseGetDataAvailability(XmlObject xml) throws OwsExceptionReport;

    protected static String getBasePath(String basePath, String prefix) {
        StringBuilder builder = new StringBuilder();
        builder.append(basePath);
        builder.append("/");
        builder.append(prefix);
        builder.append(":");
        builder.append("GetDataAvailability");
        return builder.toString();
    }

    @Override
    public GetDataAvailabilityRequest decode(XmlObject xml) throws OwsExceptionReport {
        return parseGetDataAvailability(xml);
    }

    protected String parseStringValue(XmlObject xmlObject) {
        if (xmlObject instanceof XmlString) {
            return ((XmlString) xmlObject).getStringValue();
        } else if (xmlObject instanceof XmlAnyURI) {
            return ((XmlAnyURI)xmlObject).getStringValue();
        } else {
            return ((XmlAnyTypeImpl) xmlObject).getStringValue();
        }
    }

    /**
     * Parse swes:extensions
     *
     * @param xml
     *            swes:extension
     * @return parsed {@code SwesExtensions}
     * @throws OwsExceptionReport
     *             if the swes:extension could not be parsed
     */
    protected SwesExtensions parseExtensions(XmlObject xml) throws OwsExceptionReport {
        SwesExtensions extensions = new SwesExtensions();
        for (XmlObject x : xml.selectPath(getPath(SwesConstants.XPATH_PREFIXES_SWES, SwesConstants.NS_SWES_PREFIX,
                "extension"))) {
            try {
                if (x.getDomNode().hasChildNodes()) {
                    Object obj =
                            CodingHelper.decodeXmlElement(XmlObject.Factory.parse(XmlHelper.getNodeFromNodeList(x
                                    .getDomNode().getChildNodes())));
                    SwesExtension<?> extension = null;
                    if (!(obj instanceof SwesExtension<?>)) {
                        extension = new SwesExtensionImpl<Object>().setValue(obj);
                        if (isSweAbstractDataComponent(obj)) {
                            extension.setDefinition(((SweAbstractDataComponent) obj).getDefinition());
                        }
                    } else {
                        extension = (SwesExtension<?>) obj;
                    }
                    extensions.addSwesExtension(extension);
                }
            } catch (XmlException xmle) {
                throw new XmlDecodingException("extension", xmle);
            }
        }
        return extensions;
    }

    /**
     * Check if the object is of type {@code SweAbstractDataComponent}
     *
     * @param object
     *            Object to check
     * @return <code>true</code>, if the object is of type
     *         {@code SweAbstractDataComponent}
     */
    protected boolean isSweAbstractDataComponent(final Object object) {
        return object instanceof SweAbstractDataComponent && ((SweAbstractDataComponent) object).isSetDefinition();
    }

    /**
     * Create path from values
     *
     * @param xpathPrefix
     *            XPath prefix
     * @param prefix
     *            Namespace prefix
     * @param element
     *            Element name
     * @return XPath path
     */
    protected String getPath(String xpathPrefix, String prefix, String element) {
        StringBuilder builder = new StringBuilder();
        builder.append(xpathPrefix);
        builder.append(".//");
        builder.append(prefix);
        builder.append(":");
        builder.append(element);
        return builder.toString();
    }
}
