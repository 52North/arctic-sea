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
package org.n52.iceland.statistics.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.event.events.AbstractFlowEvent;
import org.n52.iceland.event.events.CountingOutputStreamEvent;
import org.n52.iceland.event.events.ExceptionEvent;
import org.n52.iceland.event.events.OutgoingResponseEvent;
import org.n52.iceland.event.events.RequestEvent;
import org.n52.iceland.statistics.api.interfaces.StatisticsServiceEventResolver;
import org.n52.iceland.statistics.api.interfaces.datahandler.IStatisticsDataHandler;
import org.n52.iceland.statistics.impl.resolvers.CountingOutputStreamEventResolver;
import org.n52.iceland.statistics.impl.resolvers.DefaultServiceEventResolver;
import org.n52.iceland.statistics.impl.resolvers.ExceptionEventResolver;
import org.n52.iceland.statistics.impl.resolvers.OutgoingResponseEventResolver;
import org.n52.janmayen.event.Event;
import org.n52.janmayen.event.EventListener;

import com.google.common.collect.Sets;

public abstract class AbstractStatisticsServiceEventListener implements EventListener {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractStatisticsServiceEventListener.class);
    private static final int DEFAULT_THREAD_POOL_SIZE = 2;
    private static final int EVENTS_ARR_SIZE = 4;
    private final ExecutorService executorService;
    @SuppressWarnings("unchecked")
    private final Set<Class<? extends Event>> eventTypes = Sets
            .newHashSet(ExceptionEvent.class, OutgoingResponseEvent.class, CountingOutputStreamEvent.class);
    private final ConcurrentMap<Long, List<AbstractFlowEvent>> eventsCache = new ConcurrentHashMap<>();

    @Inject
    private IStatisticsDataHandler dataHandler;

    @Inject
    private StatisticsResolverFactory resolverFactory;

    public AbstractStatisticsServiceEventListener() {
        executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
    }

    public AbstractStatisticsServiceEventListener(int threadPoolSize) {
        executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Override
    public Set<Class<? extends Event>> getTypes() {
        return eventTypes;
    }

    @Override
    public void handle(Event serviceEvent) {
        LOG.debug("Event received: {}", serviceEvent);
        if (!dataHandler.isLoggingEnabled()) {
            return;
        }

        try {
            if (serviceEvent instanceof AbstractFlowEvent) {
                List<AbstractFlowEvent> eventList;
                AbstractFlowEvent evt = (AbstractFlowEvent) serviceEvent;

                // empty the list on the first event of the given group id
                if (serviceEvent instanceof RequestEvent) {
                    eventList = new ArrayList<>(EVENTS_ARR_SIZE);
                    eventsCache.put(evt.getMessageGroupId(), eventList);
                }

                // fall back
                if (eventsCache.get(evt.getMessageGroupId()) == null) {
                    eventList = new ArrayList<>(EVENTS_ARR_SIZE);
                    eventsCache.put(evt.getMessageGroupId(), eventList);
                }
                eventsCache.get(evt.getMessageGroupId()).add(evt);

                // received last event process eventsResolvers on a new thread
                if (serviceEvent instanceof OutgoingResponseEvent) {
                    BatchResolver resolvers = new BatchResolver(dataHandler);
                    eventsCache.get(evt.getMessageGroupId()).stream().forEach(l -> addEventToResolver(resolvers, l));
                    executorService.execute(resolvers);
                }

            } else {
                LOG.trace("Unssupported type of event: {}", serviceEvent.getClass());

                BatchResolver singleOp = new BatchResolver(dataHandler);
                addEventToResolver(singleOp, serviceEvent);
                executorService.execute(singleOp);
            }

        } catch (Throwable e) {
            LOG.error("Can't handle event for statistics logging: {}", serviceEvent, e);
        }
    }

    private void addEventToResolver(BatchResolver resolver, Event event) {
        StatisticsServiceEventResolver<?> evtResolver = null;

        if (event instanceof ExceptionEvent) {
            ExceptionEventResolver sosExceptionEventResolver = resolverFactory.getExceptionEventResolver();
            sosExceptionEventResolver.setEvent((ExceptionEvent) event);
            evtResolver = sosExceptionEventResolver;
        } else if (event instanceof OutgoingResponseEvent) {
            OutgoingResponseEventResolver outgoingResponseEventResolver = resolverFactory
                    .getOutgoingResponseEventResolver();
            outgoingResponseEventResolver.setEvent((OutgoingResponseEvent) event);
            evtResolver = outgoingResponseEventResolver;
        } else if (event instanceof CountingOutputStreamEvent) {
            CountingOutputStreamEventResolver countingOutputstreamEventResolver = resolverFactory
                    .getCountingOutputstreamEventResolver();
            countingOutputstreamEventResolver.setEvent((CountingOutputStreamEvent) event);
            evtResolver = countingOutputstreamEventResolver;
        } else {
            evtResolver = findResolver(event);
        }

        // Default fallback event resolver
        if (evtResolver == null) {
            DefaultServiceEventResolver defaultServiceEventResolver = resolverFactory.getDefaultServiceEventResolver();
            defaultServiceEventResolver.setEvent(event);
            evtResolver = defaultServiceEventResolver;
        }

        resolver.addEventResolver(evtResolver);

    }

    /**
     * Call this method in the constructor or before listener registration starts to register additional event types to
     * your listener
     *
     * @param types additional ServiceEvent to listener for
     */
    protected void registerEventType(Set<Class<? extends Event>> types) {
        eventTypes.addAll(types);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            this.executorService.shutdown();
        } finally {
            super.finalize();
        }
    }

    // ---------- ABSTRACT METHODS ------------ //
    /**
     * Returns the application specific resolver {@link StatisticsServiceEventResolver} based on the {@link Event}
     *
     * @param serviceEvent the event
     *
     * @return the concrete service event resolver
     */
    protected abstract StatisticsServiceEventResolver<?> findResolver(Event serviceEvent);

    public IStatisticsDataHandler getDataHandler() {
        return dataHandler;
    }

    /**
     * Custom class for persisting the resolved {@link Event}s
     */
    private static class BatchResolver implements Runnable {
        private static final Logger logger = LoggerFactory.getLogger(BatchResolver.class);
        private final List<StatisticsServiceEventResolver<?>> eventsResolvers;
        private final IStatisticsDataHandler dataHandler;

        BatchResolver(IStatisticsDataHandler dataHandler) {
            eventsResolvers = new ArrayList<>(EVENTS_ARR_SIZE);
            this.dataHandler = dataHandler;
        }

        public void addEventResolver(StatisticsServiceEventResolver<?> resolver) {
            eventsResolvers.add(resolver);
        }

        @Override
        public void run() {
            Map<String, Object> data = new HashMap<>();
            try {
                eventsResolvers.stream().forEach(l -> data.putAll(l.resolve()));
                dataHandler.persist(data);
            } catch (Throwable e) {
                logger.error("Cannot persist event", e);
            }
        }
    }

}
