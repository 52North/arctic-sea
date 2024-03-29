/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.svalbard.decode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.filter.ComparisonFilter;
import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceCommunicationObject;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.DescribeSensorRequest;
import org.n52.shetland.ogc.sos.request.GetFeatureOfInterestRequest;
import org.n52.shetland.ogc.sos.request.GetObservationByIdRequest;
import org.n52.shetland.ogc.sos.request.GetObservationRequest;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.OMHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.opengis.sos.x10.DescribeSensorDocument;
import net.opengis.sos.x10.DescribeSensorDocument.DescribeSensor;
import net.opengis.sos.x10.GetCapabilitiesDocument;
import net.opengis.sos.x10.GetCapabilitiesDocument.GetCapabilities;
import net.opengis.sos.x10.GetFeatureOfInterestDocument;
import net.opengis.sos.x10.GetFeatureOfInterestDocument.GetFeatureOfInterest;
import net.opengis.sos.x10.GetFeatureOfInterestDocument.GetFeatureOfInterest.Location;
import net.opengis.sos.x10.GetObservationByIdDocument;
import net.opengis.sos.x10.GetObservationByIdDocument.GetObservationById;
import net.opengis.sos.x10.GetObservationDocument;
import net.opengis.sos.x10.GetObservationDocument.GetObservation;
import net.opengis.sos.x10.GetObservationDocument.GetObservation.FeatureOfInterest;
import net.opengis.sos.x10.GetObservationDocument.GetObservation.Result;

/**
 * @since 1.0.0
 *
 */
