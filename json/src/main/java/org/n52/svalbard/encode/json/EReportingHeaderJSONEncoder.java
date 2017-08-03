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
package org.n52.svalbard.encode.json;

import org.n52.janmayen.Json;
import org.n52.shetland.aqd.EReportingHeader;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class EReportingHeaderJSONEncoder extends JSONEncoder<EReportingHeader> {

    public EReportingHeaderJSONEncoder() {
        super(EReportingHeader.class);
    }

    @Override
    public JsonNode encodeJSON(EReportingHeader header)
            throws EncodingException {
        ObjectNode j = Json.nodeFactory().objectNode();
        j.set(AQDJSONConstants.CHANGE, encodeObjectToJson(header.getChange()));
        j.set(AQDJSONConstants.DELETE, encodeObjectToJson(header.getDelete()));
        j.set(AQDJSONConstants.CONTENT, encodeObjectToJson(header.getContent()));
        j.set(AQDJSONConstants.INSPIRE_ID, encodeObjectToJson(header.getInspireID()));
        j.set(AQDJSONConstants.REPORTING_AUTHORITY, encodeObjectToJson(header.getReportingAuthority()));
        j.set(AQDJSONConstants.REPORTING_PERIOD, encodeObjectToJson(header.getReportingPeriod()));
        return j;
    }
}
