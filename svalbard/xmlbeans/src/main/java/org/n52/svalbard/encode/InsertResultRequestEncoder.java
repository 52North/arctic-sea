/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
import org.apache.xmlbeans.XmlString;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.request.InsertResultRequest;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import net.opengis.sos.x20.InsertResultDocument;
import net.opengis.sos.x20.InsertResultType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class InsertResultRequestEncoder extends AbstractSwesRequestEncoder<InsertResultRequest> {

    public InsertResultRequestEncoder() {
        super(Sos2Constants.Operations.InsertResult.name(), InsertResultRequest.class);
    }

    @Override
    protected XmlObject create(InsertResultRequest request) throws EncodingException {
        validateInput(request);
        InsertResultDocument doc = InsertResultDocument.Factory.newInstance(getXmlOptions());
        InsertResultType insertResult = doc.addNewInsertResult();
        insertResult.setService(request.getService());
        insertResult.setVersion(request.getVersion());
        insertResult.setTemplate(request.getTemplateIdentifier());
        addResultValues(request, insertResult);
        return doc;
    }

    private void addResultValues(InsertResultRequest request, InsertResultType insertResult) {
        XmlString xbString = XmlString.Factory.newInstance(getXmlOptions());
        xbString.setStringValue(request.getResultValues());
        insertResult.setResultValues(xbString);
    }

    @Override
    protected void validateInput(InsertResultRequest request) throws UnsupportedEncoderInputException {
        super.validateInput(request);
        if (!request.isSetTemplateIdentifier()) {
            throw new UnsupportedEncoderInputException(this, "missing templateIdentifier");
        }
        if (!request.isSetResultValues() || request.getResultValues().isEmpty()) {
            throw new UnsupportedEncoderInputException(this, "missing resultValues");
        }
    }

}
