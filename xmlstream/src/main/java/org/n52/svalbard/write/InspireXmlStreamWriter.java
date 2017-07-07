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
package org.n52.svalbard.write;

import java.io.OutputStream;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.n52.janmayen.http.MediaType;
import org.n52.shetland.inspire.InspireCitation;
import org.n52.shetland.inspire.InspireConformity;
import org.n52.shetland.inspire.InspireConformity.InspireDegreeOfConformity;
import org.n52.shetland.inspire.InspireConformityCitation;
import org.n52.shetland.inspire.InspireConstants;
import org.n52.shetland.inspire.InspireDateOf;
import org.n52.shetland.inspire.InspireDateOfCreation;
import org.n52.shetland.inspire.InspireDateOfLastRevision;
import org.n52.shetland.inspire.InspireDateOfPublication;
import org.n52.shetland.inspire.InspireKeyword;
import org.n52.shetland.inspire.InspireLanguageISO6392B;
import org.n52.shetland.inspire.InspireMandatoryKeyword;
import org.n52.shetland.inspire.InspireMandatoryKeywordValue;
import org.n52.shetland.inspire.InspireMetadataPointOfContact;
import org.n52.shetland.inspire.InspireObject;
import org.n52.shetland.inspire.InspireOriginatingControlledVocabulary;
import org.n52.shetland.inspire.InspireResourceLocator;
import org.n52.shetland.inspire.InspireSupportedCRS;
import org.n52.shetland.inspire.InspireSupportedLanguages;
import org.n52.shetland.inspire.InspireTemporalReference;
import org.n52.shetland.inspire.InspireUniqueResourceIdentifier;
import org.n52.shetland.inspire.dls.FullInspireExtendedCapabilities;
import org.n52.shetland.inspire.dls.InspireCapabilities.InspireServiceSpatialDataResourceType;
import org.n52.shetland.inspire.dls.InspireCapabilities.InspireSpatialDataServiceType;
import org.n52.shetland.inspire.dls.MinimalInspireExtendedCapabilities;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.gml.time.TimePosition;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.html.HtmlEscapers;

