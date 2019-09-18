/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasXPath;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import java.net.URI;
import java.util.Arrays;

import javax.xml.namespace.NamespaceContext;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.isotc211.x2005.gmd.DQDomainConsistencyDocument;
import org.isotc211.x2005.gmd.DQDomainConsistencyPropertyType;
import org.isotc211.x2005.gmd.DQDomainConsistencyType;
import org.isotc211.x2005.gmd.MDMetadataPropertyType;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.iso.CodeList;
import org.n52.shetland.iso.GcoConstants;
import org.n52.shetland.iso.gmd.CiResponsibleParty;
import org.n52.shetland.iso.gmd.GmdCitation;
import org.n52.shetland.iso.gmd.GmdCitationDate;
import org.n52.shetland.iso.gmd.GmdConformanceResult;
import org.n52.shetland.iso.gmd.GmdConstants;
import org.n52.shetland.iso.gmd.GmdDateType;
import org.n52.shetland.iso.gmd.GmdDomainConsistency;
import org.n52.shetland.iso.gmd.GmdQuantitativeResult;
import org.n52.shetland.iso.gmd.MDDataIdentification;
import org.n52.shetland.iso.gmd.MDMetadata;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.NamespaceContextBuilder;
import org.n52.svalbard.util.XmlHelper;
import org.w3c.dom.Node;

public class Iso19139GmdEncoderTest {
    private static final EncodingContext TYPE = EncodingContext.of(XmlBeansEncodingFlags.TYPE);
    private static final EncodingContext PROPERTY_TYPE = EncodingContext.of(XmlBeansEncodingFlags.PROPERTY_TYPE);
    private static final EncodingContext DOCUMENT_TYPE = EncodingContext.of(XmlBeansEncodingFlags.DOCUMENT);
    private static final NamespaceContext NS_CTX = new NamespaceContextBuilder()
            .add(GmlConstants.NS_GML_32, GmlConstants.NS_GML_PREFIX)
            .add(GcoConstants.NS_GCO, GcoConstants.NS_GCO_PREFIX)
            .add(GmdConstants.NS_GMD, GmdConstants.NS_GMD_PREFIX)
            .add(W3CConstants.NS_XLINK, W3CConstants.NS_XLINK_PREFIX)
            .build();


    private Iso19139GmdEncoder encoder;


    @BeforeEach
    public void setup() {
        encoder = new Iso19139GmdEncoder();
        encoder.setXmlOptions(XmlOptions::new);

        Iso19139GcoEncoder iso19139GcoEncoder = new Iso19139GcoEncoder();
        iso19139GcoEncoder.setXmlOptions(XmlOptions::new);

        EncoderRepository encoderRepository = new EncoderRepository();
        encoderRepository.setEncoders(Arrays.asList(encoder, iso19139GcoEncoder));
        encoderRepository.init();

        encoderRepository.getEncoders().stream()
            .forEach(e -> ((AbstractDelegatingEncoder<?,?>)e).setEncoderRepository(encoderRepository));
    }

    @Test
    public void checkReturnType() throws Exception {
        GmdConformanceResult cr = GmdDomainConsistency.dataCapture(true);
        GmdQuantitativeResult qr = GmdDomainConsistency.uncertaintyEstimation(20);
        assertThat(encoder.encode(cr), is(instanceOf(DQDomainConsistencyType.class)));
        assertThat(encoder.encode(cr, DOCUMENT_TYPE), is(instanceOf(DQDomainConsistencyDocument.class)));
        assertThat(encoder.encode(cr, PROPERTY_TYPE), is(instanceOf(DQDomainConsistencyPropertyType.class)));
        assertThat(encoder.encode(cr, TYPE), is(instanceOf(DQDomainConsistencyType.class)));
        assertThat(encoder.encode(qr), is(instanceOf(DQDomainConsistencyType.class)));
        assertThat(encoder.encode(qr, DOCUMENT_TYPE), is(instanceOf(DQDomainConsistencyDocument.class)));
        assertThat(encoder.encode(qr, PROPERTY_TYPE), is(instanceOf(DQDomainConsistencyPropertyType.class)));
        assertThat(encoder.encode(qr, TYPE), is(instanceOf(DQDomainConsistencyType.class)));
    }

