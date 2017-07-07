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
package org.n52.svalbard.decode;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.opengis.gml.x32.AbstractTimeGeometricPrimitiveType;
import net.opengis.gml.x32.FeaturePropertyType;
import net.opengis.sos.x20.SosInsertionMetadataPropertyType;
import net.opengis.sos.x20.SosInsertionMetadataType;
import net.opengis.swes.x20.DeleteSensorDocument;
import net.opengis.swes.x20.DeleteSensorType;
import net.opengis.swes.x20.DescribeSensorDocument;
import net.opengis.swes.x20.DescribeSensorType;
import net.opengis.swes.x20.InsertSensorDocument;
import net.opengis.swes.x20.InsertSensorType;
import net.opengis.swes.x20.InsertSensorType.Metadata;
import net.opengis.swes.x20.InsertSensorType.RelatedFeature;
import net.opengis.swes.x20.SensorDescriptionType;
import net.opengis.swes.x20.UpdateSensorDescriptionDocument;
import net.opengis.swes.x20.UpdateSensorDescriptionType;
import net.opengis.swes.x20.UpdateSensorDescriptionType.Description;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.ows.service.OwsServiceCommunicationObject;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.Sos2Constants.UpdateSensorDescriptionParams;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosInsertionMetadata;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.ogc.sos.request.DeleteSensorRequest;
import org.n52.shetland.ogc.sos.request.DescribeSensorRequest;
import org.n52.shetland.ogc.sos.request.InsertSensorRequest;
import org.n52.shetland.ogc.sos.request.UpdateSensorRequest;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.ogc.swes.SwesFeatureRelationship;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecoderResponseUnsupportedException;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * @since 1.0.0
 *
 */
