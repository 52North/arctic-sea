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
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.Extensions;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.ifoi.InsertFeatureOfInterestConstants;
import org.n52.shetland.ogc.sos.ifoi.InsertFeatureOfInterestRequest;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.XmlHelper;

import com.google.common.collect.Sets;

import net.opengis.ifoi.x10.InsertFeatureOfInterestDocument;
import net.opengis.ifoi.x10.InsertFeatureOfInterestType;

/**
 * @since 1.0.0
 */
public class InsertFeatureOfInterestRequestEncoder extends AbstractRequestEncoder<InsertFeatureOfInterestRequest> {
    public static final SchemaLocation SCHEMA_LOCATION = new SchemaLocation(InsertFeatureOfInterestConstants.NS_IFOI,
            InsertFeatureOfInterestConstants.SCHEMA_LOCATION_URL_INSERT_FEATURE_OF_INTEREST);

    public InsertFeatureOfInterestRequestEncoder() {
        super(SosConstants.SOS, Sos2Constants.SERVICEVERSION, InsertFeatureOfInterestConstants.OPERATION_NAME,
                InsertFeatureOfInterestConstants.NS_IFOI, InsertFeatureOfInterestConstants.NS_IFOI_PREFIX,
                InsertFeatureOfInterestRequest.class);
    }

    @Override
    protected XmlObject create(InsertFeatureOfInterestRequest request) throws EncodingException {
        if (request == null) {
            throw new UnsupportedEncoderInputException(this, InsertFeatureOfInterestRequest.class);
        }
        InsertFeatureOfInterestDocument doc = InsertFeatureOfInterestDocument.Factory.newInstance(getXmlOptions());
        InsertFeatureOfInterestType insertFeatureOfInterestType = doc.addNewInsertFeatureOfInterest();
        insertFeatureOfInterestType.setService(request.getService());
        insertFeatureOfInterestType.setVersion(request.getVersion());
        addExtensions(request.getExtensions(), insertFeatureOfInterestType);
        for (AbstractFeature feature : request.getFeatureMembers()) {
            if (feature instanceof FeatureCollection) {
                for (AbstractFeature f : (FeatureCollection) feature) {
                    addFeatureOfInterest(f, insertFeatureOfInterestType);
                }
            } else if (feature instanceof AbstractSamplingFeature) {
                addFeatureOfInterest(feature, insertFeatureOfInterestType);
            }
        }
        XmlHelper.makeGmlIdsUnique(doc.getDomNode());
        return doc;
    }

    private void addFeatureOfInterest(AbstractFeature feature, InsertFeatureOfInterestType insertFeatureOfInterestType)
            throws EncodingException {
        EncodingContext codingContext = EncodingContext.empty();
        XmlObject encodeObjectToXml = encodeGml(codingContext, feature);
        insertFeatureOfInterestType.addNewFeatureMember().set(encodeObjectToXml);
    }

    private void addExtensions(Extensions extensions, InsertFeatureOfInterestType insertFeatureOfInterestType)
            throws EncodingException {
        if (extensions == null || extensions.isEmpty()) {
            return;
        }
        for (Extension<?> o : extensions.getExtensions()) {
            insertFeatureOfInterestType.addNewExtension().set(encodeObjectToXml(SwesConstants.NS_SWES_20, o));
        }
    }

    private XmlObject encodeGml(EncodingContext context, Object o) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o, context);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(SCHEMA_LOCATION);
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet();
    }
}
