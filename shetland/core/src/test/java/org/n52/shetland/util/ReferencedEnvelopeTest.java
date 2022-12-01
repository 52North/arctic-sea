/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.util;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.n52.shetland.util.ReferencedEnvelope;

import org.locationtech.jts.geom.Envelope;

/**
 * @since 1.0.0
 */
public class ReferencedEnvelopeTest {

    final double y12 = 0;

    final double y11 = 1;

    final double x12 = 0;

    final double x11 = 1;

    final double x21 = 2;

    final double x22 = 3;

    final double y21 = 2;

    final double y22 = 3;

    final int srid = 4326;

    Envelope extensionEnvelope;

    ReferencedEnvelope originEnvelope;

    ReferencedEnvelope emptyReferencedEnvelope;

    Envelope emptyEnvelope;

    @BeforeEach
    public void setUpEnvelopes() {
        extensionEnvelope = new Envelope(x21, x22, y21, y22);
        originEnvelope = new ReferencedEnvelope(new Envelope(x11, x12, y11, y12), srid);
        emptyReferencedEnvelope = new ReferencedEnvelope();
        emptyEnvelope = new Envelope();
    }

    @Test
    public void testExpandToIncludeEmptyEnvelope() throws Exception {
        originEnvelope.expandToInclude(emptyEnvelope);
        assertThat(originEnvelope.getSrid(), is(4326));
        final Envelope envelope = originEnvelope.getEnvelope();
        assertThat(envelope.getMinX(), is(0.0));
        assertThat(envelope.getMaxX(), is(1.0));
        assertThat(envelope.getMinY(), is(0.0));
        assertThat(envelope.getMaxY(), is(1.0));
        assertThat(envelope.getArea(), is(1.0));
    }

    @Test
    public void testExpandToIncludeEnvelope() throws Exception {
        originEnvelope.expandToInclude(extensionEnvelope);

        assertThat(originEnvelope.getSrid(), is(srid));
        final Envelope envelope = originEnvelope.getEnvelope();
        assertThat(envelope.getMinX(), is(0.0));
        assertThat(envelope.getMaxX(), is(3.0));
        assertThat(envelope.getMinY(), is(0.0));
        assertThat(envelope.getMaxY(), is(3.0));
        assertThat(envelope.getArea(), is(9.0));
    }

    @Test
    public void testExpandToIncludeEnvelopeWithNull() throws Exception {
        final Envelope e = null;
        originEnvelope.expandToInclude(e);
        assertThat(originEnvelope.isSetSrid(), is(true));
        assertThat(originEnvelope.getSrid(), is(srid));
        assertThat(originEnvelope.isSetEnvelope(), is(true));
        final Envelope envelope = originEnvelope.getEnvelope();
        assertThat(envelope.getArea(), is(1.0));
        assertThat(envelope.getMinX(), is(0.0));
        assertThat(envelope.getMaxX(), is(1.0));
        assertThat(envelope.getMinY(), is(0.0));
        assertThat(envelope.getMaxY(), is(1.0));
    }

    @Test
    public void testExpandToIncludeReferencedEnvelopeWithNull() throws Exception {
        final ReferencedEnvelope e = null;
        originEnvelope.expandToInclude(e);
        assertThat(originEnvelope.isSetSrid(), is(true));
        assertThat(originEnvelope.getSrid(), is(srid));
        assertThat(originEnvelope.isSetEnvelope(), is(true));
        final Envelope envelope = originEnvelope.getEnvelope();
        assertThat(envelope.getArea(), is(1.0));
        assertThat(envelope.getMinX(), is(0.0));
        assertThat(envelope.getMaxX(), is(1.0));
        assertThat(envelope.getMinY(), is(0.0));
        assertThat(envelope.getMaxY(), is(1.0));
    }

    @Test
    public void testExpandToIncludeEmptyReferencedEnvelope() throws Exception {
        originEnvelope.expandToInclude(emptyReferencedEnvelope);

        assertThat(originEnvelope.getSrid(), is(4326));
        final Envelope envelope = originEnvelope.getEnvelope();
        assertThat(envelope.getMinX(), is(0.0));
        assertThat(envelope.getMaxX(), is(1.0));
        assertThat(envelope.getMinY(), is(0.0));
        assertThat(envelope.getMaxY(), is(1.0));
        assertThat(envelope.getArea(), is(1.0));
    }

