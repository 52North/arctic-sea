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

import java.util.Arrays;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.shetland.ogc.sos.drt.DeleteResultTemplateRequest;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;

import net.opengis.drt.x10.DeleteResultTemplateDocument;
import net.opengis.drt.x10.DeleteResultTemplateType;
import net.opengis.drt.x10.DeleteResultTemplateType.Tuple;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class DeleteResultTemplateDecoderTest {

    private DeleteResultTemplateDecoder decoder;

    private DeleteResultTemplateDocument encodedRequest;

    private DeleteResultTemplateType drtt;

    @Before
    public void setup() {
        DecoderRepository decoderRepository = new DecoderRepository();

        decoder = new DeleteResultTemplateDecoder();
        decoder.setDecoderRepository(decoderRepository);
        decoder.setXmlOptions(XmlOptions::new);

        decoderRepository.setDecoders(Arrays.asList(decoder));
        decoderRepository.init();

        encodedRequest = DeleteResultTemplateDocument.Factory.newInstance();
        drtt = encodedRequest.addNewDeleteResultTemplate();
        drtt.setService("test-service");
        drtt.setVersion("test-version");
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();


//    @Test
//    public void shouldThrowExceptionOnWrongInput() throws DecodingException {
//        thrown.expect(UnsupportedDecoderInputException.class);
//        thrown.expectMessage(
//                "null can not be decoded by " + decoder.getClass().getName() + " because it is not yet implemented!");
//
//        decoder.decode(XmlObject.Factory.newInstance());
//    }
//
//    @Test
//    public void shouldDecodeServiceAndVersion() throws DecodingException {
//        addResultTemplate();
//
//        DeleteResultTemplateRequest decodedRequest = decoder.decode(encodedRequest);
//
//        Assert.assertThat(decodedRequest.getService(), Is.is("test-service"));
//        Assert.assertThat(decodedRequest.getVersion(), Is.is("test-version"));
//    }
//
//    @Test
//    public void shouldDecodeResultTemplates() throws DecodingException {
//        addResultTemplate();
//
//        DeleteResultTemplateRequest decodedRequest = decoder.decode(encodedRequest);
//
//        Assert.assertThat(decodedRequest.isSetResultTemplates(), Is.is(true));
//        Assert.assertThat(decodedRequest.getResultTemplates().size(), Is.is(1));
//        Assert.assertThat(decodedRequest.getResultTemplates().get(0), Is.is("test-template"));
//    }
//
//    @Test
//    public void shouldDecodeObservedPropertyOfferingTuples() throws DecodingException {
//        addObservedPropertyOfferingTuple();
//
//        DeleteResultTemplateRequest decodedRequest = decoder.decode(encodedRequest);
//
//        Assert.assertThat(decodedRequest.isSetObservedPropertyOfferingPairs(), Is.is(true));
//        Assert.assertThat(decodedRequest.getObservedPropertyOfferingPairs().size(), Is.is(1));
//        Assert.assertThat(decodedRequest.getObservedPropertyOfferingPairs().get(0).getKey(), Is.is("test-property"));
//        Assert.assertThat(decodedRequest.getObservedPropertyOfferingPairs().get(0).getValue(), Is.is("test-offering"));
//    }
//
//    private void addObservedPropertyOfferingTuple() {
//        Tuple t = drtt.addNewTuple();
//        t.setOffering("test-offering");
//        t.setObservedProperty("test-property");
//    }

    private void addResultTemplate() {
        drtt.addNewResultTemplate().setStringValue("test-template");
    }

}
