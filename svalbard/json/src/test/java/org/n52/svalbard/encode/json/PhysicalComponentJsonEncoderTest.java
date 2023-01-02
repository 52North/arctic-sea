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
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.sensorML.SmlContact;
import org.n52.shetland.ogc.sensorML.SmlPerson;
import org.n52.shetland.ogc.sensorML.SmlResponsibleParty;
import org.n52.shetland.ogc.sensorML.elements.SmlClassifier;
import org.n52.shetland.ogc.sensorML.elements.SmlEvent;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifier;
import org.n52.shetland.ogc.sensorML.elements.SmlProperty;
import org.n52.shetland.ogc.sensorML.v20.PhysicalComponent;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.json.sensorML.v20.ContactJsonEncoder;
import org.n52.svalbard.encode.json.sensorML.v20.EventJsonEncoder;
import org.n52.svalbard.encode.json.sensorML.v20.ListJsonEncoder;
import org.n52.svalbard.encode.json.sensorML.v20.PhysicalComponentJsonEncoder;
import org.n52.svalbard.encode.json.sensorML.v20.PropertyJsonEncoder;
import org.n52.svalbard.encode.json.sensorML.v20.TermJsonEncoder;

import java.util.ArrayList;
import java.util.Arrays;
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
                                                         new ListJsonEncoder.IdentifierListJsonEncoder(),
                                                         new ListJsonEncoder.ClassifierListJsonEncoder(),
                                                         new ListJsonEncoder.PropertyListJsonEncoder(),
                                                         new ListJsonEncoder.HistoryJsonEncoder(),
                                                         new ListJsonEncoder.ContactListJsonEncoder(),
                                                         new ContactJsonEncoder(),
                                                         new EventJsonEncoder(),
                                                         new PropertyJsonEncoder(),
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

        List<SmlProperty> properties = new ArrayList<>();
        properties.add(property);

        SmlEvent event = new SmlEvent();
        event.setTime(new TimePeriod(DateTime.parse("2019-05-06T18:44:18Z"), DateTime.parse("2020-05-06T18:44:18Z")));
        event.setDefinition("event_definition");
        event.setIdentifier("event_identifier");
        event.setIdentification(event_identification);
        event.setProperty(properties);

        List<SmlEvent> history = new ArrayList<>();
        history.add(event);
        pc.setHistory(history);

        List<SmlContact> contacts = new ArrayList<>();
        SmlPerson person = new SmlPerson();
        person.setName("person_name");
        person.setAffiliation("person_affiliation");
        person.setEmail("person_email");
        person.setPhoneNumber("person_phone");
        person.setSurname("person_surname");
        person.setUserID("person_userID");
        person.setRole("person_role");
        SmlResponsibleParty responsibleParty = new SmlResponsibleParty();
        responsibleParty.setIndividualName("responsiblePartyindividualName");
        responsibleParty.setOrganizationName("responsibleParty_organizationName");
        responsibleParty.setPositionName("responsibleParty_positionName");
        responsibleParty.setPhoneVoice(Arrays.asList("responsibleParty_phoneVoice"));
        responsibleParty.setPhoneFax(Arrays.asList("responsibleParty_phoneFax"));
        responsibleParty.setDeliveryPoint(Arrays.asList("responsibleParty_deliveryPoint"));
        responsibleParty.setCity("responsibleParty_city");
        responsibleParty.setAdministrativeArea("responsibleParty_administrativeArea");
        responsibleParty.setPostalCode("responsibleParty_postalCode");
        responsibleParty.setCountry("responsibleParty_country");
        responsibleParty.setEmail("responsibleParty_email");
        responsibleParty.setOnlineResource(Arrays.asList("responsibleParty_onlineResource"));
        responsibleParty.setHoursOfService("responsibleParty_hoursOfService");
        responsibleParty.setContactInstructions("responsibleParty_contactInstructions");
        contacts.add(person);
        contacts.add(responsibleParty);
        pc.setContact(contacts);

        List<SmlClassifier> classifications = new ArrayList<>();
        SmlClassifier classifier = new SmlClassifier();
        classifier.setName("classifier_name");
        classifier.setCodeSpace("classifier_codeSpace");
        classifier.setDefinition("classifier_definition");
        classifier.setValue("classifier_value");
        pc.setClassifications(classifications);
        return pc;
    }
}
