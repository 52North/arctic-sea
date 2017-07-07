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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.isotc211.x2005.gco.CodeListValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.iso.GcoConstants;
import org.n52.shetland.ogc.sensorML.Role;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

/**
 * {@link AbstractXmlEncoder} class to decode ISO TC211 Geographic COmmon (GCO)
 * extensible markup language.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class Iso19139GcoEncoder extends AbstractIso19139GcoEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(Iso19139GcoEncoder.class);

    private static final Set<EncoderKey> ENCODER_KEYS
            = CodingHelper.encoderKeysForElements(GcoConstants.NS_GCO, Role.class);

    public Iso19139GcoEncoder() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public void addNamespacePrefixToMap(final Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(GcoConstants.NS_GCO, GcoConstants.NS_GCO_PREFIX);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(GcoConstants.GCO_SCHEMA_LOCATION);
    }

    @Override
    public XmlObject encode(Object element, EncodingContext additionalValues)
            throws EncodingException, UnsupportedEncoderInputException {
        XmlObject encodedObject = null;
        if (element instanceof Role) {
            encodedObject = encodeRole((Role) element);
        } else {
            throw new UnsupportedEncoderInputException(this, element);
        }
        XmlHelper.validateDocument(encodedObject, EncodingException::new);
        return encodedObject;
    }

    private XmlObject encodeRole(Role role) {
        CodeListValueType circ = CodeListValueType.Factory.newInstance(getXmlOptions());
        circ.setStringValue(role.getValue());
        circ.setCodeList(role.getCodeList());
        circ.setCodeListValue(role.getCodeListValue());
        return circ;
    }

}