    @Test
    public void testExpandToIncludeReferencedEnvelope() throws Exception {
        originEnvelope.expandToInclude(new ReferencedEnvelope(extensionEnvelope, srid));

        assertThat(originEnvelope.getSrid(), is(srid));
        final Envelope envelope = originEnvelope.getEnvelope();
        assertThat(envelope.getMinX(), is(0.0));
        assertThat(envelope.getMaxX(), is(3.0));
        assertThat(envelope.getMinY(), is(0.0));
        assertThat(envelope.getMaxY(), is(3.0));
        assertThat(envelope.getArea(), is(9.0));
    }

    @Test
    public void testExpandToIncludeEnvelopeToNullEnvelope() throws Exception {
        final ReferencedEnvelope nullEnvelope = new ReferencedEnvelope(null, srid);

        nullEnvelope.expandToInclude(extensionEnvelope);

        assertThat(nullEnvelope.getSrid(), is(srid));
        final Envelope envelope = nullEnvelope.getEnvelope();
        assertThat(envelope.getMinX(), is(2.0));
        assertThat(envelope.getMaxX(), is(3.0));
        assertThat(envelope.getMinY(), is(2.0));
        assertThat(envelope.getMaxY(), is(3.0));
        assertThat(envelope.getArea(), is(1.0));
    }

    @Test
    public void testIsSetSrid() throws Exception {
        final ReferencedEnvelope sosEnvelope = new ReferencedEnvelope();
        sosEnvelope.setSrid(52);
        assertThat(new ReferencedEnvelope().isSetSrid(), is(false));
        assertThat(sosEnvelope.isSetSrid(), is(true));
    }

    @Test
    public void testIsSetEnvelope() throws Exception {
        final ReferencedEnvelope sosEnvelope = new ReferencedEnvelope();
        sosEnvelope.setEnvelope(extensionEnvelope);
        assertThat(new ReferencedEnvelope().isSetEnvelope(), is(false));
        assertThat(sosEnvelope.isSetEnvelope(), is(true));
        sosEnvelope.setEnvelope(emptyEnvelope);
        assertThat(sosEnvelope.isSetEnvelope(), is(false));
    }

    @Test
    public void testIsNotNullOrEmpty() throws Exception {
        assertThat(ReferencedEnvelope.isNotNullOrEmpty(null), is(false));
        assertThat(ReferencedEnvelope.isNotNullOrEmpty(emptyReferencedEnvelope), is(false));
        assertThat(ReferencedEnvelope.isNotNullOrEmpty(originEnvelope), is(true));

    }

    @Test
    public void testEquals() {
        assertThat(new ReferencedEnvelope().equals(null), is(false));
        assertThat(new ReferencedEnvelope().equals(new Object()), is(false));
        assertThat(new ReferencedEnvelope().equals(emptyReferencedEnvelope), is(true));
        assertThat(originEnvelope.equals(emptyReferencedEnvelope), is(false));
        final ReferencedEnvelope otherEnvelope = new ReferencedEnvelope(extensionEnvelope, 52);
        final ReferencedEnvelope myEnvelope = new ReferencedEnvelope(extensionEnvelope, 1024);
        assertThat(otherEnvelope.equals(myEnvelope), is(false));
        final ReferencedEnvelope anEnvelope = new ReferencedEnvelope(new Envelope(1.0, 2.0, 3.0, 4.0), 52);
        final ReferencedEnvelope anotherEnvelope = new ReferencedEnvelope(null, 52);
        assertThat(anEnvelope.equals(anotherEnvelope), is(false));
        assertThat(anEnvelope.equals(anEnvelope), is(true));
        assertThat(anotherEnvelope.equals(anEnvelope), is(false));
    }

    @Test
    public void testHashCode() {
        final ReferencedEnvelope anEnvelope = new ReferencedEnvelope(new Envelope(1.0, 2.0, 3.0, 4.0), 52);
        final ReferencedEnvelope anotherEnvelope = new ReferencedEnvelope(null, 52);
        assertThat(anEnvelope.hashCode(), is(anEnvelope.hashCode()));
        assertThat(anEnvelope.hashCode(), is(not(anotherEnvelope.hashCode())));
    }

}
