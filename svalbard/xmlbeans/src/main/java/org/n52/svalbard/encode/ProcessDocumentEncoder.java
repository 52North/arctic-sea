/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.inspire.ompr.InspireOMPRConstants;
import org.n52.shetland.inspire.ompr.Process;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.svalbard.encode.exception.EncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import eu.europa.ec.inspire.schemas.ompr.x30.ProcessDocument;


public class ProcessDocumentEncoder extends ProcessTypeEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessDocumentEncoder.class);

    private static final Set<EncoderKey> ENCODER_KEYS =
            Sets.newHashSet(new ClassToClassEncoderKey(Process.class, ProcessDocument.class),
                    new XmlDocumentEncoderKey(InspireOMPRConstants.NS_OMPR_30, Process.class),
                    new XmlDocumentEncoderKey(InspireOMPRConstants.NS_OMPR_30, SosProcedureDescription.class),
                    new XmlDocumentEncoderKey(InspireOMPRConstants.FEATURE_CONCEPT_PROCESS, Process.class),
                    new XmlDocumentEncoderKey(InspireOMPRConstants.FEATURE_CONCEPT_PROCESS,
                            SosProcedureDescription.class));

    public ProcessDocumentEncoder() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public XmlObject encode(Process process) throws EncodingException {
        return encode(process, EncodingContext.empty());
    }

    @Override
    public XmlObject encode(Process process, EncodingContext context)
            throws EncodingException {
        ProcessDocument pd = ProcessDocument.Factory.newInstance();
        pd.setProcess(createProcess(process));
        return pd;
    }

}
