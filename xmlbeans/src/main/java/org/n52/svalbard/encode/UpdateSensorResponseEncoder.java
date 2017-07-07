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
import org.n52.shetland.ogc.sos.response.UpdateSensorResponse;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import net.opengis.swes.x20.UpdateSensorDescriptionResponseDocument;
import net.opengis.swes.x20.UpdateSensorDescriptionResponseType;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class UpdateSensorResponseEncoder extends AbstractSwesResponseEncoder<UpdateSensorResponse> {
    public UpdateSensorResponseEncoder() {
        super(Sos2Constants.Operations.UpdateSensorDescription.name(), UpdateSensorResponse.class);
    }

    @Override
    protected XmlObject create(UpdateSensorResponse response) throws EncodingException {
        final UpdateSensorDescriptionResponseDocument document =
                UpdateSensorDescriptionResponseDocument.Factory.newInstance(getXmlOptions());
        final UpdateSensorDescriptionResponseType usdr = document.addNewUpdateSensorDescriptionResponse();
        usdr.setUpdatedProcedure(response.getUpdatedProcedure());
        return document;
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(SwesConstants.SWES_20_UPDATE_SENSOR_DESCRIPTION_SCHEMA_LOCATION);
    }
}
