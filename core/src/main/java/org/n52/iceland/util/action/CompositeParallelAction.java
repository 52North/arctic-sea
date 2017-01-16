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
package org.n52.iceland.util.action;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.janmayen.GroupedAndNamedThreadFactory;

/**
 * @param <A>
 * @author <a href="mailto:shane@axiomalaska.com">Shane StClair</a>
 * @since 1.0.0
 *
 */
public abstract class CompositeParallelAction<A extends ThreadableAction> extends CompositeAction<A> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompositeParallelAction.class);

    private final ThreadFactory threadFactory;
    private final ExecutorService executor;
    private final String threadGroupName;
    private CountDownLatch countDownLatch;

    @SafeVarargs
    public CompositeParallelAction(int threads, String threadGroupName, A... actions) {
        super(actions);
        this.threadGroupName = threadGroupName;
        this.threadFactory = new GroupedAndNamedThreadFactory(threadGroupName);
        this.executor = Executors.newFixedThreadPool(threads, threadFactory);
    }

    @Override
    public void execute() {
        if (getActions() != null) {
            LOGGER.debug("Executing parallel actions");
            countDownLatch = new CountDownLatch(getActions().size());

            //preprocess and submit actions
            for (A action : getActions()){
                action.setParentCountDownLatch(countDownLatch);
                pre(action);
                executor.submit(action);
            }
            long latchSize = this.countDownLatch.getCount();

            //execute actions in parallel
            executor.shutdown(); // <-- will finish all submitted tasks
            // wait for all threads to finish
            try {
                LOGGER.debug("{}: waiting for {} threads to finish", threadGroupName, latchSize);
                countDownLatch.await();
            } catch (InterruptedException e) {
                /* nothing to do here */
            }
            LOGGER.debug("Waiting for {} threads to finish", countDownLatch.getCount());

            //postprocess actions
            getActions().forEach(this::post);
        }
    }
}
