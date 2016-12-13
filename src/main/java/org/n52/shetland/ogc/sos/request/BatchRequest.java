/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos.request;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.BatchConstants;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 *
 * @since 4.0.0
 */
public class BatchRequest extends OwsServiceRequest implements Iterable<OwsServiceRequest> {
    private final List<OwsServiceRequest> requests;

    private boolean stopAtFailure = false;

    public BatchRequest(List<OwsServiceRequest> requests) {
        super(null, null, BatchConstants.OPERATION_NAME);
        this.requests = Objects.requireNonNull(requests);
    }

    public BatchRequest() {
        this(new LinkedList<OwsServiceRequest>());
    }

    public List<OwsServiceRequest> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    public void add(OwsServiceRequest request) {
        this.requests.add(Objects.requireNonNull(request));
    }

    public boolean isEmpty() {
        return getRequests().isEmpty();
    }

    @Override
    public Iterator<OwsServiceRequest> iterator() {
        return getRequests().iterator();
    }

    public boolean isStopAtFailure() {
        return stopAtFailure;
    }

    public void setStopAtFailure(boolean stopAtFailure) {
        this.stopAtFailure = stopAtFailure;
    }

}
