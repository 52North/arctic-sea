/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.event.events;

import javax.servlet.http.HttpServletRequest;

import org.n52.janmayen.event.Event;

/**
 * Event is thrown if a new {@link HttpServletRequest} arrives.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class IncomingRequestEvent implements Event {

    private final HttpServletRequest request;

    private final long requestNumber;

    public IncomingRequestEvent(final HttpServletRequest request, long requestNumber) {
        this.request = request;
        this.requestNumber = requestNumber;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public long getRequestNumber() {
        return requestNumber;
    }

    @Override
    public String toString() {
        return String.format("IncomingRequestEvent[request=%s, requestNumber=%d]", getRequest(), getRequestNumber());
    }

}
