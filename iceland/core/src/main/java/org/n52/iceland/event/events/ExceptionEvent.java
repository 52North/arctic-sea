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
package org.n52.iceland.event.events;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Event is fired if an occured {@link Exception} is written to the {@link OutputStream} of the
 * {@link HttpServletResponse}
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class ExceptionEvent extends AbstractFlowEvent {
    private final Exception exception;

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public ExceptionEvent(final Exception exception) {
        super(Thread.currentThread().getId());
        this.exception = exception;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Exception getException() {
        return exception;
    }

    @Override
    public String toString() {
        return String.format("ExceptionEvent[exception=%s]", getException() != null ? getException().getClass()
                             .getSimpleName() : getClass());
    }
}
