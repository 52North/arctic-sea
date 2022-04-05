/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.iceland.binding;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.n52.iceland.exception.HTTPException;
import org.n52.janmayen.component.Component;
import org.n52.janmayen.http.HTTPStatus;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;

/**
 * Abstract Super class for binding implementations.
 *
 * Context: The {@code Binding.check*()} methods are called during GetCapabilities processing when collecting the
 * operations metadata.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 *
 * @since 1.0.0
 */
public interface Binding extends Component<BindingKey>, EncodingExceptionHandler {

    /**
     * HTTP DELETE request handling method.
     *
     * @param request  HTTP DELETE request
     * @param response HTTP DELETE response
     *
     * @throws HTTPException if the encoding of an exception failed
     * @throws IOException   if an IO error occurs
     */
    default void doDeleteOperation(HttpServletRequest request, HttpServletResponse response)
            throws HTTPException, IOException {
        throw new HTTPException(HTTPStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * HTTP GET request handling method.
     *
     * @param request  HTTP GET request
     * @param response HTTP GET response
     *
     * @throws HTTPException if the encoding of an exception failed
     * @throws IOException   if an IO error occurs
     */
    default void doGetOperation(HttpServletRequest request, HttpServletResponse response)
            throws HTTPException, IOException {
        throw new HTTPException(HTTPStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * HTTP DELETE request handling method.
     *
     * @param request  HTTP DELETE request
     * @param response HTTP DELETE response
     *
     * @throws HTTPException if the encoding of an exception failed
     * @throws IOException   if an IO error occurs
     */
    default void doOptionsOperation(HttpServletRequest request, HttpServletResponse response)
            throws HTTPException, IOException {
        throw new HTTPException(HTTPStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * HTTP POST request handling method.
     *
     * @param request  HTTP POST request
     * @param response HTTP POST response
     *
     * @throws HTTPException if the encoding of an exception failed
     * @throws IOException   if an IO error occurs
     */
    default void doPostOperation(HttpServletRequest request, HttpServletResponse response)
            throws HTTPException, IOException {
        throw new HTTPException(HTTPStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * HTTP PUT request handling method.
     *
     * @param request  HTTP PUT request
     * @param response HTTP PUT response
     *
     * @throws HTTPException if the encoding of an exception failed
     * @throws IOException   if an IO error occurs
     */
    default void doPutOperation(HttpServletRequest request, HttpServletResponse response)
            throws HTTPException, IOException {
        throw new HTTPException(HTTPStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Check, if the operation is supported by the decoder by the HTTP-Delete method.
     *
     * @param operation identifier of the decoder
     *
     * @return true, if the decoder {@code decoderKey} supports HTTP-Delete for operation {@code operationName}
     *
     * @throws HTTPException if an error occurs
     */
    default boolean checkOperationHttpDeleteSupported(OwsOperationKey operation) throws HTTPException {
        return false;
    }

    /**
     * Check, if the operation is supported by the decoder by the HTTP-Get method.
     *
     * @param operation identifier of the decoder
     *
     * @return true, if the decoder {@code decoderKey} supports HTTP-Get for operation {@code operationName}
     *
     * @throws HTTPException if an error occurs
     */
    default boolean checkOperationHttpGetSupported(OwsOperationKey operation) throws HTTPException {
        return false;
    }

    /**
     * Check, if the operation is supported by the decoder by the HTTP-Post method.
     *
     * @param operation identifier of the decoder
     *
     * @return true, if the decoder {@code decoderKey} supports HTTP-Post for operation {@code operationName}
     *
     * @throws HTTPException if an error occurs
     */
    default boolean checkOperationHttpPostSupported(OwsOperationKey operation) throws HTTPException {
        return false;
    }

    /**
     * Check, if the operation is supported by the decoder by the HTTP-Options method.
     *
     * @param operation identifier of the decoder
     *
     * @return true, if the decoder {@code decoderKey} supports HTTP-Post for operation {@code operationName}
     *
     * @throws HTTPException if an error occurs
     */
    default boolean checkOperationHttpOptionsSupported(OwsOperationKey operation) throws HTTPException {
        return false;
    }

    /**
     * Check, if the operation is supported by the decoder by the HTTP-Put method.
     *
     * @param operation identifier of the decoder
     *
     * @return true, if the decoder {@code decoderKey} supports HTTP-Put for operation {@code operationName}
     *
     * @throws HTTPException if an error occurs
     */
    default boolean checkOperationHttpPutSupported(OwsOperationKey operation) throws HTTPException {
        return false;
    }

}
