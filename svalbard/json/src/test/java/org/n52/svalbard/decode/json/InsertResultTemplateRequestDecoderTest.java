/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.decode.json;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.sos.request.InsertResultTemplateRequest;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.encoding.SweTextEncoding;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweTime;
import org.n52.shetland.ogc.swe.simpleType.SweTimeRange;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;

import org.n52.svalbard.decode.DecoderRepository;

/**
 * @since 1.0.0
 *
 */
public class InsertResultTemplateRequestDecoderTest {

    private InsertResultTemplateRequestDecoder decoder;

    @BeforeEach
    public void before() {
        DecoderRepository decoderRepository = new DecoderRepository();
        this.decoder = new InsertResultTemplateRequestDecoder();
        this.decoder.setDecoderRepository(decoderRepository);
        FieldDecoder fieldDecoder = new FieldDecoder();
        fieldDecoder.setDecoderRepository(decoderRepository);
        decoderRepository.setDecoders(Arrays.asList(this.decoder, fieldDecoder));
        decoderRepository.init();
    }

    @Test
    public void resultEncoding()
            throws IOException, DecodingException {
        InsertResultTemplateRequest req = load();
        assertThat(req.getResultEncoding(), is(notNullValue()));
        assertThat(req.getResultEncoding().isDecoded(), is(true));
        assertThat(req.getResultEncoding().isEncoded(), is(false));
        assertThat(req.getResultEncoding().get().get(), is(instanceOf(SweTextEncoding.class)));
        SweTextEncoding encoding = (SweTextEncoding) req.getResultEncoding().get().get();
        assertThat(encoding.getTokenSeparator(), is(","));
        assertThat(encoding.getBlockSeparator(), is("#"));
    }

    @Test
    public void resultStructure()
            throws IOException, DecodingException {
        InsertResultTemplateRequest req = load();
        assertThat(req.getResultStructure(), is(notNullValue()));
        assertThat(req.getResultStructure().isDecoded(), is(true));
        assertThat(req.getResultStructure().isEncoded(), is(false));
        assertThat(req.getResultStructure().get().get(), is(instanceOf(SweDataRecord.class)));
        SweDataRecord structure = (SweDataRecord) req.getResultStructure().get().get();
        assertThat(structure.getFields(), is(notNullValue()));
        assertThat(structure.getFields(), hasSize(3));

        SweField field1 = structure.getFields().get(0);
        assertThat(field1, is(notNullValue()));
        assertThat(field1.getName().getValue(), is("phenomenonTime"));
        assertThat(field1.getElement(), is(instanceOf(SweTimeRange.class)));
        SweTimeRange phenomenonTime = (SweTimeRange) field1.getElement();
        assertThat(phenomenonTime.getDefinition(),
                is("http://www.opengis.net/def/property/OGC/0/PhenomenonTime"));
        assertThat(phenomenonTime.getUom(), is("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian"));

        SweField field2 = structure.getFields().get(1);
        assertThat(field2, is(notNullValue()));
        assertThat(field2.getName().getValue(), is("resultTime"));
        assertThat(field2.getElement(), is(instanceOf(SweTime.class)));
        SweTime resultTime = (SweTime) field2.getElement();
        assertThat(resultTime.getDefinition(), is("http://www.opengis.net/def/property/OGC/0/ResultTime"));
        assertThat(resultTime.getUom(), is("testunit1"));

        SweField field3 = structure.getFields().get(2);
        assertThat(field3, is(notNullValue()));
        assertThat(field3.getName().getValue(), is("observable_property_6"));
        assertThat(field3.getElement(), is(instanceOf(SweQuantity.class)));
        SweQuantity quantity = (SweQuantity) field3.getElement();
        assertThat(quantity.getDefinition(), is("http://www.52north.org/test/observableProperty/6"));
        assertThat(quantity.getUom(), is("test_unit_6"));
    }

