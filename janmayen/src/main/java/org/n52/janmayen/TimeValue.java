/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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

import java.util.concurrent.TimeUnit;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public final class TimeValue {
    public static final TimeValue ZERO = new TimeValue(0, TimeUnit.NANOSECONDS);
    private final TimeUnit unit;
    private final long value;

    public TimeValue(long value, TimeUnit unit) {
        this.unit = unit;
        this.value = value;
    }

    public TimeValue to(TimeUnit unit) {
        return new TimeValue(unit.convert(this.value, this.unit), unit);
    }

    public TimeValue toNanos() {
        return to(TimeUnit.NANOSECONDS);
    }

    public TimeValue toMillis() {
        return to(TimeUnit.MILLISECONDS);
    }

    public TimeValue toMicros() {
        return to(TimeUnit.MICROSECONDS);
    }

    public TimeValue toSeconds() {
        return to(TimeUnit.SECONDS);
    }

    public TimeValue toMinutes() {
        return to(TimeUnit.MINUTES);
    }

    public TimeValue toHours() {
        return to(TimeUnit.HOURS);
    }

    public TimeValue toDays() {
        return to(TimeUnit.DAYS);
    }

    public long getNanos() {
        return this.unit.toNanos(this.value);
    }

    public long getMillis() {
        return this.unit.toMillis(this.value);
    }

    public long getMicros() {
        return this.unit.toMicros(this.value);
    }

    public long getSeconds() {
        return this.unit.toSeconds(this.value);
    }

    public long getMinutes() {
        return this.unit.toMinutes(this.value);
    }

    public long getHours() {
        return this.unit.toHours(this.value);
    }

    public long getDays() {
        return this.unit.toDays(this.value);
    }

    @Override
    public String toString() {
        return String.format("%d%s", this.value, abbreviate(unit));
    }

    public TimeUnit getUnit() {
        return this.unit;
    }

    public long getValue() {
        return this.value;
    }

    public static String abbreviate(TimeUnit unit) {
        switch (unit) {
            case NANOSECONDS:
                return "ns";
            case MICROSECONDS:
                return "μs";
            case MILLISECONDS:
                return "ms";
            case SECONDS:
                return "s";
            case MINUTES:
                return "m";
            case HOURS:
                return "h";
            case DAYS:
                return "d";
            default:
                throw new AssertionError();
        }
    }

}
