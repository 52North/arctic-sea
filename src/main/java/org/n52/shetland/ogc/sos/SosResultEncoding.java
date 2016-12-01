/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos;

import java.util.EnumMap;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swe.encoding.SweAbstractEncoding;
import org.n52.sos.exception.ows.concrete.XmlDecodingException;
import org.n52.sos.util.CodingHelper;
import org.n52.sos.util.SosHelper;
import org.n52.svalbard.HelperValues;
import org.n52.svalbard.decode.exception.DecoderResponseUnsupportedException;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.base.Strings;

/**
 * @since 4.0.0
 *
 */
public class SosResultEncoding {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(SosHelper.class);

    private String xml;

    private SweAbstractEncoding encoding;

    public SosResultEncoding() {
    }

    public SosResultEncoding(String resultEncoding)
            throws DecodingException {
        this.xml = resultEncoding;
        encoding = parseResultEncoding();
    }

    public String getXml()
            throws EncodingException, DecodingException {
        if (!isSetXml() && encoding != null) {
            if (encoding.isSetXml()) {
                setXml(encoding.getXml());
            } else {
                setXml(encodeResultEncoding());
            }
        }
        return xml;
    }

    public SosResultEncoding setEncoding(SweAbstractEncoding encoding) {
        this.encoding = encoding;
        return this;
    }

    public SweAbstractEncoding getEncoding()
            throws DecodingException {
        if (encoding == null && xml != null && !xml.isEmpty()) {
            encoding = parseResultEncoding();
        }
        return encoding;
    }

    public SosResultEncoding setXml(String xml) {
        this.xml = xml;
        return this;
    }

    private SweAbstractEncoding parseResultEncoding()
            throws DecodingException {
        try {
            Object decodedObject = CodingHelper.decodeXmlObject(Factory
                    .parse(xml));
            if (decodedObject instanceof SweAbstractEncoding) {
                return (SweAbstractEncoding) decodedObject;
            } else {
                throw new DecoderResponseUnsupportedException(xml, decodedObject);
            }
        } catch (XmlException xmle) {
            throw new XmlDecodingException("resultEncoding", xml, xmle);
        }
    }

    private String encodeResultEncoding()
            throws DecodingException, EncodingException {
        Map<HelperValues, Object> map = new EnumMap<>(HelperValues.class);
        map.put(HelperValues.DOCUMENT, null);
        return CodingHelper.encodeObjectToXmlText(SweConstants.NS_SWE_20, getEncoding(), map);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SosResultEncoding other = (SosResultEncoding) obj;
        try {
            if (this.getEncoding() != other.getEncoding() &&
                (this.getEncoding() == null || !this.getEncoding().equals(other
                 .getEncoding()))) {
                return false;
            }
        } catch (DecodingException ex) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        try {
            return getEncoding().hashCode();
        } catch (DecodingException e) {
            LOGGER.error("Error while parsing resultStructure", e);
        }
        return super.hashCode();
    }

    public boolean isEmpty() {
        return !Strings.isNullOrEmpty(xml);
    }

    public boolean isSetXml() {
        return !Strings.isNullOrEmpty(this.xml);
    }

}
