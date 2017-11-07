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

import net.opengis.swes.x20.DescribeSensorDocument;
import net.opengis.swes.x20.DescribeSensorType;
import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.DescribeSensorRequest;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * @author <a href="mailto:j.schulte@52north.org">Jan Schulte</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">J6uuml;rrens, Eike Hinderk</a>
 */
public class DescribeSensorV2RequestEncoder extends AbstractSwesRequestEncoder<DescribeSensorRequest> {

    public DescribeSensorV2RequestEncoder() {
        super(SosConstants.Operations.DescribeSensor.name(), DescribeSensorRequest.class);
    }

    @Override
    protected XmlObject create(DescribeSensorRequest request) throws EncodingException {
        DescribeSensorDocument doc = DescribeSensorDocument.Factory.newInstance(getXmlOptions());
        DescribeSensorType descSensType = doc.addNewDescribeSensor();
        addVersion(descSensType, request);
        addService(descSensType, request);
        addProcedure(descSensType, request);
        addOutputFormat(descSensType, request);
        return doc;
    }

    private void addVersion(DescribeSensorType descSensType, DescribeSensorRequest request) {
        if (request.getVersion() != null) {
            descSensType.setVersion(request.getVersion());
        } else {
            descSensType.setVersion(Sos2Constants.SERVICEVERSION);
        }
    }

    private void addService(DescribeSensorType descSensType, DescribeSensorRequest request) {
        if (request.getService() != null) {
            descSensType.setService(request.getService());
        } else {
            descSensType.setService(SosConstants.SOS);
        }
    }

    private void addProcedure(DescribeSensorType descSensType, DescribeSensorRequest request) {
        descSensType.setProcedure(request.getProcedure());
    }

    private void addOutputFormat(DescribeSensorType descSensType, DescribeSensorRequest request) {
        descSensType.setProcedureDescriptionFormat(request.getProcedureDescriptionFormat());
    }

}
