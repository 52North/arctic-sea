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
import org.n52.shetland.ogc.sos.response.InsertObservationResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import net.opengis.sos.x20.InsertObservationResponseDocument;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class InsertObservationResponseEncoder extends AbstractSosResponseEncoder<InsertObservationResponse> {
    public InsertObservationResponseEncoder() {
        super(SosConstants.Operations.InsertObservation.name(), InsertObservationResponse.class);
    }

    @Override
    protected XmlObject create(InsertObservationResponse response) throws EncodingException {
        InsertObservationResponseDocument doc = InsertObservationResponseDocument.Factory.newInstance(getXmlOptions());
        doc.addNewInsertObservationResponse();
        return doc;
    }

    @Override
    public Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(Sos2Constants.SOS_INSERT_OBSERVATION_SCHEMA_LOCATION);
    }
}
