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

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.janmayen.http.MediaType;
import org.n52.shetland.inspire.base.InspireBaseConstants;
import org.n52.shetland.inspire.base2.DocumentCitation;
import org.n52.shetland.inspire.base2.InspireBase2Constants;
import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.shetland.inspire.ompr.InspireOMPRConstants;
import org.n52.shetland.inspire.ompr.Process;
import org.n52.shetland.inspire.ompr.ProcessParameter;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.sos.ProcedureDescriptionFormat;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.JavaHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import eu.europa.ec.inspire.schemas.ompr.x30.ProcessDocument;
import eu.europa.ec.inspire.schemas.ompr.x30.ProcessPropertyType;
import eu.europa.ec.inspire.schemas.ompr.x30.ProcessType;
import eu.europa.ec.inspire.schemas.ompr.x30.ProcessType.InspireId;
import net.opengis.gml.x32.FeaturePropertyType;

public class ProcessTypeEncoder
        extends AbstractGmlEncoderv321<XmlObject, Process>
        implements ProcedureEncoder<XmlObject, Process> {

    private final Logger LOGGER = LoggerFactory.getLogger(ProcessTypeEncoder.class);

    private final Set<SupportedType> SUPPORTED_TYPES =
            Sets.newHashSet(new ProcedureDescriptionFormat(InspireOMPRConstants.OMPR_30_OUTPUT_FORMAT_URL),
                    new ProcedureDescriptionFormat(InspireOMPRConstants.OMPR_30_OUTPUT_FORMAT_MIME_TYPE));

    private final Map<String, ImmutableMap<String, Set<String>>> SUPPORTED_PROCEDURE_DESCRIPTION_FORMATS =
            ImmutableMap.of(SosConstants.SOS,
                    ImmutableMap.<String, Set<String>> builder()
                            .put(Sos2Constants.SERVICEVERSION,
                                    ImmutableSet.of(InspireOMPRConstants.OMPR_30_OUTPUT_FORMAT_URL))
                            .put(Sos1Constants.SERVICEVERSION,
                                    ImmutableSet.of(InspireOMPRConstants.OMPR_30_OUTPUT_FORMAT_MIME_TYPE))
                            .build());

    private final Set<EncoderKey> ENCODER_KEYS = CollectionHelper.union(
            CodingHelper.encoderKeysForElements(InspireOMPRConstants.NS_OMPR_30, SosProcedureDescription.class,
                    Process.class),
            CodingHelper.encoderKeysForElements(InspireOMPRConstants.OMPR_30_OUTPUT_FORMAT_MIME_TYPE,
                    SosProcedureDescription.class, Process.class),
            CodingHelper.encoderKeysForElements(InspireOMPRConstants.FEATURE_CONCEPT_PROCESS,
                    SosProcedureDescription.class, Process.class));

    public ProcessTypeEncoder() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.unmodifiableSet(SUPPORTED_TYPES);
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
    public Set<String> getSupportedProcedureDescriptionFormats(final String service, final String version) {
        if (SUPPORTED_PROCEDURE_DESCRIPTION_FORMATS.containsKey(service)
                && SUPPORTED_PROCEDURE_DESCRIPTION_FORMATS.get(service).containsKey(version)) {
            return SUPPORTED_PROCEDURE_DESCRIPTION_FORMATS.get(service).get(version);
        }
        return Collections.emptySet();
    }

    @Override
    public XmlObject encode(Process process) throws EncodingException {
        return encode(process, EncodingContext.empty());
    }

    @Override
    public XmlObject encode(Process process, EncodingContext ec) throws EncodingException {
        return createProcess(process);
    }

    protected ProcessType createProcess(Process process) throws EncodingException {
        if (process.isSetXml()) {
            XmlObject encodedObject = null;
            try {
                encodedObject = XmlObject.Factory.parse(process.getXml());
                if (encodedObject instanceof ProcessType) {
                    ProcessType pt = (ProcessType) encodedObject;
                    checkForInspireId(pt, process);
                    return pt;
                } else if (encodedObject instanceof ProcessDocument) {
                    return ((ProcessDocument) encodedObject).getProcess();
                } else if (encodedObject instanceof ProcessPropertyType) {
                    return ((ProcessPropertyType) encodedObject).getProcess();
                } else {
                    throw new UnsupportedEncoderInputException(this, process);
                }
            } catch (final XmlException xmle) {
                throw new EncodingException(xmle);
            }
        } else {
            ProcessType pt = ProcessType.Factory.newInstance();
            if (!process.isSetGmlID()) {
                process.setGmlId("p_" + JavaHelper.generateID(process.toString()));
            }
            pt.setId(process.getGmlId());

            addInspireId(pt, process);
            addName(pt, process);
            addType(pt, process);
            addDocumentation(pt, process);
            addProcessParameter(pt, process);
            addResponsibleParty(pt, process);
            return pt;
        }
    }

    private void checkForInspireId(ProcessType pt, Process process) throws EncodingException {
        if (pt.getInspireId() == null) {
            if (process.isSetIdentifier()) {
                addInspireId(pt, process);
            } else {
                InspireId iId = pt.addNewInspireId();
                iId.setNil();
                iId.setNilReason("unknown");
            }
        }

    }

    private void addInspireId(ProcessType pt, Process process) throws EncodingException {
        pt.addNewInspireId().set(encodeBASEPropertyType(process.getInspireId()));
    }

    private void addName(ProcessType pt, Process process) {
        if (process.isSetName()) {
            pt.addNewName2().setStringValue(process.getFirstName().getValue());
        }
    }

    private void addType(ProcessType pt, Process process) {
        if (process.isSetType()) {
            pt.addNewType().setStringValue(process.getType());
        } else {
            pt.addNewType().setNil();
        }
    }

    private void addDocumentation(ProcessType pt, Process process) throws EncodingException {
        if (process.isSetDocumentation()) {
            for (DocumentCitation documentCitation : process.getDocumentation()) {
                pt.addNewDocumentation().addNewDocumentCitation().set(encodeBASE2(documentCitation));
            }
        }
    }

    private void addProcessParameter(ProcessType pt, Process process) throws EncodingException {
        if (process.isSetProcessParameter()) {
            for (ProcessParameter processParameter : process.getProcessParameter()) {
                pt.addNewProcessParameter().addNewProcessParameter().set(encodeOMPR(processParameter));
            }
        }
    }

    private void addResponsibleParty(ProcessType pt, Process process) throws EncodingException {
        if (process.isSetResponsibleParty()) {
            for (RelatedParty relatedParty : process.getResponsibleParty()) {
                pt.addNewResponsibleParty().addNewRelatedParty().set(encodeBASE2(relatedParty));
            }
        }
    }

    @Override
    protected XmlObject createFeature(FeaturePropertyType featurePropertyType, AbstractFeature abstractFeature,
            EncodingContext context) throws EncodingException {
        if (context.has(XmlBeansEncodingFlags.ENCODE) && !context.getBoolean(XmlBeansEncodingFlags.ENCODE)) {
            featurePropertyType.setHref(abstractFeature.getIdentifierCodeWithAuthority().getValue());
            if (abstractFeature.isSetName()) {
                featurePropertyType.setTitle(abstractFeature.getFirstName().getValue());
            }
            return featurePropertyType;
        }
        return encodeOMPRDocument(abstractFeature);
    }

    protected XmlObject encodeOMPR(Object o) throws EncodingException {
        return encodeObjectToXml(InspireOMPRConstants.NS_OMPR_30, o);
    }

    protected XmlObject encodeOMPRDocument(Object o) throws EncodingException {
        return encodeObjectToXmlDocument(InspireBaseConstants.NS_BASE, o);
    }

    protected XmlObject encodeBASE(Object o) throws EncodingException {
        return encodeObjectToXml(InspireBaseConstants.NS_BASE, o);
    }

    protected XmlObject encodeBASE(Object o, EncodingContext encodingContext) throws EncodingException {
        return encodeObjectToXml(InspireBaseConstants.NS_BASE, o, encodingContext);
    }

    protected XmlObject encodeBASEPropertyType(Object o) throws EncodingException {
        return encodeObjectToXmlPropertyType(InspireBaseConstants.NS_BASE, o);
    }

    protected XmlObject encodeBASEDocument(Object o) throws EncodingException {
        return encodeObjectToXmlDocument(InspireBaseConstants.NS_BASE, o);
    }

    protected XmlObject encodeBASE2(Object o) throws EncodingException {
        return encodeObjectToXml(InspireBase2Constants.NS_BASE2, o);
    }

    protected XmlObject encodeBASE2(Object o, EncodingContext encodingContext) throws EncodingException {
        return encodeObjectToXml(InspireBase2Constants.NS_BASE2, o, encodingContext);
    }

    protected XmlObject encodeBASE2PropertyType(Object o) throws EncodingException {
        return encodeObjectToXmlPropertyType(InspireBase2Constants.NS_BASE2, o);
    }

    protected XmlObject encodeBASE2Document(Object o) throws EncodingException {
        return encodeObjectToXmlDocument(InspireBase2Constants.NS_BASE2, o);
    }

}