public class SwesDecoderv20 extends AbstractSwesDecoderv20<OwsServiceCommunicationObject> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwesDecoderv20.class);

    @SuppressWarnings("unchecked")
    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
            CodingHelper.decoderKeysForElements(SwesConstants.NS_SWES_20, DescribeSensorDocument.class,
                    InsertSensorDocument.class, UpdateSensorDescriptionDocument.class, DeleteSensorDocument.class),
            CodingHelper.xmlDecoderKeysForOperation(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                    SosConstants.Operations.DescribeSensor, Sos2Constants.Operations.InsertSensor,
                    Sos2Constants.Operations.UpdateSensorDescription, Sos2Constants.Operations.DeleteSensor));

    public SwesDecoderv20() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public OwsServiceRequest decode(XmlObject xmlObject) throws DecodingException {
        LOGGER.debug("REQUESTTYPE:" + xmlObject.getClass());
        XmlHelper.validateDocument(xmlObject);
        if (xmlObject instanceof DescribeSensorDocument) {
            return parseDescribeSensor((DescribeSensorDocument) xmlObject);
        } else if (xmlObject instanceof InsertSensorDocument) {
            return parseInsertSensor((InsertSensorDocument) xmlObject);
        } else if (xmlObject instanceof UpdateSensorDescriptionDocument) {
            return parseUpdateSensorDescription((UpdateSensorDescriptionDocument) xmlObject);
        } else if (xmlObject instanceof DeleteSensorDocument) {
            return parseDeleteSensor((DeleteSensorDocument) xmlObject);
        } else {
            throw new UnsupportedDecoderXmlInputException(this, xmlObject);
        }
    }

    /**
     * parses the passes XmlBeans document and creates a SOS describeSensor
     * request
     *
     * @param xbDescSenDoc
     *            XmlBeans document representing the describeSensor request
     * @return Returns SOS describeSensor request
     *
     *
     * @throws DecodingException
     *             * if validation of the request failed
     */
    private OwsServiceRequest parseDescribeSensor(final DescribeSensorDocument xbDescSenDoc) throws DecodingException {
        final DescribeSensorRequest descSensorRequest = new DescribeSensorRequest();
        final DescribeSensorType xbDescSensor = xbDescSenDoc.getDescribeSensor();
        descSensorRequest.setService(xbDescSensor.getService());
        descSensorRequest.setVersion(xbDescSensor.getVersion());
        descSensorRequest.setProcedure(xbDescSensor.getProcedure());
        descSensorRequest.setProcedureDescriptionFormat(xbDescSensor.getProcedureDescriptionFormat());
        if (xbDescSensor.isSetValidTime()) {
            descSensorRequest.setValidTime(getValidTime(xbDescSensor.getValidTime()));
        }
        // extensions
        descSensorRequest.setExtensions(parseExtensibleRequest(xbDescSensor));
        return descSensorRequest;
    }

    private OwsServiceRequest parseInsertSensor(final InsertSensorDocument xbInsSensDoc) throws DecodingException {
        final InsertSensorRequest request = new InsertSensorRequest();
        final InsertSensorType xbInsertSensor = xbInsSensDoc.getInsertSensor();
        request.setService(xbInsertSensor.getService());
        request.setVersion(xbInsertSensor.getVersion());
        // format
        request.setProcedureDescriptionFormat(xbInsertSensor.getProcedureDescriptionFormat());
        // observable properties
        if (CollectionHelper.isNotNullOrEmpty(xbInsertSensor.getObservablePropertyArray())) {
            request.setObservableProperty(Arrays.asList(xbInsertSensor.getObservablePropertyArray()));
        }
        // related features
        if (CollectionHelper.isNotNullOrEmpty(xbInsertSensor.getRelatedFeatureArray())) {
            request.setRelatedFeature(parseRelatedFeature(xbInsertSensor.getRelatedFeatureArray()));
        }
        // metadata
        if (CollectionHelper.isNotNullOrEmpty(xbInsertSensor.getMetadataArray())) {
            request.setMetadata(parseMetadata(xbInsertSensor.getMetadataArray()));
        }
        // extensions
        request.setExtensions(parseExtensibleRequest(xbInsertSensor));
        try {
            final XmlObject xbProcedureDescription = XmlObject.Factory
                    .parse(getNodeFromNodeList(xbInsertSensor.getProcedureDescription().getDomNode().getChildNodes()));

            final Decoder<?, XmlObject> decoder =
                    getDecoder(new XmlNamespaceDecoderKey(xbInsertSensor.getProcedureDescriptionFormat(),
                            xbProcedureDescription.getClass()));
            if (decoder != null) {
                final Object decodedProcedureDescription = decoder.decode(xbProcedureDescription);
                if (decodedProcedureDescription instanceof SosProcedureDescription) {
                    request.setProcedureDescription((SosProcedureDescription) decodedProcedureDescription);
                } else if (decodedProcedureDescription instanceof AbstractFeature) {
                    request.setProcedureDescription(new SosProcedureDescription<AbstractFeature>(
                            (AbstractFeature) decodedProcedureDescription));
                }
            }
        } catch (final XmlException xmle) {
            throw new DecodingException("Error while parsing procedure description of InsertSensor request!", xmle);
        }
        return request;
    }

    private OwsServiceRequest parseDeleteSensor(final DeleteSensorDocument xbDelSenDoc) throws DecodingException {
        final DeleteSensorRequest request = new DeleteSensorRequest();
        DeleteSensorType deleteSensor = xbDelSenDoc.getDeleteSensor();
        request.setService(deleteSensor.getService());
        request.setVersion(deleteSensor.getVersion());
        request.setProcedureIdentifier(deleteSensor.getProcedure());
        // extensions
        request.setExtensions(parseExtensibleRequest(deleteSensor));
        return request;
    }

    /**
     * parses the Xmlbeans UpdateSensorDescription document to a SOS request.
     *
     * @param xbUpSenDoc
     *            UpdateSensorDescription document
     * @return SOS UpdateSensor request
     *
     *
     * @throws DecodingException
     *             * if an error occurs.
     */
    private OwsServiceRequest parseUpdateSensorDescription(final UpdateSensorDescriptionDocument xbUpSenDoc)
            throws DecodingException {
        final UpdateSensorRequest request = new UpdateSensorRequest();
        final UpdateSensorDescriptionType xbUpdateSensor = xbUpSenDoc.getUpdateSensorDescription();
        request.setService(xbUpdateSensor.getService());
        request.setVersion(xbUpdateSensor.getVersion());
        request.setProcedureIdentifier(xbUpdateSensor.getProcedure());
        request.setProcedureDescriptionFormat(xbUpdateSensor.getProcedureDescriptionFormat());
        // extensions
        request.setExtensions(parseExtensibleRequest(xbUpdateSensor));
        for (final Description description : xbUpdateSensor.getDescriptionArray()) {
            SensorDescriptionType sensorDescription = description.getSensorDescription();

            try {
                // TODO exception if valid time is set
                final XmlObject xmlObject = XmlObject.Factory
                        .parse(getNodeFromNodeList(sensorDescription.getData().getDomNode().getChildNodes()));
                Decoder<?, XmlObject> decoder = getDecoder(getDecoderKey(xmlObject));
                if (decoder == null) {
                    throw new DecodingException(UpdateSensorDescriptionParams.procedureDescriptionFormat,
                            "The requested procedureDescritpionFormat is not supported!");
                }

                final Object decodedObject = decoder.decode(xmlObject);

                SosProcedureDescription<?> sosProcedureDescription = null;
                if (decodedObject instanceof SosProcedureDescription) {
                    sosProcedureDescription = (SosProcedureDescription) decodedObject;
                } else if (decodedObject instanceof AbstractFeature) {
                    sosProcedureDescription =
                            new SosProcedureDescription<AbstractFeature>((AbstractFeature) decodedObject);
                }

                if (sensorDescription.isSetValidTime()) {
                    sosProcedureDescription.setValidTime(getValidTime(sensorDescription.getValidTime()));
                }
                request.addProcedureDescriptionString(sosProcedureDescription);
            } catch (final XmlException xmle) {
                throw new DecodingException("Error while parsing procedure description of UpdateSensor request!",
                        xmle);
            }
        }
        return request;
    }

    private SosInsertionMetadata parseMetadata(final Metadata[] metadataArray) throws DecodingException {

        final SosInsertionMetadata sosMetadata = new SosInsertionMetadata();
        try {
            for (final Metadata metadata : metadataArray) {
                SosInsertionMetadataType xbSosInsertionMetadata = null;
                if (metadata.getInsertionMetadata() != null
                        && metadata.getInsertionMetadata().schemaType() == SosInsertionMetadataType.type) {
                    xbSosInsertionMetadata = (SosInsertionMetadataType) metadata.getInsertionMetadata();
                } else {
                    if (metadata.getDomNode().hasChildNodes()) {
                        final Node node = getNodeFromNodeList(metadata.getDomNode().getChildNodes());
                        final SosInsertionMetadataPropertyType xbMetadata =
                                SosInsertionMetadataPropertyType.Factory.parse(node);
                        xbSosInsertionMetadata = xbMetadata.getSosInsertionMetadata();
                    }
                }
                if (xbSosInsertionMetadata != null) {
                    // featureOfInterest types
                    if (xbSosInsertionMetadata.getFeatureOfInterestTypeArray() != null) {
                        sosMetadata.setFeatureOfInterestTypes(
                                Arrays.asList(xbSosInsertionMetadata.getFeatureOfInterestTypeArray()));
                    }
                    // observation types
                    if (xbSosInsertionMetadata.getObservationTypeArray() != null) {
                        sosMetadata
                                .setObservationTypes(Arrays.asList(xbSosInsertionMetadata.getObservationTypeArray()));
                    }
                }
            }
        } catch (final XmlException xmle) {
            throw new DecodingException("An error occurred while parsing the metadata in the http post request", xmle);
        }
        return sosMetadata;
    }

    private List<SwesFeatureRelationship> parseRelatedFeature(final RelatedFeature[] relatedFeatureArray)
            throws DecodingException {
        List<SwesFeatureRelationship> sosRelatedFeatures = new ArrayList<>(relatedFeatureArray.length);
        for (final RelatedFeature relatedFeature : relatedFeatureArray) {
            final SwesFeatureRelationship sosFeatureRelationship = new SwesFeatureRelationship();

            final FeaturePropertyType fpt = relatedFeature.getFeatureRelationship().getTarget();
            if (fpt.getHref() != null && !fpt.getHref().isEmpty()) {
                final String identifier = fpt.getHref();
                final AbstractSamplingFeature feature = new SamplingFeature(new CodeWithAuthority(identifier));
                if (fpt.getTitle() != null && !fpt.getTitle().isEmpty()) {
                    feature.setName(Lists.newArrayList(new CodeType(fpt.getTitle())));
                }
                if (checkForRequestUrl(fpt.getHref())) {
                    feature.setUrl(fpt.getHref());
                }
                feature.setFeatureType(OGCConstants.UNKNOWN);
                sosFeatureRelationship.setFeature(feature);
            } else {
                final Object decodedObject = decodeXmlElement(fpt);
                if (decodedObject instanceof AbstractSamplingFeature) {
                    sosFeatureRelationship.setFeature((AbstractSamplingFeature) decodedObject);
                } else {
                    throw new DecoderResponseUnsupportedException(fpt.xmlText(), decodedObject);
                }
            }
            sosFeatureRelationship.setRole(relatedFeature.getFeatureRelationship().getRole());
            sosRelatedFeatures.add(sosFeatureRelationship);
        }
        return sosRelatedFeatures;
    }

    private boolean checkForRequestUrl(final String href) {
        return href.toLowerCase(Locale.ROOT).contains("request=");
    }

    private Node getNodeFromNodeList(final NodeList nodeList) {
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    return nodeList.item(i);
                }
            }
        }
        return null;
    }

    private Time getValidTime(net.opengis.swes.x20.DescribeSensorType.ValidTime validTime) throws DecodingException {
        Object decodeXmlElement = decodeXmlElement(validTime.getAbstractTimeGeometricPrimitive());
        if (decodeXmlElement instanceof Time) {
            return (Time) decodeXmlElement;
        } else {
            throw unsupportedValidTime(Sos2Constants.DescribeSensorParams.validTime,
                                       validTime.getAbstractTimeGeometricPrimitive());
        }
    }

    private Time getValidTime(net.opengis.swes.x20.SensorDescriptionType.ValidTime validTime)
            throws DecodingException {
        Object decodeXmlElement = decodeXmlElement(validTime.getAbstractTimeGeometricPrimitive());
        if (decodeXmlElement instanceof Time) {
            return (Time) decodeXmlElement;
        } else {
            throw unsupportedValidTime(Sos2Constants.UpdateSensorDescriptionParams.validTime,
                                       validTime.getAbstractTimeGeometricPrimitive());
        }
    }

    private static DecodingException unsupportedValidTime(Enum<?> parameter,
                                                          AbstractTimeGeometricPrimitiveType validTime) {
        return new DecodingException(parameter,
                                     "The validTime element ({}) is not supported",
                                     validTime.schemaType());
    }
}
