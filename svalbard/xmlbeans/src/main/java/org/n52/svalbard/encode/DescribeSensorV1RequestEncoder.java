/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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

import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.DescribeSensorRequest;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import net.opengis.sos.x10.DescribeSensorDocument;

/**
 * @author <a href="mailto:j.schulte@52north.org">Jan Schulte</a>
 */
public class DescribeSensorV1RequestEncoder extends AbstractSosV1RequestEncoder<DescribeSensorRequest> {

    public DescribeSensorV1RequestEncoder() {
        super(SosConstants.Operations.DescribeSensor.name(), DescribeSensorRequest.class);
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet();
    }

    @Override
    protected XmlObject create(DescribeSensorRequest request) throws EncodingException {
        DescribeSensorDocument doc = DescribeSensorDocument.Factory.newInstance(getXmlOptions());
        DescribeSensorDocument.DescribeSensor descSens = doc.addNewDescribeSensor();
        addVersion(descSens, request);
        addService(descSens, request);
        addProcedure(descSens, request);
        addOutputFormat(descSens, request);
        return doc;
    }

    private void addVersion(DescribeSensorDocument.DescribeSensor descSens, DescribeSensorRequest request) {
        if (request.getVersion() != null) {
            descSens.setVersion(request.getVersion());
        } else {
            descSens.setVersion(Sos1Constants.SERVICEVERSION);
        }
    }

    private void addService(DescribeSensorDocument.DescribeSensor descSens, DescribeSensorRequest request) {
        if (request.getService() != null) {
            descSens.setService(request.getService());
        } else {
            descSens.setService(SosConstants.SOS);
        }
    }

    private void addProcedure(DescribeSensorDocument.DescribeSensor descSens, DescribeSensorRequest request) {
        descSens.setProcedure(request.getProcedure());
    }

    private void addOutputFormat(DescribeSensorDocument.DescribeSensor descSens, DescribeSensorRequest request) {
        descSens.setOutputFormat(request.getProcedureDescriptionFormat());
    }

}
