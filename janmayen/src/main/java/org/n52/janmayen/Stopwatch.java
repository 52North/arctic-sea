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
package org.n52.janmayen;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Stopwatch {
    private static final TimeUnit[] UNITS = TimeUnit.values();
    private static final String ZERO = "0ns";
    private static final DecimalFormat DECIMAL_FORMAT;

    static {
        DECIMAL_FORMAT = new DecimalFormat();
        DECIMAL_FORMAT.setRoundingMode(RoundingMode.UP);
        DECIMAL_FORMAT.setMaximumFractionDigits(3);
        DECIMAL_FORMAT.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ROOT));
    }

    private boolean isRunning;
    private long elapsed;
    private long start;

    public Stopwatch() {
    }

    public synchronized boolean isRunning() {
        return isRunning;
    }

    public synchronized Stopwatch start() {
        if (isRunning) {
            throw new IllegalStateException();
        }
        isRunning = true;
        start = System.nanoTime();
        return this;
    }

    public synchronized Stopwatch stop() {
        long now = System.nanoTime();
        if (!isRunning) {
            throw new IllegalStateException();
        }
        isRunning = false;
        elapsed += now - start;
        return this;
    }

    public synchronized Stopwatch reset() {
        elapsed = 0;
        isRunning = false;
        return this;
    }

    public synchronized long elapsed() {
        return isRunning ? System.nanoTime() - start + elapsed : elapsed;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean exact) {
        long nanos = elapsed();
        return exact ? toStringExact(nanos) : toString(nanos);
    }

    private static String toString(long nanos) {
        if (nanos == 0) {
            return ZERO;
        }
        if (nanos < 0) {
            throw new IllegalArgumentException();
        }

        for (int i = UNITS.length - 1; i >= 0; i--) {
            long v = UNITS[i].convert(nanos, TimeUnit.NANOSECONDS);
            if (v > 0) {
                double value = (double) nanos / UNITS[i].toNanos(1);
                return DECIMAL_FORMAT.format(value) + TimeValue.abbreviate(UNITS[i]);
            }
        }
        return ZERO;
    }

    private static String toStringExact(long nanos) {
        if (nanos == 0) {
            return ZERO;
        }
        if (nanos < 0) {
            throw new IllegalArgumentException();
        }
        long n = nanos;
        StringBuilder builder = new StringBuilder();
        for (int i = UNITS.length - 1; i >= 0; i--) {
            long v = UNITS[i].convert(nanos, TimeUnit.NANOSECONDS);
            if (v <= 0) {
                continue;
            }
            if (builder.length() >= 0) {
                builder.append(' ');
            }
            builder.append(v).append(TimeValue.abbreviate(UNITS[i]));
            n -= UNITS[i].toNanos(v);
        }
        return builder.toString();
    }

}
