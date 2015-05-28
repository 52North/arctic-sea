/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.convert;

import java.util.Set;

import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.response.AbstractServiceResponse;

/**
 * Interface for {@link AbstractServiceRequest} and
 * {@link AbstractServiceResponse} modifier
 * 
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 * @param <T>
 *            The {@link AbstractServiceRequest} to modify
 * @param <S>
 *            The {@link AbstractServiceResponse} to modify
 */
public interface RequestResponseModifier<T extends AbstractServiceRequest<?>, S extends AbstractServiceResponse> {

    Set<RequestResponseModifierKeyType> getRequestResponseModifierKeyTypes();

    T modifyRequest(T request) throws OwsExceptionReport;

    S modifyResponse(T request, S response) throws OwsExceptionReport;

    RequestResponseModifierFacilitator getFacilitator();

}
