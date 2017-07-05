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
package org.n52.svalbard.gml.v321.encode;

import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.SortedMap;

import org.junit.Test;
import org.n52.sos.exception.ows.concrete.UnsupportedEncoderInputException;
import org.n52.sos.ogc.om.values.QuantityValue;
import org.n52.sos.ogc.om.values.RectifiedGridCoverage;
import org.n52.sos.ogc.om.values.Value;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.util.XmlHelper;

import com.google.common.collect.Maps;

import net.opengis.gml.x32.RectifiedGridCoverageDocument;

public class RectifiedGridCoverageDocumentEncoderTest {

    private RectifiedGridCoverageDocumentEncoder encoder = new RectifiedGridCoverageDocumentEncoder();

    @Test
    public void test_encoding() throws UnsupportedEncoderInputException, OwsExceptionReport {
        RectifiedGridCoverageDocument encoded = encoder.encode(getRectifiedGridCoverage());

        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
    }

    private RectifiedGridCoverage getRectifiedGridCoverage() {
        RectifiedGridCoverage gridCoverage = new RectifiedGridCoverage("test");
        SortedMap<Double, Value<?>> values = Maps.newTreeMap();
        values.put(2.5, new QuantityValue(10.0));
        values.put(5.0, new QuantityValue(8.0));
        values.put(10.0, new QuantityValue(3.0));
        gridCoverage.setValue(values);
        gridCoverage.setUnit("C");
        return gridCoverage;
    }
}
