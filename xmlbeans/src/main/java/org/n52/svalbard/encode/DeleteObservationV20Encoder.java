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
import org.n52.shetland.ogc.sos.delobs.DeleteObservationConstants;
import org.n52.shetland.ogc.sos.delobs.DeleteObservationResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import com.google.common.collect.Sets;

import net.opengis.sosdo.x10.DeleteObservationResponseDocument;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 * @since 1.0.0
 */
public class DeleteObservationV20Encoder extends AbtractVersionedResponseEncoder<DeleteObservationResponse> {
    public static final SchemaLocation SCHEMA_LOCATION = new SchemaLocation(DeleteObservationConstants.NS_SOSDO_2_0,
            DeleteObservationConstants.NS_SOSDO_2_0_SCHEMA_LOCATION);

    public DeleteObservationV20Encoder() {
        super(SosConstants.SOS, Sos2Constants.SERVICEVERSION, DeleteObservationConstants.Operations.DeleteObservation
                .name(), DeleteObservationConstants.NS_SOSDO_2_0, DeleteObservationConstants.NS_SOSDO_PREFIX,
                DeleteObservationResponse.class, DeleteObservationConstants.NS_SOSDO_2_0);
    }

    @Override
    protected XmlObject create(DeleteObservationResponse dor) throws EncodingException {
        if (dor == null) {
            throw new UnsupportedEncoderInputException(this, DeleteObservationResponse.class);
        }
        DeleteObservationResponseDocument xbDeleteObsDoc =
                DeleteObservationResponseDocument.Factory.newInstance(getXmlOptions());
        xbDeleteObsDoc.addNewDeleteObservationResponse();
        return xbDeleteObsDoc;
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
