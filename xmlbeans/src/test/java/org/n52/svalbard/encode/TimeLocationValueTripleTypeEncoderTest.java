/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.inspire.omso.v30.encode;

import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.joda.time.DateTime;
import org.junit.Test;
import org.n52.sos.exception.ows.concrete.UnsupportedEncoderInputException;
import org.n52.sos.ogc.gml.time.TimeInstant;
import org.n52.sos.ogc.om.TimeLocationValueTriple;
import org.n52.sos.ogc.om.values.CategoryValue;
import org.n52.sos.ogc.om.values.CountValue;
import org.n52.sos.ogc.om.values.QuantityValue;
import org.n52.sos.ogc.om.values.Value;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.util.GeometryHandler;
import org.n52.sos.util.JTSHelper;
import org.n52.sos.util.XmlHelper;

import com.vividsolutions.jts.geom.Geometry;

import eu.europa.ec.inspire.schemas.omso.x30.CategoricalTimeLocationValueTripleType;
import eu.europa.ec.inspire.schemas.omso.x30.MeasurementTimeLocationValueTripleType;
import net.opengis.waterml.x20.TimeValuePairType;

public class TimeLocationValueTripleTypeEncoderTest {

    private TimeLocationValueTripleTypeEncoder encoder = new TimeLocationValueTripleTypeEncoder();


    @Test
    public void test_Quantity() throws UnsupportedEncoderInputException, OwsExceptionReport {
        TimeValuePairType encoded = encoder.encode(getQuantityTimeLocationValueTriple());
        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
        assertThat(encoded, instanceOf(MeasurementTimeLocationValueTripleType.class));
    }

    @Test
    public void test_Count() throws UnsupportedEncoderInputException, OwsExceptionReport {
        TimeValuePairType encoded = encoder.encode(getCountTimeLocationValueTriple());
        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
        assertThat(encoded, instanceOf(MeasurementTimeLocationValueTripleType.class));
    }

    @Test
    public void test_Categorical() throws UnsupportedEncoderInputException, OwsExceptionReport {
        TimeValuePairType encoded = encoder.encode(getCategoricalTimeLocationValueTriple());
        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
        assertThat(encoded, instanceOf(CategoricalTimeLocationValueTripleType.class));
    }

    private TimeLocationValueTriple getQuantityTimeLocationValueTriple() throws OwsExceptionReport {
        return getTimeLocationValueTriple(new QuantityValue(15.6, "C"));
    }

    private TimeLocationValueTriple getCountTimeLocationValueTriple() throws OwsExceptionReport {
        return getTimeLocationValueTriple(new CountValue(15));
    }

    private TimeLocationValueTriple getCategoricalTimeLocationValueTriple() throws OwsExceptionReport {
        return getTimeLocationValueTriple(new CategoryValue("test", "test_voc"));
    }

    private TimeLocationValueTriple getTimeLocationValueTriple(Value<?> value) throws OwsExceptionReport {
        return new TimeLocationValueTriple(new TimeInstant(new DateTime()), value, getGeometry() );
    }

    private Geometry getGeometry() throws OwsExceptionReport {
        final String wktString =
                GeometryHandler.getInstance().getWktString("7.52", "52.7", 4326);
        return JTSHelper.createGeometryFromWKT(wktString, 4326);
    }
}
