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
package org.n52.iceland.cache.ctrl;

import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.cache.ContentCacheController;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.faroe.ConfigurationError;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.janmayen.lifecycle.Destroyable;
import org.n52.iceland.util.Validation;

/**
 * Abstract class for capabilities cache controller implementations that
 * schedules a complete cache update at a configured interval.
 *
 * @since 1.0.0
 */
@Configurable
public abstract class AbstractSchedulingContentCacheController implements ContentCacheController, Destroyable {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSchedulingContentCacheController.class);

    private boolean initialized = false;
    private long updateInterval;
    private final Timer timer = new Timer("52n-iceland-capabilities-cache-controller", true);
    private TimerTask current = null;

    /**
     * Starts a new timer task
     */
    private void schedule() {
        /*
         * Timers can not be rescheduled. To make the interval changeable
         * reschedule a new timer.
         */
        current = new UpdateTimerTask();
        long delay = getUpdateInterval();
        if (!isInitialized()) {
            delay = 1;
            setInitialized(true);
        }
        if (delay > 0) {
            LOGGER.info("Next CapabilitiesCacheUpdate in {}m: {}", delay / 60000,
                    new DateTime(System.currentTimeMillis() + delay));
            timer.schedule(current, delay);
        }
    }

    @Setting(ScheduledContentCacheControllerSettings.CAPABILITIES_CACHE_UPDATE_INTERVAL)
    public void setUpdateInterval(int interval) throws ConfigurationError {
        Validation.greaterEqualZero("Cache update interval", interval);
        if (this.updateInterval != interval) {
            this.updateInterval = interval;
            reschedule();
        }
    }

    private long getUpdateInterval() {
        return this.updateInterval * 60000;
    }

    /**
     * Stops the current task, if available and starts a new {@link TimerTask}.
     *
     * @see #schedule()
     */
    private void reschedule() {
        cancelCurrent();
        schedule();
    }

    private void cancelCurrent() {
        if (this.current != null) {
            this.current.cancel();
            LOGGER.debug("Current {} canceled", UpdateTimerTask.class.getSimpleName());
        }
    }

    private void cancelTimer() {
        if (this.timer != null) {
            this.timer.cancel();
            LOGGER.debug("Cache Update timer canceled.");
        }
    }

    @Override
    public void destroy() {
        cancelCurrent();
        cancelTimer();
    }

    /**
     * @return the initialized
     */
    protected boolean isInitialized() {
        return initialized;
    }

    /**
     * @param initialized
     *            the initialized to set
     */
    protected void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    private class UpdateTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                update();
                LOGGER.info("Timertask: capabilities cache update successful!");
                schedule();
            } catch (OwsExceptionReport e) {
                LOGGER.error("Fatal error: Timertask couldn't update capabilities cache! Switch log level to DEBUG to get more details.");
                LOGGER.debug("Exception thrown", e);
            }
        }
    }
}
