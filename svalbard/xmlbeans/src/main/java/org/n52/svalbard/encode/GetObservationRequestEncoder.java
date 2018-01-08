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

import java.util.Set;

import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.GetObservationRequest;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import net.opengis.fes.x20.SpatialOpsDocument;
import net.opengis.fes.x20.TemporalOpsDocument;
import net.opengis.sos.x20.GetObservationDocument;
import net.opengis.sos.x20.GetObservationType;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 5.0.0
 *
 */
public class GetObservationRequestEncoder extends AbstractSosRequestEncoder<GetObservationRequest>
        implements ExtensibleRequestEncoder {

    public GetObservationRequestEncoder() {
        super(SosConstants.Operations.GetObservation.name(), GetObservationRequest.class);
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(Sos2Constants.SOS_GET_OBSERVATION_SCHEMA_LOCATION);
    }

    @Override
    protected XmlObject create(GetObservationRequest request) throws EncodingException {
        GetObservationDocument doc = GetObservationDocument.Factory.newInstance(getXmlOptions());
        GetObservationType got = doc.addNewGetObservation();
        addService(got, request);
        addVersion(got, request);
        addExtension(got, request);
        addProcedure(got, request);
        addOffering(got, request);
        addObservedProperty(got, request);
        addTemporalFilter(got, request);
        addFeatureOfInterest(got, request);
        addSpatialFilter(got, request);
        addResponseFormat(got, request);
        return doc;
    }

    private void addProcedure(GetObservationType got, GetObservationRequest request) {
        if (request.isSetProcedure()) {
            request.getProcedures().forEach(got::addProcedure);
        }
    }

    private void addOffering(GetObservationType got, GetObservationRequest request) {
        if (request.isSetOffering()) {
            request.getOfferings().forEach(got::addOffering);
        }

    }

    private void addObservedProperty(GetObservationType got, GetObservationRequest request) {
        if (request.isSetObservableProperty()) {
            request.getObservedProperties().forEach(got::addObservedProperty);
        }
    }

    private void addTemporalFilter(GetObservationType got, GetObservationRequest request) throws EncodingException {
        if (request.isSetTemporalFilter()) {
            for (TemporalFilter temporalFilter : request.getTemporalFilters()) {
                // TODO fixme
                XmlObject encodeFes = encodeFes(temporalFilter);
                if (encodeFes instanceof TemporalOpsDocument) {
                    substitute(got.addNewTemporalFilter().addNewTemporalOps(),
                            ((TemporalOpsDocument) encodeFes).getTemporalOps());
                }
            }
        }
    }

    private void addFeatureOfInterest(GetObservationType got, GetObservationRequest request) {
        if (request.isSetFeatureOfInterest()) {
            request.getFeatureIdentifiers().forEach(got::addFeatureOfInterest);
        }
    }

    private void addSpatialFilter(GetObservationType got, GetObservationRequest request) throws EncodingException {
        if (request.isSetSpatialFilter()) {
            // TODO fixme
            XmlObject encodeFes = encodeFes(request.getSpatialFilter());
            if (encodeFes instanceof SpatialOpsDocument) {
                substitute(got.addNewSpatialFilter().getSpatialOps(),
                        ((SpatialOpsDocument) encodeFes).getSpatialOps());
            }
        }
    }

    private void addResponseFormat(GetObservationType got, GetObservationRequest request) {
        if (request.isSetResponseFormat()) {
            got.setResponseFormat(request.getResponseFormat());
        }
    }

}
