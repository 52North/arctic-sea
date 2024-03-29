/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.sos.response;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.sos.BatchConstants;
import org.n52.shetland.ogc.sos.response.BatchResponse.ExceptionOrResponse;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 *
 * @since 1.0.0
 */
public class BatchResponse extends OwsServiceResponse implements Iterable<ExceptionOrResponse> {
    private final List<ExceptionOrResponse> responses = new LinkedList<ExceptionOrResponse>();

    public BatchResponse() {
        this(new LinkedList<>());
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public BatchResponse(List<ExceptionOrResponse> responses) {
        this(null, null, responses);
    }

    public BatchResponse(String service, String version, List<ExceptionOrResponse> responses) {
        this(service, version, BatchConstants.OPERATION_NAME, responses);
    }

    public BatchResponse(String service, String version, String operationName, List<ExceptionOrResponse> responses) {
        super(service, version, operationName);
        if (responses != null) {
            this.responses.addAll(responses);
        }
    }

    public List<ExceptionOrResponse> getResponses() {
        return Collections.unmodifiableList(responses);
    }

    public void add(OwsExceptionReport e) {
        this.responses.add(new ExceptionOrResponse(e));
    }

    public void add(OwsServiceResponse r) {
        this.responses.add(new ExceptionOrResponse(r));
    }

    public void add(ExceptionOrResponse eor) {
        this.responses.add(Objects.requireNonNull(eor));
    }

    public boolean isEmpty() {
        return getResponses().isEmpty();
    }

    @Override
    public Iterator<ExceptionOrResponse> iterator() {
        return getResponses().iterator();
    }

    public static class ExceptionOrResponse {
        private final OwsExceptionReport exception;

        private final OwsServiceResponse response;

        private ExceptionOrResponse(OwsExceptionReport exception, OwsServiceResponse response) {
            this.exception = exception;
            this.response = response;
        }

        public ExceptionOrResponse(OwsServiceResponse response) {
            this(null, Objects.requireNonNull(response));
        }

        public ExceptionOrResponse(OwsExceptionReport exception) {
            this(Objects.requireNonNull(exception), null);
        }

        public boolean isException() {
            return exception != null;
        }

        @SuppressFBWarnings({ "EI_EXPOSE_REP" })
        public OwsExceptionReport getException() {
            return exception;
        }

        @SuppressFBWarnings({ "EI_EXPOSE_REP" })
        public OwsServiceResponse getResponse() {
            return response;
        }
    }
}
