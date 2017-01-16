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

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.n52.shetland.aqd.EReportingHeader;
import org.n52.shetland.aqd.ReportObligationType;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.StreamingValue;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.AbstractStreaming;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.util.JavaHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.SosHelperValues;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.write.AqdGetObservationResponseXmlStreamWriter;

public class AqdGetObservationResponseEncoder extends AbstractAqdResponseEncoder<GetObservationResponse>
        implements StreamingDataEncoder {

    public AqdGetObservationResponseEncoder() {
        super(SosConstants.Operations.GetObservation.name(), GetObservationResponse.class);
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return Collections.emptySet();
    }

    @Override
    public boolean forceStreaming() {
        return true;
    }

    @Override
    protected XmlObject create(GetObservationResponse response) throws EncodingException {
        try {
            FeatureCollection featureCollection = getFeatureCollection(response);
            // TODO get FLOW from response
            EReportingHeader eReportingHeader = getEReportingHeader(getReportObligationType(response));
            featureCollection.addMember(eReportingHeader);
            TimePeriod timePeriod = new TimePeriod();
            boolean mergeStreaming = response.hasStreamingData() && !response.isSetMergeObservation();
            TimeInstant resultTime = new TimeInstant(new DateTime(DateTimeZone.UTC));
            int counter = 1;
            for (OmObservation observation : response.getObservationCollection()) {
                if (mergeStreaming) {
                    AbstractStreaming value = (AbstractStreaming) observation.getValue();
                    if (value instanceof StreamingValue) {
                        for (OmObservation omObservation : value.mergeObservation()) {
                            getAqdHelper().processObservation(omObservation, timePeriod, resultTime, featureCollection,
                                    eReportingHeader, counter++);
                        }
                    } else {
                        while (value.hasNextValue()) {
                            getAqdHelper().processObservation(value.nextSingleObservation(), timePeriod, resultTime,
                                    featureCollection, eReportingHeader, counter++);
                        }
                    }
                } else {
                    getAqdHelper().processObservation(observation, timePeriod, resultTime, featureCollection,
                            eReportingHeader, counter++);
                }
            }
            if (!timePeriod.isEmpty()) {
                eReportingHeader.setReportingPeriod(Referenceable.of((Time) timePeriod));
            }
            EncodingContext ctx = EncodingContext.empty().with(SosHelperValues.ENCODE_NAMESPACE, OmConstants.NS_OM_2)
                    .with(SosHelperValues.DOCUMENT);
            return encodeGml(ctx, featureCollection);
        } catch (OwsExceptionReport ex) {
            throw new EncodingException(ex);
        }
    }

    @Override
    protected void create(GetObservationResponse response, OutputStream outputStream, EncodingValues encodingValues)
            throws EncodingException {
        FeatureCollection featureCollection = getFeatureCollection(response);
        EReportingHeader eReportingHeader;
        try {
            eReportingHeader = getEReportingHeader(getReportObligationType(response));
        } catch (OwsExceptionReport ex) {
            throw new EncodingException(ex);
        }
        featureCollection.addMember(eReportingHeader);
        TimePeriod timePeriod = addToFeatureCollectionAndGetTimePeriod(featureCollection, response, eReportingHeader);
        if (!timePeriod.isEmpty()) {
            eReportingHeader.setReportingPeriod(Referenceable.of((Time) timePeriod));
        }
        encodingValues.setEncodingNamespace(OmConstants.NS_OM_2);
        encodingValues.setAdditionalValues(encodingValues.getAdditionalValues()
                .with(SosHelperValues.ENCODE_NAMESPACE, OmConstants.NS_OM_2).with(SosHelperValues.DOCUMENT));
        try {
            new AqdGetObservationResponseXmlStreamWriter().write(featureCollection, outputStream, encodingValues);
        } catch (XMLStreamException xmlse) {
            throw new EncodingException("Error while writing element to stream!", xmlse);
        }
    }

    private ReportObligationType getReportObligationType(GetObservationResponse response) throws OwsExceptionReport {
        return getAqdHelper().getFlow(response.getExtensions());
    }

    private TimePeriod addToFeatureCollectionAndGetTimePeriod(FeatureCollection featureCollection,
            GetObservationResponse response, EReportingHeader eReportingHeader) {
        TimeInstant resultTime = new TimeInstant(new DateTime(DateTimeZone.UTC));
        TimePeriod timePeriod = new TimePeriod();
        int counter = 1;
        for (OmObservation observation : response.getObservationCollection()) {
            getAqdHelper().processObservation(observation, timePeriod, resultTime, featureCollection, eReportingHeader,
                    counter++);

        }
        return timePeriod;
    }

    private FeatureCollection getFeatureCollection(GetObservationResponse response) throws EncodingException {
        FeatureCollection featureCollection = new FeatureCollection();
        featureCollection.setGmlId("fc_" + JavaHelper.generateID(new DateTime().toString()));

        return featureCollection;
    }

}
