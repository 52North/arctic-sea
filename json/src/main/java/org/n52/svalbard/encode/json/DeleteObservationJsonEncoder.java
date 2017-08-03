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

import org.n52.shetland.ogc.ows.exception.CompositeOwsException;
import org.n52.shetland.ogc.ows.exception.MissingParameterValueException;
import org.n52.shetland.ogc.ows.exception.MissingServiceParameterException;
import org.n52.shetland.ogc.ows.exception.MissingVersionParameterException;
import org.n52.shetland.ogc.sos.delobs.DeleteObservationConstants;
import org.n52.shetland.ogc.sos.delobs.DeleteObservationResponse;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 */
public class DeleteObservationJsonEncoder extends AbstractSosResponseEncoder<DeleteObservationResponse> {

    public DeleteObservationJsonEncoder() {
        super(DeleteObservationResponse.class,
              DeleteObservationConstants.Operations.DeleteObservation);
    }

    @Override
    protected void encodeResponse(ObjectNode json, DeleteObservationResponse t)
            throws EncodingException {
        if (t == null) {
            throw new UnsupportedEncoderInputException(this, DeleteObservationResponse.class);
        }
        final CompositeOwsException exceptions = new CompositeOwsException();
        if (t.getService() == null) {
            exceptions.add(new MissingServiceParameterException());
        }
        if (t.getVersion() == null) {
            exceptions.add(new MissingVersionParameterException());
        }
        if (DeleteObservationConstants.NS_SOSDO_1_0.equals(t.getOperationVersion())) {
            if (t.getObservationId() == null || t.getObservationId().isEmpty()) {
                exceptions.add(new MissingParameterValueException(DeleteObservationConstants.PARAM_OBSERVATION));
            } else {
                json.put(JSONConstants.DELETED_OBSERVATION, t.getObservationId());
            }
        }
        try {
            exceptions.throwIfNotEmpty();
        } catch (CompositeOwsException e) {
            throw new EncodingException(e);
        }
    }

}
