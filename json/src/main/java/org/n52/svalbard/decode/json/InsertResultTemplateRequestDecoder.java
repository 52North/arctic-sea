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
package org.n52.svalbard.decode.json;

import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosResultEncoding;
import org.n52.shetland.ogc.sos.SosResultStructure;
import org.n52.shetland.ogc.sos.request.InsertResultTemplateRequest;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.encoding.SweTextEncoding;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @since 1.0.0
 *
 */
public class InsertResultTemplateRequestDecoder
        extends AbstractSosRequestDecoder<InsertResultTemplateRequest> {
    private final ObservationDecoder observationDecoder = new ObservationDecoder();

    public InsertResultTemplateRequestDecoder() {
        super(InsertResultTemplateRequest.class, SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                Sos2Constants.Operations.InsertResultTemplate);
    }

    @Override
    public String getSchemaURI() {
        return SchemaConstants.Request.INSERT_RESULT_TEMPLATE;
    }

    @Override
    public InsertResultTemplateRequest decodeRequest(JsonNode node)
            throws DecodingException {
        InsertResultTemplateRequest irtr = new InsertResultTemplateRequest();
        if (!node.path(JSONConstants.IDENTIFIER).isMissingNode()) {
            irtr.setIdentifier(node.path(JSONConstants.IDENTIFIER).textValue());
        }
        irtr.setObservationTemplate(parseObservationTemplate(node));
        irtr.setResultStructure(parseResultStructure(node.path(JSONConstants.RESULT_STRUCTURE)));
        irtr.setResultEncoding(parseResultEncoding(node.path(JSONConstants.RESULT_ENCODING)));
        return irtr;
    }

    private OmObservationConstellation parseObservationTemplate(JsonNode node)
            throws DecodingException {
        OmObservationConstellation oc =
                observationDecoder.parseObservationConstellation(node.path(JSONConstants.OBSERVATION_TEMPLATE));
        oc.addOffering(node.path(JSONConstants.OFFERING).textValue());
        return oc;
    }

    private SosResultStructure parseResultStructure(JsonNode node)
            throws DecodingException {
        SweDataRecord dataRecord = parseFields(node.path(JSONConstants.FIELDS));
        return new SosResultStructure(dataRecord);
    }

    private SosResultEncoding parseResultEncoding(JsonNode node) {
        SweTextEncoding textEncoding = new SweTextEncoding();
        textEncoding.setTokenSeparator(node.path(JSONConstants.TOKEN_SEPARATOR).textValue());
        textEncoding.setBlockSeparator(node.path(JSONConstants.BLOCK_SEPARATOR).textValue());
        if (!node.path(JSONConstants.DECIMAL_SEPARATOR).isMissingNode()) {
            textEncoding.setDecimalSeparator(node.path(JSONConstants.DECIMAL_SEPARATOR).textValue());
        }
        return new SosResultEncoding(textEncoding);
    }

    protected SweDataRecord parseFields(JsonNode node)
            throws DecodingException {
        SweDataRecord dataRecord = new SweDataRecord();
        for (JsonNode field : node) {
            dataRecord.addField(decodeJsonToObject(field, SweField.class));
        }
        return dataRecord;
    }
}
