/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.iceland.event.events;

import org.n52.iceland.request.operator.RequestOperator;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;

/**
 * Event should be fired when a new {@link OwsServiceRequest} arrives in
 * the implemented {@link RequestOperator}
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class RequestEvent extends AbstractFlowEvent {

    private final OwsServiceRequest request;

    public RequestEvent(OwsServiceRequest request) {
        super(Thread.currentThread().getId());
        this.request = request;
    }

    public OwsServiceRequest getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return String.format("RequestEvent[request=%s]", getRequest());
    }
}
