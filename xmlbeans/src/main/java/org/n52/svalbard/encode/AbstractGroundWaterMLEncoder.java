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
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gwml.GWMLConstants;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import net.opengis.gml.x32.FeaturePropertyType;

public abstract class AbstractGroundWaterMLEncoder<T, S> extends AbstractGmlEncoderv321<T, S> {

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(GWMLConstants.NS_GWML_22, GWMLConstants.NS_GWML_2_PREFIX);
        nameSpacePrefixMap.put(GWMLConstants.NS_GWML_WELL_22, GWMLConstants.NS_GWML_WELL_2_PREFIX);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(GWMLConstants.GWML_22_SCHEMA_LOCATION, GWMLConstants.GWML_WELL_22_SCHEMA_LOCATION);
    }

    protected XmlObject encodeGML(Object o) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o);
    }

    protected XmlObject encodeGML(Object o, EncodingContext ec) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o, ec);
    }

    protected XmlObject encodeSweCommon(Object o) throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o);
    }

    protected XmlObject encodeSweCommon(Object o, EncodingContext ec) throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o, ec);
    }

    protected XmlObject encodeGWML(Object o) throws EncodingException {
        return encodeObjectToXml(GWMLConstants.NS_GWML_22, o);
    }

    protected XmlObject encodeGWML(Object o, EncodingContext ec) throws EncodingException {
        return encodeObjectToXml(GWMLConstants.NS_GWML_22, o, ec);
    }

    protected XmlObject encodeGWMLProperty(Object o) throws EncodingException {
        return encodeObjectToXmlPropertyType(GWMLConstants.NS_GWML_22, o);
    }

    protected XmlObject encodeGWMLProperty(Object o, EncodingContext ec) throws EncodingException {
        return encodeObjectToXmlPropertyType(GWMLConstants.NS_GWML_22, o, ec);
    }

    protected XmlObject encodeGWMLDocument(Object o) throws EncodingException {
        return encodeObjectToXmlDocument(GWMLConstants.NS_GWML_22, o);
    }

    @Override
    protected XmlObject createFeature(FeaturePropertyType featurePropertyType, AbstractFeature abstractFeature,
            EncodingContext context) throws EncodingException {
        if (context.has(XmlBeansEncodingFlags.ENCODE)
                && !context.getBoolean(XmlBeansEncodingFlags.ENCODE)) {
            featurePropertyType.setHref(abstractFeature.getIdentifierCodeWithAuthority().getValue());
            if (abstractFeature.isSetName()) {
                featurePropertyType.setTitle(abstractFeature.getFirstName().getValue());
            }
            return featurePropertyType;
        }
        return encodeGWMLDocument(abstractFeature);
    }
}
