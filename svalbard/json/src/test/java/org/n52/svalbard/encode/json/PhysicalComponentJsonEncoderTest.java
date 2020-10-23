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
package org.n52.svalbard.encode.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.sensorML.elements.SmlEvent;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifier;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifierList;
import org.n52.shetland.ogc.sensorML.elements.SmlProperty;
import org.n52.shetland.ogc.sensorML.v20.PhysicalComponent;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.json.sensorML.v20.EventJsonEncoder;
import org.n52.svalbard.encode.json.sensorML.v20.HistoryJsonEncoder;
import org.n52.svalbard.encode.json.sensorML.v20.PropertyJsonEncoder;
import org.n52.svalbard.encode.json.sensorML.v20.PropertyListJsonEncoder;
import org.n52.svalbard.encode.json.sensorML.v20.TermJsonEncoder;
import org.n52.svalbard.encode.json.sensorML.v20.ClassifierListJsonEncoder;
import org.n52.svalbard.encode.json.sensorML.v20.IdentifierListJsonEncoder;
import org.n52.svalbard.encode.json.sensorML.v20.PhysicalComponentJsonEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class PhysicalComponentJsonEncoderTest {

    private EncoderRepository encoderRepository;

    private PhysicalComponentJsonEncoder physicalComponentJsonEncoder;

    @BeforeEach
    public void setUp() {
        encoderRepository = new EncoderRepository();
        physicalComponentJsonEncoder = new PhysicalComponentJsonEncoder();
        encoderRepository.setEncoders(Lists.newArrayList(physicalComponentJsonEncoder,
                                                         new IdentifierListJsonEncoder(),
                                                         new ClassifierListJsonEncoder(),
                                                         new HistoryJsonEncoder(),
                                                         new EventJsonEncoder(),
                                                         new PropertyJsonEncoder(),
                                                         new PropertyListJsonEncoder(),
                                                         new TermJsonEncoder()));
        encoderRepository.init();
        encoderRepository.getEncoders()
            .forEach(encoder -> ((JSONEncoder) encoder).setEncoderRepository(encoderRepository));
    }

    @Test
    public void testValid() throws EncodingException {
        PhysicalComponent dummyComponent = createDummyComponent();
        JsonNode encode = physicalComponentJsonEncoder.encode(dummyComponent);
        System.out.println(encode.toPrettyString());
    }

    private PhysicalComponent createDummyComponent() {
        PhysicalComponent pc = new PhysicalComponent();
        pc.setIdentifier("testIdentifier");
        pc.setDescription("testDescription");

        List<SmlIdentifier> pc_identification = new ArrayList<>();
        List<SmlIdentifier> event_identification = new ArrayList<>();
        SmlIdentifier pc_identifier = new SmlIdentifier();
        SmlIdentifier event_identifier = new SmlIdentifier();

        pc_identifier.setName("pc_identifier_name");
        event_identifier.setName("event_identifier_name");
        pc_identifier.setLabel("pc_identifier_label");
        event_identifier.setLabel("event_identifier_label");
        pc_identifier.setValue("pc_identifier_value");
        event_identifier.setValue("event_identifier_value");
        pc_identifier.setDefinition("pc_identifier_definition");
        event_identifier.setDefinition("event_identifier_definition");
        pc_identification.add(pc_identifier);
        event_identification.add(event_identifier);
        pc.setIdentifications(pc_identification);

        SmlProperty property = new SmlProperty();
        QuantityValue embeddedComponent = new QuantityValue();
        embeddedComponent.setDefinition("quantity_definition");
        embeddedComponent.setIdentifier("quantity_identifier");
        embeddedComponent.setName("quantity_name");
        embeddedComponent.setLabel("quantity_label");
        embeddedComponent.setValue(52D);
        property.setAbstractDataComponent(embeddedComponent);

        SmlEvent event = new SmlEvent();
        event.setTime(new TimeInstant(DateTime.parse("2019-05-06T18:44:18Z")));
        event.setDefinition("event_definition");
        event.setIdentifier("event_identifier");
        event.setIdentification(event_identification);

        List<SmlEvent> history = new ArrayList<>();
        history.add(event);
        pc.setHistory(history);

        return pc;
    }
}
