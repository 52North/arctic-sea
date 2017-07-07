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

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * @author <a href="mailto:j.schulte@52north.org">Jan Schulte</a>
 * @param <T> the request type
 */
public abstract class AbstractSosV1RequestEncoder<T extends OwsServiceRequest> extends AbstractRequestEncoder<T> {

    public AbstractSosV1RequestEncoder(String operation, Class<T> responseType) {
        super(SosConstants.SOS,
              Sos1Constants.SERVICEVERSION,
              operation,
              Sos1Constants.NS_SOS,
              SosConstants.NS_SOS_PREFIX,
              responseType);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Collections.singleton(Sos1Constants.SOS1_SCHEMA_LOCATION);
    }

    protected XmlObject encodeOws(Object o) throws EncodingException {
        return encodeObjectToXml(OWSConstants.NS_OWS, o);
    }

}
