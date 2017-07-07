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

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.GetFeatureOfInterestResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.XmlHelper;

import com.google.common.collect.Sets;

import net.opengis.sos.x20.GetFeatureOfInterestResponseDocument;
import net.opengis.sos.x20.GetFeatureOfInterestResponseType;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GetFeatureOfInterestResponseEncoder extends AbstractSosResponseEncoder<GetFeatureOfInterestResponse> {
    public GetFeatureOfInterestResponseEncoder() {
        super(SosConstants.Operations.GetFeatureOfInterest.name(), GetFeatureOfInterestResponse.class);
    }

    @Override
    protected XmlObject create(GetFeatureOfInterestResponse response) throws EncodingException {
        GetFeatureOfInterestResponseDocument document =
                GetFeatureOfInterestResponseDocument.Factory.newInstance(getXmlOptions());
        GetFeatureOfInterestResponseType xbGetFoiResponse = document.addNewGetFeatureOfInterestResponse();
        AbstractFeature feature = response.getAbstractFeature();
        if (feature instanceof FeatureCollection) {
            for (AbstractFeature f : (FeatureCollection) feature) {
                addFeatureOfInterest(f, xbGetFoiResponse);
            }
        } else if (feature instanceof SamplingFeature) {
            addFeatureOfInterest(feature, xbGetFoiResponse);
        }
        XmlHelper.makeGmlIdsUnique(document.getDomNode());
        return document;
    }

    private void addFeatureOfInterest(AbstractFeature feature, GetFeatureOfInterestResponseType response)
            throws EncodingException {
        EncodingContext codingContext = EncodingContext.empty();
        XmlObject encodeObjectToXml = encodeGml(codingContext, feature);
        response.addNewFeatureMember().set(encodeObjectToXml);
    }

    @Override
    public Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(Sos2Constants.SOS_GET_FEATURE_OF_INTEREST_SCHEMA_LOCATION);
    }
}
