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

import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.inspire.base.InspireBaseConstants;
import org.n52.shetland.inspire.base2.InspireBase2Constants;
import org.n52.shetland.inspire.ef.InspireEfConstants;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

public abstract class AbstractEnvironmentalFaciltityEncoder<T, S>
        extends AbstractGmlEncoderv321<T, S> {

    @Override
    public void addNamespacePrefixToMap(final Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(InspireEfConstants.NS_EF, InspireEfConstants.NS_EF_PREFIX);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(InspireEfConstants.EF_40_SCHEMA_LOCATION);
    }

    protected XmlObject encodeGML32(Object o) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o);
    }

    protected XmlObject encodeGML32(Object o, EncodingContext context) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o, context);
    }

    protected XmlObject encodeEF(Object o) throws EncodingException {
        return encodeObjectToXml(InspireEfConstants.NS_EF, o);
    }

    protected XmlObject encodeEF(Object o, EncodingContext context) throws EncodingException {
        return encodeObjectToXml(InspireEfConstants.NS_EF, o, context);
    }

    protected XmlObject encodeEFPropertyType(Object o) throws EncodingException {
        return encodeObjectToXmlPropertyType(InspireEfConstants.NS_EF, o);
    }

    protected XmlObject encodeEFDocument(Object o) throws EncodingException {
        return encodeObjectToXmlDocument(InspireEfConstants.NS_EF, o);
    }

    protected XmlObject encodeBASE2(Object o) throws EncodingException {
        return encodeObjectToXml(InspireBase2Constants.NS_BASE2, o);
    }

    protected XmlObject encodeBASE2(Object o, EncodingContext context) throws EncodingException {
        return encodeObjectToXml(InspireBase2Constants.NS_BASE2, o, context);
    }

    protected XmlObject encodeBASE2PropertyType(Object o) throws EncodingException {
        return encodeObjectToXmlPropertyType(InspireBase2Constants.NS_BASE2, o);
    }

    protected XmlObject encodeBASE2Document(Object o) throws EncodingException {
        return encodeObjectToXmlDocument(InspireBase2Constants.NS_BASE2, o);
    }

    protected XmlObject encodeBASE(Object o) throws EncodingException {
        return encodeObjectToXml(InspireBaseConstants.NS_BASE, o);
    }

    protected XmlObject encodeBASE(Object o, EncodingContext context) throws EncodingException {
        return encodeObjectToXml(InspireBaseConstants.NS_BASE, o, context);
    }

    protected XmlObject encodeBASEPropertyType(Object o) throws EncodingException {
        return encodeObjectToXmlPropertyType(InspireBaseConstants.NS_BASE, o);
    }

    protected XmlObject encodeBASEDocument(Object o) throws EncodingException {
        return encodeObjectToXmlDocument(InspireBaseConstants.NS_BASE, o);
    }

}