    @Test
    public void observationTemplate()
            throws IOException, DecodingException {
        InsertResultTemplateRequest req = load();
        OmObservationConstellation oc = req.getObservationTemplate();
        assertThat(oc, is(notNullValue()));
        assertThat(oc.getObservationType(),
                is("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement"));
        assertThat(oc.getProcedure(), is(notNullValue()));
        assertThat(oc.getProcedure().getIdentifier(), is("http://52north.org/example/procedure/6"));
        assertThat(oc.getObservableProperty(), is(notNullValue()));
        assertThat(oc.getObservableProperty().getIdentifier(), is("http://52north.org/example/observedProperty/6"));
        assertThat(oc.getFeatureOfInterest(), is(notNullValue()));
        assertThat(oc.getFeatureOfInterest().getIdentifierCodeWithAuthority(), is(notNullValue()));
        assertThat(oc.getFeatureOfInterest().getIdentifierCodeWithAuthority().getCodeSpace(),
                is("http://www.opengis.net/def/nil/OGC/0/unknown"));
        assertThat(oc.getFeatureOfInterest().getIdentifierCodeWithAuthority().getValue(),
                is("http://52north.org/example/feature/6"));
    }

    @Test
    public void offering()
            throws IOException, DecodingException {
        InsertResultTemplateRequest req = load();
        assertThat(req.getObservationTemplate(), is(notNullValue()));
        assertThat(req.getObservationTemplate().getOfferings(), hasSize(1));
        assertThat(req.getObservationTemplate().getOfferings(), hasItem("offering6"));
    }

    @Test
    public void identifier()
            throws IOException, DecodingException {
        assertThat(load().getIdentifier().getValue(), is("http://www.52north.org/test/procedure/6/template/1"));
    }

    @Test
    public void dataRecord() throws DecodingException, IOException {
        InsertResultTemplateRequest req = load("/examples/sos/InsertResultTemplateRequest_DataRecord.json");
        assertThat(req.getResultStructure(), is(notNullValue()));
        assertThat(req.getResultStructure().isDecoded(), is(true));
        assertThat(req.getResultStructure().isEncoded(), is(false));
        assertThat(req.getResultStructure().get().get(), is(instanceOf(SweDataRecord.class)));
        SweDataRecord structure = (SweDataRecord) req.getResultStructure().get().get();
        assertThat(structure.getFields(), is(notNullValue()));
        assertThat(structure.getFields(), hasSize(3));

        SweField field1 = structure.getFields().get(0);
        assertThat(field1, is(notNullValue()));
        assertThat(field1.getName().getValue(), is("phenomenonTime"));
        assertThat(field1.getElement(), is(instanceOf(SweTimeRange.class)));
        SweTimeRange phenomenonTime = (SweTimeRange) field1.getElement();
        assertThat(phenomenonTime.getDefinition(),
                is("http://www.opengis.net/def/property/OGC/0/PhenomenonTime"));
        assertThat(phenomenonTime.getUom(), is("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian"));

        SweField field2 = structure.getFields().get(1);
        assertThat(field2, is(notNullValue()));
        assertThat(field2.getName().getValue(), is("resultTime"));
        assertThat(field2.getElement(), is(instanceOf(SweTime.class)));
        SweTime resultTime = (SweTime) field2.getElement();
        assertThat(resultTime.getDefinition(), is("http://www.opengis.net/def/property/OGC/0/ResultTime"));
        assertThat(resultTime.getUom(), is("testunit1"));

        SweField field3 = structure.getFields().get(2);
        assertThat(field3, is(notNullValue()));
        assertThat(field3.getName().getValue(), is("observable_property_6"));
        assertThat(field3.getElement(), is(instanceOf(SweDataRecord.class)));
        SweDataRecord record = (SweDataRecord) field3.getElement();
        assertThat(record.getDefinition(), is("http://www.52north.org/test/observableProperty/6"));
        assertThat(record.getFields(), hasSize(5));
    }

    protected InsertResultTemplateRequest load()
            throws IOException, DecodingException {
        return load("/examples/sos/InsertResultTemplateRequest.json");
    }

    protected InsertResultTemplateRequest load(String file)
            throws IOException, DecodingException {
        final JsonNode json = JsonLoader.fromResource(file);
        final InsertResultTemplateRequest req = decoder.decodeJSON(json, true);
        assertThat(req, is(notNullValue()));
        return req;
    }
}
