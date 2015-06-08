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
package org.n52.iceland.config.json;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.n52.iceland.lifecycle.Destroyable;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Debouncer implements Destroyable {

    private final ScheduledExecutorService executor = Executors
            .newScheduledThreadPool(1);
    private final Object lock = new Object();
    private final Runnable runnable;
    private final int interval;
    private DelayedTask delayed;

    public Debouncer(int interval, Runnable runnable) {
        this.interval = interval;
        this.runnable = runnable;
    }

    public void call() {
        DelayedTask task = new DelayedTask();
        DelayedTask prev;
        do {
            synchronized (this.lock) {
                prev = this.delayed;
                this.delayed = task;
            }
            if (prev == null) {
                this.executor
                        .schedule(task, this.interval, TimeUnit.MILLISECONDS);
            }
        } while (prev != null && !prev.postpone());
    }

    @Override
    public void destroy() {
        this.executor.shutdownNow().forEach(Runnable::run);
    }

    private class DelayedTask implements Runnable {
        private final Object lock = new Object();
        private long dueTime;

        DelayedTask() {
            postpone();
        }

        private boolean postpone() {
            synchronized (this.lock) {
                if (this.dueTime < 0) {
                    return false;
                }
                this.dueTime = System.currentTimeMillis() + interval;
                return true;
            }
        }

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
