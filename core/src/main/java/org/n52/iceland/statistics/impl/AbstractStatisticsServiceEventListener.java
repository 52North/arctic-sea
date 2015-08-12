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

import org.n52.iceland.event.ServiceEvent;
import org.n52.iceland.event.ServiceEventListener;
import org.n52.iceland.event.events.AbstractFlowEvent;
import org.n52.iceland.event.events.CountingOutputstreamEvent;
import org.n52.iceland.event.events.ExceptionEvent;
import org.n52.iceland.event.events.OutgoingResponseEvent;
import org.n52.iceland.event.events.RequestEvent;
import org.n52.iceland.statistics.api.interfaces.StatisticsServiceEventResolver;
import org.n52.iceland.statistics.api.interfaces.datahandler.IStatisticsDataHandler;
import org.n52.iceland.statistics.impl.resolvers.CountingOutputstreamEventResolver;
import org.n52.iceland.statistics.impl.resolvers.DefaultServiceEventResolver;
import org.n52.iceland.statistics.impl.resolvers.ExceptionEventResolver;
import org.n52.iceland.statistics.impl.resolvers.OutgoingResponseEventResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public abstract class AbstractStatisticsServiceEventListener implements ServiceEventListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final int DEFAULT_THREAD_POOL_SIZE = 2;
    private static final int EVENTS_ARR_SIZE = 4;
    private final ExecutorService executorService;
    @SuppressWarnings("unchecked")
    private final Set<Class<? extends ServiceEvent>> eventTypes =
            Sets.newHashSet(ExceptionEvent.class, OutgoingResponseEvent.class, CountingOutputstreamEvent.class);
    private ConcurrentMap<Long, List<AbstractFlowEvent>> eventsCache = new ConcurrentHashMap<>();

    @Inject
    protected IStatisticsDataHandler dataHandler;

    @Inject
    private StatisticsResolverFactory resolverFactory;

    public AbstractStatisticsServiceEventListener() {
        executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
    }

    public AbstractStatisticsServiceEventListener(int threadPoolSize) {
        executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Override
    public Set<Class<? extends ServiceEvent>> getTypes() {
        return eventTypes;
    }

    @Override
    public void handle(ServiceEvent serviceEvent) {
        logger.debug("Event received: {}", serviceEvent);
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

                eventsCache.get(evt.getMessageGroupId()).add(evt);

                // received last event process eventsResolvers on a new thread
                if (serviceEvent instanceof OutgoingResponseEvent) {
                    BatchResolver resolvers = new BatchResolver(dataHandler);
                    eventsCache.get(evt.getMessageGroupId()).stream().forEach(l -> addEventToResolver(resolvers, l));
                    executorService.execute(resolvers);
                }

            } else {
                logger.trace("Unssupported type of event: {}", serviceEvent.getClass());

                BatchResolver singleOp = new BatchResolver(dataHandler);
                addEventToResolver(singleOp, serviceEvent);
                executorService.execute(singleOp);
            }

        } catch (Throwable e) {
            logger.error("Can't handle event for statistics logging: {}", serviceEvent, e);
        } finally {

        }
    }

    private void addEventToResolver(BatchResolver resolver, ServiceEvent event) {
        StatisticsServiceEventResolver<?> evtResolver = null;

        if (event instanceof ExceptionEvent) {
            ExceptionEventResolver sosExceptionEventResolver = resolverFactory.getExceptionEventResolver();
            sosExceptionEventResolver.setEvent((ExceptionEvent) event);
            evtResolver = sosExceptionEventResolver;
        } else if (event instanceof OutgoingResponseEvent) {
            OutgoingResponseEventResolver outgoingResponseEventResolver = resolverFactory.getOutgoingResponseEventResolver();
            outgoingResponseEventResolver.setEvent((OutgoingResponseEvent) event);
            evtResolver = outgoingResponseEventResolver;
        } else if (event instanceof CountingOutputstreamEvent) {
            CountingOutputstreamEventResolver countingOutputstreamEventResolver = resolverFactory.getCountingOutputstreamEventResolver();
            countingOutputstreamEventResolver.setEvent((CountingOutputstreamEvent) event);
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
     * Call this method in the constructor to register additional event types to
     * your listener
     * 
     * @param types
     *            additional ServiceEvent to listener for
     */
    protected void registerEventType(Set<Class<? extends ServiceEvent>> types) {
        eventTypes.addAll(types);
    }

    @Override
    protected void finalize() throws Throwable {
        this.executorService.shutdown();
    }

    // ---------- ABSTRACT METHODS ------------ //

    /**
     * Returns the application specific resolver
     * {@link StatisticsServiceEventResolver} based on the {@link ServiceEvent}
     * 
     * @param serviceEvent
     * @return
     */
    protected abstract StatisticsServiceEventResolver<?> findResolver(ServiceEvent serviceEvent);

    /**
     * Custom class for persisting the resolved {@link ServiceEvent}s
     */
    private static class BatchResolver implements Runnable {
        private static final Logger logger = LoggerFactory.getLogger(BatchResolver.class);
        private final List<StatisticsServiceEventResolver<?>> eventsResolvers;
        private final IStatisticsDataHandler dataHandler;

        public BatchResolver(IStatisticsDataHandler dataHandler) {
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
