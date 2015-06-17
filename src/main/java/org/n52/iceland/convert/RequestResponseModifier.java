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


import org.n52.iceland.component.Component;
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
 */
public interface RequestResponseModifier extends Component<RequestResponseModifierKey> {

    AbstractServiceRequest<?> modifyRequest(AbstractServiceRequest<?> request) throws OwsExceptionReport;

    AbstractServiceResponse modifyResponse(AbstractServiceRequest<?> request, AbstractServiceResponse response) throws OwsExceptionReport;

    RequestResponseModifierFacilitator getFacilitator();

}
