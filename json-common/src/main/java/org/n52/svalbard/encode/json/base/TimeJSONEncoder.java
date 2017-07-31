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
package org.n52.svalbard.encode.json.base;

import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.gml.time.TimePosition;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.svalbard.encode.json.JSONEncoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class TimeJSONEncoder
        extends JSONEncoder<Time> {
    public TimeJSONEncoder() {
        super(Time.class);
    }

    private String encodeTimePosition(TimePosition timePosition) {
        if (timePosition.isSetIndeterminateValue()) {
            return timePosition.getIndeterminateValue().getValue();
        } else if (timePosition.isSetTimeFormat()) {
            return DateTimeHelper.formatDateTime2String(timePosition.getTime(), timePosition.getTimeFormat());
        } else if (timePosition.isSetTime()) {
            return DateTimeHelper.formatDateTime2IsoString(timePosition.getTime());
        } else {
            return null;
        }
    }

    @Override
    public JsonNode encodeJSON(Time time) {
        if (time instanceof TimeInstant) {
            TimeInstant ti = (TimeInstant) time;
            return nodeFactory().textNode(encodeTimePosition(ti.getTimePosition()));
        }
        if (time instanceof TimePeriod) {
            TimePeriod tp = (TimePeriod) time;
            ArrayNode a = nodeFactory().arrayNode();
            a.add(encodeTimePosition(tp.getStartTimePosition()));
            a.add(encodeTimePosition(tp.getEndTimePosition()));
            return a;
        } else {
            return null;
        }
    }
}
