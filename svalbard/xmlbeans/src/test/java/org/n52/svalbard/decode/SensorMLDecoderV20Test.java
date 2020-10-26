/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

import net.opengis.sensorml.x20.DataInterfaceType;
import net.opengis.swe.x20.DataRecordPropertyType;
import net.opengis.swe.x20.DataRecordType.Field;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.hamcrest.CoreMatchers;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.sensorML.AbstractSensorML;
import org.n52.shetland.ogc.sensorML.SmlContact;
import org.n52.shetland.ogc.sensorML.SmlResponsibleParty;
import org.n52.shetland.ogc.sensorML.elements.SmlClassifier;
import org.n52.shetland.ogc.sensorML.elements.SmlEvent;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifier;
import org.n52.shetland.ogc.sensorML.elements.SmlProperty;
import org.n52.shetland.ogc.sensorML.v20.AggregateProcess;
import org.n52.shetland.ogc.sensorML.v20.PhysicalComponent;
import org.n52.shetland.ogc.sensorML.v20.SmlDataInterface;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.svalbard.decode.exception.DecodingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 1.0.0
 */
public class SensorMLDecoderV20Test {

    private static final String DOES_NOT_MATCH = " does not match expected value!";
    private static final String INCORRECT_COUNT = " does have expected number of elements!";
    private static final String NOT_SET = " is not set correctly!";

    private SensorMLDecoderV20 decoder;

    @BeforeEach
    public void setup() {
        DecoderRepository decoderRepository = new DecoderRepository();

        decoder = new SensorMLDecoderV20();
        decoder.setDecoderRepository(decoderRepository);
        decoder.setXmlOptions(XmlOptions::new);

        SweCommonDecoderV20 sweCommonDecoder = new SweCommonDecoderV20();
        sweCommonDecoder.setDecoderRepository(decoderRepository);
        sweCommonDecoder.setXmlOptions(XmlOptions::new);

        GmlDecoderv321 gmlDecoder = new GmlDecoderv321();
        gmlDecoder.setDecoderRepository(decoderRepository);
        gmlDecoder.setXmlOptions(XmlOptions::new);

        Iso19139GmdDecoder gmdDecoder = new Iso19139GmdDecoder();
        gmdDecoder.setDecoderRepository(decoderRepository);
        gmdDecoder.setXmlOptions(XmlOptions::new);

        Iso19139GcoDecoder gcoDecoder = new Iso19139GcoDecoder();

        decoderRepository.setDecoders(Arrays.asList(decoder, sweCommonDecoder, gmlDecoder, gmdDecoder, gcoDecoder));
        decoderRepository.init();
    }

    @Test
    public void shouldDecodeEventDescription() throws IOException, XmlException, DecodingException {
        try (InputStream stream = SensorMLDecoderV20Test.class.getResourceAsStream("/SensorML20EventDescription.xml")) {
            XmlObject object = XmlObject.Factory.parse(stream);
            AbstractSensorML ssml = decoder.decode(object);
            assertThat(ssml, is(instanceOf(PhysicalComponent.class)));
            // Validate Event description
            assertThat("No event parsed!", ssml.getHistory().size() == 1);
            for (SmlEvent smlEvent : ssml.getHistory()) {
                assertThat("sml:Event->label" + DOES_NOT_MATCH,
                           "testLabel".equals(smlEvent.getLabel()));
                assertThat("sml:Event->description" + DOES_NOT_MATCH,
                           "testDescription".equals(smlEvent.getDescription()));
                assertThat("sml:Event->identifier" + DOES_NOT_MATCH,
                           "testIdentifier".equals(smlEvent.getIdentifier()));
                Time referenceInstant = new TimeInstant(DateTime.parse("2019-05-06T18:44:18Z"));
                assertThat("sml:Event->time" + DOES_NOT_MATCH,
                           referenceInstant.equals(smlEvent.getTime()));

                // Check counts before checking individual elements
                assertThat("sml:Event->identification" + INCORRECT_COUNT,
                           smlEvent.getIdentification().size() == 1);
                assertThat("sml:Event->classification" + INCORRECT_COUNT,
                           smlEvent.getClassification().getClassification().size() == 3);
                assertThat("sml:Event->contacts" + INCORRECT_COUNT,
                           smlEvent.getContacts().getMembers().size() == 1);
                assertThat("sml:Event->property" + INCORRECT_COUNT,
                           smlEvent.getProperty().size() == 4);

                // Check individual elements
                smlEvent.getIdentification().forEach(this::checkSmlIdentifier);
                smlEvent.getClassification().getClassification().forEach(this::checkClassification);
                smlEvent.getContacts().getMembers().forEach(this::checkContacts);
                smlEvent.getProperty().forEach(this::checkProperty);

            }

        }
    }

