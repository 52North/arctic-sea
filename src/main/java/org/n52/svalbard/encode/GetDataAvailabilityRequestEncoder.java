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

import com.google.common.collect.Sets;
import java.util.Set;
import net.opengis.sosgda.x10.GetDataAvailabilityDocument;
import net.opengis.sosgda.x10.GetDataAvailabilityType;
import net.opengis.swes.x20.ExtensibleRequestType;
import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityRequest;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

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
        if (request.getNamespace().equals(GetDataAvailabilityConstants.NS_GDA_20)) {
            return createGDAv20(request);
        } else {
            return createGDAv10(request);
        }
    }

    private XmlObject createGDAv10(GetDataAvailabilityRequest request) {
        GetDataAvailabilityDocument document = GetDataAvailabilityDocument.Factory.newInstance();
        GetDataAvailabilityType gdat = document.addNewGetDataAvailability();
        addService(gdat, request);
        addVersion(gdat, request);
        addOffering(gdat, request);
        addProcedures(gdat, request);
        addFeatureOfInterests(gdat, request);
        addObservedProperties(gdat, request);
        return document;
    }

    private XmlObject createGDAv20(GetDataAvailabilityRequest request) {
        net.opengis.sosgda.x20.GetDataAvailabilityDocument document = net.opengis.sosgda.x20.GetDataAvailabilityDocument.Factory.newInstance();
        net.opengis.sosgda.x20.GetDataAvailabilityType gdat = document.addNewGetDataAvailability();
        addService(gdat, request);
        addVersion(gdat, request);
        addOfferingV20(gdat, request);
        addProceduresV20(gdat, request);
        addFeatureOfInterestsV20(gdat, request);
        addObservedPropertiesV20(gdat, request);
        return document;
    }

    private void addVersion(ExtensibleRequestType gdat, GetDataAvailabilityRequest request) {
        gdat.setVersion(request.getVersion());
    }

    private void addService(ExtensibleRequestType gdat, GetDataAvailabilityRequest request) {
        if (request.isSetService()) {
            gdat.setService(request.getService());
        } else {
            gdat.setService(SosConstants.SOS);
        }
    }

    private void addObservedProperties(GetDataAvailabilityType gdat, GetDataAvailabilityRequest request) {
        request.getObservedProperties().forEach((obsProp) -> {
            gdat.addObservedProperty(obsProp);
        });
    }

    private void addFeatureOfInterests(GetDataAvailabilityType gdat, GetDataAvailabilityRequest request) {
        request.getFeaturesOfInterest().forEach((foi) -> {
            gdat.addFeatureOfInterest(foi);
        });
    }

    private void addProcedures(GetDataAvailabilityType gdat, GetDataAvailabilityRequest request) {
        request.getProcedures().forEach((proc) -> {
            gdat.addProcedure(proc);
        });
    }

    private void addOffering(GetDataAvailabilityType gdat, GetDataAvailabilityRequest request) {
        request.getOfferings().forEach((off) -> {
            gdat.addOffering(off);
        });
    }

    private void addOfferingV20(net.opengis.sosgda.x20.GetDataAvailabilityType gdat, GetDataAvailabilityRequest request) {
        request.getOfferings().forEach((off) -> {
            gdat.addOffering(off);
        });
    }

    private void addProceduresV20(net.opengis.sosgda.x20.GetDataAvailabilityType gdat, GetDataAvailabilityRequest request) {
        request.getProcedures().forEach((proc) -> {
            gdat.addProcedure(proc);
        });
    }

    private void addFeatureOfInterestsV20(net.opengis.sosgda.x20.GetDataAvailabilityType gdat, GetDataAvailabilityRequest request) {
        request.getFeaturesOfInterest().forEach((foi) -> {
            gdat.addFeatureOfInterest(foi);
        });
    }

    private void addObservedPropertiesV20(net.opengis.sosgda.x20.GetDataAvailabilityType gdat, GetDataAvailabilityRequest request) {
        request.getObservedProperties().forEach((obsProp) -> {
            gdat.addObservedProperty(obsProp);
        });
    }
}
