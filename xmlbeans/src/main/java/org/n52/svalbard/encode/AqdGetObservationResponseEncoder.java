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

import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.n52.shetland.aqd.EReportingHeader;
import org.n52.shetland.aqd.ReportObligationType;
import org.n52.shetland.aqd.ReportObligations;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.util.JavaHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.write.AqdGetObservationResponseXmlStreamWriter;

public class AqdGetObservationResponseEncoder extends AbstractAqdResponseEncoder<GetObservationResponse> {

    public AqdGetObservationResponseEncoder() {
        super(SosConstants.Operations.GetObservation.name(), GetObservationResponse.class);
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return Collections.emptySet();
    }

    @Override
    protected XmlObject create(GetObservationResponse response) throws EncodingException {
        try {
            FeatureCollection featureCollection = createFeatureCollection(response);
            // TODO get FLOW from response
            EReportingHeader eReportingHeader = getEReportingHeader(getReportObligationType(response));
            featureCollection.addMember(eReportingHeader);
            TimePeriod timePeriod = new TimePeriod();
            TimeInstant resultTime = new TimeInstant(new DateTime(DateTimeZone.UTC));
            int counter = 1;
            while (response.getObservationCollection().hasNext()) {
                OmObservation observation = response.getObservationCollection().next();
                processObservation(observation, timePeriod, resultTime,
                                                  featureCollection, eReportingHeader, counter++);
            }
            if (!timePeriod.isEmpty()) {
                eReportingHeader.setReportingPeriod(Referenceable.of((Time) timePeriod));
            }
            EncodingContext ctx = EncodingContext.empty()
                    .with(XmlEncoderFlags.ENCODE_NAMESPACE, OmConstants.NS_OM_2)
                    .with(XmlBeansEncodingFlags.DOCUMENT);
            return encodeGml(ctx, featureCollection);
        } catch (OwsExceptionReport ex) {
            throw new EncodingException(ex);
        }
    }

    @Override
    protected void create(GetObservationResponse response, OutputStream outputStream, EncodingContext ctx)
            throws EncodingException {
        FeatureCollection featureCollection = createFeatureCollection(response);
        EReportingHeader eReportingHeader;
        TimePeriod timePeriod;
        try {
            eReportingHeader = getEReportingHeader(getReportObligationType(response));
            featureCollection.addMember(eReportingHeader);
            timePeriod = addToFeatureCollectionAndGetTimePeriod(featureCollection, response, eReportingHeader);
        } catch (OwsExceptionReport ex) {
            throw new EncodingException(ex);
        }
        if (!timePeriod.isEmpty()) {
            eReportingHeader.setReportingPeriod(Referenceable.of((Time) timePeriod));
        }
        try {
            EncodingContext context = ctx.with(EncoderFlags.ENCODER_REPOSITORY, getEncoderRepository())
                    .with(XmlEncoderFlags.XML_OPTIONS, (Supplier<XmlOptions>) this::getXmlOptions)
                    .with(XmlEncoderFlags.ENCODE_NAMESPACE, OmConstants.NS_OM_2)
                    .with(XmlBeansEncodingFlags.DOCUMENT);
            new AqdGetObservationResponseXmlStreamWriter(context, outputStream, featureCollection).write();
        } catch (XMLStreamException xmlse) {
            throw new EncodingException("Error while writing element to stream!", xmlse);
        }
    }

    private ReportObligationType getReportObligationType(GetObservationResponse response) throws OwsExceptionReport {
        return ReportObligations.getFlow(response.getExtensions());
    }

    private TimePeriod addToFeatureCollectionAndGetTimePeriod(FeatureCollection featureCollection,
                                                              GetObservationResponse response,
                                                              EReportingHeader eReportingHeader)
            throws OwsExceptionReport {
        TimeInstant resultTime = new TimeInstant(new DateTime(DateTimeZone.UTC));
        TimePeriod timePeriod = new TimePeriod();
        int counter = 1;
        while (response.getObservationCollection().hasNext()) {
            OmObservation observation = response.getObservationCollection().next();
            processObservation(observation, timePeriod, resultTime,
                                              featureCollection, eReportingHeader, counter++);
        }
        return timePeriod;
    }

    private FeatureCollection createFeatureCollection(GetObservationResponse response) throws EncodingException {
        FeatureCollection featureCollection = new FeatureCollection();
        featureCollection.setGmlId("fc_" + JavaHelper.generateID(new DateTime().toString()));
        return featureCollection;
    }

}