    private void checkProperty(SmlProperty smlProperty) {
        assertThat("swe:field->definition" + NOT_SET, smlProperty.getAbstractDataComponent().isSetDefinition());
        assertThat("swe:field->label" + NOT_SET, smlProperty.getAbstractDataComponent().isSetLabel());
    }

    /**
     * For now only checks if all fields are set, but not if they are set correctly
     *
     * @param classifier element to be checked
     */
    private void checkClassification(SmlClassifier classifier) {
        assertThat("sml:classifier->definition" + NOT_SET, !classifier.getDefinition().isEmpty());
        assertThat("sml:classifier->label" + NOT_SET, !classifier.getLabel().isEmpty());
        assertThat("sml:classifier->value" + NOT_SET, !classifier.getValue().isEmpty());
    }

    private void checkContacts(SmlContact smlContact) {
        assertThat(smlContact, is(instanceOf(SmlResponsibleParty.class)));
        assertThat("sml:contact->role" + DOES_NOT_MATCH, "researcher".equals(smlContact.getRole()));
        assertThat("sml:contact->title" + DOES_NOT_MATCH, "John Doe".equals(
            ((SmlResponsibleParty) smlContact).getIndividualName()));
    }

    private void checkSmlIdentifier(SmlIdentifier identifier) {
        assertThat("sml:identifier->label" + DOES_NOT_MATCH, "UUID".equals(identifier.getLabel()));
        assertThat("sml:value->value" + DOES_NOT_MATCH,
                   "e3c8df0d-02e9-446d-a59b-224a14b89f9a".equals(identifier.getValue()));
    }

    @Test
    @Disabled("not yet implemented")
    public void bla() throws IOException, XmlException, DecodingException {
        try (InputStream stream = SensorMLDecoderV20Test.class.getResourceAsStream("/AggregateProcess-dwd.xml")) {
            XmlObject object = XmlObject.Factory.parse(stream);
            AbstractSensorML ssml = decoder.decode(object);
            assertThat(ssml, is(instanceOf(AggregateProcess.class)));
        }
    }

    @Test
    @Disabled("not yet implemented")
    public void shouldDecodeDataInterface() throws DecodingException {
        DataInterfaceType xbDataInterface = DataInterfaceType.Factory.newInstance();
        SmlDataInterface parsedDataInterface = decoder.parseDataInterfaceType(xbDataInterface);
        assertThat(parsedDataInterface, is(notNullValue()));
    }

    @Test
    @Disabled("not yet implemented")
    public void shouldDecodeDataInterfaceData() throws DecodingException {
        DataInterfaceType xbDataInterface = DataInterfaceType.Factory.newInstance();
        xbDataInterface.addNewData();
        SmlDataInterface parsedDataInterface = decoder.parseDataInterfaceType(xbDataInterface);
        assertThat(parsedDataInterface.getData(), is(notNullValue()));
    }

    @Test
    @Disabled("not yet implemented")
    public void shouldDecodeDataInterfaceInterfaceParameters() throws DecodingException {
        DataInterfaceType xbDataInterface = DataInterfaceType.Factory.newInstance();
        DataRecordPropertyType xbInterfaceParameters = xbDataInterface.addNewInterfaceParameters();
        Field field = xbInterfaceParameters.addNewDataRecord().addNewField();
        field.setName("test-field-name");
        SmlDataInterface parsedDataInterface = decoder.parseDataInterfaceType(xbDataInterface);
        assertThat(parsedDataInterface.isSetInterfaceParameters(), is(true));
        assertThat(parsedDataInterface.getInterfaceParameters(), CoreMatchers.isA(SweDataRecord.class));
    }

}
