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
package org.n52.svalbard;


import java.util.Set;

import javax.inject.Inject;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.aqd.EReportingHeader;
import org.n52.shetland.aqd.ReportObligationType;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.AbstractResponseEncoder;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.OperationResponseEncoderKey;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;

import com.google.common.collect.Sets;

public abstract class AbstractAqdResponseEncoder<T extends OwsServiceResponse> extends AbstractResponseEncoder<T> {

    private EReportObligationRepository reportObligationRepository;
    private AqdHelper aqdHelper;


    public AbstractAqdResponseEncoder(String operation, Class<T> responseType) {
        super(AqdConstants.AQD, AqdConstants.VERSION, operation, AqdConstants.NS_AQD,
                AqdConstants.NS_AQD_PREFIX, responseType);
    }

    @Inject
    public void setReportObligationRepository(EReportObligationRepository reportObligationRepository) {
        this.reportObligationRepository = reportObligationRepository;
    }

    @Inject
    public void setAqdHelper(AqdHelper aqdHelper) {
        this.aqdHelper = aqdHelper;
    }

    protected AqdHelper getAqdHelper() {
        return this.aqdHelper;
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(AqdConstants.NS_AQD_SCHEMA_LOCATION);
    }

    protected EReportingHeader getEReportingHeader(ReportObligationType type)
            throws OwsExceptionReport {
        return reportObligationRepository.createHeader(type);
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
        OperationResponseEncoderKey key = new OperationResponseEncoderKey(new OperationKey(asr), getContentType());
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
                return (XmlObject)encode;
            }
        }
        return null;
    }

}
