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

import java.util.Collections;
import java.util.Set;

import org.n52.shetland.inspire.ompr.InspireOMPRConstants;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.sos.ProcedureDescriptionFormat;
import org.n52.shetland.inspire.ompr.Process;

import com.google.common.collect.Sets;

import eu.europa.ec.inspire.schemas.base.x33.IdentifierType;
import eu.europa.ec.inspire.schemas.ompr.x30.ProcessType;

public abstract class AbstractProcessDecoder<S>
        extends AbstractXmlDecoder<S, Process>
        implements ProcedureDecoder<Process, S> {

    private static final Set<SupportedType> SUPPORTED_TYPES =
            Sets.newHashSet(new ProcedureDescriptionFormat(InspireOMPRConstants.OMPR_30_OUTPUT_FORMAT_URL),
                    new ProcedureDescriptionFormat(InspireOMPRConstants.OMPR_30_OUTPUT_FORMAT_MIME_TYPE));

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.unmodifiableSet(SUPPORTED_TYPES);
    }

    @Override
    public Set<String> getSupportedProcedureDescriptionFormats(final String service, final String version) {
        return Collections.emptySet();
    }

    protected Process parseProcessType(ProcessType pt) {
        Process process = new Process();
        parseInspireId(pt, process);
        return process;
    }

    private void parseInspireId(ProcessType pt, Process process) {
        IdentifierType identifier = pt.getInspireId().getIdentifier();
        String localId = identifier.getLocalId();
        String namespace = identifier.getNamespace();
        CodeWithAuthority codeWithAuthority;
        if (localId.contains(namespace)) {
            codeWithAuthority = new CodeWithAuthority(localId, namespace);
        } else {
            codeWithAuthority = new CodeWithAuthority(getIdentifier(localId, namespace), namespace);
        }
        process.setIdentifier(codeWithAuthority);
    }

    private String getIdentifier(String localId, String namespace) {
        if (namespace.endsWith("=")) {
            return namespace + localId;
        }
        if (namespace.startsWith("urn")) {
            return namespace + ":" + localId;
        } else if (namespace.startsWith("http")) {
            return namespace + "/" + localId;
        }
        return namespace + "-" + localId;
    }
}
