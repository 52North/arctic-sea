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

import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.sos.exception.ows.concrete.XmlDecodingException;
import org.n52.sos.util.CodingHelper;
import org.n52.sos.util.SosHelper;
import org.n52.svalbard.HelperValues;
import org.n52.svalbard.decode.exception.DecoderResponseUnsupportedException;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

/**
 * @since 4.0.0
 *
 */
public class SosResultStructure {

    private static final Logger LOGGER = LoggerFactory.getLogger(SosHelper.class);

    private SweAbstractDataComponent resultStructure;

    private String xml;

    public SosResultStructure() {
    }

    public SosResultStructure(String resultStructure) throws DecodingException {
        this.xml = resultStructure;
        this.resultStructure = parseResultStructure();
    }

    public String getXml() throws DecodingException, EncodingException {
        if (!isSetXml() && resultStructure != null) {
            if (resultStructure.isSetXml()) {
                setXml(resultStructure.getXml());
            } else {
                setXml(encodeResultStructure());
            }
        }
        return xml;
    }

    public SosResultStructure setResultStructure(SweAbstractDataComponent resultStructure) {
        this.resultStructure = resultStructure;
        return this;
    }

    public SweAbstractDataComponent getResultStructure() throws DecodingException {
        if (resultStructure == null && xml != null && !xml.isEmpty()) {
            resultStructure = parseResultStructure();
        }
        return resultStructure;
    }

    public SosResultStructure setXml(String xml) {
        this.xml = xml;
        return this;
    }

    private SweAbstractDataComponent parseResultStructure() throws DecodingException {
        try {
            Object decodedObject = CodingHelper.decodeXmlObject(Factory.parse(xml));
            if (decodedObject instanceof SweAbstractDataComponent) {
                return (SweAbstractDataComponent) decodedObject;
            } else {
                throw new DecoderResponseUnsupportedException(xml, decodedObject);
            }
        } catch (XmlException xmle) {
            throw new XmlDecodingException("resultStructure", xml, xmle);
        }
    }

    private String encodeResultStructure() throws DecodingException, EncodingException {
        Map<HelperValues, String> map = Maps.newEnumMap(HelperValues.class);
        map.put(HelperValues.DOCUMENT, null);
        return CodingHelper.encodeObjectToXmlText(SweConstants.NS_SWE_20, getResultStructure(), map);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SosResultStructure) {
            SosResultStructure other = (SosResultStructure) o;
            try {
                if (getResultStructure() == other.getResultStructure()) {
                    return true;
                } else if (getResultStructure() != null) {
                    return getResultStructure().equals(other.getResultStructure());
                }
            } catch (DecodingException ex) {
                return false;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        try {
            return getResultStructure().hashCode();
        } catch (DecodingException e) {
            LOGGER.error("Error while parsing resultStructure", e);
        }
        return super.hashCode();
    }

    public boolean isEmpty() {
        return !Strings.isNullOrEmpty(xml);
    }

    public boolean isSetXml() {
        return !Strings.isNullOrEmpty(xml);
    }
}