public class SosDecoderv100 extends AbstractXmlDecoder<XmlObject, OwsServiceCommunicationObject> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SosDecoderv100.class);

    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
            CodingHelper.decoderKeysForElements(Sos1Constants.NS_SOS, GetCapabilitiesDocument.class,
                    DescribeSensorDocument.class, GetObservationDocument.class, GetFeatureOfInterestDocument.class,
                    GetObservationByIdDocument.class),
            CodingHelper.xmlDecoderKeysForOperation(SosConstants.SOS, Sos1Constants.SERVICEVERSION,
                    SosConstants.Operations.GetCapabilities, SosConstants.Operations.GetObservation,
                    SosConstants.Operations.GetFeatureOfInterest, SosConstants.Operations.GetObservationById,
                    SosConstants.Operations.DescribeSensor));

    public SosDecoderv100() {
        LOGGER.debug("Decoder for the following namespaces initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public OwsServiceCommunicationObject decode(XmlObject xmlObject) throws DecodingException {
        OwsServiceCommunicationObject request = null;
        LOGGER.debug("REQUESTTYPE:" + xmlObject.getClass());

        /*
         * Add O&M 1.0.0 namespace to GetObservation document. XmlBeans removes
         * the namespace from the document because there are no om:... elements
         * in the document. But the validation fails if the <resultModel>
         * element is set with e.g. om:Measurement.
         */
        if (xmlObject instanceof GetObservationDocument) {
            XmlCursor cursor = xmlObject.newCursor();
            cursor.toFirstChild();
            cursor.insertNamespace(OmConstants.NS_OM_PREFIX, OmConstants.NS_OM);
            cursor.dispose();
        }
        // validate document
        XmlHelper.validateDocument(xmlObject);

        if (xmlObject instanceof GetCapabilitiesDocument) {
            // getCapabilities request
            GetCapabilitiesDocument getCapsDoc = (GetCapabilitiesDocument) xmlObject;
            request = parseGetCapabilities(getCapsDoc);
        } else if (xmlObject instanceof DescribeSensorDocument) {
            // DescribeSensor request (still SOS 1.0 NS_URI
            DescribeSensorDocument descSensorDoc = (DescribeSensorDocument) xmlObject;
            request = parseDescribeSensor(descSensorDoc);
        } else if (xmlObject instanceof GetObservationDocument) {
            // getObservation request
            GetObservationDocument getObsDoc = (GetObservationDocument) xmlObject;
            request = parseGetObservation(getObsDoc);
        } else if (xmlObject instanceof GetFeatureOfInterestDocument) {
            // getFeatureOfInterest request
            GetFeatureOfInterestDocument getFoiDoc = (GetFeatureOfInterestDocument) xmlObject;
            request = parseGetFeatureOfInterest(getFoiDoc);
        } else if (xmlObject instanceof GetObservationByIdDocument) {
            // getObservationById request
            GetObservationByIdDocument getObsByIdDoc = (GetObservationByIdDocument) xmlObject;
            request = parseGetObservationById(getObsByIdDoc);
        } else {
            throw new UnsupportedDecoderXmlInputException(this, xmlObject);
        }
        return request;
    }

    /**
     * parses the XmlBean representing the getCapabilities request and creates a
     * SosGetCapabilities request
     *
     * @param getCapsDoc
     *            XmlBean created from the incoming request stream
     * @return Returns SosGetCapabilitiesRequest representing the request
     *
     */
    private OwsServiceRequest parseGetCapabilities(GetCapabilitiesDocument getCapsDoc) {

        GetCapabilities getCaps = getCapsDoc.getGetCapabilities();
        GetCapabilitiesRequest request = new GetCapabilitiesRequest(getCaps.getService());

        if (getCaps.getAcceptFormats() != null && getCaps.getAcceptFormats().sizeOfOutputFormatArray() != 0) {
            request.setAcceptFormats(Arrays.asList(getCaps.getAcceptFormats().getOutputFormatArray()));
        }

        if (getCaps.getAcceptVersions() != null && getCaps.getAcceptVersions().sizeOfVersionArray() != 0) {
            request.setAcceptVersions(Arrays.asList(getCaps.getAcceptVersions().getVersionArray()));
        }

        if (getCaps.getSections() != null && getCaps.getSections().getSectionArray().length != 0) {
            request.setSections(Arrays.asList(getCaps.getSections().getSectionArray()));
        }

        return request;
    }

    /**
     * parses the XmlBean representing the describeSensor request and creates a
     * DescribeSensor request
     *
     * @param descSensorDoc
     *            XmlBean created from the incoming request stream
     * @return Returns SosDescribeSensorRequest representing the request
     */
    private OwsServiceCommunicationObject parseDescribeSensor(DescribeSensorDocument descSensorDoc) {

        DescribeSensorRequest request = new DescribeSensorRequest();
        DescribeSensor descSensor = descSensorDoc.getDescribeSensor();
        request.setService(descSensor.getService());
        request.setVersion(descSensor.getVersion());
        // parse outputFormat through MediaType to ensure it's a mime type and
        // eliminate whitespace variations
        request.setProcedureDescriptionFormat(MediaType.normalizeString(descSensor.getOutputFormat()));
        request.setProcedure(descSensor.getProcedure());
        return request;
    }

    /**
     * parses the XmlBean representing the getObservation request and creates a
     * SoSGetObservation request
     *
     * @param getObsDoc
     *            XmlBean created from the incoming request stream
     * @return Returns SosGetObservationRequest representing the request
     *
     *
     * @throws DecodingException
     *             * If parsing the XmlBean failed
     */
    private OwsServiceRequest parseGetObservation(GetObservationDocument getObsDoc) throws DecodingException {
        GetObservationRequest getObsRequest = new GetObservationRequest();

        GetObservation getObs = getObsDoc.getGetObservation();

        getObsRequest.setService(getObs.getService());
        getObsRequest.setVersion(getObs.getVersion());
        getObsRequest.setOfferings(Arrays.asList(getObs.getOffering()));
        getObsRequest.setObservedProperties(Arrays.asList(getObs.getObservedPropertyArray()));
        getObsRequest.setProcedures(Arrays.asList(getObs.getProcedureArray()));
        getObsRequest.setTemporalFilters(parseTemporalFilters4GetObservation(getObs.getEventTimeArray()));
        getObsRequest.setSrsName(getObs.getSrsName());

        if (getObs.isSetFeatureOfInterest()) {
            FeatureOfInterest featureOfInterest = getObs.getFeatureOfInterest();
            if (featureOfInterest.isSetSpatialOps()) {
                Object filter = decodeXmlElement(featureOfInterest.getSpatialOps());
                if (filter instanceof SpatialFilter) {
                    getObsRequest.setSpatialFilter((SpatialFilter) filter);
                }
            } else if (featureOfInterest.getObjectIDArray() != null) {
                Set<String> featureIdentifiers = Sets.newHashSet();
                for (String string : featureOfInterest.getObjectIDArray()) {
                    featureIdentifiers.add(string);
                }
                getObsRequest.setFeatureIdentifiers(Lists.newArrayList(featureIdentifiers));
            }
        }

        if (getObs.isSetResult()) {
            getObsRequest.setResultFilter(parseResultFilter(getObs.getResult()));
        }

        // return error message
        if (getObs.isSetResponseFormat()) {
            getObsRequest.setResponseFormat(decodeResponseFormat(getObs.getResponseFormat()));
        } else {
            getObsRequest.setResponseFormat(OmConstants.CONTENT_TYPE_OM.toString());
        }
        if (getObs.isSetResultModel()) {
            getObsRequest.setResultModel(OMHelper.getObservationTypeFor(getObs.getResultModel()));
        }

        return getObsRequest;
    }

    /**
     * parses the passes XmlBeans document and creates a SOS
     * getFeatureOfInterest request
     *
     * @param getFoiDoc
     *            XmlBeans document representing the getFeatureOfInterest
     *            request
     * @return Returns SOS getFeatureOfInterest request
     *
     *
     * @throws DecodingException
     *             * if validation of the request failed
     */
    private OwsServiceRequest parseGetFeatureOfInterest(GetFeatureOfInterestDocument getFoiDoc)
            throws DecodingException {

        GetFeatureOfInterestRequest getFoiRequest = new GetFeatureOfInterestRequest();
        GetFeatureOfInterest getFoi = getFoiDoc.getGetFeatureOfInterest();
        getFoiRequest.setService(getFoi.getService());
        getFoiRequest.setVersion(getFoi.getVersion());
        getFoiRequest.setFeatureIdentifiers(Arrays.asList(getFoi.getFeatureOfInterestIdArray()));
        getFoiRequest.setSpatialFilters(parseSpatialFilters4GetFeatureOfInterest(getFoi.getLocation()));

        return getFoiRequest;
    }

    private OwsServiceRequest parseGetObservationById(GetObservationByIdDocument getObsByIdDoc)
            throws DecodingException {
        GetObservationByIdRequest getObsByIdRequest = new GetObservationByIdRequest();
        GetObservationById getObsById = getObsByIdDoc.getGetObservationById();
        getObsByIdRequest.setService(getObsById.getService());
        getObsByIdRequest.setVersion(getObsById.getVersion());
        if (getObsById.isSetResponseFormat()) {
            getObsByIdRequest.setResponseFormat(decodeResponseFormat(getObsById.getResponseFormat()));

        } else {
            getObsByIdRequest.setResponseFormat(OmConstants.CONTENT_TYPE_OM.toString());
        }
        net.opengis.sos.x10.ResponseModeType.Enum responseMode = getObsById.getResponseMode();
        if (responseMode != null && responseMode.toString().equalsIgnoreCase(SosConstants.RESPONSE_MODE_INLINE)) {
            getObsByIdRequest.setResponseMode(SosConstants.RESPONSE_MODE_INLINE);
        }
        if (getObsById.isSetResultModel()) {
            getObsByIdRequest.setResultModel(OMHelper.getObservationTypeFor(getObsById.getResultModel()));
        }
        getObsByIdRequest.setObservationIdentifier(Arrays.asList(getObsById.getObservationId()));
        return getObsByIdRequest;
    }

    /**
     * Parses the spatial filters of a GetFeatureOfInterest request.
     *
     * @param location
     *            XmlBean representing the spatial filter parameter of the
     *            request
     * @return Returns SpatialFilter created from the passed foi request
     *         parameter
     *
     * @throws DecodingException
     *             * if creation of the SpatialFilter failed
     */
    private List<SpatialFilter> parseSpatialFilters4GetFeatureOfInterest(Location location) throws DecodingException {
        List<SpatialFilter> sosSpatialFilters = new LinkedList<>();
        if (location != null && location.getSpatialOps() != null) {
            Object filter = decodeXmlElement(location.getSpatialOps());
            if (filter instanceof SpatialFilter) {
                sosSpatialFilters.add((SpatialFilter) filter);
            }
        }
        return sosSpatialFilters;
    }

    /**
     * parses the Time of the requests and returns an array representing the
     * temporal filters
     *
     * @param temporalFilters
     *            array of XmlObjects representing the Time element in the
     *            request
     * @return Returns array representing the temporal filters
     *
     *
     * @throws DecodingException
     *             * if parsing of the element failed
     */
    private List<TemporalFilter> parseTemporalFilters4GetObservation(GetObservation.EventTime[] temporalFilters)
            throws DecodingException {

        List<TemporalFilter> sosTemporalFilters = new LinkedList<>();

        for (GetObservation.EventTime temporalFilter : temporalFilters) {
            Object filter = decodeXmlElement(temporalFilter.getTemporalOps());
            if (filter instanceof TemporalFilter) {
                sosTemporalFilters.add((TemporalFilter) filter);
            }
        }
        return sosTemporalFilters;
    }

    private ComparisonFilter parseResultFilter(Result result) throws DecodingException {
        if (result != null && result.getComparisonOps() != null) {
            Object decodeXmlElement = decodeXmlElement(result.getComparisonOps());
            if (decodeXmlElement instanceof ComparisonFilter) {
                return (ComparisonFilter) decodeXmlElement;
            }
        }
        return null;
    }

    private String decodeResponseFormat(String responseFormat) throws DecodingException {
        try {
            // parse responseFormat through MediaType to ensure it's a mime
            // type and eliminate whitespace variations
            return MediaType.normalizeString(URLDecoder.decode(responseFormat,
                                                               StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            throw new DecodingException("Error while decoding response format!", e);
        }
    }

}
