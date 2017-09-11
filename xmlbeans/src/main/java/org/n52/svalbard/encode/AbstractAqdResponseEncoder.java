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

import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.apache.xmlbeans.XmlObject;
import org.n52.faroe.ConfigurationError;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.shetland.aqd.AbstractEReportingHeader;
import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.aqd.EReportObligationRepository;
import org.n52.shetland.aqd.EReportingHeader;
import org.n52.shetland.aqd.ReportObligationType;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.util.EReportingSetting;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

@Configurable
public abstract class AbstractAqdResponseEncoder<T extends OwsServiceResponse> extends AbstractResponseEncoder<T> {

    private Optional<EReportObligationRepository> reportObligationRepository;

    private String namespace;

    private String observationPrefix;

    public AbstractAqdResponseEncoder(String operation, Class<T> responseType) {
        super(AqdConstants.AQD, AqdConstants.VERSION, operation, AqdConstants.NS_AQD, AqdConstants.NS_AQD_PREFIX,
                responseType);
    }

    @Inject
    public void setReportObligationRepository(Optional<EReportObligationRepository> reportObligationRepository) {
        this.reportObligationRepository = reportObligationRepository;
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(AqdConstants.NS_AQD_SCHEMA_LOCATION);
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

    protected XmlObject encodeGml(Object o) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o);
    }

    protected XmlObject encodeGml(EncodingContext context, Object o) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o, context);
    }

    protected XmlObject encodeOws(Object o) throws EncodingException {
        return encodeObjectToXml(OWSConstants.NS_OWS, o);
    }

    protected XmlObject encodeOws(EncodingContext context, Object o) throws EncodingException {
        return encodeObjectToXml(OWSConstants.NS_OWS, o, context);
    }

    protected XmlObject encodeFes(Object o) throws EncodingException {
        return encodeObjectToXml(FilterConstants.NS_FES_2, o);
    }

    protected XmlObject encodeFes(EncodingContext context, Object o) throws EncodingException {
        return encodeObjectToXml(FilterConstants.NS_FES_2, o, context);
    }

    protected XmlObject encodeSwe(Object o) throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o);
    }

    protected XmlObject encodeSwe(EncodingContext context, Object o) throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o, context);
    }

    protected OwsServiceResponse changeResponseServiceVersion(OwsServiceResponse response) {
        response.setService(SosConstants.SOS);
        response.setVersion(Sos2Constants.SERVICEVERSION);
        return response;
    }

    /**
     * Get the {@link Encoder} for the {@link OwsServiceResponse} and the
     * requested contentType
     *
     * @param asr
     *            {@link OwsServiceResponse} to get {@link Encoder} for
     * @return {@link Encoder} for the {@link OwsServiceResponse}
     */
    protected Encoder<Object, OwsServiceResponse> getEncoder(OwsServiceResponse asr) {
        OperationResponseEncoderKey key = new OperationResponseEncoderKey(new OwsOperationKey(asr), getContentType());
        Encoder<Object, OwsServiceResponse> encoder = getEncoder(key);
        if (encoder == null) {
            throw new RuntimeException(new NoEncoderForKeyException(key));
        }
        return encoder;
    }

    protected XmlObject encodeWithSosEncoder(T response) throws EncodingException {
        Encoder<Object, OwsServiceResponse> encoder = getEncoder(changeResponseServiceVersion(response));
        if (encoder != null) {
            Object encode = encoder.encode(response);
            if (encode != null && encode instanceof XmlObject) {
                return (XmlObject) encode;
            }
        }
        return null;
    }

}