    @Test
    public void checkValidity() throws EncodingException , DecodingException {
        assertThat(XmlHelper.validateDocument(encoder.encode(GmdDomainConsistency.dataCapture(GmlConstants.NilReason.unknown), DOCUMENT_TYPE)), is(true));
        assertThat(XmlHelper.validateDocument(encoder.encode(GmdDomainConsistency.dataCapture(true), DOCUMENT_TYPE)), is(true));
        assertThat(XmlHelper.validateDocument(encoder.encode(GmdDomainConsistency.timeCoverage(GmlConstants.NilReason.unknown), DOCUMENT_TYPE)), is(true));
        assertThat(XmlHelper.validateDocument(encoder.encode(GmdDomainConsistency.timeCoverage(true), DOCUMENT_TYPE)), is(true));
        assertThat(XmlHelper.validateDocument(encoder.encode(GmdDomainConsistency.uncertaintyEstimation(5), DOCUMENT_TYPE)), is(true));
        assertThat(XmlHelper.validateDocument(encoder.encode(GmdDomainConsistency.uncertaintyEstimation(GmlConstants.NilReason.unknown), DOCUMENT_TYPE)), is(true));
    }

    @Test
    public void checkConformanceResult() throws EncodingException {
        Node node = encoder.encode(GmdDomainConsistency.dataCapture(true), DOCUMENT_TYPE).getDomNode();
        assertThat(node, hasXPath("/gmd:DQ_DomainConsistency", NS_CTX));
        assertThat(node, hasXPath("/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString", NS_CTX, is("EC/50/2008")));
        assertThat(node, hasXPath("/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date", NS_CTX, is("2008")));
        assertThat(node, hasXPath("/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:dateType/gmd:CI_DateTypeCode", NS_CTX, is("publication")));
        assertThat(node, hasXPath("/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:dateType/gmd:CI_DateTypeCode/@codeList", NS_CTX, is("eng")));
        assertThat(node, hasXPath("/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:dateType/gmd:CI_DateTypeCode/@codeListValue", NS_CTX, is("publication")));
        assertThat(node, hasXPath("/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:explanation/gco:CharacterString", NS_CTX, is("Data Capture")));
        assertThat(node, hasXPath("/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:pass/gco:Boolean", NS_CTX, is("true")));
    }

    @Test
    public void checkQuantitativeResult() throws EncodingException {
        Node node = encoder.encode(GmdDomainConsistency.uncertaintyEstimation(5), DOCUMENT_TYPE).getDomNode();
        assertThat(node, hasXPath("/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_QuantitativeResult/gmd:valueUnit/gml:BaseUnit/@gml:id", NS_CTX, startsWith("PercentageUnit")));
        assertThat(node, hasXPath("/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_QuantitativeResult/gmd:valueUnit/gml:BaseUnit/gml:identifier/@codeSpace", NS_CTX, is("http://dd.eionet.europa.eu/vocabularies/aq/resultquality/uncertaintyestimation/")));
        assertThat(node, hasXPath("/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_QuantitativeResult/gmd:valueUnit/gml:BaseUnit/gml:catalogSymbol/@codeSpace", NS_CTX, is("http://www.opengis.net/def/uom/UCUM/")));
        assertThat(node, hasXPath("/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_QuantitativeResult/gmd:valueUnit/gml:BaseUnit/gml:catalogSymbol", NS_CTX, is("%")));
        assertThat(node, hasXPath("/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_QuantitativeResult/gmd:valueUnit/gml:BaseUnit/gml:unitsSystem/@xlink:href", NS_CTX, is("http://www.opengis.net/def/uom/UCUM/")));
        assertThat(node, hasXPath("/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_QuantitativeResult/gmd:value/gco:Record", NS_CTX, is("5.0")));
    }

//    @Test
//    public void checkMDMetadataReferenceEncoding() throws EncodingException {
//        Referenceable<MDMetadata> mdMmetadata = Referenceable.of(new Reference().setHref(URI.create("href")).setTitle("title"));
//        XmlObject xmlObject = encoder.encode(mdMmetadata);
//        xmlObject.validate();
//        assertThat(xmlObject, instanceOf(MDMetadataPropertyType.class));
//    }

    @Test
    public void checkMDMetadataEncoding() throws EncodingException {
        MDDataIdentification identificationInfo = new MDDataIdentification(new GmdCitation("title", new GmdCitationDate(GmdDateType.publication(), "2018-10-11")), "abstrakt", "ger");
        MDMetadata mdMmetadata = new MDMetadata(new CiResponsibleParty(new org.n52.shetland.iso.gco.Role(CodeList.CiRoleCodes.CI_RoleCode_author.name())), DateTime.now(), identificationInfo);
        XmlObject xmlObject = encoder.encode(mdMmetadata, EncodingContext.of(XmlBeansEncodingFlags.PROPERTY_TYPE));
        xmlObject.validate();
        assertThat(xmlObject, instanceOf(MDMetadataPropertyType.class));
    }
}
