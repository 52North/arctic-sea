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

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.request.InsertResultTemplateRequest;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class InsertResultTemplateRequestEncoder extends AbstractSwesRequestEncoder<InsertResultTemplateRequest> {

    public InsertResultTemplateRequestEncoder() {
        super(Sos2Constants.Operations.InsertResultTemplate.name(), InsertResultTemplateRequest.class);
    }

    @Override
    protected XmlObject create(InsertResultTemplateRequest request) throws EncodingException {
        validateInput(request);
        return null;
    }

    private void validateInput(InsertResultTemplateRequest request) throws UnsupportedEncoderInputException {
        if (request == null) {
            throw new UnsupportedEncoderInputException(this, "null");
        }
        if (!request.isSetObservationTemplate()) {
            throw new UnsupportedEncoderInputException(this, "missing ObservationTemplate");
        }
        if (!request.isSetResultStructure()) {
            throw new UnsupportedEncoderInputException(this, "missing resultStructure");
        }
        if (!request.isSetResultEncoding()) {
            throw new UnsupportedEncoderInputException(this, "missing resultEncoding");
        }
    }

}
