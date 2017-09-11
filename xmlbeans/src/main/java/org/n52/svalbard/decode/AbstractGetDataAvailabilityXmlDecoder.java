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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;

import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityRequest;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.svalbard.decode.exception.DecodingException;

public abstract class AbstractGetDataAvailabilityXmlDecoder
        extends AbstractSwesDecoderv20<GetDataAvailabilityRequest> {

    public abstract GetDataAvailabilityRequest parseGetDataAvailability(XmlObject xml)
            throws DecodingException;

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
    public GetDataAvailabilityRequest decode(XmlObject objectToDecode)
            throws DecodingException {
        return parseGetDataAvailability(objectToDecode);
    }

    protected String parseStringValue(XmlObject xmlObject) {
        if (xmlObject instanceof XmlString) {
            return ((XmlString) xmlObject).getStringValue();
        } else if (xmlObject instanceof XmlAnyURI) {
            return ((XmlAnyURI) xmlObject).getStringValue();
        } else {
            return ((XmlAnyTypeImpl) xmlObject).getStringValue();
        }
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
