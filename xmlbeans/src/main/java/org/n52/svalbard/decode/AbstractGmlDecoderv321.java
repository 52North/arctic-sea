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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.AbstractGML;
import org.n52.shetland.ogc.gml.AbstractGeometry;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.GmlMeasureType;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.values.BooleanValue;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.GeometryValue;
import org.n52.shetland.ogc.om.values.HrefAttributeValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.ReferenceValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweCategory;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.xlink.W3CHrefAttribute;
import org.n52.svalbard.ConformanceClass;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;

import net.opengis.gml.x32.AbstractFeatureType;
import net.opengis.gml.x32.AbstractGMLType;
import net.opengis.gml.x32.CodeType;
import net.opengis.gml.x32.CodeWithAuthorityType;
import net.opengis.gml.x32.ReferenceType;
import net.opengis.om.x20.NamedValuePropertyType;
import net.opengis.om.x20.NamedValueType;

public abstract class AbstractGmlDecoderv321<T, S>
        extends AbstractXmlDecoder<T, S>
        implements ConformanceClass {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGmlDecoderv321.class);

    protected AbstractGML parseAbstractGMLType(AbstractGMLType agmlt, AbstractGML abstractGML)
            throws DecodingException {
        parseIdentifier(agmlt, abstractGML);
        parseNames(agmlt, abstractGML);
        paresDescription(agmlt, abstractGML);
        // parseMetaDataProperty(agmlt, abstractGML);
        return null;
    }

    protected AbstractFeature parseAbstractFeatureType(AbstractFeatureType aft, AbstractFeature abstractFeature)
            throws DecodingException {
        parseAbstractGMLType(aft, abstractFeature);
        // parseBoundedBy(aft, abstractFeature);
        // parseLocation(aft, abstractFeature);
        return abstractFeature;
    }

    protected AbstractGML parseIdentifier(AbstractGMLType agmlt, AbstractGML abstractGML) throws DecodingException {
        if (agmlt.isSetIdentifier()) {
            abstractGML.setIdentifier(parseCodeWithAuthorityTye(agmlt.getIdentifier()));
        }
        return abstractGML;
    }

    protected AbstractGML parseNames(AbstractGMLType agmlt, AbstractGML abstractGML) throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(agmlt.getNameArray())) {
            for (CodeType ct : agmlt.getNameArray()) {
                abstractGML.addName(parseCodeType(ct));
            }
        }
        return abstractGML;
    }

    protected AbstractGML paresDescription(AbstractGMLType agmlt, AbstractGML abstractGML) {
        if (agmlt.isSetDescription()) {
            if (agmlt.getDescription().isSetHref()) {
                abstractGML.setDescription(agmlt.getDescription().getHref());
            } else {
                abstractGML.setDescription(agmlt.getDescription().getStringValue());
            }
            // } else if (agmlt.isSetDescriptionReference()) {
            // TODO
        }
        return abstractGML;

    }

    protected CodeWithAuthority parseCodeWithAuthorityTye(CodeWithAuthorityType xbCodeWithAuthority) {
        if (xbCodeWithAuthority.getStringValue() != null && !xbCodeWithAuthority.getStringValue().isEmpty()) {
            CodeWithAuthority sosCodeWithAuthority = new CodeWithAuthority(xbCodeWithAuthority.getStringValue());
            sosCodeWithAuthority.setCodeSpace(xbCodeWithAuthority.getCodeSpace());
            return sosCodeWithAuthority;
        }
        return null;
    }

    protected org.n52.shetland.ogc.gml.CodeType parseCodeType(CodeType element) throws DecodingException {
        org.n52.shetland.ogc.gml.CodeType codeType = new org.n52.shetland.ogc.gml.CodeType(element.getStringValue());
        if (element.isSetCodeSpace()) {
            try {
                codeType.setCodeSpace(new URI(element.getCodeSpace()));
            } catch (URISyntaxException e) {
                throw new DecodingException(e, "Error while creating URI from '{}'", element.getCodeSpace());
            }
        }
        return codeType;
    }

    protected org.n52.shetland.ogc.gml.ReferenceType parseReferenceType(ReferenceType rt) {
        org.n52.shetland.ogc.gml.ReferenceType referenceType = new org.n52.shetland.ogc.gml.ReferenceType("UNKNOWN");
        if (rt.isSetTitle() && !Strings.isNullOrEmpty(rt.getTitle())) {
            referenceType.setTitle(rt.getTitle());
        }
        if (rt.isSetHref() && !Strings.isNullOrEmpty(rt.getHref())) {
            referenceType.setHref(rt.getHref());
        }
        if (rt.isSetRole() && !Strings.isNullOrEmpty(rt.getRole())) {
            referenceType.setRole(rt.getRole());
        }
        return referenceType;
    }

    protected List<org.n52.shetland.ogc.gml.ReferenceType> parseReferenceType(ReferenceType[] referenceTypes) {
        List<org.n52.shetland.ogc.gml.ReferenceType> list = Lists.newArrayList();
        if (CollectionHelper.isNotNullOrEmpty(referenceTypes)) {
            for (ReferenceType referenceType : referenceTypes) {
                list.add(parseReferenceType(referenceType));
            }
        }
        return list;
    }

    protected Set<NamedValue<?>> parseNamedValueTypeArray(NamedValuePropertyType[] namedValuePropertyArray)
            throws DecodingException {
        Set<NamedValue<?>> parameters = Sets.newTreeSet();
        for (NamedValuePropertyType namedValueProperty : namedValuePropertyArray) {
            parameters.add(parseNamedValueType(namedValueProperty));
        }
        return parameters;
    }

    protected NamedValue<?> parseNamedValueType(NamedValuePropertyType namedValueProperty) throws DecodingException {
        if (namedValueProperty.isSetNamedValue()) {
            NamedValueType namedValue = namedValueProperty.getNamedValue();
            NamedValue<?> sosNamedValue = parseNamedValueValue(namedValue.getValue());
            org.n52.shetland.ogc.gml.ReferenceType referenceType =
                    (org.n52.shetland.ogc.gml.ReferenceType) decodeXmlObject(namedValue.getName());
            sosNamedValue.setName(referenceType);
            return sosNamedValue;
        } else if (namedValueProperty.isSetHref()) {
            NamedValue<org.n52.shetland.ogc.gml.ReferenceType> sosNamedValue = new NamedValue<>();
            org.n52.shetland.ogc.gml.ReferenceType referenceType =
                    new org.n52.shetland.ogc.gml.ReferenceType(namedValueProperty.getHref());
            if (namedValueProperty.isSetTitle()) {
                referenceType.setTitle(namedValueProperty.getTitle());
            }
            sosNamedValue.setName(referenceType);
            return sosNamedValue;
        } else {
            throw new UnsupportedDecoderInputException(this, namedValueProperty);
        }
    }

    protected NamedValue<?> parseNamedValueValue(XmlObject xml) throws DecodingException {
        XmlObject xmlObject = xml;
        if (xmlObject.schemaType() == XmlAnyTypeImpl.type) {
            try {
                xmlObject = XmlObject.Factory.parse(xml.xmlText().trim());
            } catch (XmlException e) {
                LOGGER.error("Error while parsing NamedValueValue", e);
            }
        }
        Object value;

        if (XmlBoolean.Factory.newInstance().schemaType().equals(xmlObject.schemaType())) {
            value = ((XmlBoolean) xmlObject).getBooleanValue();
        } else if (XmlString.Factory.newInstance().schemaType().equals(xmlObject.schemaType())) {
            value = ((XmlString) xmlObject).getStringValue();
        } else if (XmlInt.Factory.newInstance().schemaType().equals(xmlObject.schemaType())) {
            value = ((XmlInt) xmlObject).getIntValue();
        } else if (XmlInteger.Factory.newInstance().schemaType().equals(xmlObject.schemaType())) {
            value = ((XmlInteger) xmlObject).getBigIntegerValue().intValue();
        } else if (XmlDouble.Factory.newInstance().schemaType().equals(xmlObject.schemaType())) {
            value = ((XmlDouble) xmlObject).getDoubleValue();
        } else {
            value = decodeXmlObject(xmlObject);
        }
        if (value instanceof BooleanValue) {
            NamedValue<Boolean> namedValue = new NamedValue<>();
            namedValue.setValue((BooleanValue) value);
            return namedValue;
        } else if (value instanceof SweBoolean) {
            NamedValue<Boolean> namedValue = new NamedValue<>();
            namedValue.setValue(new BooleanValue(((SweBoolean) value).getValue()));
            return namedValue;
        } else if (value instanceof Boolean) {
            NamedValue<Boolean> namedValue = new NamedValue<>();
            namedValue.setValue(new BooleanValue((Boolean) value));
            return namedValue;
        } else if (value instanceof CategoryValue) {
            NamedValue<String> namedValue = new NamedValue<>();
            namedValue.setValue((CategoryValue) value);
            return namedValue;
        } else if (value instanceof SweCategory) {
            NamedValue<String> namedValue = new NamedValue<>();
            namedValue.setValue(
                    new CategoryValue(((SweCategory) value).getValue(), ((SweCategory) value).getCodeSpace()));
            return namedValue;
        } else if (value instanceof CountValue) {
            NamedValue<Integer> namedValue = new NamedValue<>();
            namedValue.setValue((CountValue) value);
            return namedValue;
        } else if (value instanceof SweCount) {
            NamedValue<Integer> namedValue = new NamedValue<>();
            namedValue.setValue(new CountValue(((SweCount) value).getValue()));
            return namedValue;
        } else if (value instanceof Integer) {
            NamedValue<Integer> namedValue = new NamedValue<>();
            namedValue.setValue(new CountValue((Integer) value));
            return namedValue;
        } else if (value instanceof GeometryValue) {
            NamedValue<Geometry> namedValue = new NamedValue<>();
            namedValue.setValue((GeometryValue) value);
            return namedValue;
        } else if (value instanceof QuantityValue) {
            NamedValue<Double> namedValue = new NamedValue<>();
            namedValue.setValue((QuantityValue) value);
            return namedValue;
        } else if (value instanceof GmlMeasureType) {
            NamedValue<Double> namedValue = new NamedValue<>();
            namedValue.setValue(
                    new QuantityValue(((GmlMeasureType) value).getValue(), ((GmlMeasureType) value).getUnit()));
            return namedValue;
        } else if (value instanceof SweQuantity) {
            NamedValue<Double> namedValue = new NamedValue<>();
            namedValue.setValue(new QuantityValue(((SweQuantity) value).getValue(), ((SweQuantity) value).getUom()));
            return namedValue;
        } else if (value instanceof Double) {
            NamedValue<Double> namedValue = new NamedValue<>();
            namedValue.setValue(new QuantityValue((Double) value));
            return namedValue;
        } else if (value instanceof TextValue) {
            NamedValue<String> namedValue = new NamedValue<>();
            namedValue.setValue((TextValue) value);
            return namedValue;
        } else if (value instanceof SweText) {
            NamedValue<String> namedValue = new NamedValue<>();
            namedValue.setValue(new TextValue(((SweText) value).getValue()));
            return namedValue;
        } else if (value instanceof String) {
            NamedValue<String> namedValue = new NamedValue<>();
            namedValue.setValue(new TextValue((String) value));
            return namedValue;
        } else if (value instanceof AbstractGeometry) {
            NamedValue<Geometry> namedValue = new NamedValue<>();
            namedValue.setValue(new GeometryValue((AbstractGeometry) value));
            return namedValue;
        } else if (value instanceof ReferenceType) {
            NamedValue<org.n52.shetland.ogc.gml.ReferenceType> namedValue = new NamedValue<>();
            namedValue.setValue(new ReferenceValue((org.n52.shetland.ogc.gml.ReferenceType) value));
            return namedValue;
        } else if (value instanceof W3CHrefAttribute) {
            NamedValue<W3CHrefAttribute> namedValue = new NamedValue<>();
            namedValue.setValue(new HrefAttributeValue((W3CHrefAttribute) value));
            return namedValue;
        } else {
            throw new UnsupportedDecoderInputException(this, xmlObject);
        }
    }
}