/**
 * XML stream writer for INSPIRE DLS ExtendedCapabilities
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class InspireXmlStreamWriter extends XmlStreamWriter<InspireObject> implements InspireConstants {
    public InspireXmlStreamWriter(
            EncodingContext context,
            OutputStream outputStream,
            InspireObject element)
            throws XMLStreamException {
        super(context, outputStream, element);
    }

    @Override
    public void write() throws XMLStreamException, EncodingException {
        if (getElement() instanceof FullInspireExtendedCapabilities) {
            writeFullInspireExtendedCapabilities((FullInspireExtendedCapabilities) getElement());
        } else if (getElement() instanceof MinimalInspireExtendedCapabilities) {
            writeMinimlaInspireExtendedCapabilities((MinimalInspireExtendedCapabilities) getElement());
        } else if (getElement() instanceof InspireSupportedLanguages) {
            writeSupportedLanguages((InspireSupportedLanguages) getElement(), true);
        } else if (getElement() instanceof InspireSupportedCRS) {
            writeSupportedCRS((InspireSupportedCRS) getElement(), true);
        }
        finish();
    }

    /**
     * Write minimal INSPIRE DLS ExtendedCapabilities
     *
     * @param minimalInspireExtendedCapabilities
     *            INSPIRE DLS ExtendedCapabilities to write
     * @throws XMLStreamException
     *             If an error occurs when writing the INSPIRE DLS
     *             ExtendedCapabilities to stream
     */
    private void writeMinimlaInspireExtendedCapabilities(
            MinimalInspireExtendedCapabilities minimalInspireExtendedCapabilities) throws XMLStreamException {
        start(QN_EXTENDED_CAPABILITIES);
        writeInspireCommonNamespaces(true);
        writeInspireDLSNamespaces();
        writeMetadataUrl(minimalInspireExtendedCapabilities.getMetadataUrl());
        writeSupportedLanguages(minimalInspireExtendedCapabilities.getSupportedLanguages(), false);
        writeResponseLanguage(minimalInspireExtendedCapabilities.getResponseLanguage());
        for (InspireUniqueResourceIdentifier inspireUniqueResourceIdentifier : minimalInspireExtendedCapabilities
                .getSpatialDataSetIdentifier()) {
            writeSpatialDataSetIdentifier(inspireUniqueResourceIdentifier);
        }
        writeSupportedCRS(minimalInspireExtendedCapabilities.getSupportedCRS(), false);
        end(QN_EXTENDED_CAPABILITIES);
    }

    /**
     * Write full INSPIRE DLS ExtendedCapabilities
     *
     * @param fullInspireExtendedCapabilities
     *            INSPIRE DLS ExtendedCapabilities to write
     * @throws XMLStreamException
     *             If an error occurs when writing the INSPIRE DLS
     *             ExtendedCapabilities to stream
     */
    private void writeFullInspireExtendedCapabilities(FullInspireExtendedCapabilities fullInspireExtendedCapabilities)
            throws XMLStreamException {
        start(QN_EXTENDED_CAPABILITIES);
        writeInspireCommonNamespaces(true);
        writeInspireDLSNamespaces();
        for (InspireResourceLocator resourceLocator : fullInspireExtendedCapabilities.getResourceLocator()) {
            writeResourceLocator(resourceLocator);
        }
        writeResourceType(fullInspireExtendedCapabilities.getResourceType());
        writeTemporalReference(fullInspireExtendedCapabilities.getTemporalReferences());
        for (InspireConformity conformity : fullInspireExtendedCapabilities.getConformity()) {
            writeConformity(conformity);
        }
        for (InspireMetadataPointOfContact metadataPointOfContact : fullInspireExtendedCapabilities
                .getMetadataPointOfContacts()) {
            writeMetadataPointOfContact(metadataPointOfContact);
        }
        writeMetadataDate(fullInspireExtendedCapabilities.getMetadataDate());
        writeSpatialDataServiceType(fullInspireExtendedCapabilities.getSpatialDataServiceType());
        for (InspireMandatoryKeyword mandatoryKeyword : fullInspireExtendedCapabilities.getMandatoryKeywords()) {
            writeMandatoryKeyword(mandatoryKeyword);
        }
        if (fullInspireExtendedCapabilities.isSetKeywords()) {
            for (InspireKeyword keyword : fullInspireExtendedCapabilities.getKeywords()) {
                writeKeyword(keyword);
            }
        }
        writeSupportedLanguages(fullInspireExtendedCapabilities.getSupportedLanguages(), false);
        writeResponseLanguage(fullInspireExtendedCapabilities.getResponseLanguage());
        if (fullInspireExtendedCapabilities.isSetMetadataUrl()) {
            writeMetadataUrl(fullInspireExtendedCapabilities.getMetadataUrl());
        }
        for (InspireUniqueResourceIdentifier inspireUniqueResourceIdentifier : fullInspireExtendedCapabilities
                .getSpatialDataSetIdentifier()) {
            writeSpatialDataSetIdentifier(inspireUniqueResourceIdentifier);
        }
        writeSupportedCRS(fullInspireExtendedCapabilities.getSupportedCRS(), false);
        end(QN_EXTENDED_CAPABILITIES);
    }

    /**
     * Write {@link InspireCitation} to stream
     *
     * @param citation
     *            {@link InspireCitation} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeCitationContent(InspireCitation citation) throws XMLStreamException {
        writeTitle(citation.getTitle());
        writeDateOf(citation.getDateOf());
        if (citation.isSetUrls()) {
            for (String url : citation.getUrls()) {
                writeURI(url);
            }
        }
        if (citation.isSetResourceLocators()) {
            for (InspireResourceLocator resourceLocator : citation.getResourceLocator()) {
                writeResourceLocator(resourceLocator);
            }
        }
    }

    /**
     * Write code element to stream
     *
     * @param code
     *            element value
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeCode(String code) throws XMLStreamException {
        start(QN_CODE);
        chars(code);
        endInline(QN_CODE);
    }

    /**
     * Write {@link InspireConformity} to stream
     *
     * @param conformity
     *            {@link InspireConformity} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeConformity(InspireConformity conformity) throws XMLStreamException {
        start(QN_CONFORMITY);
        writeSpecification(conformity.getInspireSpecification());
        writeDegree(conformity.getInspireDegreeOfConformity());
        end(QN_CONFORMITY);

    }

    /**
     * Write {@link InspireLanguageISO6392B} as default language element to
     * stream
     *
     * @param defaultLanguage
     *            {@link InspireLanguageISO6392B} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeDefaultLanguage(InspireLanguageISO6392B defaultLanguage) throws XMLStreamException {
        start(QN_DEFAULT_LANGUAGE);
        writeLanguage(defaultLanguage);
        end(QN_DEFAULT_LANGUAGE);
    }

    /**
     * Write {@link InspireDegreeOfConformity} to stream
     *
     * @param inspireDegreeOfConformity
     *            {@link InspireDegreeOfConformity} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeDegree(InspireDegreeOfConformity inspireDegreeOfConformity) throws XMLStreamException {
        start(QN_DEGREE);
        chars(inspireDegreeOfConformity.toString());
        endInline(QN_DEGREE);
    }

    /**
     * Write {@link InspireDateOf} to stream
     *
     * @param dateOf
     *            {@link InspireDateOf} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeDateOf(InspireDateOf dateOf) throws XMLStreamException {
        if (dateOf instanceof InspireDateOfPublication) {
            writeDateOfPublication((InspireDateOfPublication) dateOf);
        } else if (dateOf instanceof InspireDateOfCreation) {
            writeDateOfCreation((InspireDateOfCreation) dateOf);
        } else if (dateOf instanceof InspireDateOfLastRevision) {
            writeDateOfLastRevision((InspireDateOfLastRevision) dateOf);
        }
    }

    /**
     * Write {@link InspireDateOfPublication} to stream
     *
     * @param dateOf
     *            {@link InspireDateOfPublication} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeDateOfPublication(InspireDateOfPublication dateOf) throws XMLStreamException {
        start(QN_DATE_OF_PUBLICATION);
        time(dateOf);
        endInline(QN_DATE_OF_PUBLICATION);
    }

    /**
     * Write {@link InspireDateOfCreation} to stream
     *
     * @param dateOf
     *            {@link InspireDateOfCreation} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeDateOfCreation(InspireDateOfCreation dateOf) throws XMLStreamException {
        start(QN_DATE_OF_CREATION);
        time(dateOf);
        endInline(QN_DATE_OF_CREATION);
    }

    /**
     * Write {@link InspireDateOfLastRevision} to stream
     *
     * @param dateOf
     *            {@link InspireDateOfLastRevision} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeDateOfLastRevision(InspireDateOfLastRevision dateOf) throws XMLStreamException {
        start(QN_DATE_OF_LAST_REVISION);
        time(dateOf);
        endInline(QN_DATE_OF_LAST_REVISION);
    }

    /**
     * Write email address element to stream
     *
     * @param emailAddress
     *            email adress element to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeEmailAddress(String emailAddress) throws XMLStreamException {
        start(QN_EMAIL_ADDRESS);
        chars(emailAddress);
        endInline(QN_EMAIL_ADDRESS);
    }

    /**
     * Write {@link TimePosition} as end date element to stream
     *
     * @param time
     *            {@link TimePosition} to write as end date element to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeEndDate(TimePosition time) throws XMLStreamException {
        start(QN_END_DATE);
        time(time);
        endInline(QN_END_DATE);

    }

    /**
     * Write {@link TimeInstant} as individual date element to stream
     *
     * @param time
     *            {@link TimeInstant} to write as individual date element to
     *            stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeIndividualDate(TimeInstant time) throws XMLStreamException {
        start(QN_INDIVIDUAL_DATE);
        time(time);
        endInline(QN_INDIVIDUAL_DATE);

    }

    /**
     * Write namespaces to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeInspireDLSNamespaces() throws XMLStreamException {
        namespace(NS_INSPIRE_DLS_PREFIX, NS_INSPIRE_DLS);
    }

    /**
     * Write namespaces to stream
     *
     * @param root
     *            indicator if this is a root element and namespaces should be
     *            added
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeInspireCommonNamespaces(boolean root) throws XMLStreamException {
        if (root) {
            namespace(NS_INSPIRE_COMMON_PREFIX, NS_INSPIRE_COMMON);
        }
    }

    /**
     * Write {@link TimePeriod} as interval of dates element to stream
     *
     * @param time
     *            {@link TimePeriod} to write as interval of dates element to
     *            stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeIntervalOfTime(TimePeriod time) throws XMLStreamException {
        start(QN_INTERVAL_OF_DATES);
        writeStartingDate(time.getStartTimePosition());
        writeEndDate(time.getEndTimePosition());
        end(QN_INTERVAL_OF_DATES);

    }

    /**
     * Write {@link InspireKeyword} to stream
     *
     * @param keyword
     *            {@link InspireKeyword} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeKeyword(InspireKeyword keyword) throws XMLStreamException {
        if (keyword.isSetOriginatingControlledVocabulary()) {
            writeOriginatingControlledVocabulary(keyword.getOriginatingControlledVocabulary());
        }
        start(QN_KEYWORD);
        writeKeywordValue(keyword.getKeywordValue());
        end(QN_KEYWORD);

    }

    /**
     * Write keyword value element to stream
     *
     * @param keywordValue
     *            keyword value element to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeKeywordValue(String keywordValue) throws XMLStreamException {
        start(QN_KEYWORD_VALUE);
        chars(keywordValue);
        endInline(QN_KEYWORD_VALUE);
    }

    /**
     * Write {@link InspireMandatoryKeywordValue} to stream
     *
     * @param keywordValue
     *            {@link InspireMandatoryKeywordValue} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeKeywordValue(InspireMandatoryKeywordValue keywordValue) throws XMLStreamException {
        writeKeywordValue(keywordValue.toString());
    }

    /**
     * Write {@link InspireLanguageISO6392B} to stream
     *
     * @param language
     *            {@link InspireLanguageISO6392B} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeLanguage(InspireLanguageISO6392B language) throws XMLStreamException {
        start(QN_LANGUAGE);
        chars(language.value());
        endInline(QN_LANGUAGE);
    }

    /**
     * Write {@link InspireMandatoryKeyword} to stream
     *
     * @param mandatoryKeyword
     *            {@link InspireMandatoryKeyword} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeMandatoryKeyword(InspireMandatoryKeyword mandatoryKeyword) throws XMLStreamException {
        start(QN_MANDATORY_KEYWORD);
        writeKeywordValue(mandatoryKeyword.getKeywordValue());
        end(QN_MANDATORY_KEYWORD);
    }

    /**
     * Write {@link MediaType} to stream
     *
     * @param mediaType
     *            {@link MediaType} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeMediaType(MediaType mediaType) throws XMLStreamException {
        start(QN_MEDIA_TYPE);
        chars(mediaType.toString());
        endInline(QN_MEDIA_TYPE);
    }

    /**
     * Write {@link TimeInstant} as metadata date element to stream
     *
     * @param metadataDate
     *            {@link TimeInstant} to write as metadata date element to
     *            stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeMetadataDate(TimeInstant metadataDate) throws XMLStreamException {
        start(QN_METADATA_DATE);
        time(metadataDate);
        endInline(QN_METADATA_DATE);
    }

    /**
     * Write {@link InspireMetadataPointOfContact} to stream
     *
     * @param metadataPointOfContact
     *            {@link InspireMetadataPointOfContact} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeMetadataPointOfContact(InspireMetadataPointOfContact metadataPointOfContact)
            throws XMLStreamException {
        start(QN_METADATA_POINT_OF_CONTACT);
        writeOrganisationName(metadataPointOfContact.getOrganisationName());
        writeEmailAddress(metadataPointOfContact.getEmailAddress());
        end(QN_METADATA_POINT_OF_CONTACT);
    }

    /**
     * Write {@link InspireResourceLocator} as metadata URL element to stream
     *
     * @param metadataUrl
     *            {@link InspireResourceLocator} to write as metadata URL
     *            element to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeMetadataUrl(InspireResourceLocator metadataUrl) throws XMLStreamException {
        start(QN_METADATA_URL);
        writeResourceLocatorContent(metadataUrl);
        end(QN_METADATA_URL);
    }

    /**
     * Write namespace element to stream
     *
     * @param namespace
     *            namespace element value to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeNamespace(String namespace) throws XMLStreamException {
        start(QN_NAMESPACE);
        chars(namespace);
        end(QN_NAMESPACE);
    }

    /**
     * Write {@link InspireOriginatingControlledVocabulary} to stream
     *
     * @param originatingControlledVocabulary
     *            {@link InspireOriginatingControlledVocabulary} to write to
     *            stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeOriginatingControlledVocabulary(
            InspireOriginatingControlledVocabulary originatingControlledVocabulary) throws XMLStreamException {
        start(QN_ORIGINATING_CONTROLLED_VOCABULARY);
        writeCitationContent(originatingControlledVocabulary);
        end(QN_ORIGINATING_CONTROLLED_VOCABULARY);
    }

    /**
     * Write organisation name element to stream
     *
     * @param organisationName
     *            organisation name element value to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeOrganisationName(String organisationName) throws XMLStreamException {
        start(QN_ORGANISATION_NAME);
        chars(organisationName);
        endInline(QN_SPATIAL_DATA_SERVICE_TYPE);
    }

    /**
     * Write {@link InspireLanguageISO6392B} as response language element to
     * stream
     *
     * @param responseLanguage
     *            {@link InspireLanguageISO6392B} to write as response language
     *            element to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeResponseLanguage(InspireLanguageISO6392B responseLanguage) throws XMLStreamException {
        start(QN_RESPONSE_LANGUAGE);
        writeLanguage(responseLanguage);
        end(QN_RESPONSE_LANGUAGE);

    }

    /**
     * Write {@link InspireResourceLocator} to stream
     *
     * @param resourceLocator
     *            {@link InspireResourceLocator} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeResourceLocatorContent(InspireResourceLocator resourceLocator) throws XMLStreamException {
        writeUrl(resourceLocator.getURL());
        if (resourceLocator.isSetMediaTypes()) {
            for (MediaType mediaType : resourceLocator.getMediaTypes()) {
                writeMediaType(mediaType);
            }
        }
    }

    /**
     * Write {@link InspireResourceLocator} to stream
     *
     * @param resourceLocator
     *            {@link InspireResourceLocator} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeResourceLocator(InspireResourceLocator resourceLocator) throws XMLStreamException {
        start(QN_RESOURCE_LOCATOR);
        writeResourceLocatorContent(resourceLocator);
        end(QN_RESOURCE_LOCATOR);
    }

    /**
     * Write {@link InspireServiceSpatialDataResourceType} as resource type
     * element to stream
     *
     * @param resourceType
     *            {@link InspireServiceSpatialDataResourceType} to write as
     *            resource type element to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeResourceType(InspireServiceSpatialDataResourceType resourceType) throws XMLStreamException {
        start(QN_RESOURCE_TYPE);
        chars(resourceType.toString());
        endInline(QN_RESOURCE_TYPE);
    }

    /**
     * Write {@link InspireSupportedLanguages} to stream
     *
     * @param supportedLanguages
     *            {@link InspireSupportedLanguages} to write to stream
     * @param root
     *            indicator if this is a root element and namespaces should be
     *            added
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeSupportedLanguages(InspireSupportedLanguages supportedLanguages, boolean root)
            throws XMLStreamException {
        start(QN_SUPPORTED_LANGUAGES);
        writeInspireCommonNamespaces(root);
        writeDefaultLanguage(supportedLanguages.getDefaultLanguage());
        if (supportedLanguages.isSetSupportedLanguages()) {
            for (InspireLanguageISO6392B supportedLanguage : supportedLanguages.getSupportedLanguages()) {
                writeSupportedLanguage(supportedLanguage);
            }
        }
        end(QN_SUPPORTED_LANGUAGES);

    }

    /**
     * Write {@link InspireSpatialDataServiceType} to stream
     *
     * @param spatialDataServiceType
     *            {@link InspireSpatialDataServiceType} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeSpatialDataServiceType(InspireSpatialDataServiceType spatialDataServiceType)
            throws XMLStreamException {
        start(QN_SPATIAL_DATA_SERVICE_TYPE);
        chars(spatialDataServiceType.toString());
        endInline(QN_SPATIAL_DATA_SERVICE_TYPE);

    }

    /**
     * Write {@link InspireUniqueResourceIdentifier} as spatial dataset
     * identifier element to stream
     *
     * @param inspireUniqueResourceIdentifier
     *            {@link InspireUniqueResourceIdentifier} to write as spatial
     *            dataset identifier element to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeSpatialDataSetIdentifier(InspireUniqueResourceIdentifier inspireUniqueResourceIdentifier)
            throws XMLStreamException {
        start(QN_SPATIAL_DATASET_IDENTIFIER);
        writeUniqueResourceIdentifierContent(inspireUniqueResourceIdentifier);
        endInline(QN_SPATIAL_DATASET_IDENTIFIER);
    }

    /**
     * Write {@link InspireConformityCitation} as specification element to
     * stream
     *
     * @param inspireSpecification
     *            {@link InspireConformityCitation} to write as specification
     *            element to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeSpecification(InspireConformityCitation inspireSpecification) throws XMLStreamException {
        start(QN_SPECIFICATION);
        writeCitationContent(inspireSpecification);
        end(QN_SPECIFICATION);
    }

    /**
     * Write {@link TimePosition} as starting date element to stream
     *
     * @param time
     *            {@link TimePosition} to write as starting date element to
     *            stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeStartingDate(TimePosition time) throws XMLStreamException {
        start(QN_STARTING_DATE);
        time(time);
        endInline(QN_STARTING_DATE);

    }

    /**
     * Write {@link InspireLanguageISO6392B} as supported language element to
     * stream
     *
     * @param supportedLanguage
     *            {@link InspireLanguageISO6392B} to write as supported language
     *            element to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeSupportedLanguage(InspireLanguageISO6392B supportedLanguage) throws XMLStreamException {
        start(QN_SUPPORTED_LANGUAGE);
        writeLanguage(supportedLanguage);
        end(QN_SUPPORTED_LANGUAGE);

    }

    /**
     * Write {@link Time} as temporal extent element to stream
     *
     * @param temporalExtent
     *            {@link Time} to write as temporal extent element to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeTemporalExtent(Time temporalExtent) throws XMLStreamException {
        start(QN_TEMPORAL_EXTENT);
        if (temporalExtent instanceof TimeInstant) {
            writeIndividualDate((TimeInstant) temporalExtent);
        } else if (temporalExtent instanceof TimePeriod) {
            writeIntervalOfTime((TimePeriod) temporalExtent);
        }
        end(QN_TEMPORAL_EXTENT);

    }

    /**
     * Write {@link InspireTemporalReference} to stream
     *
     * @param temporalReference
     *            {@link InspireTemporalReference} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeTemporalReference(InspireTemporalReference temporalReference) throws XMLStreamException {
        if (!temporalReference.isSetDateOfCreation() && !temporalReference.isSetDateOfLastRevision()
                && !temporalReference.isSetDatesOfPublication() && !temporalReference.isSetTemporalExtents()) {
            empty(QN_TEMPORAL_REFERENCE);
        } else {
            start(QN_TEMPORAL_REFERENCE);
            if (temporalReference.isSetDateOfCreation()) {
                writeDateOfCreation(temporalReference.getDateOfCreation());
            }
            if (temporalReference.isSetDateOfLastRevision()) {
                writeDateOfLastRevision(temporalReference.getDateOfLastRevision());
            }
            if (temporalReference.isSetDatesOfPublication()) {
                for (InspireDateOfPublication dateOfPublication : temporalReference.getDatesOfPublication()) {
                    writeDateOfPublication(dateOfPublication);
                }
            }
            if (temporalReference.isSetTemporalExtents()) {
                for (Time temporalExtent : temporalReference.getTemporalExtents()) {
                    writeTemporalExtent(temporalExtent);
                }
            }
            end(QN_TEMPORAL_REFERENCE);
        }
    }

    /**
     * Write {@link InspireTemporalReference} to stream
     *
     * @param temporalReferences
     *            {@link InspireTemporalReference} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeTemporalReference(List<InspireTemporalReference> temporalReferences) throws XMLStreamException {
        if (CollectionHelper.isNotEmpty(temporalReferences)) {
            for (InspireTemporalReference temporalReference : temporalReferences) {
                writeTemporalReference(temporalReference);
            }
        } else {
            empty(QN_TEMPORAL_REFERENCE);
        }
    }

    /**
     * Write title element to stream
     *
     * @param title
     *            title element value to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeTitle(String title) throws XMLStreamException {
        start(QN_TITLE);
        chars(title);
        endInline(QN_TITLE);
    }

    /**
     * Write {@link InspireUniqueResourceIdentifier} to stream
     *
     * @param uniqueResourceIdentifier
     *            {@link InspireUniqueResourceIdentifier} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeUniqueResourceIdentifierContent(InspireUniqueResourceIdentifier uniqueResourceIdentifier)
            throws XMLStreamException {
        // TODO Check if metadataURL and/or Code/Namespace (attr(name, value);)
        writeCode(uniqueResourceIdentifier.getCode());
        if (uniqueResourceIdentifier.isSetNamespace()) {
            writeNamespace(uniqueResourceIdentifier.getNamespace());
        }
    }

    /**
     * Write URI element to stream
     *
     * @param url
     *            URI element value to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeURI(String url) throws XMLStreamException {
        start(QN_URI);
        chars(HtmlEscapers.htmlEscaper().escape(url));
        endInline(QN_URI);
    }

    /**
     * Write URL element to stream
     *
     * @param url
     *            URL element value to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeUrl(String url) throws XMLStreamException {
        start(QN_URL);
        chars(HtmlEscapers.htmlEscaper().escape(url));
        endInline(QN_URL);
    }

    /**
     * Write {@link InspireSupportedCRS} to stream
     *
     * @param supportedCRSes
     *            {@link InspireSupportedCRS} to write to stream
     * @param root
     *            indicator if this is a root element and namespaces should be
     *            added
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeSupportedCRS(InspireSupportedCRS supportedCRSes, boolean root) throws XMLStreamException {
        start(QN_SUPPORTED_CRS);
        if (root) {
            writeInspireDLSNamespaces();
        }
        writeDefaultCRS(supportedCRSes.getDefaultCRS());
        if (supportedCRSes.isSetSupportedCRSs()) {
            for (String supportedCRS : supportedCRSes.getOtherCRS()) {
                writeOtherCRS(supportedCRS);
            }
        }
        end(QN_SUPPORTED_CRS);

    }

    /**
     * Write default CRS string as default CRS element to stream
     *
     * @param crs
     *            default CRS string to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeDefaultCRS(String crs) throws XMLStreamException {
        start(QN_DEFAULT_CRS);
        chars(crs);
        endInline(QN_DEFAULT_CRS);
    }

    /**
     * Write CRS string as other CRS element to stream
     *
     * @param crs
     *            CRS string to write as other CRS element to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeOtherCRS(String crs) throws XMLStreamException {
        start(QN_OTHER_CRS);
        chars(crs);
        endInline(QN_OTHER_CRS);
    }

    /**
     * Write CRS string to stream
     *
     * @param crs
     *            CRS string to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing the object to stream
     */
    private void writeCRS(String crs) throws XMLStreamException {
        start(QN_CRS);
        chars(crs);
        endInline(QN_CRS);
    }

}
