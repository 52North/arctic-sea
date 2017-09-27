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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.om.ObservationStream;
import org.n52.shetland.ogc.om.ObservationType;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.response.AbstractObservationResponse;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @param <T>
 *            the response type
 *
 * @since 1.0.0
 */
public abstract class AbstractObservationResponseEncoder<T extends AbstractObservationResponse>
        extends AbstractSosResponseEncoder<T>
        implements org.n52.svalbard.encode.ObservationEncoder<JsonNode, T> {
    public AbstractObservationResponseEncoder(Class<T> type, String operation) {
        super(type, operation);
    }

    public AbstractObservationResponseEncoder(Class<T> type, Enum<?> operation) {
        super(type, operation);
    }

    @Override
    protected void encodeResponse(ObjectNode json, T t) throws EncodingException {
        ArrayNode obs = json.putArray(JSONConstants.OBSERVATIONS);
        try {
            while (t.getObservationCollection().hasNext()) {
                OmObservation o = t.getObservationCollection().next();
                if (o.getValue() instanceof ObservationStream) {
                    ObservationStream value = (ObservationStream) o.getValue();
                        while (value.hasNext()) {
                            obs.add(encodeObjectToJson(value.next()));
                    }
                } else {
                    obs.add(encodeObjectToJson(o));
                }
            }
        } catch (OwsExceptionReport ex) {
            throw new EncodingException(ex);
        }
    }

    @Override
    public boolean isObservationAndMeasurmentV20Type() {
        return false;
    }

    @Override
    public boolean supportsResultStreamingForMergedValues() {
        return false;
    }

    @Override
    public boolean shouldObservationsWithSameXBeMerged() {
        return false;
    }

    @Override
    public Set<String> getSupportedResponseFormats(String service, String version) {
        return Collections.singleton(MediaTypes.APPLICATION_JSON.toString());
    }

    @Override
    public Map<String, Set<SupportedType>> getSupportedResponseFormatObservationTypes() {
        return Collections.singletonMap(MediaTypes.APPLICATION_JSON.toString(),
                Sets.newHashSet(new ObservationType(OmConstants.OBS_TYPE_CATEGORY_OBSERVATION),
                        new ObservationType(OmConstants.OBS_TYPE_COUNT_OBSERVATION),
                        new ObservationType(OmConstants.OBS_TYPE_GEOMETRY_OBSERVATION),
                        new ObservationType(OmConstants.OBS_TYPE_MEASUREMENT),
                        new ObservationType(OmConstants.OBS_TYPE_TEXT_OBSERVATION),
                        new ObservationType(OmConstants.OBS_TYPE_TRUTH_OBSERVATION),
                        new ObservationType(OmConstants.OBS_TYPE_SWE_ARRAY_OBSERVATION),
                        new ObservationType(OmConstants.OBS_TYPE_REFERENCE_OBSERVATION)));
    }
}
