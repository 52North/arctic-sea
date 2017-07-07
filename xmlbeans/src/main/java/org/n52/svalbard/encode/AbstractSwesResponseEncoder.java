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

import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.w3c.SchemaLocation;

import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @param <T>
 *            the response type
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public abstract class AbstractSwesResponseEncoder<T extends OwsServiceResponse> extends AbstractResponseEncoder<T> {
    public AbstractSwesResponseEncoder(String operation, Class<T> responseType) {
        super(SosConstants.SOS, Sos2Constants.SERVICEVERSION, operation, SwesConstants.NS_SWES_20,
                SwesConstants.NS_SWES_PREFIX, responseType);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(SwesConstants.SWES_20_SCHEMA_LOCATION);
    }
}
