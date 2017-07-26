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

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.inspire.InspireConformity;
import org.n52.shetland.inspire.InspireConformity.InspireDegreeOfConformity;
import org.n52.shetland.inspire.InspireConformityCitation;
import org.n52.shetland.inspire.InspireDateOfCreation;
import org.n52.shetland.inspire.InspireDateOfLastRevision;
import org.n52.shetland.inspire.InspireDateOfPublication;
import org.n52.shetland.inspire.InspireKeyword;
import org.n52.shetland.inspire.InspireLanguageISO6392B;
import org.n52.shetland.inspire.InspireMandatoryKeyword;
import org.n52.shetland.inspire.InspireMandatoryKeywordValue;
import org.n52.shetland.inspire.InspireMetadataPointOfContact;
import org.n52.shetland.inspire.InspireOriginatingControlledVocabulary;
import org.n52.shetland.inspire.InspireResourceLocator;
import org.n52.shetland.inspire.InspireSupportedCRS;
import org.n52.shetland.inspire.InspireSupportedLanguages;
import org.n52.shetland.inspire.InspireTemporalReference;
import org.n52.shetland.inspire.InspireUniqueResourceIdentifier;
import org.n52.shetland.inspire.dls.FullInspireExtendedCapabilities;
import org.n52.shetland.inspire.dls.InspireCapabilities.InspireServiceSpatialDataResourceType;
import org.n52.shetland.inspire.dls.MinimalInspireExtendedCapabilities;
import org.n52.shetland.inspire.omso.InspireOMSOConstants;
import org.n52.shetland.ogc.AbstractSupportedStringType;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.ObservationType;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class InspireEncoderTest {

    private static XmlOptions xmlOptions = new XmlOptions();

    EncoderRepository encoderRepository;

    Set<SupportedType> supportedTypes = Sets.newHashSet();

    @Before
    public void setup() {
        encoderRepository = new EncoderRepository();

        encoderRepository.setEncoders(Lists.newArrayList(new PointObservationTypeEncoder(),
                new PointTimeSeriesObservationTypeEncoder(),
                new TrajectoryObservationTypeEncoder(),
                new ProfileObservationTypeEncoder(),
                new MultiPointObservationTypeEncoder()));
        encoderRepository.init();

        this.encoderRepository.getEncoders().stream()
        .map(Encoder::getSupportedTypes)
        .filter(Objects::nonNull)
        .forEachOrdered(this.supportedTypes::addAll);
    }


    private Set<? extends SupportedType> typesFor(Class<? extends SupportedType> key) {
        return this.supportedTypes;
    }

    @SuppressWarnings("unchecked")
    public Set<String> getObservationTypesAsString() {
        return getSupportedTypeAsString((Set<AbstractSupportedStringType>) typesFor(ObservationType.class));
    }

    private Set<String> getSupportedTypeAsString(Set<? extends AbstractSupportedStringType> types) {
        return types.stream().map(AbstractSupportedStringType::getValue).collect(toSet());
    }

    @Test
    public void test_observationTypes() {
        Assert.assertThat(getObservationTypesAsString().contains(InspireOMSOConstants.OBS_TYPE_POINT_OBSERVATION), Matchers.is(true));
        Assert.assertThat(getObservationTypesAsString().contains(InspireOMSOConstants.OBS_TYPE_POINT_TIME_SERIES_OBSERVATION), Matchers.is(true));
        Assert.assertThat(getObservationTypesAsString().contains(InspireOMSOConstants.OBS_TYPE_TRAJECTORY_OBSERVATION), Matchers.is(true));
        Assert.assertThat(getObservationTypesAsString().contains(InspireOMSOConstants.OBS_TYPE_PROFILE_OBSERVATION), Matchers.is(true));
        Assert.assertThat(getObservationTypesAsString().contains(InspireOMSOConstants.OBS_TYPE_MULTI_POINT_OBSERVATION), Matchers.is(true));
    }

    /*
     * xmlns:xsd="http://www.w3.org/2001/XMLSchema"
     * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation=
     * "http://inspire.ec.europa.eu/schemas/inspire_dls/1.0 http://inspire.ec.europa.eu/schemas/inspire_dls/1.0/inspire_dls.xsd"
     */
//
//    @BeforeClass
//    public static void init() {
//        Map<String, String> prefixes = new HashMap<String, String>();
//        prefixes.put(InspireConstants.NS_INSPIRE_COMMON, InspireConstants.NS_INSPIRE_COMMON_PREFIX);
//        prefixes.put(InspireConstants.NS_INSPIRE_DLS, InspireConstants.NS_INSPIRE_DLS_PREFIX);
//        xmlOptions.setSaveSuggestedPrefixes(prefixes);
//        xmlOptions.setSaveImplicitNamespaces(prefixes);
//        xmlOptions.setSaveAggressiveNamespaces();
//        xmlOptions.setSavePrettyPrint();
//        xmlOptions.setSaveNamespacesFirst();
//        xmlOptions.setCharacterEncoding("UTF-8");
//    }
//
//    @Test
//    public void enocodeMinimalInspireExtendedCapabilities() throws UnsupportedEncoderInputException,
//            OwsExceptionReport, SAXException, IOException {
//        InspireXmlEncoder inspireEncoder = new InspireXmlEncoder();
//        validate(inspireEncoder.encode(getMinimalInspireExtendedCapabilities()));
//    }
//
//    @Test
//    public void enocodeFullIsnpireExtendedCapabilities() throws UnsupportedEncoderInputException, OwsExceptionReport,
//            SAXException, IOException {
//        InspireXmlEncoder inspireEncoder = new InspireXmlEncoder();
//        validate(inspireEncoder.encode(getFullInspireExtendedCapabilities()));
//    }
//
//    @Test
//    public void valid_iso8601() {
//        // date
//        String datePattern = "\\d{4}-(1[0-2]|0[1-9])-(3[0-1]|0[1-9]|[1-2][0-9])";
//        String date = "2013-09-26";
//        Assert.assertThat(Pattern.matches(datePattern, date), Matchers.is(true));
//        // time
//        String timePattern = "(T(2[0-3]|[0-1][0-9]):([0-5][0-9]):([0-5][0-9])(\\.[0-9]+)?)?";
//        String time_HH_MM_SS_S = "T12:49:41.740";
//        Assert.assertThat(Pattern.matches(timePattern, time_HH_MM_SS_S), Matchers.is(true));
//        String time_HH_MM_SS = "T12:49:41";
//        Assert.assertThat(Pattern.matches(timePattern, time_HH_MM_SS), Matchers.is(true));
//        // offset
//        String offsetPattern = "(Z|[+|-](2[0-3]|[0-1][0-9]):([0-5][0-9]))?";
//        String offset_PLUS_HH_MM = "+02:00";
//        Assert.assertThat(Pattern.matches(offsetPattern, offset_PLUS_HH_MM), Matchers.is(true));
//        String offset_MINUS_HH_MM = "-02:00";
//        Assert.assertThat(Pattern.matches(offsetPattern, offset_MINUS_HH_MM), Matchers.is(true));
//        String offset_Z = "Z";
//        Assert.assertThat(Pattern.matches(offsetPattern, offset_Z), Matchers.is(true));
//        // date time
//        String dtPattern = datePattern + timePattern;
//        Assert.assertThat(Pattern.matches(dtPattern, date + time_HH_MM_SS_S), Matchers.is(true));
//        Assert.assertThat(Pattern.matches(dtPattern, date + time_HH_MM_SS), Matchers.is(true));
//        // date time offset
//        String dtoPattern = dtPattern + offsetPattern;
//        Assert.assertThat(Pattern.matches(dtoPattern, date + time_HH_MM_SS_S + offset_PLUS_HH_MM), Matchers.is(true));
//        Assert.assertThat(Pattern.matches(dtoPattern, date + time_HH_MM_SS_S + offset_MINUS_HH_MM), Matchers.is(true));
//        Assert.assertThat(Pattern.matches(dtoPattern, date + time_HH_MM_SS_S + offset_Z), Matchers.is(true));
//        Assert.assertThat(Pattern.matches(dtoPattern, date + time_HH_MM_SS + offset_PLUS_HH_MM), Matchers.is(true));
//        Assert.assertThat(Pattern.matches(dtoPattern, date + time_HH_MM_SS + offset_MINUS_HH_MM), Matchers.is(true));
//        Assert.assertThat(Pattern.matches(dtoPattern, date + time_HH_MM_SS + offset_Z), Matchers.is(true));
//        // valid patter for schema: \d{4}-(1[0-2]|0[1-9])-(3[0-1]|0[1-9]|[1-2][0-9])(T(2[0-3]|[0-1][0-9]):([0-5][0-9]):([0-5][0-9])(\.[0-9]+)?)?(Z|[+|-](2[0-3]|[0-1][0-9]):([0-5][0-9]))?
//
////        String pattern =
////                "\\d{4}-(1[0-2]|0[1-9])-(3[0-1]|0[1-9]|[1-2][0-9])(T(2[0-3]|[0-1][0-9]):([0-5][0-9]):([0-5][0-9])(\\.[0-9]+)?)?(Z|([+|-](2[0-3]|[0-1][0-9]):([0-5][0-9]):([0-5][0-9])(\\.[0-9])?)?)?";
////        Assert.assertThat(Pattern.matches(pattern, "2013-09-26T12:49:41.740+02:00"), Matchers.is(true));
//    }

    private MinimalInspireExtendedCapabilities getMinimalInspireExtendedCapabilities() {
        // --------------------
        InspireResourceLocator resourceLocator = new InspireResourceLocator("http://min.test.org/sos");
        resourceLocator.addMediaType(MediaTypes.APPLICATION_SOAP_XML);
        // --------------------
        InspireSupportedLanguages inspireSupportedLanguages =
                new InspireSupportedLanguages(InspireLanguageISO6392B.ENG);
        // --------------------
        InspireLanguageISO6392B responseLanguage = InspireLanguageISO6392B.ENG;
        // --------------------
        Set<InspireUniqueResourceIdentifier> spatialDataSetIdentifier = Sets.newHashSet();
        InspireUniqueResourceIdentifier iuri = new InspireUniqueResourceIdentifier("test");
        iuri.setNamespace("http://test.org");
        spatialDataSetIdentifier.add(iuri);
        // --------------------
        return new MinimalInspireExtendedCapabilities(resourceLocator, inspireSupportedLanguages, responseLanguage,
                spatialDataSetIdentifier,new InspireSupportedCRS("4326"));
    }

    private void validate(XmlObject xmlObject) throws SAXException, IOException {
        SchemaFactory sf = SchemaFactory.newInstance(
        XMLConstants.XML_NS_URI );
        Schema schema = sf.newSchema(InspireEncoderTest.class.getResource("/inspire_dls/1.0/inspire_dls.xsd"));
        Validator validator = schema.newValidator();
        validator.validate(new DOMSource(xmlObject.getDomNode()));
    }

    private FullInspireExtendedCapabilities getFullInspireExtendedCapabilities() {

        InspireResourceLocator resourceLocator = new InspireResourceLocator("http://full.test.org/sos");
        resourceLocator.addMediaType(MediaTypes.APPLICATION_SOAP_XML);
        // -------------------
        InspireTemporalReference temporalReference = new InspireTemporalReference();
        temporalReference.setDateOfCreation(new InspireDateOfCreation(new DateTime()));
        temporalReference.setDateOfLastRevision(new InspireDateOfLastRevision(new DateTime()));
        temporalReference.addDateOfPublication(new InspireDateOfPublication(new DateTime()));
        temporalReference.addTemporalExtent(new TimeInstant(new DateTime()));
        temporalReference.addTemporalExtent(new TimePeriod(new DateTime(), new DateTime().plus(3456)));
        // -------------------
        InspireConformityCitation inspireConformityCitation =
                new InspireConformityCitation("Test", new InspireDateOfCreation(new DateTime()));
        InspireConformity conformity =
                new InspireConformity(inspireConformityCitation, InspireDegreeOfConformity.notEvaluated);
        // -------------------
        InspireMetadataPointOfContact inspireMetadataPointOfContact =
                new InspireMetadataPointOfContact("test", "test@test.te");
        // -------------------
        InspireOriginatingControlledVocabulary inspireOriginatingControlledVocabulary =
                new InspireOriginatingControlledVocabulary("Test", new InspireDateOfCreation(new DateTime()));
        InspireMandatoryKeyword inspireMandatoryKeyword =
                new InspireMandatoryKeyword(InspireMandatoryKeywordValue.humanServiceEditor,
                        inspireOriginatingControlledVocabulary);
        // --------------------
        InspireSupportedLanguages inspireSupportedLanguages =
                new InspireSupportedLanguages(InspireLanguageISO6392B.ENG);
        // --------------------
        InspireLanguageISO6392B responseLanguage = InspireLanguageISO6392B.ENG;
        // --------------------
        InspireUniqueResourceIdentifier iuri = new InspireUniqueResourceIdentifier("test");
        iuri.setNamespace("http://test.org");
        // --------------------
        FullInspireExtendedCapabilities inspireExtendedCapabilities =
                new FullInspireExtendedCapabilities(resourceLocator, inspireSupportedLanguages, responseLanguage, iuri, new InspireSupportedCRS("4326"));
        inspireExtendedCapabilities.setResourceType(InspireServiceSpatialDataResourceType.service);
        inspireExtendedCapabilities.addKeyword(new InspireKeyword("test"));
        inspireExtendedCapabilities.addMandatoryKeyword(inspireMandatoryKeyword);
        // -------------------
        inspireExtendedCapabilities.setMetadataDate(new TimeInstant(new DateTime()));
        // -------------------
        inspireExtendedCapabilities.addMetadataPointOfContact(inspireMetadataPointOfContact);
        inspireExtendedCapabilities.addConformity(conformity);
        inspireExtendedCapabilities.addTemporalReference(temporalReference);
        return inspireExtendedCapabilities;
    }
}
