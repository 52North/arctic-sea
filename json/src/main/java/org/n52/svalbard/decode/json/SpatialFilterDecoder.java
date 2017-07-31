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

import org.n52.shetland.ogc.filter.FilterConstants.SpatialOperator;
import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.JSONValidator;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.vividsolutions.jts.geom.Geometry;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class SpatialFilterDecoder
        extends JSONDecoder<SpatialFilter> {
    public SpatialFilterDecoder() {
        super(SpatialFilter.class);
    }

    @Override
    public SpatialFilter decodeJSON(JsonNode node, boolean validate)
            throws DecodingException {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        }
        if (validate) {
            JSONValidator.getInstance().validateAndThrow(node, SchemaConstants.Common.SPATIAL_FILTER);
        }
        if (node.isObject()) {
            final String oName = node.fields().next().getKey();
            final SOp o = SOp.valueOf(oName);
            JsonNode value = node.path(oName).path(JSONConstants.VALUE);
            JsonNode ref = node.path(oName).path(JSONConstants.REF);
            return new SpatialFilter(o.getOp(), decodeGeometry(value), ref.textValue());
        } else {
            return null;
        }
    }

    private Geometry decodeGeometry(JsonNode value)
            throws DecodingException {
        return decodeJsonToObject(value, Geometry.class);
    }

    private enum SOp {
        equals(SpatialOperator.Equals), disjount(SpatialOperator.Disjoint), touches(SpatialOperator.Touches), within(
                SpatialOperator.Within), overlaps(SpatialOperator.Overlaps), crosses(
                        SpatialOperator.Crosses), intersects(SpatialOperator.Intersects), contains(
                                SpatialOperator.Contains), dWithin(SpatialOperator.DWithin), beyond(
                                        SpatialOperator.Beyond), bbox(SpatialOperator.BBOX);
        private SpatialOperator op;

        SOp(SpatialOperator op) {
            this.op = op;
        }

        public SpatialOperator getOp() {
            return op;
        }
    }
}
