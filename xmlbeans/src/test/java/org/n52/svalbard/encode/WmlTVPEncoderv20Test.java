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
import org.n52.shetland.ogc.om.series.wml.DefaultPointMetadata;
import org.n52.shetland.ogc.om.series.wml.DefaultTVPMeasurementMetadata;
import org.n52.shetland.ogc.om.series.wml.MeasurementTimeseriesMetadata;
import org.n52.shetland.ogc.om.series.wml.Metadata;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants.InterpolationType;
import org.n52.shetland.ogc.om.values.MultiValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.encode.exception.EncodingException;

import net.opengis.gml.x32.ReferenceType;
import net.opengis.waterml.x20.DefaultTVPMeasurementMetadataDocument;
import net.opengis.waterml.x20.MeasurementTimeseriesDocument;
import net.opengis.waterml.x20.MeasurementTimeseriesMetadataType;
import net.opengis.waterml.x20.TVPDefaultMetadataPropertyType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 * @since 1.0.0
 */
public class WmlTVPEncoderv20Test {

    private static final long UTC_TIMESTAMP = 43200l;

    private WmlTVPEncoderv20 encoder;

    private ObservationValue<MultiValue<List<TimeValuePair>>> mv;

    @Before
    public void initObjects() {
        encoder = new WmlTVPEncoderv20();

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

        Assert.assertThat(encodedElement, CoreMatchers.instanceOf(MeasurementTimeseriesDocument.class));
        final MeasurementTimeseriesDocument measurementTimeseriesDocument =
                (MeasurementTimeseriesDocument) encodedElement;
        Assert.assertThat(measurementTimeseriesDocument.getTimeseries().isSetMetadata(), Is.is(true));
        Assert.assertThat(measurementTimeseriesDocument.getTimeseries().getMetadata().getTimeseriesMetadata(),
                CoreMatchers.instanceOf(MeasurementTimeseriesMetadataType.class));
        final MeasurementTimeseriesMetadataType measurementTimeseriesMetadataType =
                (MeasurementTimeseriesMetadataType) measurementTimeseriesDocument.getTimeseries().getMetadata()
                        .getTimeseriesMetadata();
        Assert.assertThat(measurementTimeseriesMetadataType.isSetCumulative(), Is.is(true));
        Assert.assertThat(measurementTimeseriesMetadataType.getCumulative(), Is.is(false));
    }

    @Test
    public void shouldEncodeCumulativeProperty() throws EncodingException {
        mv.setMetadata(new Metadata().setTimeseriesmetadata(new MeasurementTimeseriesMetadata().setCumulative(true)));

        XmlObject encodedElement = encoder.encode(mv);

        Assert.assertThat(((MeasurementTimeseriesMetadataType) ((MeasurementTimeseriesDocument) encodedElement)
                .getTimeseries().getMetadata().getTimeseriesMetadata()).getCumulative(), Is.is(true));
    }

    @Test
    public void shouldEncodeInterpolationType() throws EncodingException, XmlException {
        final InterpolationType type = WaterMLConstants.InterpolationType.MinPrec;

        mv.setDefaultPointMetadata(new DefaultPointMetadata()
                .setDefaultTVPMeasurementMetadata(new DefaultTVPMeasurementMetadata().setInterpolationtype(type)));

        XmlObject encodedElement = encoder.encode(mv);

        TVPDefaultMetadataPropertyType defaultPointMetadata =
                ((MeasurementTimeseriesDocument) encodedElement).getTimeseries().getDefaultPointMetadataArray(0);
        DefaultTVPMeasurementMetadataDocument tvpMeasurementMetadataDocument =
                DefaultTVPMeasurementMetadataDocument.Factory.parse(defaultPointMetadata.xmlText());
        ReferenceType interpolationType =
                tvpMeasurementMetadataDocument.getDefaultTVPMeasurementMetadata().getInterpolationType();
        Assert.assertThat(interpolationType.getHref(),
                Is.is("http://www.opengis.net/def/waterml/2.0/interpolationType/MinPrec"));
        Assert.assertThat(interpolationType.getTitle(), Is.is("MinPrec"));
    }

    @Test
    public void shouldEncodeInterpolationTypeContinuousAsDefault() throws EncodingException, XmlException {
        XmlObject encodedElement = encoder.encode(mv);

        TVPDefaultMetadataPropertyType defaultPointMetadata =
                ((MeasurementTimeseriesDocument) encodedElement).getTimeseries().getDefaultPointMetadataArray(0);
        DefaultTVPMeasurementMetadataDocument tvpMeasurementMetadataDocument =
                DefaultTVPMeasurementMetadataDocument.Factory.parse(defaultPointMetadata.xmlText());
        ReferenceType interpolationType =
                tvpMeasurementMetadataDocument.getDefaultTVPMeasurementMetadata().getInterpolationType();
        Assert.assertThat(interpolationType.getHref(),
                Is.is("http://www.opengis.net/def/waterml/2.0/interpolationType/Continuous"));
        Assert.assertThat(interpolationType.getTitle(), Is.is("Continuous"));
    }

    // TODO add tests für sosObservation or remove duplicate code in
    // WmlTVPEncoderv20

}
