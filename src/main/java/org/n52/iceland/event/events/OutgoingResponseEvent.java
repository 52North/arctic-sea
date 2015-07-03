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
package org.n52.iceland.event.events;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.n52.iceland.event.ServiceEvent;

import com.google.common.base.MoreObjects;

/**
 * Event is thrown if a {@link HttpServletResponse} was sent back.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class OutgoingResponseEvent extends AbstractFlowEvent implements ServiceEvent {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final long requestNumber;
    private final long elapsedTime;

    public OutgoingResponseEvent(HttpServletRequest request, HttpServletResponse response, long requestNumber, long elapsedTime) {
        super(Thread.currentThread().getId());
        this.request = request;
        this.response = response;
        this.requestNumber = requestNumber;
        this.elapsedTime = elapsedTime;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public long getRequestNumber() {
        return requestNumber;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("request", this.request).add("response", this.response).add("requestNumber", this.requestNumber)
                .add("elapsedTime", this.elapsedTime).toString();
    }

}
