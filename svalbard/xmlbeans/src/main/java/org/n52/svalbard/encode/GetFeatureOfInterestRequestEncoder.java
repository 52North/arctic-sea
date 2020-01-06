/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.GetFeatureOfInterestRequest;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import net.opengis.fes.x20.SpatialOpsDocument;
import net.opengis.sos.x20.GetFeatureOfInterestDocument;
import net.opengis.sos.x20.GetFeatureOfInterestType;

/**
 * XML {@link Decoder} for {@link GetFeatureOfInterestRequest}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 5.0.0
 *
 */
public class GetFeatureOfInterestRequestEncoder extends AbstractSosRequestEncoder<GetFeatureOfInterestRequest>
        implements ExtensibleRequestEncoder {

    public GetFeatureOfInterestRequestEncoder() {
        super(SosConstants.Operations.GetFeatureOfInterest.name(), GetFeatureOfInterestRequest.class);
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(Sos2Constants.SOS_GET_OBSERVATION_SCHEMA_LOCATION);
    }

    @Override
    protected XmlObject create(GetFeatureOfInterestRequest request) throws EncodingException {
        GetFeatureOfInterestDocument doc = GetFeatureOfInterestDocument.Factory.newInstance(getXmlOptions());
        GetFeatureOfInterestType gfoit = doc.addNewGetFeatureOfInterest();
        addService(gfoit, request);
        addVersion(gfoit, request);
        addExtension(gfoit, request);
        addProcedure(gfoit, request);
        addObservedProperty(gfoit, request);
        addFeatureOfInterest(gfoit, request);
        addSpatialFilters(gfoit, request);
        return doc;
    }

    private void addProcedure(GetFeatureOfInterestType gfoit, GetFeatureOfInterestRequest request) {
        if (request.isSetProcedures()) {
            request.getProcedures().forEach(gfoit::addProcedure);
        }
    }

    private void addObservedProperty(GetFeatureOfInterestType gfoit, GetFeatureOfInterestRequest request) {
        if (request.isSetObservableProperties()) {
            request.getObservedProperties().forEach(gfoit::addObservedProperty);
        }
    }

    private void addFeatureOfInterest(GetFeatureOfInterestType gfoit, GetFeatureOfInterestRequest request) {
        if (request.isSetFeatureOfInterestIdentifiers()) {
            request.getFeatureIdentifiers().forEach(gfoit::addFeatureOfInterest);
        }
    }

    private void addSpatialFilters(GetFeatureOfInterestType got, GetFeatureOfInterestRequest request)
            throws EncodingException {
        if (request.isSetSpatialFilters()) {
            for (SpatialFilter spatialFilter : request.getSpatialFilters()) {
                // TODO fixme
                XmlObject encodeFes = encodeFes(spatialFilter);
                if (encodeFes instanceof SpatialOpsDocument) {
                    substitute(got.addNewSpatialFilter().getSpatialOps(),
                            ((SpatialOpsDocument) encodeFes).getSpatialOps());
                }
            }
        }
    }

}
