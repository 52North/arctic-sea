/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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

import java.util.Date;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.TimeValuePair;
import org.n52.shetland.ogc.om.series.DefaultPointMetadata;
import org.n52.shetland.ogc.om.series.MeasurementTimeseriesMetadata;
import org.n52.shetland.ogc.om.series.Metadata;
import org.n52.shetland.ogc.om.series.tsml.DefaultTVPMeasurementMetadata;
import org.n52.shetland.ogc.om.series.tsml.TimeseriesMLConstants;
import org.n52.shetland.ogc.om.series.tsml.TimeseriesMLConstants.InterpolationType;
import org.n52.shetland.ogc.om.values.MultiValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.encode.exception.EncodingException;

import net.opengis.gml.x32.ReferenceType;
import net.opengis.tsml.x10.PointMetadataDocument;
import net.opengis.tsml.x10.MeasurementTVPDocument;
import net.opengis.tsml.x10.TimeseriesMetadataType;
import net.opengis.tsml.x10.TimeseriesTVPDocument;
import net.opengis.tsml.x10.TimeseriesTVPType;

/**
 * TODO(specki): rewrite
 */
public class TsmlTVPEncoderv10Test {

    private static final long UTC_TIMESTAMP = 43200l;

    private TsmlTVPEncoderv10 encoder;

    private ObservationValue<MultiValue<List<TimeValuePair>>> mv;

    @Before
    public void initObjects() {
        encoder = new TsmlTVPEncoderv10();

        MultiValue<List<TimeValuePair>> value = new TVPValue();
        String unit = "test-unit";
        value.setUnit(unit);
        TimeValuePair tvp1 =
                new TimeValuePair(new TimeInstant(new Date(UTC_TIMESTAMP)), new QuantityValue(52.1234567890));
        List<TimeValuePair> valueList = CollectionHelper.list(tvp1);
        value.setValue(valueList);

        mv = new MultiObservationValues<>();
        mv.setValue(value);
    }

    @Test
    public void shouldSetDefaultCumulativeProperty() throws EncodingException {
        XmlObject encodedElement = encoder.encode(mv);

        Assert.assertThat(encodedElement, CoreMatchers.instanceOf(TimeseriesTVPDocument.class));
        final TimeseriesTVPDocument timeseriesDocument =
                (TimeseriesTVPDocument) encodedElement;
        Assert.assertThat(timeseriesDocument.getTimeseriesTVP().isSetMetadata(), Is.is(true));
        Assert.assertThat(timeseriesDocument.getTimeseriesTVP().getMetadata().getTimeseriesMetadata(),
                CoreMatchers.instanceOf(TimeseriesMetadataType.class));
        final TimeseriesMetadataType measurementTimeseriesMetadataType =
                timeseriesDocument.getTimeseriesTVP().getMetadata()
                        .getTimeseriesMetadata();
        Assert.assertThat(measurementTimeseriesMetadataType.isSetCumulative(), Is.is(true));
        Assert.assertThat(measurementTimeseriesMetadataType.getCumulative(), Is.is(false));
    }

    @Test
    public void shouldEncodeCumulativeProperty() throws EncodingException {
        mv.setMetadata(new Metadata().setTimeseriesmetadata(new MeasurementTimeseriesMetadata().setCumulative(true)));

        XmlObject encodedElement = encoder.encode(mv);

        Assert.assertThat(((TimeseriesMetadataType) ((TimeseriesTVPDocument) encodedElement)
                .getTimeseriesTVP().getMetadata().getTimeseriesMetadata()).getCumulative(), Is.is(true));
    }

    @Test
    public void shouldEncodeInterpolationType() throws EncodingException, XmlException {
        final InterpolationType type = TimeseriesMLConstants.InterpolationType.MinPrec;

        mv.setDefaultPointMetadata(new DefaultPointMetadata()
                .setDefaultTVPMeasurementMetadata(new DefaultTVPMeasurementMetadata().setInterpolationtype(type)));

        XmlObject encodedElement = encoder.encode(mv);

        TimeseriesTVPType.DefaultPointMetadata defaultPointMetadata =
                ((TimeseriesTVPDocument) encodedElement).getTimeseriesTVP().getDefaultPointMetadata();
        PointMetadataDocument tvpMeasurementMetadataDocument =
                PointMetadataDocument.Factory.parse(defaultPointMetadata.xmlText());
        ReferenceType interpolationType =
                tvpMeasurementMetadataDocument.getPointMetadata().getInterpolationType();
        Assert.assertThat(interpolationType.getHref(),
                Is.is("http://www.opengis.net/def/timeseries/InterpolationCode/MinPrec"));
        Assert.assertThat(interpolationType.getTitle(), Is.is("MinPrec"));
    }

    @Test
    public void shouldEncodeInterpolationTypeContinuousAsDefault() throws EncodingException, XmlException {
        XmlObject encodedElement = encoder.encode(mv);

        TimeseriesTVPType.DefaultPointMetadata defaultPointMetadata =
                ((TimeseriesTVPDocument) encodedElement).getTimeseriesTVP().getDefaultPointMetadata();
        PointMetadataDocument tvpMeasurementMetadataDocument =
                PointMetadataDocument.Factory.parse(defaultPointMetadata.xmlText());
        ReferenceType interpolationType =
                tvpMeasurementMetadataDocument.getPointMetadata().getInterpolationType();
        Assert.assertThat(interpolationType.getHref(),
                Is.is("http://www.opengis.net/def/timeseries/InterpolationCode/Continuous"));
        Assert.assertThat(interpolationType.getTitle(), Is.is("Continuous"));
    }


    // TODO add tests für sosObservation or remove duplicate code in
    // WmlTVPEncoderv20

}
