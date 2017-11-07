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

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosProcedureDescriptionUnknownType;
import org.n52.shetland.ogc.sos.request.InsertSensorRequest;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.XmlHelper;

import net.opengis.swes.x20.InsertSensorDocument;
import net.opengis.swes.x20.InsertSensorType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class InsertSensorV2RequestEncoder extends AbstractSwesRequestEncoder<InsertSensorRequest> {

    public InsertSensorV2RequestEncoder() {
        super(Sos2Constants.Operations.InsertSensor.name(), InsertSensorRequest.class);
    }

    @Override
    protected XmlObject create(InsertSensorRequest request) throws EncodingException {
        validateInput(request);
        InsertSensorDocument doc = InsertSensorDocument.Factory.newInstance(getXmlOptions());
        InsertSensorType insertSensor = doc.addNewInsertSensor();
        addVersion(insertSensor);
        addService(insertSensor);
        addProcedureDescriptionFormat(request, insertSensor);
        addProcedureDescription(request, insertSensor);
        addObservableProperties(request, insertSensor);
        addMetadata(request, insertSensor);
        return doc;
    }

    private void validateInput(InsertSensorRequest request) throws UnsupportedEncoderInputException {
        if (request == null) {
            throw new UnsupportedEncoderInputException(this, "null input received.");
        }
        if (!request.isSetProcedureDescriptionFormat()) {
            throw new UnsupportedEncoderInputException(this,
                    "procedure description format not defined in InsertSensorRequest.");
        }
        if (!request.isSetProcedureDescription()) {
            throw new UnsupportedEncoderInputException(this,
                    "procedure description is missing in InsertSensorRequest.");
        }
        if (!request.isSetObservableProperty()) {
            throw new UnsupportedEncoderInputException(this, "observed property is missing in InsertSensorRequest.");
        }
        if (!request.isSetMetadata()) {
            throw new UnsupportedEncoderInputException(this, "metadata field is missing in InsertSensorRequest.");
        }
    }

    private void addProcedureDescription(InsertSensorRequest request, InsertSensorType insertSensor)
            throws EncodingException {

        XmlObject xmlObj;
        if (request.getProcedureDescription() instanceof SosProcedureDescriptionUnknownType &&
                request.getProcedureDescription().isSetXml()) {
            try {
                xmlObj = XmlHelper.parseXmlString(request.getProcedureDescription().getXml());
            } catch (DecodingException de) {
                throw new EncodingException("An xml error occured when parsing the request!", de);
            }
        }
        xmlObj = encodeObjectToXml(request.getProcedureDescriptionFormat(),
                request.getProcedureDescription().getProcedureDescription());

        insertSensor.addNewProcedureDescription().set(xmlObj);
    }

    private void addMetadata(InsertSensorRequest request, InsertSensorType insertSensor) throws EncodingException {
        XmlObject encodedMetadata = encodeObjectToXmlPropertyType(Sos2Constants.NS_SOS_20, request.getMetadata());
        insertSensor.addNewMetadata().addNewInsertionMetadata().set(encodedMetadata);
    }

    private void addObservableProperties(InsertSensorRequest request, InsertSensorType insertSensor) {
        request.getObservableProperty().stream()
            .forEach(c -> { insertSensor.addNewObservableProperty().setStringValue(c);});
    }

    private void addProcedureDescriptionFormat(InsertSensorRequest request, InsertSensorType insertSensor) {
        insertSensor.setProcedureDescriptionFormat(request.getProcedureDescriptionFormat());
    }

    private void addService(InsertSensorType insertSensor) {
        insertSensor.setService(SosConstants.SOS);
    }

    private void addVersion(InsertSensorType insertSensor) {
        insertSensor.setVersion(Sos2Constants.SERVICEVERSION);
    }

}
