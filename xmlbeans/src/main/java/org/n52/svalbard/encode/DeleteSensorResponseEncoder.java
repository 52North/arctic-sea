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

import net.opengis.swes.x20.DeleteSensorResponseDocument;
import net.opengis.swes.x20.DeleteSensorResponseType;

import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.response.DeleteSensorResponse;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class DeleteSensorResponseEncoder extends AbstractSwesResponseEncoder<DeleteSensorResponse> {
    public DeleteSensorResponseEncoder() {
        super(Sos2Constants.Operations.DeleteSensor.name(), DeleteSensorResponse.class);
    }

    @Override
    protected XmlObject create(DeleteSensorResponse response) throws EncodingException {
        DeleteSensorResponseDocument document =
                DeleteSensorResponseDocument.Factory.newInstance(getXmlOptions());
        DeleteSensorResponseType dsr = document.addNewDeleteSensorResponse();
        dsr.setDeletedProcedure(response.getDeletedProcedure());
        return document;
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(SwesConstants.SWES_20_DELETE_SENSOR_SCHEMA_LOCATION);
    }
}
