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
import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityRequest;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:j.schulte@52north.org">Jan Schulte</a>
 *
 * @since 4.0.0
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
        GetDataAvailabilityDocument document = GetDataAvailabilityDocument.Factory.newInstance();
        GetDataAvailabilityType gdat = document.addNewGetDataAvailability();
        addService(gdat, request);
        addVersion(gdat, request);
        addProcedures(gdat, request);
        addFeatureOfInterests(gdat, request);
        addObservedProperties(gdat, request);
        return document;
    }

    private void addObservedProperties(GetDataAvailabilityType availabilityType, GetDataAvailabilityRequest request) {
        request.getObservedProperties().forEach((obsProp) -> {
            availabilityType.addObservedProperty(obsProp);
        });
    }

    private void addFeatureOfInterests(GetDataAvailabilityType availabilityType, GetDataAvailabilityRequest request) {
        request.getFeaturesOfInterest().forEach((foi) -> {
            availabilityType.addFeatureOfInterest(foi);
        });
    }

    private void addProcedures(GetDataAvailabilityType availabilityType, GetDataAvailabilityRequest request) {
        request.getProcedures().forEach((procedure) -> {
            availabilityType.addProcedure(procedure);
        });
    }

    private void addVersion(GetDataAvailabilityType gdat, GetDataAvailabilityRequest request) {
        gdat.setVersion(request.getVersion());
    }

    private void addService(GetDataAvailabilityType gdat, GetDataAvailabilityRequest request) {
        if (request.isSetService()) {
            gdat.setService(request.getService());
        } else {
            gdat.setService(SosConstants.SOS);
        }
    }

}
