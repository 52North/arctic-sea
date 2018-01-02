/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.om.values.GeometryValue;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

import com.google.common.io.BaseEncoding.DecodingException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * @since 1.0.0
 *
 */
public class OmObservationTest {

    @Test
    public final void should_have_SpatialFilteringProfileParameter() throws OwsExceptionReport, DecodingException {
        OmObservation omObservation = new OmObservation();
        NamedValue<Geometry> namedValue = new NamedValue<>();
        namedValue.setName(new ReferenceType(OmConstants.PARAM_NAME_SAMPLING_GEOMETRY));
        GeometryFactory fac = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
        namedValue.setValue(new GeometryValue(fac.createPoint(new Coordinate(34.5, 76.4))));
        // test no parameter is set
        assertFalse(omObservation.isSetParameter());
        assertFalse(omObservation.isSetSpatialFilteringProfileParameter());
        omObservation.addParameter(namedValue);
        // test with set SpatialFilteringProfile parameter
        assertTrue(omObservation.isSetParameter());
        assertTrue(omObservation.isSetSpatialFilteringProfileParameter());
        assertThat(omObservation.getSpatialFilteringProfileParameter(), is(equalTo(namedValue)));
    }

}
