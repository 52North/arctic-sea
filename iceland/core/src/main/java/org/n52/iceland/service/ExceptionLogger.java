/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.service;

import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.event.events.ExceptionEvent;
import org.n52.janmayen.event.Event;
import org.n52.janmayen.event.EventListener;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

/**
 * Single point of exception logging.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class ExceptionLogger implements EventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLogger.class);
    private static final String MESSAGE = "Exception thrown";

    @Override
    public Set<Class<? extends Event>> getTypes() {
        return Collections.<Class<? extends Event>>singleton(ExceptionEvent.class);
    }

    @Override
    public void handle(Event event) {
        final ExceptionEvent ee = (ExceptionEvent) event;

        // TODO review logging of exceptions. Stacktrace only on debug level?
        if (ee.getException() instanceof OwsExceptionReport) {
            final OwsExceptionReport owse = (OwsExceptionReport) ee.getException();
            if (owse.getStatus() == null) {
                log(owse);
            } else if (owse.getStatus().getCode() >= 500) {
                LOGGER.error(MESSAGE, owse);
            } else if (owse.getStatus().getCode() >= 400) {
                LOGGER.warn(MESSAGE, owse);
            } else {
                log(owse);
            }
        } else {
            LOGGER.debug("Error processing request", ee.getException());
        }
    }

    private void log(final OwsExceptionReport owse) {
        LOGGER.debug(MESSAGE, owse);
    }
}
