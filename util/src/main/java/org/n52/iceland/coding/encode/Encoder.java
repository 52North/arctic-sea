/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.coding.encode;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.n52.iceland.coding.SupportedType;

import org.n52.iceland.component.Component;
import org.n52.iceland.exception.CodingException;
import org.n52.iceland.exception.UnsupportedEncoderInputException;
import org.n52.iceland.util.http.MediaType;

/**
 * Generic interface for Encoders.
 *
 * @param <T>
 *            the resulting type, the "Target"
 * @param <S>
 *            the input type, the "Source"
 *
 * @since 1.0.0
 */
public interface Encoder<T, S> extends Component<EncoderKey> {

    /**
     * Encodes the specified object.
     *
     * @param objectToEncode
     *            the object to encode
     *
     * @return the encoded object
     * @throws org.n52.iceland.exception.CodingException
     *             if an error occurs
     * @throws UnsupportedEncoderInputException
     *             if the supplied object (or any of it's contents) is not
     *             supported by this encoder
     */
    T encode(S objectToEncode) throws CodingException, UnsupportedEncoderInputException;

    /**
     * Encodes the specified object with the specified {@linkplain HelperValues}
     * .
     *
     * @param objectToEncode
     *            the object to encode
     * @param additionalValues
     *            the helper values
     *
     * @return the encoded object
     * @throws org.n52.iceland.exception.CodingException
     *             if an error occurs
     * @throws UnsupportedEncoderInputException
     *             if the supplied object (or any of it's contents) is not
     *             supported by this encoder
     */
    T encode(S objectToEncode, Map<HelperValues, String> additionalValues) throws CodingException, UnsupportedEncoderInputException;

    /**
     * Get the {@link SupportedType}
     *
     * @return the supported key types
     */
    default Set<SupportedType> getSupportedTypes() {
        return Collections.emptySet();
    }

    /**
     * Add the namespace prefix of this {@linkplain Encoder} instance to the
     * given {@linkplain Map}.
     *
     * @param nameSpacePrefixMap
     */
    default void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        // do nothing
    }

    /**
     * @return the content type of the encoded response.
     */
    MediaType getContentType();
    
    
    // TODO add javadoc for each value
    enum HelperValues {
        @Deprecated GMLID, @Deprecated EXIST_FOI_IN_DOC, VERSION, TYPE,
        /**
         * Encode the given 'object to encode' in a <tt>*Document</tt> object
         * and not <tt>*Type</tt>.
         */
        DOCUMENT, PROPERTY_TYPE, @Deprecated FOR_OBSERVATION, ENCODE, ENCODE_NAMESPACE, REFERENCED,
        /**
         * Encode the given <tt>OwsExceptionReport</tt> not into an
         * <tt>ows:ExceptionReport</tt> but one <tt>ows:Exception</tt>.
         */
        ENCODE_OWS_EXCEPTION_ONLY
    }
}
