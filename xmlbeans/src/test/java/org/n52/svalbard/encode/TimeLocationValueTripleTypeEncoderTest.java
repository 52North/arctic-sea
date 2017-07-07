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
package org.n52.svalbard.encode;

import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.joda.time.DateTime;
import org.junit.Test;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.om.TimeLocationValueTriple;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.shetland.util.JTSHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.XmlHelper;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;

import eu.europa.ec.inspire.schemas.omso.x30.CategoricalTimeLocationValueTripleType;
import eu.europa.ec.inspire.schemas.omso.x30.MeasurementTimeLocationValueTripleType;
import net.opengis.waterml.x20.TimeValuePairType;

public class TimeLocationValueTripleTypeEncoderTest {

//    private TimeLocationValueTripleTypeEncoder encoder = new TimeLocationValueTripleTypeEncoder();
//
//
//    @Test
//    public void test_Quantity() throws EncodingException, DecodingException, ParseException {
//        TimeValuePairType encoded = encoder.encode(getQuantityTimeLocationValueTriple());
//        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
//        assertThat(encoded, instanceOf(MeasurementTimeLocationValueTripleType.class));
//    }
//
//    @Test
//    public void test_Count() throws EncodingException, DecodingException, ParseException {
//        TimeValuePairType encoded = encoder.encode(getCountTimeLocationValueTriple());
//        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
//        assertThat(encoded, instanceOf(MeasurementTimeLocationValueTripleType.class));
//    }
//
//    @Test
//    public void test_Categorical() throws EncodingException, DecodingException, ParseException {
//        TimeValuePairType encoded = encoder.encode(getCategoricalTimeLocationValueTriple());
//        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
//        assertThat(encoded, instanceOf(CategoricalTimeLocationValueTripleType.class));
//    }
//
//    private TimeLocationValueTriple getQuantityTimeLocationValueTriple() throws EncodingException, ParseException {
//        return getTimeLocationValueTriple(new QuantityValue(15.6, "C"));
//    }
//
//    private TimeLocationValueTriple getCountTimeLocationValueTriple() throws EncodingException, ParseException {
//        return getTimeLocationValueTriple(new CountValue(15));
//    }
//
//    private TimeLocationValueTriple getCategoricalTimeLocationValueTriple() throws EncodingException, ParseException {
//        return getTimeLocationValueTriple(new CategoryValue("test", "test_voc"));
//    }
//
//    private TimeLocationValueTriple getTimeLocationValueTriple(Value<?> value) throws EncodingException, ParseException {
//        return new TimeLocationValueTriple(new TimeInstant(new DateTime()), value, getGeometry() );
//    }
//
//    private Geometry getGeometry() throws ParseException {
//        final String wktString = "POINT (52.7 7.52)";
//        return JTSHelper.createGeometryFromWKT(wktString, 4326);
//    }
}
