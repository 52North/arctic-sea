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
package org.n52.svalbard;

import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.XmlBeansEncodingFlags;

/**
 * @author Matthes Rieke
 * @deprecated use {@link EncodingContext} with any enum (e.g. {@link XmlBeansEncodingFlags})
 */
@Deprecated
public enum SosHelperValues implements HelperValues {
    @Deprecated
    GMLID,
    @Deprecated
    EXIST_FOI_IN_DOC,
    VERSION,
    /**
     * Encode the given 'object to encode' in a <tt>*Document</tt> object and not <tt>*Type</tt>.
     *
     * @deprecated use {@link XmlBeansEncodingFlags#PROPERTY_TYPE}.
     */
    @Deprecated
    FOR_OBSERVATION,
    ENCODE,
    REFERENCED,
    /**
     * Encode the given <tt>OwsExceptionReport</tt> not into an
     * <tt>ows:ExceptionReport</tt> but one <tt>ows:Exception</tt>.
     */
    ENCODE_OWS_EXCEPTION_ONLY;
}
