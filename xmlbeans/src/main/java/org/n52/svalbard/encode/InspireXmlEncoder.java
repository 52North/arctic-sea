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
import java.util.Set;
import java.util.function.Supplier;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.inspire.InspireConstants;
import org.n52.shetland.inspire.InspireObject;
import org.n52.shetland.inspire.InspireSupportedCRS;
import org.n52.shetland.inspire.InspireSupportedLanguages;
import org.n52.shetland.inspire.dls.FullInspireExtendedCapabilities;
import org.n52.shetland.inspire.dls.InspireExtendedCapabilities;
import org.n52.shetland.inspire.dls.MinimalInspireExtendedCapabilities;
import org.n52.shetland.ogc.swes.SwesExtension;
import org.n52.shetland.util.DateTimeFormatException;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.write.InspireXmlStreamWriter;

import com.google.common.collect.Sets;

/**
 * XML encoder class for the INSPIRE schema
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class InspireXmlEncoder extends AbstractXmlEncoder<XmlObject, Object> {

    private static final Set<EncoderKey> ENCODER_KEYS = Sets.union(
            CodingHelper.encoderKeysForElements(InspireConstants.NS_INSPIRE_DLS,
                                                InspireExtendedCapabilities.class),
            CodingHelper.encoderKeysForElements(InspireConstants.NS_INSPIRE_COMMON,
                                                SwesExtension.class,
                                                InspireSupportedLanguages.class,
                                                InspireSupportedCRS.class));

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public XmlObject encode(Object objectToEncode) throws EncodingException {
        return encode(objectToEncode, EncodingContext.empty());
    }

    @Override
    public XmlObject encode(Object objectToEncode, EncodingContext ctx)
            throws EncodingException {
        if (objectToEncode instanceof InspireObject) {
            return encodeObject((InspireObject) objectToEncode, ctx);
        } else if (objectToEncode instanceof SwesExtension<?>) {
            SwesExtension<?> swesExtension = (SwesExtension<?>) objectToEncode;
            if (swesExtension.getValue() instanceof InspireObject) {
                return encodeObject((InspireObject) swesExtension.getValue(), ctx);
            }
        }
        throw new UnsupportedEncoderInputException(this, objectToEncode);
    }

    private XmlObject encodeObject(InspireObject objectToEncode, EncodingContext ctx) throws EncodingException {
        try {
            checkIfSupported(objectToEncode);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            EncodingContext context = ctx.with(EncoderFlags.ENCODER_REPOSITORY, getEncoderRepository())
                    .with(XmlEncoderFlags.XML_OPTIONS, (Supplier<XmlOptions>) this::getXmlOptions);
            new InspireXmlStreamWriter(context, out, objectToEncode).write();
            String s = out.toString("UTF8");
            return XmlObject.Factory.parse(s);
        } catch (XMLStreamException | DateTimeFormatException | XmlException | UnsupportedEncodingException ex) {
            throw new EncodingException("Error encoding Inspire extended capabilities!", ex);
        }
    }

    private void checkIfSupported(InspireObject objectToEncode) throws EncodingException {
        if (!(objectToEncode instanceof InspireSupportedLanguages) &&
            !(objectToEncode instanceof InspireSupportedCRS) &&
            !(objectToEncode instanceof FullInspireExtendedCapabilities) &&
            !(objectToEncode instanceof MinimalInspireExtendedCapabilities)) {
            throw new UnsupportedEncoderInputException(this, objectToEncode);
        }
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(InspireConstants.INSPIRE_COMMON_10_SCHEMA_LOCATION,
                               InspireConstants.INSPIRE_DLS_10_SCHEMA_LOCATION);
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(InspireConstants.NS_INSPIRE_COMMON, InspireConstants.NS_INSPIRE_COMMON_PREFIX);
        nameSpacePrefixMap.put(InspireConstants.NS_INSPIRE_DLS, InspireConstants.NS_INSPIRE_DLS_PREFIX);
    }

    @Override
    public MediaType getContentType() {
        return MediaTypes.TEXT_XML;
    }

    // private XmlObject createMinimalInspireExtendedCapabilities(
    // MinimalInspireExtendedCapabilities sosInspireExtendedCapabilities) throws
    // OwsExceptionReport {
    // try {
    // ExtendedCapabilitiesType extendedCapabilitiesType =
    // createExtendedCapabilitiesType();
    // List<JAXBElement<?>> content = extendedCapabilitiesType.getContent();
    // if (!sosInspireExtendedCapabilities.isSetMetadataUrl()) {
    //
    // }
    // addMetadataUrl(sosInspireExtendedCapabilities.getMetadataUrl(), content);
    // addSupportedLanguages(sosInspireExtendedCapabilities, content);
    // addResponseLanguage(sosInspireExtendedCapabilities, content);
    // addSpatialDataSetIdentifier(sosInspireExtendedCapabilities.getSpatialDataSetIdentifier(),
    // content);
    // return
    // convertDocumentToXmlObject(createInspireExtendedCapabilities(extendedCapabilitiesType));
    // } catch (MalformedURLException mue) {
    // throw new
    // NoApplicableCodeException().causedBy(mue).withMessage("Error while createing XML document!");
    // } catch (SAXException saxe) {
    // throw new
    // NoApplicableCodeException().causedBy(saxe).withMessage("Error while createing XML document!");
    // }
    // }
    //
    // private XmlObject createFullInspireExtendedCapabilities(
    // FullInspireExtendedCapabilities sosInspireExtendedCapabilities) throws
    // OwsExceptionReport {
    // try {
    // ExtendedCapabilitiesType extendedCapabilitiesType =
    // createExtendedCapabilitiesType();
    // List<JAXBElement<?>> content = extendedCapabilitiesType.getContent();
    // addResourceLocator(sosInspireExtendedCapabilities, content);
    // addResourceType(sosInspireExtendedCapabilities, content);
    // addTemporalReference(sosInspireExtendedCapabilities, content);
    // // addConformity(sosInspireExtendedCapabilities, content);
    // addMetadataPointOfContact(sosInspireExtendedCapabilities, content);
    // addMetadataDate(sosInspireExtendedCapabilities, content);
    // addSpatialDataServiceType(sosInspireExtendedCapabilities, content);
    // addMandatoryKeyword(sosInspireExtendedCapabilities, content);
    // addKeyword(sosInspireExtendedCapabilities, content);
    // addSupportedLanguages(sosInspireExtendedCapabilities, content);
    // addResponseLanguage(sosInspireExtendedCapabilities, content);
    // if (sosInspireExtendedCapabilities.isSetMetadataUrl()) {
    // addMetadataUrl(sosInspireExtendedCapabilities.getMetadataUrl(), content);
    // }
    // addConformity(sosInspireExtendedCapabilities, content);
    // return
    // convertDocumentToXmlObject(createInspireExtendedCapabilities(extendedCapabilitiesType));
    // } catch (MalformedURLException mue) {
    // throw new
    // NoApplicableCodeException().causedBy(mue).withMessage("Error while createing XML document!");
    // } catch (SAXException saxe) {
    // throw new
    // NoApplicableCodeException().causedBy(saxe).withMessage("Error while createing XML document!");
    // }
    // }
    //
    // private void addResourceLocator(FullInspireExtendedCapabilities
    // sosInspireExtendedCapabilities,
    // List<JAXBElement<?>> content) throws CodedException {
    // if (!sosInspireExtendedCapabilities.isSetResourceLocators()) {
    // throw new
    // NoApplicableCodeException().withMessage("Required resource locator is missing!");
    // }
    // // TODO different languages
    // // ResLocInspireInteroperabilityRegulationGer
    // // createResLocInspireInteroperabilityRegulationGer =
    // //
    // objectFactoryInspireCommon.createResLocInspireInteroperabilityRegulationGer();
    // List<InspireResourceLocator> sosInspireResourceLocators =
    // sosInspireExtendedCapabilities.getResourceLocator();
    // for (InspireResourceLocator sosInspireResourceLocator :
    // sosInspireResourceLocators) {
    // content.add(objectFactoryInspireCommon
    // .createExtendedCapabilitiesTypeResourceLocator(createResourceLocatorType(sosInspireResourceLocator)));
    // }
    // }
    //
    // private void addResourceType(FullInspireExtendedCapabilities
    // sosInspireExtendedCapabilities,
    // List<JAXBElement<?>> content) throws CodedException {
    // if (!sosInspireExtendedCapabilities.isSetResourceType()) {
    // throw new
    // NoApplicableCodeException().withMessage("Required resource type is missing!");
    // }
    // content.add(objectFactoryInspireCommon
    // .createExtendedCapabilitiesTypeResourceType(ServiceSpatialDataResourceType.fromValue(ResourceType
    // .fromValue(sosInspireExtendedCapabilities.getResourceType().name()))));
    // }
    //
    // private void addTemporalReference(FullInspireExtendedCapabilities
    // sosInspireExtendedCapabilities,
    // List<JAXBElement<?>> content) throws CodedException {
    // if (!sosInspireExtendedCapabilities.isSetTemporalReferences()) {
    // throw new
    // NoApplicableCodeException().withMessage("Required temporal reference is missing!");
    // }
    // for (InspireTemporalReference sosTemporalReference :
    // sosInspireExtendedCapabilities.getTemporalReferences()) {
    // TemporalReference temporalReference =
    // objectFactoryInspireCommon.createTemporalReference();
    // if (sosTemporalReference.isSetDateOfCreation()) {
    // temporalReference.setDateOfCreation(DateTimeHelper.formatDateTime2String(sosTemporalReference
    // .getDateOfCreation().getTimePosition()));
    // }
    // if (sosTemporalReference.isSetDateOfLastRevision()) {
    // temporalReference.setDateOfLastRevision(DateTimeHelper.formatDateTime2String(sosTemporalReference
    // .getDateOfLastRevision().getTimePosition()));
    // }
    // if (sosTemporalReference.isSetDatesOfPublication()) {
    // for (TimeInstant dateOfPublication :
    // sosTemporalReference.getDatesOfPublication()) {
    // temporalReference.getDateOfPublication().add(
    // DateTimeHelper.formatDateTime2String(dateOfPublication.getTimePosition()));
    // }
    // }
    // if (sosTemporalReference.isSetTemporalExtents()) {
    // List<TemporalExtent> temporalExtents =
    // temporalReference.getTemporalExtent();
    // for (Time sosTemporalExtent : sosTemporalReference.getTemporalExtents())
    // {
    // TemporalExtent temporalExtent =
    // objectFactoryInspireCommon.createTemporalExtent();
    // if (sosTemporalExtent instanceof TimeInstant) {
    // JAXBElement<String> individualDate =
    // objectFactoryInspireCommon.createIndividualDate(DateTimeHelper
    // .formatDateTime2String(((TimeInstant)
    // sosTemporalExtent).getTimePosition()));
    // temporalExtent.getTemporalExtentElement().add(individualDate);
    // } else if (sosTemporalExtent instanceof TimePeriod) {
    // IntervalOfDates intervalOfDates =
    // objectFactoryInspireCommon.createIntervalOfDates();
    // TimePeriod timePeriod = (TimePeriod) sosTemporalExtent;
    // intervalOfDates.setStartingDate(DateTimeHelper.formatDateTime2String(timePeriod
    // .getStartTimePosition()));
    // intervalOfDates.setEndDate(DateTimeHelper.formatDateTime2String(timePeriod
    // .getEndTimePosition()));
    // JAXBElement<IntervalOfDates> intervalDate =
    // objectFactoryInspireCommon.createIntervalOfDates(intervalOfDates);
    // temporalExtent.getTemporalExtentElement().add(intervalDate);
    // }
    // temporalExtents.add(temporalExtent);
    // }
    // }
    // content.add(objectFactoryInspireCommon.createExtendedCapabilitiesTypeTemporalReference(temporalReference));
    // }
    // }
    //
    // private void addConformity(FullInspireExtendedCapabilities
    // sosInspireExtendedCapabilities,
    // List<JAXBElement<?>> content) throws CodedException {
    // if (!sosInspireExtendedCapabilities.isSetConformity()) {
    // throw new
    // NoApplicableCodeException().withMessage("Required conformity is missing!");
    // }
    // for (InspireConformity sosConformity :
    // sosInspireExtendedCapabilities.getConformity()) {
    // Conformity conformity = objectFactoryInspireCommon.createConformity();
    // conformity.setSpecification(createCitationConformity(sosConformity.getInspireSpecification()));
    // conformity.setDegree(DegreeOfConformity.fromValue(sosConformity.getInspireDegreeOfConformity().name()));
    // content.add(objectFactoryInspireCommon.createExtendedCapabilitiesTypeConformity(conformity));
    // }
    // }
    //
    // private void addMetadataPointOfContact(FullInspireExtendedCapabilities
    // sosInspireExtendedCapabilities,
    // List<JAXBElement<?>> content) throws CodedException {
    // if (!sosInspireExtendedCapabilities.isSetMetadataPointOfContact()) {
    // throw new
    // NoApplicableCodeException().withMessage("Required metadata point of contact is missing!");
    // }
    // for (InspireMetadataPointOfContact sosMetadataPointOfContact :
    // sosInspireExtendedCapabilities
    // .getMetadataPointOfContacts()) {
    // MetadataPointOfContact metadataPointOfContact =
    // objectFactoryInspireCommon.createMetadataPointOfContact();
    // metadataPointOfContact.setOrganisationName(sosMetadataPointOfContact.getOrganisationName());
    // metadataPointOfContact.setEmailAddress(sosMetadataPointOfContact.getEmailAddress());
    // content.add(objectFactoryInspireCommon
    // .createExtendedCapabilitiesTypeMetadataPointOfContact(metadataPointOfContact));
    // }
    // }
    //
    // private void addMetadataDate(FullInspireExtendedCapabilities
    // sosInspireExtendedCapabilities,
    // List<JAXBElement<?>> content) throws CodedException {
    // if (!sosInspireExtendedCapabilities.isSetMetadataDate()) {
    // throw new
    // NoApplicableCodeException().withMessage("Required metadata date is missing!");
    // }
    // content.add(objectFactoryInspireCommon.createExtendedCapabilitiesTypeMetadataDate(DateTimeHelper
    // .formatDateTime2String(sosInspireExtendedCapabilities.getMetadataDate().getTimePosition())));
    // }
    //
    // private void addSpatialDataServiceType(FullInspireExtendedCapabilities
    // sosInspireExtendedCapabilities,
    // List<JAXBElement<?>> content) throws CodedException {
    // if (!sosInspireExtendedCapabilities.isSetSpatialDataServiceType()) {
    // throw new
    // NoApplicableCodeException().withMessage("Required spatial data service type is missing!");
    // }
    // content.add(objectFactoryInspireCommon
    // .createExtendedCapabilitiesTypeSpatialDataServiceType(SpatialDataServiceType
    // .fromValue(sosInspireExtendedCapabilities.getSpatialDataServiceType().name())));
    // }
    //
    // private void addMandatoryKeyword(FullInspireExtendedCapabilities
    // sosInspireExtendedCapabilities,
    // List<JAXBElement<?>> content) throws CodedException {
    // if (!sosInspireExtendedCapabilities.isSetMandatoryKeyword()) {
    // throw new
    // NoApplicableCodeException().withMessage("Required mandatory keyword is missing!");
    // }
    // for (InspireMandatoryKeyword sosMandatoryKeyword :
    // sosInspireExtendedCapabilities.getMandatoryKeywords()) {
    // ClassificationOfSpatialDataService classificationOfSpatialDataService =
    // objectFactoryInspireCommon.createClassificationOfSpatialDataService();
    //
    // addOriginatingControlledVocabularyToKeyword(classificationOfSpatialDataService,
    // sosMandatoryKeyword);
    // classificationOfSpatialDataService.setKeywordValue(sosMandatoryKeyword.getKeywordValue().name());
    // content.add(objectFactoryInspireCommon
    // .createExtendedCapabilitiesTypeMandatoryKeyword(classificationOfSpatialDataService));
    // }
    // }
    //
    // private void addKeyword(FullInspireExtendedCapabilities
    // sosInspireExtendedCapabilities,
    // List<JAXBElement<?>> content) throws DateTimeFormatException {
    // if (sosInspireExtendedCapabilities.isSetKeywords()) {
    // for (org.n52.sos.inspire.InspireKeyword sosKeyword :
    // sosInspireExtendedCapabilities.getKeywords()) {
    // InspireKeyword inspireKeyword =
    // objectFactoryInspireCommon.createInspireKeyword();
    // addOriginatingControlledVocabularyToKeyword(inspireKeyword, sosKeyword);
    // inspireKeyword.setKeywordValue(sosKeyword.getKeywordValue());
    // content.add(objectFactoryInspireCommon.createExtendedCapabilitiesTypeKeyword(inspireKeyword));
    // }
    // }
    // }
    //
    // private void addOriginatingControlledVocabularyToKeyword(InspireKeyword
    // inspireKeyword,
    // AbstractInspireKeyword<?> sosKeyword) throws DateTimeFormatException {
    // if (sosKeyword.isSetOriginatingControlledVocabulary()) {
    // inspireKeyword.setOriginatingControlledVocabulary(createOriginatingControlledVocabulary(sosKeyword
    // .getOriginatingControlledVocabulary()));
    // }
    // }
    //
    // private void
    // addSupportedLanguages(InspireExtendedCapabilitiesSupportedLanguage
    // sosInspireExtendedCapabilities,
    // List<JAXBElement<?>> content) throws CodedException {
    // if (!sosInspireExtendedCapabilities.isSetSupportedLanguages()) {
    // throw new
    // NoApplicableCodeException().withMessage("Required supported languages is missing!");
    // }
    // InspireSupportedLanguages sosSupportedLanguages =
    // sosInspireExtendedCapabilities.getSupportedLanguages();
    // SupportedLanguagesType supportedLanguagesType =
    // objectFactoryInspireCommon.createSupportedLanguagesType();
    // supportedLanguagesType.setDefaultLanguage(createLanguageElementISO6392B(sosSupportedLanguages
    // .getDefaultLanguage()));
    // if (sosSupportedLanguages.isSetSupportedLanguages()) {
    // for (InspireLanguageISO6392B sosLanguage :
    // sosSupportedLanguages.getSupportedLanguages()) {
    // supportedLanguagesType.getSupportedLanguage().add(createLanguageElementISO6392B(sosLanguage));
    // }
    // }
    // content.add(objectFactoryInspireCommon
    // .createExtendedCapabilitiesTypeSupportedLanguages(supportedLanguagesType));
    // }
    //
    // private void
    // addResponseLanguage(InspireExtendedCapabilitiesResponseLanguage
    // sosInspireExtendedCapabilities,
    // List<JAXBElement<?>> content) throws CodedException {
    // if (!sosInspireExtendedCapabilities.isSetResponseLanguage()) {
    // throw new
    // NoApplicableCodeException().withMessage("Required response languages is missing!");
    // }
    // content.add(objectFactoryInspireCommon
    // .createExtendedCapabilitiesTypeResponseLanguage(createLanguageElementISO6392B(sosInspireExtendedCapabilities
    // .getResponseLanguage())));
    // }
    //
    // private void
    // addSpatialDataSetIdentifier(Set<InspireUniqueResourceIdentifier>
    // inspireUniqueResourceIdentifier,
    // List<JAXBElement<?>> content) {
    // for (InspireUniqueResourceIdentifier uniqueResourceIdentifier :
    // inspireUniqueResourceIdentifier) {
    // UniqueResourceIdentifier createUniqueResourceIdentifier =
    // objectFactoryInspireCommon.createUniqueResourceIdentifier();
    // createUniqueResourceIdentifier.setCode(uniqueResourceIdentifier.getCode());
    // if (uniqueResourceIdentifier.isSetNamespace()) {
    // createUniqueResourceIdentifier.setNamespace((uniqueResourceIdentifier.getNamespace()));
    // }
    // if (uniqueResourceIdentifier.isSetMetadataUrl()) {
    // createUniqueResourceIdentifier.setMetadataURL(uniqueResourceIdentifier.getMetadataUrl());
    // }
    // content.add(extendedObjectFactoryInspireDls
    // .createInspireExtendedCapabilitiesTypeSpatialDataSetIdentifier(createUniqueResourceIdentifier));
    // }
    // }
    //
    // private void addMetadataUrl(InspireResourceLocator
    // inspireResourceLocator, List<JAXBElement<?>> content) {
    // content.add(objectFactoryInspireCommon
    // .createExtendedCapabilitiesTypeMetadataUrl(createResourceLocatorType(inspireResourceLocator)));
    // }
    //
    // private eu.europa.ec.inspire.schemas.common._1.MediaType
    // getMediaType(MediaType mediaType) {
    // return
    // eu.europa.ec.inspire.schemas.common._1.MediaType.fromValue(mediaType.toString());
    // }
    //
    // private XmlObject convertDocumentToXmlObject(JAXBElement<?> document)
    // throws OwsExceptionReport,
    // MalformedURLException, SAXException {
    // try {
    // Class<?> clazz = document.getValue().getClass();
    // JAXBContext context =
    // JAXBContext.newInstance(clazz.getPackage().getName());
    // SchemaFactory sf = SchemaFactory.newInstance(W3CConstants.NS_XS);
    // sf.newSchema();
    // Schema schema = sf.newSchema(new
    // URL(InspireConstants.SCHEMA_LOCATION_URL_INSPIRE_DLS));
    // Marshaller m = context.createMarshaller();
    // m.setSchema(schema);
    // m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    // final StringWriter writer = new StringWriter();
    // m.marshal(document, writer);
    // return XmlObject.Factory.parse(writer.toString());
    // } catch (JAXBException jaxbe) {
    // throw new
    // NoApplicableCodeException().causedBy(jaxbe).withMessage("Error while marshalling JAXBContext!");
    // } catch (XmlException xmle) {
    // throw new NoApplicableCodeException().causedBy(xmle).withMessage(
    // "Error while converting JAXBContext object to XmlObject!");
    // }
    // }
    //
    // private ResourceLocatorType
    // createResourceLocatorType(InspireResourceLocator sosResourceLocator) {
    // ResourceLocatorType resourceLocator =
    // objectFactoryInspireCommon.createResourceLocatorType();
    // if (sosResourceLocator.isSetUrl()) {
    // resourceLocator.setURL(sosResourceLocator.getURL());
    // }
    // if (sosResourceLocator.isSetMediaTypes()) {
    // for (MediaType mediaType : sosResourceLocator.getMediaTypes()) {
    // resourceLocator.getMediaType().add(getMediaType(mediaType));
    // }
    // }
    // return resourceLocator;
    // }
    //
    // private void addValuesToCitation(Citation citation, InspireCitation
    // sosInspireCitation)
    // throws DateTimeFormatException {
    // citation.setTitle(sosInspireCitation.getTitle());
    // TimeInstant dateOf = (TimeInstant) sosInspireCitation.getDateOf();
    // if (sosInspireCitation.getDateOf() instanceof InspireDateOfCreation) {
    // citation.setDateOfCreation(DateTimeHelper.formatDateTime2String(dateOf.getTimePosition()));
    // } else if (sosInspireCitation.getDateOf() instanceof
    // InspireDateOfLastRevision) {
    // citation.setDateOfLastRevision(DateTimeHelper.formatDateTime2String(dateOf.getTimePosition()));
    // } else if (sosInspireCitation.getDateOf() instanceof
    // InspireDateOfPublication) {
    // citation.setDateOfPublication(DateTimeHelper.formatDateTime2String(dateOf.getTimePosition()));
    // }
    // if (sosInspireCitation.isSetUrls()) {
    // for (String url : sosInspireCitation.getUrls()) {
    // citation.getURI().add(url);
    // }
    // }
    // if (sosInspireCitation.isSetResourceLocators()) {
    // for (InspireResourceLocator sosResourceLocator :
    // sosInspireCitation.getResourceLocator()) {
    // citation.getResourceLocator().add(createResourceLocatorType(sosResourceLocator));
    // }
    // }
    // }
    //
    // private CitationConformity createCitationConformity(InspireCitation
    // sosInspireSpecification)
    // throws DateTimeFormatException {
    // CitationConformity citationConformity =
    // objectFactoryInspireCommon.createCitationConformity();
    // addValuesToCitation(citationConformity, sosInspireSpecification);
    // return citationConformity;
    // }
    //
    // private OriginatingControlledVocabulary
    // createOriginatingControlledVocabulary(
    // InspireOriginatingControlledVocabulary
    // sosOriginatingControlledVocabulary) throws DateTimeFormatException {
    // OriginatingControlledVocabulary originatingControlledVocabulary =
    // objectFactoryInspireCommon.createOriginatingControlledVocabulary();
    // addValuesToCitation(originatingControlledVocabulary,
    // sosOriginatingControlledVocabulary);
    // return originatingControlledVocabulary;
    // }
    //
    // private LanguageElementISO6392B
    // createLanguageElementISO6392B(InspireLanguageISO6392B sosLanguage) {
    // LanguageElementISO6392B languageElementISO6392B =
    // objectFactoryInspireCommon.createLanguageElementISO6392B();
    // languageElementISO6392B.setLanguage(sosLanguage.value());
    // return languageElementISO6392B;
    // }
}
