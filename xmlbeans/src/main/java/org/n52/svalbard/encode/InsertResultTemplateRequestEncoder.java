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
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.InsertResultTemplateRequest;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swe.encoding.SweAbstractEncoding;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import net.opengis.sos.x20.InsertResultTemplateDocument;
import net.opengis.sos.x20.InsertResultTemplateType;
import net.opengis.sos.x20.ResultTemplateType;

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
        InsertResultTemplateDocument doc = InsertResultTemplateDocument.Factory.newInstance(getXmlOptions());
        InsertResultTemplateType insertResultTemplate = doc.addNewInsertResultTemplate();
        addService(insertResultTemplate);
        addVersion(insertResultTemplate);
        // reduced element hierarchy and switched to sos:ResultTemplate level
        ResultTemplateType resultTemplate = insertResultTemplate.addNewProposedTemplate().addNewResultTemplate();
        addIdentifier(resultTemplate, request);
        addOffering(resultTemplate, request);
        addObservationTemplate(resultTemplate, request);
        addResultEncoding(resultTemplate, request.getResultEncoding().get().get());
        return doc;
    }

    private void validateInput(InsertResultTemplateRequest request) throws UnsupportedEncoderInputException {
        if (request == null) {
            throw new UnsupportedEncoderInputException(this, "null");
        }
        if (!request.isSetObservationTemplate()) {
            throw new UnsupportedEncoderInputException(this, "missing ObservationTemplate");
        }
        if (request.getObservationTemplate().getOfferings() == null ||
                request.getObservationTemplate().getOfferings().size() != 1) {
            throw new UnsupportedEncoderInputException(this, "missing offering");
        }
        if (!request.isSetResultStructure()) {
            throw new UnsupportedEncoderInputException(this, "missing resultStructure");
        }
        if (!request.isSetResultEncoding() || !request.getResultEncoding().get().isPresent()) {
            throw new UnsupportedEncoderInputException(this, "missing resultEncoding");
        }
    }

    private void addService(InsertResultTemplateType insertResultTemplate) {
        insertResultTemplate.setService(SosConstants.SOS);
    }

    private void addVersion(InsertResultTemplateType insertResultTemplate) {
        insertResultTemplate.setVersion(Sos2Constants.SERVICEVERSION);
    }

    private void addIdentifier(ResultTemplateType resultTemplate, InsertResultTemplateRequest request) {
        // identifier is optional
        if (request.getIdentifier() != null &&
                request.getIdentifier().getValue() != null &&
                !request.getIdentifier().getValue().isEmpty()) {
            resultTemplate.setIdentifier(request.getIdentifier().getValue());
        }
    }

    private void addOffering(ResultTemplateType resultTemplate, InsertResultTemplateRequest request) {
        resultTemplate.setOffering(request.getObservationTemplate().getOfferings().iterator().next());
    }

    private void addObservationTemplate(ResultTemplateType resultTemplate, InsertResultTemplateRequest request)
            throws EncodingException {
        resultTemplate.addNewObservationTemplate().addNewOMObservation()
            .set(encodeObjectToXml(OmConstants.NS_OM_2,
                    request.getObservationTemplate(),
                    EncodingContext.empty().with(XmlBeansEncodingFlags.PROPERTY_TYPE)));
    }

    private void addResultEncoding(ResultTemplateType resultTemplate, SweAbstractEncoding sweAbstractEncoding)
            throws EncodingException {
        resultTemplate.addNewResultEncoding().addNewAbstractEncoding()
            .set(encodeObjectToXml(SweConstants.NS_SWE_20,
                    sweAbstractEncoding,
                    EncodingContext.empty().with(XmlBeansEncodingFlags.PROPERTY_TYPE)));
    }
}
