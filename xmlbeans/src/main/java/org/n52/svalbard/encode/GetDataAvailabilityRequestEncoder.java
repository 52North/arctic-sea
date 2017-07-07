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

import java.util.Set;

import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityRequest;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:j.schulte@52north.org">Jan Schulte</a>
 */
public class GetDataAvailabilityRequestEncoder extends AbstractSosRequestEncoder<GetDataAvailabilityRequest> {

    public GetDataAvailabilityRequestEncoder() {
        super(GetDataAvailabilityConstants.OPERATION_NAME, GetDataAvailabilityRequest.class);
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(GetDataAvailabilityConstants.GET_DATA_AVAILABILITY_SCHEMA_LOCATION);
    }

    @Override
    protected XmlObject create(GetDataAvailabilityRequest request) throws EncodingException {
        return request.getNamespace().equals(GetDataAvailabilityConstants.NS_GDA_20)
               ? createGDAv20(request) : createGDAv10(request);
    }

    private XmlObject createGDAv10(GetDataAvailabilityRequest request) {
        net.opengis.sosgda.x10.GetDataAvailabilityDocument document
                = net.opengis.sosgda.x10.GetDataAvailabilityDocument.Factory.newInstance();
        net.opengis.sosgda.x10.GetDataAvailabilityType gdat = document.addNewGetDataAvailability();
        gdat.setService(request.isSetService() ? request.getService() : SosConstants.SOS);
        gdat.setVersion(request.isSetVersion() ? request.getVersion() : Sos1Constants.SERVICEVERSION);
        request.getOfferings().forEach(gdat::addOffering);
        request.getProcedures().forEach(gdat::addProcedure);
        request.getFeaturesOfInterest().forEach(gdat::addFeatureOfInterest);
        request.getObservedProperties().forEach(gdat::addObservedProperty);
        return document;
    }

    private XmlObject createGDAv20(GetDataAvailabilityRequest request) {
        net.opengis.sosgda.x20.GetDataAvailabilityDocument document
                = net.opengis.sosgda.x20.GetDataAvailabilityDocument.Factory.newInstance();
        net.opengis.sosgda.x20.GetDataAvailabilityType gdat = document.addNewGetDataAvailability();
        gdat.setService(request.isSetService() ? request.getService() : SosConstants.SOS);
        gdat.setVersion(request.isSetVersion() ? request.getVersion() : Sos2Constants.SERVICEVERSION);
        request.getOfferings().forEach(gdat::addOffering);
        request.getProcedures().forEach(gdat::addProcedure);
        request.getFeaturesOfInterest().forEach(gdat::addFeatureOfInterest);
        request.getObservedProperties().forEach(gdat::addObservedProperty);
        return document;
    }

}
