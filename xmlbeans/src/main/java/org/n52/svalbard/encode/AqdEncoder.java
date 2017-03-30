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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;
import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.aqd.EReportingHeader;
import org.n52.shetland.aqd.ReportObligationType;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.StreamingValue;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.AbstractStreaming;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.util.JavaHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.shetland.aqd.EReportObligationRepository;
import org.n52.svalbard.SosHelperValues;
import org.n52.svalbard.XmlBeansEncodingFlags;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.shetland.util.AqdHelper;
import org.n52.svalbard.util.CodingHelper;
import org.n52.shetland.aqd.ReportObligations;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

public class AqdEncoder extends AbstractXmlEncoder<XmlObject, Object>
        implements ObservationEncoder<XmlObject, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AqdEncoder.class);

    private static final Set<EncoderKey> ENCODER_KEY_TYPES = CodingHelper.encoderKeysForElements(AqdConstants.NS_AQD,
            GetObservationResponse.class, OmObservation.class, EReportingHeader.class);

    private AqdHelper aqdHelper;

    private EReportObligationRepository reportObligationRepository;

    public AqdEncoder() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEY_TYPES));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEY_TYPES);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(AqdConstants.NS_AQD_SCHEMA_LOCATION);
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(AqdConstants.NS_AQD, AqdConstants.NS_AQD_PREFIX);
    }

    @Override
    public boolean isObservationAndMeasurmentV20Type() {
        return false;
    }

    @Override
    public boolean shouldObservationsWithSameXBeMerged() {
        return false;
    }

    @Override
    public boolean supportsResultStreamingForMergedValues() {
        return false;
    }

    @Override
    public Set<String> getSupportedResponseFormats(String service, String version) {
        if (SosConstants.SOS.equals(service) && Sos1Constants.VERSION.equals(version)) {
            return Sets.newHashSet(AqdConstants.AQD_CONTENT_TYPE.toString());
        }
        return Sets.newHashSet(AqdConstants.NS_AQD);
    }

    @Override
    public Map<String, Set<SupportedType>> getSupportedResponseFormatObservationTypes() {
        return Collections.emptyMap();
    }

    @Override
    public XmlObject encode(Object element, EncodingContext additionalValues)
            throws EncodingException, UnsupportedEncoderInputException {
        if (element instanceof GetObservationResponse) {
            return encodeGetObservationResponse((GetObservationResponse) element);
        } else if (element instanceof OmObservation) {
            return encodeOmObservation((OmObservation) element);
        } else if (element instanceof EReportingHeader) {
            return encodeEReportingHeader((EReportingHeader) element);
        }
        throw new UnsupportedEncoderInputException(this, element);
    }

    private XmlObject encodeGetObservationResponse(GetObservationResponse response) throws EncodingException {
        try {
            FeatureCollection featureCollection = getFeatureCollection(response);
            // TODO get FLOW from response
            EReportingHeader eReportingHeader = getEReportingHeader(getReportObligationType(response));
            featureCollection.addMember(eReportingHeader);
            TimePeriod timePeriod = new TimePeriod();
            TimeInstant resultTime = new TimeInstant(new DateTime(DateTimeZone.UTC));
            boolean mergeStreaming = response.hasStreamingData() && !response.isSetMergeObservation();
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
                    .with(XmlBeansEncodingFlags.DOCUMENT, null);
            return encodeObjectToXml(GmlConstants.NS_GML_32, featureCollection, ctx);
        } catch (OwsExceptionReport ex) {
            throw new EncodingException(ex);
        }
    }

    private XmlObject encodeOmObservation(OmObservation element) throws EncodingException {
        return encodeObjectToXml(OmConstants.NS_OM_2, element);
    }

    private XmlObject encodeEReportingHeader(EReportingHeader element) throws EncodingException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new EReportingHeaderEncoder(element).write(baos);
            return XmlObject.Factory.parse(baos.toString("UTF8"));
        } catch (XMLStreamException | XmlException | UnsupportedEncodingException xmlse) {
            throw new EncodingException("Error encoding response", xmlse);
        }

    }

    private ReportObligationType getReportObligationType(GetObservationResponse response) throws EncodingException {
        try {
            return ReportObligations.getFlow(response.getExtensions());
        } catch (OwsExceptionReport ex) {
            throw new EncodingException(ex);
        }
    }

    private FeatureCollection getFeatureCollection(GetObservationResponse response) throws EncodingException {
        FeatureCollection featureCollection = new FeatureCollection();
        featureCollection.setGmlId("fc_" + JavaHelper.generateID(new DateTime().toString()));

        return featureCollection;
    }

    private AqdHelper getAqdHelper() {
        return this.aqdHelper;
    }

    @Inject
    public void setAqdHelper(AqdHelper aqdHelper) {
        this.aqdHelper = Objects.requireNonNull(aqdHelper);
    }

    protected EReportingHeader getEReportingHeader(ReportObligationType type) throws OwsExceptionReport {
        return this.reportObligationRepository.createHeader(type);
    }

    @Inject
    public void setReportObligationRepository(EReportObligationRepository reportObligationRepository) {
        this.reportObligationRepository = Objects.requireNonNull(reportObligationRepository);
    }
}
