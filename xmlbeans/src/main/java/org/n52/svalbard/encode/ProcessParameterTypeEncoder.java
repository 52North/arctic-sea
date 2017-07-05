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
import org.n52.janmayen.http.MediaType;
import org.n52.shetland.inspire.ompr.InspireOMPRConstants;
import org.n52.shetland.inspire.ompr.ProcessParameter;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import eu.europa.ec.inspire.schemas.ompr.x30.ProcessParameterType;

public class ProcessParameterTypeEncoder extends AbstractXmlEncoder<XmlObject, ProcessParameter> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessParameterTypeEncoder.class);

    private static final Set<EncoderKey> ENCODER_KEYS = Sets.newHashSet(
            new ClassToClassEncoderKey(ProcessParameterType.class, ProcessParameter.class),
            new XmlDocumentEncoderKey(InspireOMPRConstants.NS_OMPR_30, ProcessParameter.class));

    public ProcessParameterTypeEncoder() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.emptySet();
    }

    @Override
    public void addNamespacePrefixToMap(final Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(InspireOMPRConstants.NS_OMPR_30, InspireOMPRConstants.NS_OMPR_PREFIX);
    }

    @Override
    public MediaType getContentType() {
        return InspireOMPRConstants.OMPR_30_CONTENT_TYPE;
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(InspireOMPRConstants.OMPR_SCHEMA_LOCATION);
    }

    @Override
    public XmlObject encode(ProcessParameter processParameter) throws EncodingException {
        return encode(processParameter, EncodingContext.empty());
    }

    @Override
    public XmlObject encode(ProcessParameter processParameter, EncodingContext context)
            throws EncodingException {
        return createProcessParameter(processParameter);
    }

    protected ProcessParameterType createProcessParameter(ProcessParameter processParameter) throws EncodingException {
        ProcessParameterType ppt = ProcessParameterType.Factory.newInstance();
        ppt.addNewName().set(encodeObjectToXml(GmlConstants.NS_GML_32, processParameter.getName()));
        if (processParameter.isSetDescription()) {
            ppt.setDescription(processParameter.getDescription());
        }
        return ppt;
    }

}
