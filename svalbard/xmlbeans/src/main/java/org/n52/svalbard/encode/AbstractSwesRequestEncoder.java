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

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

/**
 * @author <a href="mailto:j.schulte@52north.org">Jan Schulte</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">J6uuml;rrens, Eike Hinderk</a>
 * @param <T> the request type
 */
public abstract class AbstractSwesRequestEncoder<T extends OwsServiceRequest> extends AbstractRequestEncoder<T> {

    public AbstractSwesRequestEncoder(String operation, Class<T> responseType) {
        super(SosConstants.SOS, Sos2Constants.SERVICEVERSION, operation, SwesConstants.NS_SWES_20,
              SwesConstants.NS_SWES_PREFIX, responseType);
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return CollectionHelper.set(SwesConstants.SWES_20_SCHEMA_LOCATION);
    }

    protected void validateInput(T request) throws UnsupportedEncoderInputException {
        if (request == null) {
            throw new UnsupportedEncoderInputException(this, "null");
        }
        if (!request.isSetService()) {
            throw new UnsupportedEncoderInputException(this, "missing service");
        }
        if (!request.isSetVersion()) {
            throw new UnsupportedEncoderInputException(this, "missing version");
        }
    }

}
