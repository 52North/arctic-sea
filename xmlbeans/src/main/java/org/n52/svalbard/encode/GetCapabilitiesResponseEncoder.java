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

import java.util.Collection;
import java.util.Set;

import net.opengis.sos.x20.CapabilitiesDocument;
import net.opengis.sos.x20.CapabilitiesType;
import net.opengis.sos.x20.CapabilitiesType.Contents;
import net.opengis.sos.x20.ContentsType;
import net.opengis.sos.x20.InsertionCapabilitiesDocument;
import net.opengis.sos.x20.InsertionCapabilitiesType;
import net.opengis.sos.x20.ObservationOfferingType;
import net.opengis.swes.x20.AbstractContentsType.Offering;
import net.opengis.swes.x20.FeatureRelationshipType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.ows.OwsCapabilities;
import org.n52.shetland.ogc.ows.OwsCapabilitiesExtension;
import org.n52.shetland.ogc.ows.OwsOperationsMetadata;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.StringBasedExtension;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesResponse;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosCapabilities;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosInsertionCapabilities;
import org.n52.shetland.ogc.sos.SosObservationOffering;
import org.n52.shetland.ogc.sos.SosOffering;
import org.n52.shetland.ogc.sos.extension.SosObservationOfferingExtension;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GetCapabilitiesResponseEncoder extends AbstractSosResponseEncoder<GetCapabilitiesResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCapabilitiesResponseEncoder.class);
    private static final String ID_FORMAT_STRING = "%s_%d";

    public GetCapabilitiesResponseEncoder() {
        super(SosConstants.Operations.GetCapabilities.name(), GetCapabilitiesResponse.class);
    }

    @Override
    protected XmlObject create(GetCapabilitiesResponse response) throws EncodingException {
        CapabilitiesDocument doc = CapabilitiesDocument.Factory.newInstance(getXmlOptions());
        CapabilitiesType xbCaps = doc.addNewCapabilities();

        if (response.isStatic()) {
            String xml = response.getXmlString();
            LOGGER.trace("Response is static. XML-String:\n{}\n", xml);
            try {
                doc.set(XmlObject.Factory.parse(xml));
                return doc;
            } catch (XmlException ex) {
                throw new EncodingException("Error encoding static capabilities", ex);
            }
        }

        // set version.
        if (response.getCapabilities().getVersion() != null) {
            xbCaps.setVersion(response.getCapabilities().getVersion());
        } else {
            xbCaps.setVersion(response.getVersion());
        }
        encodeServiceIdentification(response.getCapabilities(), xbCaps);
        encodeServiceProvider(response.getCapabilities(), xbCaps);
        encodeOperationsMetadata(response.getCapabilities(), xbCaps);
        if (response.getCapabilities() instanceof SosCapabilities) {
            SosCapabilities caps = (SosCapabilities) response.getCapabilities();
            encodeFilterCapabilities(caps, xbCaps);
            encodeContents(caps, xbCaps, response.getVersion());
            encodeExtensions(caps, xbCaps);
        }
        return doc;
    }

    private void setExtensions(XmlObject xml, OwsCapabilitiesExtension extension) throws EncodingException {
        if (extension instanceof SosInsertionCapabilities) {
            xml.set(createInsertionCapabilities((SosInsertionCapabilities) extension));
        } else if (extension instanceof StringBasedExtension) {
            try {
                xml.set(XmlObject.Factory.parse(((StringBasedExtension) extension).getExtension()));
            } catch (XmlException ex) {
                throw errorEncodingSwesExtension(ex);
            }
        } else {
            throw new EncodingException("The extension element is not supported by this service!");
        }
    }

    private XmlObject createInsertionCapabilities(SosInsertionCapabilities caps) {
        InsertionCapabilitiesDocument doc = InsertionCapabilitiesDocument.Factory.newInstance(getXmlOptions());
        InsertionCapabilitiesType xbCaps = doc.addNewInsertionCapabilities();
        caps.getFeatureOfInterestTypes().stream().filter(foiType -> !foiType.equals(SosConstants.NOT_DEFINED))
                .forEachOrdered(foiType -> xbCaps.addFeatureOfInterestType(foiType));
        caps.getObservationTypes().stream().filter(oType -> !oType.equals(SosConstants.NOT_DEFINED))
                .forEachOrdered(oType -> xbCaps.addObservationType(oType));
        caps.getProcedureDescriptionFormats().stream().filter(pdf -> !pdf.equals(SosConstants.NOT_DEFINED))
                .forEachOrdered(pdf -> xbCaps.addProcedureDescriptionFormat(pdf));
        caps.getSupportedEncodings().stream().filter(se -> !se.equals(SosConstants.NOT_DEFINED))
                .forEachOrdered(se -> xbCaps.addSupportedEncoding(se));
        return doc;
    }

    /**
     * Sets the content section to the Capabilities document.
     *
     * @param xbContents
     *            SOS 2.0 contents section
     * @param offerings
     *            SOS offerings for contents
     * @param version
     *            SOS response version
     *
     *
     * @throws EncodingException
     *             * if an error occurs.
     */
    protected void setContents(Contents xbContents, Collection<SosObservationOffering> offerings, String version)
            throws EncodingException {
        final ContentsType xbContType = xbContents.addNewContents();

        // for gml:id generation
        int offeringCounter = 0;
        for (final SosObservationOffering offering : offerings) {
            if (offering.isValidObservationOffering()) {
                ++offeringCounter;
                encodeObservationOffering(offering, offeringCounter, xbContType);

            }
        }
        // FIXME: change swes:AbstractOffering to sos:ObservationOffering and
        // the namespace prefix ns to sos due to
        // XMLBeans problems with substitution
        // (http://www.mail-archive.com/dev%40xmlbeans.apache.org/msg00962.html).
        renameContentsElementNames(xbContents);
    }

    /**
     * Creates a XML FeatureRelationship for the relatedFeature
     *
     * @param featureRelationship
     *            XML feature relationship
     * @param relatedFeatureTarget
     *            Feature target identifier
     * @param roles
     *            Features role
     */
    private void createRelatedFeature(final FeatureRelationshipType featureRelationship,
            final String relatedFeatureTarget, final Collection<String> roles) {
        featureRelationship.addNewTarget().setHref(relatedFeatureTarget);
        if (roles != null) {
            roles.forEach(featureRelationship::setRole);
        }
    }

    private void renameContentsElementNames(final Contents xbContents) {
        for (final Offering offering : xbContents.getContents().getOfferingArray()) {
            final XmlCursor cursor = offering.getAbstractOffering().newCursor();
            cursor.setName(Sos2Constants.QN_OBSERVATION_OFFERING);
            cursor.removeAttribute(W3CConstants.QN_XSI_TYPE);
            if (cursor.toChild(Sos2Constants.QN_SOS_OBSERVED_AREA)) {
                cursor.setName(Sos2Constants.QN_SOS_OBSERVED_AREA);
                cursor.toParent();
            }
            if (cursor.toChild(Sos2Constants.QN_SOS_PHENOMENON_TIME)) {
                cursor.setName(Sos2Constants.QN_SOS_PHENOMENON_TIME);
                cursor.toParent();
            }
            if (cursor.toChild(Sos2Constants.QN_SOS_RESULT_TIME)) {
                cursor.setName(Sos2Constants.QN_SOS_RESULT_TIME);
                cursor.toParent();
            }
            if (cursor.toChild(Sos2Constants.QN_SOS_RESPONSE_FORMAT)) {
                cursor.setName(Sos2Constants.QN_SOS_RESPONSE_FORMAT);
                while (cursor.toNextSibling(Sos2Constants.QN_SOS_RESPONSE_FORMAT)) {
                    cursor.setName(Sos2Constants.QN_SOS_RESPONSE_FORMAT);
                }
                cursor.toParent();
            }
            if (cursor.toChild(Sos2Constants.QN_SOS_OBSERVATION_TYPE)) {
                cursor.setName(Sos2Constants.QN_SOS_OBSERVATION_TYPE);
                while (cursor.toNextSibling(Sos2Constants.QN_SOS_OBSERVATION_TYPE)) {
                    cursor.setName(Sos2Constants.QN_SOS_OBSERVATION_TYPE);
                }
                cursor.toParent();
            }
            if (cursor.toChild(Sos2Constants.QN_SOS_FEATURE_OF_INTEREST_TYPE)) {
                cursor.setName(Sos2Constants.QN_SOS_FEATURE_OF_INTEREST_TYPE);
                while (cursor.toNextSibling(Sos2Constants.QN_SOS_FEATURE_OF_INTEREST_TYPE)) {
                    cursor.setName(Sos2Constants.QN_SOS_FEATURE_OF_INTEREST_TYPE);
                }
            }
            cursor.dispose();
        }
    }

    @Override
    public Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(Sos2Constants.SOS_GET_CAPABILITIES_SCHEMA_LOCATION);
    }

    private void encodeServiceIdentification(OwsCapabilities caps, CapabilitiesType xbCaps) throws EncodingException {
        if (caps.getServiceIdentification().isPresent()) {
            xbCaps.addNewServiceIdentification().set(encodeOws(caps.getServiceIdentification().get()));
        }
    }

    private void encodeServiceProvider(OwsCapabilities caps, CapabilitiesType xbCaps) throws EncodingException {
        if (caps.getServiceProvider().isPresent()) {
            xbCaps.addNewServiceProvider().set(encodeOws(caps.getServiceProvider().get()));
        }
    }

    private void encodeOperationsMetadata(OwsCapabilities caps, CapabilitiesType xbCaps) throws EncodingException {
        if (caps.getOperationsMetadata().map(OwsOperationsMetadata::getOperations).filter(x -> !x.isEmpty())
                .isPresent()) {
            xbCaps.addNewOperationsMetadata().set(encodeOws(caps.getOperationsMetadata().get()));
        }
    }

    private void encodeFilterCapabilities(SosCapabilities caps, CapabilitiesType xbCaps) throws EncodingException {
        if (caps.getFilterCapabilities().isPresent()) {
            xbCaps.addNewFilterCapabilities().addNewFilterCapabilities()
                    .set(encodeFes(caps.getFilterCapabilities().get()));
        }
    }

    private void encodeContents(SosCapabilities caps, CapabilitiesType xbCaps, String version)
            throws EncodingException {
        if (caps.getContents().isPresent()) {
            setContents(xbCaps.addNewContents(), caps.getContents().get(), version);
        }
    }

    private void encodeExtensions(SosCapabilities caps, CapabilitiesType xbCaps) throws EncodingException {
        for (OwsCapabilitiesExtension e : caps.getExtensions()) {
            setExtensions(xbCaps.addNewExtension(), e);
        }
    }

    private void encodeObservationOffering(SosObservationOffering offering, int offeringCounter,
            ContentsType xbContType) throws EncodingException {
        final ObservationOfferingType xbObsOff = ObservationOfferingType.Factory.newInstance(getXmlOptions());

        SosOffering sosOffering = offering.getOffering();
        xbObsOff.setIdentifier(sosOffering.getIdentifier());
        if (sosOffering.isSetName()) {
            for (CodeType name : sosOffering.getName()) {
                xbObsOff.addNewName().set(encodeObjectToXml(GmlConstants.NS_GML_32, name));
            }
        }
        if (sosOffering.isSetDescription()) {
            xbObsOff.setDescription(sosOffering.getDescription());
        }
        encodeOfferingExtension(offering, xbObsOff);

        offering.getProcedures().forEach(xbObsOff::setProcedure);

        encodeObservableProperties(offering, xbObsOff);
        encodeRelatedFeatures(offering, xbObsOff);
        encodeObservedArea(offering, xbObsOff);
        encodePhenomenonTime(offering, offeringCounter, xbObsOff);
        encodeResultTime(offering, offeringCounter, xbObsOff);
        encodeResponseFormat(offering, xbObsOff);
        encodeObservationType(offering, xbObsOff);
        encodeFeatureOfInterestTypes(offering, xbObsOff);
        encodeProcedureDescriptionFormats(offering, xbObsOff);
        xbContType.addNewOffering().setAbstractOffering(xbObsOff);
        /*
         * Offering addNewOffering = xbContType.addNewOffering();
         * addNewOffering.addNewAbstractOffering().set(xbObsOff); XmlHelper
         * .substituteElement(addNewOffering.getAbstractOffering(), xbObsOff);
         */
    }

    private void encodeOfferingExtension(SosObservationOffering sosOffering, ObservationOfferingType xbObsOff)
            throws EncodingException {
        for (Extension<?> extention : sosOffering.getExtensions().getExtensions()) {
            if (extention.getValue() instanceof SosObservationOfferingExtension) {
                SosObservationOfferingExtension extension = (SosObservationOfferingExtension) extention.getValue();
                try {
                    xbObsOff.addNewExtension().set(XmlObject.Factory.parse(extension.getExtension()));
                } catch (XmlException ex) {
                    throw errorEncodingSwesExtension(ex);
                }
            } else {
                xbObsOff.addNewExtension().set(encodeObjectToXml(extention.getNamespace(), extention));
            }

        }
    }

    private void encodeObservableProperties(SosObservationOffering offering, ObservationOfferingType xbObsOff) {
        // TODO: pdf [0..*]
        // set observableProperties [0..*]
        offering.getObservableProperties().forEach(xbObsOff::addObservableProperty);
    }

    private void encodeRelatedFeatures(SosObservationOffering offering, ObservationOfferingType xbObsOff) {
        // set relatedFeatures [0..*]
        if (offering.isSetRelatedFeature()) {
            offering.getRelatedFeatures().forEach((target, roles) -> createRelatedFeature(
                    xbObsOff.addNewRelatedFeature().addNewFeatureRelationship(), target, roles));
        }
    }

    private void encodeObservedArea(SosObservationOffering offering, ObservationOfferingType xbObsOff)
            throws EncodingException {
        // set observed area [0..1]
        if (offering.isSetObservedArea() && offering.getObservedArea().isSetEnvelope()
                && offering.getObservedArea().isSetSrid()) {
            XmlObject encodeObjectToXml = encodeGml(offering.getObservedArea());
            xbObsOff.addNewObservedArea().addNewEnvelope().set(encodeObjectToXml);
        }
    }

    private void encodePhenomenonTime(SosObservationOffering offering, int offeringCounter,
            ObservationOfferingType xbObsOff) throws EncodingException {
        // set up phenomenon time [0..1]
        if (offering.getPhenomenonTime() instanceof TimePeriod) {
            TimePeriod tp = (TimePeriod) offering.getPhenomenonTime();
            if (!tp.isEmpty()) {
                tp.setGmlId(String.format(ID_FORMAT_STRING, Sos2Constants.EN_PHENOMENON_TIME, offeringCounter));
                XmlObject xmlObject = encodeGml(tp);
                xbObsOff.addNewPhenomenonTime().addNewTimePeriod().set(xmlObject);
                xbObsOff.getPhenomenonTime().substitute(Sos2Constants.QN_SOS_PHENOMENON_TIME,
                        xbObsOff.getPhenomenonTime().schemaType());
            }
        }
    }

    private void encodeResultTime(SosObservationOffering offering, int offeringCounter,
            ObservationOfferingType xbObsOff) throws EncodingException {
        // set resultTime [0..1]
        if (offering.getResultTime() instanceof TimePeriod) {
            TimePeriod tp = (TimePeriod) offering.getResultTime();
            tp.setGmlId(String.format(ID_FORMAT_STRING, Sos2Constants.EN_RESULT_TIME, offeringCounter));
            if (!tp.isEmpty()) {
                XmlObject xmlObject = encodeGml(tp);
                xbObsOff.addNewResultTime().addNewTimePeriod().set(xmlObject);
                xbObsOff.getResultTime().substitute(Sos2Constants.QN_SOS_RESULT_TIME,
                        xbObsOff.getResultTime().schemaType());
            }
        }
    }

    private void encodeResponseFormat(SosObservationOffering offering, ObservationOfferingType xbObsOff) {
        // set responseFormat [0..*]
        if (offering.isSetResponseFormats()) {
            offering.getResponseFormats().forEach(xbObsOff::addResponseFormat);
        }
    }

    private void encodeObservationType(SosObservationOffering offering, ObservationOfferingType xbObsOff) {
        // set observationType [0..*]
        if (offering.isSetObservationTypes()) {
            offering.getObservationTypes().forEach(xbObsOff::addObservationType);
        }
    }

    private void encodeFeatureOfInterestTypes(SosObservationOffering offering, ObservationOfferingType xbObsOff) {
        // set featureOfInterestType [0..1]
        if (offering.isSetFeatureOfInterestTypes()) {
            offering.getFeatureOfInterestTypes().forEach(xbObsOff::addFeatureOfInterestType);
        }
    }

    private void encodeProcedureDescriptionFormats(SosObservationOffering offering, ObservationOfferingType xbObsOff) {
        if (offering.isSetProcedureDescriptionFormats()) {
            offering.getProcedureDescriptionFormats().forEach(xbObsOff::addProcedureDescriptionFormat);
        }
    }

    private static EncodingException errorEncodingSwesExtension(XmlException ex) {
        return new EncodingException("Error encoding SwesExtension", ex);
    }
}
