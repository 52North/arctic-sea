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

import org.n52.shetland.ogc.filter.FilterConstants.TimeOperator;
import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.JSONValidator;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class TemporalFilterDecoder
        extends JSONDecoder<TemporalFilter> {
    public TemporalFilterDecoder() {
        super(TemporalFilter.class);
    }

    @Override
    public TemporalFilter decodeJSON(JsonNode node, boolean validate)
            throws DecodingException {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        }
        if (validate) {
            JSONValidator.getInstance().validateAndThrow(node, SchemaConstants.Common.TEMPORAL_FILTER);
        }
        if (node.isObject()) {
            return parseTemporalFilter(node);
        } else {
            return null;
        }
    }

    protected TemporalFilter parseTemporalFilter(JsonNode node)
            throws DecodingException {
        if (node.isObject()) {
            final String oName = node.fields().next().getKey();
            final TOp o = TOp.valueOf(oName);
            return new TemporalFilter(o.getOp(), parseTime(node.path(oName).path(JSONConstants.VALUE)),
                    node.path(oName).path(JSONConstants.REF).textValue());
        } else {
            return null;
        }
    }

    private enum TOp {
        before(TimeOperator.TM_Before), after(TimeOperator.TM_After), begins(TimeOperator.TM_Begins), ends(
                TimeOperator.TM_Ends), endedBy(TimeOperator.TM_EndedBy), begunBy(TimeOperator.TM_BegunBy), during(
                        TimeOperator.TM_During), equals(TimeOperator.TM_Equals), contains(
                                TimeOperator.TM_Contains), overlaps(TimeOperator.TM_Overlaps), meets(
                                        TimeOperator.TM_Meets), metBy(
                                                TimeOperator.TM_MetBy), overlappedBy(TimeOperator.TM_OverlappedBy);
        private TimeOperator op;

        TOp(TimeOperator op) {
            this.op = op;
        }

        public TimeOperator getOp() {
            return op;
        }
    }
}
