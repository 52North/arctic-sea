/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
        validateInput(request);
        DescribeSensorDocument doc = DescribeSensorDocument.Factory.newInstance(getXmlOptions());
        DescribeSensorType descSensType = doc.addNewDescribeSensor();
        descSensType.setVersion(request.getVersion());
        descSensType.setService(request.getService());
        descSensType.setProcedure(request.getProcedure());
        descSensType.setProcedureDescriptionFormat(request.getProcedureDescriptionFormat());
        return doc;
    }

}
