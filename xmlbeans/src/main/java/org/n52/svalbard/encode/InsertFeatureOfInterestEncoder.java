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
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.ifoi.InsertFeatureOfInterestConstants;
import org.n52.shetland.ogc.sos.ifoi.InsertFeatureOfInterestResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import com.google.common.collect.Sets;

import net.opengis.ifoi.x10.InsertFeatureOfInterestResponseDocument;

/**
 * @since 1.0.0
 */
public class InsertFeatureOfInterestEncoder extends AbstractResponseEncoder<InsertFeatureOfInterestResponse> {
    public static final SchemaLocation SCHEMA_LOCATION = new SchemaLocation(InsertFeatureOfInterestConstants.NS_IFOI,
            InsertFeatureOfInterestConstants.SCHEMA_LOCATION_URL_INSERT_FEATURE_OF_INTEREST);

    public InsertFeatureOfInterestEncoder() {
        super(SosConstants.SOS, Sos2Constants.SERVICEVERSION, InsertFeatureOfInterestConstants.OPERATION_NAME,
                InsertFeatureOfInterestConstants.NS_IFOI, InsertFeatureOfInterestConstants.NS_IFOI_PREFIX,
                InsertFeatureOfInterestResponse.class);
    }


    @Override
    protected XmlObject create(InsertFeatureOfInterestResponse ifoir) throws EncodingException {
        if (ifoir == null) {
            throw new UnsupportedEncoderInputException(this, InsertFeatureOfInterestResponse.class);
        }
        InsertFeatureOfInterestResponseDocument ifoird =
                InsertFeatureOfInterestResponseDocument.Factory.newInstance(getXmlOptions());
        ifoird.addNewInsertFeatureOfInterestResponse();
        return ifoird;
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
