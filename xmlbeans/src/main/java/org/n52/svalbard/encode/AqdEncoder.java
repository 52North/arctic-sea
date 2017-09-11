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
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import javax.inject.Inject;
import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.n52.faroe.ConfigurationError;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.shetland.aqd.AbstractEReportingHeader;
import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.aqd.EReportObligationRepository;
import org.n52.shetland.aqd.EReportingHeader;
import org.n52.shetland.aqd.ReportObligationType;
import org.n52.shetland.aqd.ReportObligations;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.ObservationStream;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.StreamingValue;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.util.EReportingSetting;
import org.n52.shetland.util.JavaHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.write.EReportingHeaderEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

@Configurable
public class AqdEncoder extends AbstractXmlEncoder<XmlObject, Object>
        implements ObservationEncoder<XmlObject, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AqdEncoder.class);

    private static final Set<EncoderKey> ENCODER_KEY_TYPES = CodingHelper.encoderKeysForElements(AqdConstants.NS_AQD,
            GetObservationResponse.class, OmObservation.class, EReportingHeader.class);

    private Optional<EReportObligationRepository> reportObligationRepository;

    private String namespace;

    private String observationPrefix;

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
        if (SosConstants.SOS.equals(service) && Sos1Constants.SERVICEVERSION.equals(version)) {
            return Sets.newHashSet(AqdConstants.AQD_CONTENT_TYPE.toString());
        }
        return Sets.newHashSet(AqdConstants.NS_AQD);
    }

    @Override
    public Map<String, Set<SupportedType>> getSupportedResponseFormatObservationTypes() {
        return Collections.emptyMap();
    }

    @Override
    public XmlObject encode(Object element, EncodingContext ctx)
            throws EncodingException, UnsupportedEncoderInputException {
        if (element instanceof GetObservationResponse) {
            return encodeGetObservationResponse((GetObservationResponse) element, ctx);
        } else if (element instanceof OmObservation) {
            return encodeOmObservation((OmObservation) element, ctx);
        } else if (element instanceof EReportingHeader) {
            return encodeEReportingHeader((EReportingHeader) element, ctx);
        }
        throw new UnsupportedEncoderInputException(this, element);
    }

    private XmlObject encodeGetObservationResponse(GetObservationResponse response, EncodingContext ctx)
            throws EncodingException {
        try {
            FeatureCollection featureCollection = getFeatureCollection(response);
            // TODO get FLOW from response
            EReportingHeader eReportingHeader = getEReportingHeader(getReportObligationType(response));
            featureCollection.addMember(eReportingHeader);
            TimePeriod timePeriod = new TimePeriod();
            TimeInstant resultTime = new TimeInstant(new DateTime(DateTimeZone.UTC));
            int counter = 1;
            ObservationStream observationCollection = response.getObservationCollection();

            while (observationCollection.hasNext()) {

                OmObservation observation = observationCollection.next();
                if (observation.getValue() instanceof ObservationStream) {
                    ObservationStream value = (ObservationStream) observation.getValue();

                    if (value instanceof StreamingValue) {
                        value = value.merge();
                    }

                    while (value.hasNext()) {
                        processObservation(value.next(), timePeriod, resultTime, featureCollection, eReportingHeader,
                                counter++);
                    }

                } else {
                    processObservation(observation, timePeriod, resultTime, featureCollection, eReportingHeader,
                            counter++);
                }
            }
            if (!timePeriod.isEmpty()) {
                eReportingHeader.setReportingPeriod(Referenceable.of((Time) timePeriod));
            }
            return encodeObjectToXml(GmlConstants.NS_GML_32, featureCollection, ctx
                    .with(XmlEncoderFlags.ENCODE_NAMESPACE, OmConstants.NS_OM_2).with(XmlBeansEncodingFlags.DOCUMENT));
        } catch (OwsExceptionReport ex) {
            throw new EncodingException(ex);
        }
    }

    private XmlObject encodeOmObservation(OmObservation element, EncodingContext context) throws EncodingException {
        return encodeObjectToXml(OmConstants.NS_OM_2, element, context);
    }

    private XmlObject encodeEReportingHeader(EReportingHeader element, EncodingContext ctx) throws EncodingException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            EncodingContext context = ctx.with(EncoderFlags.ENCODER_REPOSITORY, getEncoderRepository())
                    .with(XmlEncoderFlags.XML_OPTIONS, (Supplier<XmlOptions>) this::getXmlOptions);
            new EReportingHeaderEncoder(context, baos, element).write();
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

    protected void processObservation(OmObservation observation, TimePeriod timePeriod, TimeInstant resultTime,
            FeatureCollection featureCollection, AbstractEReportingHeader eReportingHeader, int counter) {
        if (observation.isSetPhenomenonTime()) {
            // generate gml:id
            observation.setGmlId(getObservationId(counter));
            // add xlink:href to eReportingHeader.content
            eReportingHeader.addContent((AbstractFeature) new OmObservation()
                    .setIdentifier(new CodeWithAuthority(getObservationXlink(observation.getGmlId()))));
            timePeriod.extendToContain(observation.getPhenomenonTime());
            observation.setResultTime(resultTime);
            featureCollection.addMember(observation);
        }
    }

    protected String getObservationXlink(String gmlId) {
        StringBuilder id = new StringBuilder();
        if (isSetEReportingNamespace()) {
            id.append(getEReportingNamespace());
            if (!getEReportingNamespace().endsWith("/")) {
                id.append("/");
            }
        } else {
            id.append("#");
        }
        id.append(gmlId);
        return id.toString();

    }

    protected String getObservationId(int counter) {
        return (isSetEReportingObservationPrefix() ? getEReportingObservationPrefix() : "o_")
                .concat(Integer.toString(counter));
    }

    public String getEReportingNamespace() {
        return namespace;
    }

    @Setting(EReportingSetting.EREPORTING_NAMESPACE)
    public void setEReportingNamespace(String namespace) throws ConfigurationError {
        this.namespace = namespace;
    }

    protected boolean isSetEReportingNamespace() {
        return !Strings.isNullOrEmpty(getEReportingNamespace());
    }

    @Setting(EReportingSetting.EREPORTING_OBSERVATION_PREFIX)
    public void setEReportingObservationPrefix(String observationPrefix) throws ConfigurationError {
        this.observationPrefix = observationPrefix;
    }

    protected String getEReportingObservationPrefix() {
        return observationPrefix;
    }

    protected boolean isSetEReportingObservationPrefix() {
        return !Strings.isNullOrEmpty(getEReportingObservationPrefix());
    }

    protected EReportingHeader getEReportingHeader(ReportObligationType type)
            throws OwsExceptionReport, EncodingException {
        if (reportObligationRepository.isPresent()) {
            return reportObligationRepository.get().createHeader(type);
        }
        throw new EncodingException("Missing implementatation of %s!", EReportObligationRepository.class);
    }

    @Inject
    public void setReportObligationRepository(Optional<EReportObligationRepository> reportObligationRepository) {
        this.reportObligationRepository = reportObligationRepository;
    }
}
