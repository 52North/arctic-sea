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

import java.util.Set;

import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.DataAvailability;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.ObservationFormatDescriptor;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.ProcedureDescriptionFormatDescriptor;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GetDataAvailabilityJsonEncoder
        extends AbstractSosResponseEncoder<GetDataAvailabilityResponse> {
    public GetDataAvailabilityJsonEncoder() {
        super(GetDataAvailabilityResponse.class, GetDataAvailabilityConstants.OPERATION_NAME);
    }

    @Override
    protected void encodeResponse(ObjectNode json, GetDataAvailabilityResponse t)
            throws EncodingException {
        ArrayNode a = json.putArray(GetDataAvailabilityConstants.DATA_AVAILABILITY);
        for (DataAvailability da : t.getDataAvailabilities()) {
            ObjectNode objectNode = a.addObject();
            objectNode.put(JSONConstants.FEATURE_OF_INTEREST, da.getFeatureOfInterest().getHref())
                    .put(JSONConstants.PROCEDURE, da.getProcedure().getHref())
                    .put(JSONConstants.OBSERVED_PROPERTY, da.getObservedProperty().getHref())
                    .set(JSONConstants.PHENOMENON_TIME, encodeObjectToJson(da.getPhenomenonTime()));
            if (t.isSetResponseFormat() && GetDataAvailabilityConstants.NS_GDA_20.equals(t.getResponseFormat())) {
                if (da.isSetOffering()) {
                    objectNode.put(JSONConstants.OFFERING, da.getOffering().getHref());
                }
                if (da.isSetFormatDescriptors()) {
                    ObjectNode fdNode = objectNode.putObject(GetDataAvailabilityConstants.FORMAT_DESCRIPTOR);
                    encodeProcedureFormatDescriptor(da.getFormatDescriptor().getProcedureDescriptionFormatDescriptor(),
                            fdNode);
                    encodeObservationFormatDescriptor(da.getFormatDescriptor().getObservationFormatDescriptors(),
                            fdNode);
                }
            }
            if (da.isSetCount()) {
                objectNode.put(JSONConstants.COUNT, da.getCount());
            }
        }
    }

    private void encodeProcedureFormatDescriptor(
            ProcedureDescriptionFormatDescriptor procedureDescriptionFormatDescriptor, ObjectNode fdNode) {
        ObjectNode pfdNode = fdNode.putObject(GetDataAvailabilityConstants.PROCEDURE_FORMAT_DESCRIPTOR);
        pfdNode.put(GetDataAvailabilityConstants.PROCEDURE_DESCRIPTION_FORMAT,
                procedureDescriptionFormatDescriptor.getProcedureDescriptionFormat());
    }

    private void encodeObservationFormatDescriptor(Set<ObservationFormatDescriptor> observationFormatDescriptors,
            ObjectNode fdNode) {
        ArrayNode ofdArray = fdNode.putArray(GetDataAvailabilityConstants.OBSERVATION_FORMAT_DESCRIPTOR);
        for (ObservationFormatDescriptor ofd : observationFormatDescriptors) {
            ObjectNode ofdNode = ofdArray.addObject();
            ofdNode.put(GetDataAvailabilityConstants.RESPONSE_FORMAT, ofd.getResponseFormat());
            ArrayNode otArray = ofdNode.putArray(GetDataAvailabilityConstants.OBSERVATION_TYPE);
            for (String obsType : ofd.getObservationTypes()) {
                otArray.add(obsType);
            }
        }
    }
}
