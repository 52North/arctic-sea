/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.janmayen;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Postpones a operation until a configurable amount of time is passed without further requests for the operation.
 *
 * @since 1.0.0
 *
 * @author Christian Autermann
 */
public class Debouncer {

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final Object lock = new Object();
    private final Runnable runnable;
    private final int interval;
    private DelayedTask delayed;

    /**
     * Creates a new {@code Debouncer}.
     *
     * @param interval the interval to wait for new {@linkplain #call() calls}.
     * @param runnable the task to execute
     */
    public Debouncer(int interval, Runnable runnable) {
        this.interval = interval;
        this.runnable = runnable;
    }

    /**
     * Request the execution of the operation.
     */
    public void call() {
        DelayedTask task = new DelayedTask();
        DelayedTask prev;
        do {
            synchronized (this.lock) {
                prev = this.delayed;
                this.delayed = task;
            }
            if (prev == null) {
                this.executor.schedule(task, this.interval, TimeUnit.MILLISECONDS);
            }
        } while (prev != null && !prev.postpone());
    }

    /**
     * Shut this {@code Debouncer} down by executing any pending request.
     *
     * Note: further calls to this class are undefined.
     */
    public void finish() {
        this.executor.shutdownNow().forEach(Runnable::run);
    }

    /**
     * Delayable task.
     */
    private class DelayedTask implements Runnable {
        private final Object lock = new Object();
        private long dueTime;

        DelayedTask() {
            postpone();
        }

        /**
         * Postpone this task for {@code interval} milliseconds.
         *
         * @return {@code true} if this task was rescheduled, {@code false} if this task has already been executed
         */
        private boolean postpone() {
            synchronized (this.lock) {
                if (this.dueTime < 0) {
                    return false;
                }
                this.dueTime = System.currentTimeMillis() + interval;
                return true;
            }
        }

        /**
         * Executes the operations or reschedules itself if postponed.
         */
        @Override
        public void run() {
            synchronized (this.lock) {
                long remaining = this.dueTime - System.currentTimeMillis();
                if (remaining > 0) {
                    // Re-schedule task
                    Debouncer.this.executor
                            .schedule(this, remaining, TimeUnit.MILLISECONDS);
                } else {
                    this.dueTime = -1;
                    try {
                        Debouncer.this.runnable.run();
                    } finally {
                        synchronized (Debouncer.this.lock) {
                            Debouncer.this.delayed = null;
                        }
                    }
                }
            }
        }
    }

}
