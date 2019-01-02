/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.gml;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.NoSuchElementException;

import org.joda.time.DateTime;
import org.junit.Test;

import org.n52.shetland.ogc.gml.time.IndeterminateValue;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimePosition;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public class TimeTest {

    @Test
    public void datetime() {
        TimePosition timePosition = new TimePosition(DateTime.now());

        assertThat("time position is set", timePosition.isSetTime(), is(true));
        assertThat("indeterminate value is not", timePosition.isSetIndeterminateValue(), is(false));
        assertThat("time format is set", timePosition.isSetTimeFormat(), is(true));
    }

    @Test
    public void missingFormat() {
        TimePosition timePosition = new TimePosition(DateTime.now(), null);
        TimePosition timePositionWithDefaultFormat = new TimePosition(DateTime.now());
        assertThat("provided nullable time format will return default time format", timePosition.getTimeFormat(),
                is(equalTo(timePositionWithDefaultFormat.getTimeFormat())));
    }

    @Test(expected = NoSuchElementException.class)
    public void missingIndeterminateValue() {
        TimePosition timePosition = new TimePosition(DateTime.now());
        timePosition.getIndeterminateValue();
    }

    @Test(expected = NoSuchElementException.class)
    public void missingTime() {
        TimePosition timePosition = new TimePosition(IndeterminateValue.UNKNOWN);
        timePosition.getTime();
    }

    @Test
    public void inteterminate() {
        TimePosition timePosition = new TimePosition(IndeterminateValue.NOW);

        assertThat("time position is not set", timePosition.isSetTime(), is(false));
        assertThat("indeterminate value is set", timePosition.isSetIndeterminateValue(), is(true));
        assertThat("indeterminate value is 'now'", timePosition.getIndeterminateValue(), is(IndeterminateValue.NOW));
        assertThat("format is not set", timePosition.isSetTimeFormat(), is(false));
    }

    @Test
    public void format() {
        TimePosition timePosition = new TimePosition(DateTime.now(), Time.TimeFormat.ISO8601);

        assertThat("time position is set", timePosition.isSetTime(), is(true));
        assertThat("indeterminate value is not set", timePosition.isSetIndeterminateValue(), is(false));
        assertThat("format is set", timePosition.isSetTimeFormat(), is(true));
        assertThat("format is correct", timePosition.getTimeFormat().toString(), is(Time.TimeFormat.ISO8601.toString()));
    }

    @Test
    public void defaultFormat() {
        TimePosition timePosition = new TimePosition(DateTime.now());
        assertThat("format is set to default", timePosition.getTimeFormat(), is(Time.TimeFormat.ISO8601));
    }

    @Test
    public void nullFormat() {
        TimePosition timePosition = new TimePosition(DateTime.now(), null);
        assertThat("time position is set", timePosition.isSetTime(), is(true));
        assertThat("time format is set", timePosition.isSetTimeFormat(), is(false));
    }

}
