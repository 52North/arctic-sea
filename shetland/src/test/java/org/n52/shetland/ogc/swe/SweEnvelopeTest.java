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
package org.n52.shetland.ogc.swe;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import org.n52.shetland.ogc.swe.SweConstants.SweCoordinateNames;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.util.ReferencedEnvelope;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SweEnvelopeTest {

    @Test public void
    should_create_valid_sosSweEnvelope_from_sosEnvelope() {

        final int srid = 52;
        final double x1 = 1;
        final double y1 = 2;
        final double y2 = 3;
        final double x2 = 4;
        final String uom = "deg";
        final ReferencedEnvelope sosEnvelope = new ReferencedEnvelope(new Envelope(x1, x2, y1, y2), srid);
        final SweEnvelope sweEnvelope = new SweEnvelope(sosEnvelope, uom, false);

        // srid
        assertThat(sweEnvelope.getReferenceFrame(), is(Integer.toString(srid)));
        // x1
        final List<? extends SweCoordinate<?>> lcCoordinates = sweEnvelope.getLowerCorner().getCoordinates();
        assertThat(((Double) lcCoordinates.get(0).getValue().getValue()), is(x1));
        // y1
        assertThat(((Double) lcCoordinates.get(1).getValue().getValue()), is(y1));
        // x2
        final List<? extends SweCoordinate<?>> ucCoordinates = sweEnvelope.getUpperCorner().getCoordinates();
        assertThat(((Double) ucCoordinates.get(0).getValue().getValue()), is(x2));
        // y2
        assertThat(((Double) ucCoordinates.get(1).getValue().getValue()), is(y2));
        // uom
        assertThat(((SweQuantity) lcCoordinates.get(0).getValue()).getUom(), is(uom));
        assertThat(((SweQuantity) lcCoordinates.get(1).getValue()).getUom(), is(uom));
        assertThat(((SweQuantity) ucCoordinates.get(0).getValue()).getUom(), is(uom));
        assertThat(((SweQuantity) ucCoordinates.get(1).getValue()).getUom(), is(uom));
        // northing
        assertThat(lcCoordinates.get(0).getName(), is(SweCoordinateNames.EASTING));
        assertThat(ucCoordinates.get(0).getName(), is(SweCoordinateNames.EASTING));
        // easting
        assertThat(lcCoordinates.get(1).getName(), is(SweCoordinateNames.NORTHING));
        assertThat(ucCoordinates.get(1).getName(), is(SweCoordinateNames.NORTHING));
    }

}
