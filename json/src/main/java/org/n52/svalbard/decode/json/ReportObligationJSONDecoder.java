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

import org.n52.shetland.aqd.EReportingChange;
import org.n52.shetland.aqd.ReportObligation;
import org.n52.shetland.inspire.base.Identifier;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;

public class ReportObligationJSONDecoder
        extends AbstractJSONDecoder<ReportObligation> {

    public ReportObligationJSONDecoder() {
        super(ReportObligation.class);
    }

    @Override
    public ReportObligation decodeJSON(JsonNode node, boolean validate) throws DecodingException {
        ReportObligation reportObligation = new ReportObligation();
        reportObligation.setChange(decodeJsonToObject(node.path(AQDJSONConstants.CHANGE), EReportingChange.class));
        reportObligation.setInspireID(decodeJsonToObject(node.path(AQDJSONConstants.INSPIRE_ID), Identifier.class));
        reportObligation.setReportingPeriod(parseReferenceableTime(node.path(AQDJSONConstants.REPORTING_PERIOD)));
        return reportObligation;
    }

}
