/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.statistics.api.utils;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.n52.iceland.event.events.CountingOutputStreamEvent;
import org.n52.iceland.exception.ows.OperationNotSupportedException;
import org.n52.iceland.request.GetCapabilitiesRequest;
import org.n52.iceland.statistics.api.interfaces.StatisticsServiceEventHandler;
import org.n52.iceland.statistics.impl.handlers.CountingOutputStreamEventHandler;
import org.n52.iceland.statistics.impl.handlers.DefaultServiceEventHandler;
import org.n52.iceland.statistics.impl.handlers.exceptions.CodedExceptionEventHandler;

public class EventHandlerFinderTest {

    @Test
    public void findDirectDefaultServiceEvent() {
        Map<String, StatisticsServiceEventHandler<?>> handlers = new HashMap<>();
        CountingOutputStreamEventHandler handler = new CountingOutputStreamEventHandler();

        CountingOutputStreamEvent request = new CountingOutputStreamEvent();
        handlers.put(request.getClass().getSimpleName(), handler);

        Assert.assertNotNull(EventHandlerFinder.findHandler(request, handlers));
    }

    @Test(
            expected = NullPointerException.class)
    public void findNoHandlers() {
        Map<String, StatisticsServiceEventHandler<?>> handlers = new HashMap<>();
        DefaultServiceEventHandler handler = new DefaultServiceEventHandler();

        handlers.put("morpheus", handler);
        GetCapabilitiesRequest request = new GetCapabilitiesRequest("SOS");

        EventHandlerFinder.findHandler(request, handlers);
    }

    @Test
    public void findSubclassAsHandler() {
        Map<String, StatisticsServiceEventHandler<?>> handlers = new HashMap<>();
        CodedExceptionEventHandler handler = new CodedExceptionEventHandler();

        OperationNotSupportedException exception = new OperationNotSupportedException("GetCapabilities");

        handlers.put("CodedException", handler);

        Assert.assertNotNull(EventHandlerFinder.findHandler(exception, handlers));
    }
}
