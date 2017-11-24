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

import java.util.LinkedList;
import java.util.List;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.InsertObservationRequest;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import net.opengis.sos.x20.InsertObservationDocument;
import net.opengis.sos.x20.InsertObservationType;
import net.opengis.sos.x20.InsertObservationType.Observation;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class InsertObservationRequestEncoder extends AbstractSwesRequestEncoder<InsertObservationRequest> {

    public InsertObservationRequestEncoder() {
        super(SosConstants.Operations.InsertObservation.name(), InsertObservationRequest.class);
    }

    @Override
    protected XmlObject create(InsertObservationRequest request) throws EncodingException {
        validateInput(request);
        InsertObservationDocument doc = InsertObservationDocument.Factory.newInstance(getXmlOptions());
        InsertObservationType insertObservation = doc.addNewInsertObservation();
        insertObservation.setService(request.getService());
        insertObservation.setVersion(request.getVersion());
        addOfferings(request.getOfferings(), insertObservation);
        addObservations(request.getObservations(), insertObservation);
        return doc;
    }

    private void addObservations(List<OmObservation> observations, InsertObservationType insertObservation)
            throws EncodingException {
        Observation ob = insertObservation.addNewObservation();
        final List<EncodingException> thrownExceptions = new LinkedList<>();
        observations.stream().forEach(o -> {
            try {
                if (thrownExceptions.isEmpty()) {
                    ob.addNewOMObservation().set(encodeObjectToXmlDocument(OmConstants.NS_OM_2, o));
                }
            } catch (EncodingException e) {
                thrownExceptions.add(e);
            }
        });
        if (!thrownExceptions.isEmpty()) {
            throw thrownExceptions.get(0);
        }
    }

    private void addOfferings(List<String> offerings, InsertObservationType insertObservation) {
        offerings.stream().forEach(o -> insertObservation.addNewOffering().setStringValue(o));
    }

    @Override
    protected void validateInput(InsertObservationRequest request) throws UnsupportedEncoderInputException {
        super.validateInput(request);
        if (!request.getVersion().equals(Sos2Constants.SERVICEVERSION)) {
            throw new UnsupportedEncoderInputException(this, "SOS 1.0.0 insert observation request");
        }
        if (!request.isSetOfferings()) {
            throw new UnsupportedEncoderInputException(this, "missing offering(s)");
        }
        if (!request.isSetObservation()) {
            throw new UnsupportedEncoderInputException(this, "missing observation(s)");
        }
    }

}
