/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.shetland.rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 * Constants for the W3C Time Ontology.
 *
 * @see <a href="https://www.w3.org/TR/owl-time/">Time Ontology in OWL</a>
 */
@SuppressWarnings({"checkstyle:ConstantName", "checkstyle:LineLength", "checkstyle:DeclarationOrder"})
public class TIME {
    private static final Model MODEL = ModelFactory.createDefaultModel();
    /**
     * The namespace of the vocabulary as a string.
     */
    public static final String NS = "http://www.w3.org/2006/time#";

    /**
     * The namespace of the vocabulary as a resource.
     */
    public static final Resource NAMESPACE = MODEL.createResource(NS);

    // Classes
    public static final Resource TemporalEntity = MODEL.createResource("http://www.w3.org/2006/time#TemporalEntity");
    public static final Resource Instant = MODEL.createResource("http://www.w3.org/2006/time#Instant");
    public static final Resource Interval = MODEL.createResource("http://www.w3.org/2006/time#Interval");
    public static final Resource ProperInterval = MODEL.createResource("http://www.w3.org/2006/time#ProperInterval");
    public static final Resource DateTimeInterval = MODEL.createResource("http://www.w3.org/2006/time#DateTimeInterval");
    public static final Resource TemporalPosition = MODEL.createResource("http://www.w3.org/2006/time#TemporalPosition");
    public static final Resource TimePosition = MODEL.createResource("http://www.w3.org/2006/time#TimePosition");
    public static final Resource GeneralDateTimeDescription = MODEL.createResource("http://www.w3.org/2006/time#GeneralDateTimeDescription");
    public static final Resource DateTimeDescription = MODEL.createResource("http://www.w3.org/2006/time#DateTimeDescription");
    public static final Resource MonthOfYear = MODEL.createResource("http://www.w3.org/2006/time#MonthOfYear");
    public static final Resource TemporalDuration = MODEL.createResource("http://www.w3.org/2006/time#TemporalDuration");
    public static final Resource Duration = MODEL.createResource("http://www.w3.org/2006/time#Duration");
    public static final Resource GeneralDurationDescription = MODEL.createResource("http://www.w3.org/2006/time#GeneralDurationDescription");
    public static final Resource DurationDescription = MODEL.createResource("http://www.w3.org/2006/time#DurationDescription");
    public static final Resource TemporalUnit = MODEL.createResource("http://www.w3.org/2006/time#TemporalUnit");
    public static final Resource TRS = MODEL.createResource("http://www.w3.org/2006/time#TRS");
    public static final Resource TimeZone = MODEL.createResource("http://www.w3.org/2006/time#TimeZone");
    public static final Resource DayOfWeek = MODEL.createResource("http://www.w3.org/2006/time#DayOfWeek");

    // Properties
    public static final Property hasTime = MODEL.createProperty("http://www.w3.org/2006/time#hasTime");
    public static final Property hasBeginning = MODEL.createProperty("http://www.w3.org/2006/time#hasBeginning");
    public static final Property hasEnd = MODEL.createProperty("http://www.w3.org/2006/time#hasEnd");
    public static final Property hasTemporalDuration = MODEL.createProperty("http://www.w3.org/2006/time#hasTemporalDuration");
    public static final Property hasDuration = MODEL.createProperty("http://www.w3.org/2006/time#hasDuration");
    public static final Property hasDurationDescription = MODEL.createProperty("http://www.w3.org/2006/time#hasDurationDescription");
    public static final Property hasXSDDuration = MODEL.createProperty("http://www.w3.org/2006/time#hasXSDDuration");
    public static final Property before = MODEL.createProperty("http://www.w3.org/2006/time#before");
    public static final Property after = MODEL.createProperty("http://www.w3.org/2006/time#after");
    public static final Property inside = MODEL.createProperty("http://www.w3.org/2006/time#inside");
    public static final Property intervalEquals = MODEL.createProperty("http://www.w3.org/2006/time#intervalEquals");
    public static final Property intervalDisjoint = MODEL.createProperty("http://www.w3.org/2006/time#intervalDisjoint");
    public static final Property intervalAfter = MODEL.createProperty("http://www.w3.org/2006/time#intervalAfter");
    public static final Property intervalBefore = MODEL.createProperty("http://www.w3.org/2006/time#intervalBefore");
    public static final Property intervalMeets = MODEL.createProperty("http://www.w3.org/2006/time#intervalMeets");
    public static final Property intervalMetBy = MODEL.createProperty("http://www.w3.org/2006/time#intervalMetBy");
    public static final Property intervalOverlaps = MODEL.createProperty("http://www.w3.org/2006/time#intervalOverlaps");
    public static final Property intervalOverlappedBy = MODEL.createProperty("http://www.w3.org/2006/time#intervalOverlappedBy");
    public static final Property intervalStarts = MODEL.createProperty("http://www.w3.org/2006/time#intervalStarts");
    public static final Property intervalStartedBy = MODEL.createProperty("http://www.w3.org/2006/time#intervalStartedBy");
    public static final Property intervalFinishes = MODEL.createProperty("http://www.w3.org/2006/time#intervalFinishes");
    public static final Property intervalFinishedBy = MODEL.createProperty("http://www.w3.org/2006/time#intervalFinishedBy");
    public static final Property intervalContains = MODEL.createProperty("http://www.w3.org/2006/time#intervalContains");
    public static final Property intervalDuring = MODEL.createProperty("http://www.w3.org/2006/time#intervalDuring");
    public static final Property intervalIn = MODEL.createProperty("http://www.w3.org/2006/time#intervalIn");
    public static final Property hasDateTimeDescription = MODEL.createProperty("http://www.w3.org/2006/time#hasDateTimeDescription");
    @Deprecated
    public static final Property xsdDateTime = MODEL.createProperty("http://www.w3.org/2006/time#xsdDateTime");
    public static final Property inTemporalPosition = MODEL.createProperty("http://www.w3.org/2006/time#inTemporalPosition");
    public static final Property inTimePosition = MODEL.createProperty("http://www.w3.org/2006/time#inTimePosition");
    public static final Property inDateTime = MODEL.createProperty("http://www.w3.org/2006/time#inDateTime");
    public static final Property inXSDDate = MODEL.createProperty("http://www.w3.org/2006/time#inXSDDate");
    @Deprecated
    public static final Property inXSDDateTime = MODEL.createProperty("http://www.w3.org/2006/time#inXSDDateTime");
    public static final Property inXSDDateTimeStamp = MODEL.createProperty("http://www.w3.org/2006/time#inXSDDateTimeStamp");
    public static final Property inXSDgYearMonth = MODEL.createProperty("http://www.w3.org/2006/time#inXSDgYearMonth");
    public static final Property inXSDgYear = MODEL.createProperty("http://www.w3.org/2006/time#inXSDgYear");
    public static final Property numericDuration = MODEL.createProperty("http://www.w3.org/2006/time#numericDuration");
    public static final Property unitType = MODEL.createProperty("http://www.w3.org/2006/time#unitType");
    public static final Property years = MODEL.createProperty("http://www.w3.org/2006/time#years");
    public static final Property months = MODEL.createProperty("http://www.w3.org/2006/time#months");
    public static final Property weeks = MODEL.createProperty("http://www.w3.org/2006/time#weeks");
    public static final Property days = MODEL.createProperty("http://www.w3.org/2006/time#days");
    public static final Property hours = MODEL.createProperty("http://www.w3.org/2006/time#hours");
    public static final Property minutes = MODEL.createProperty("http://www.w3.org/2006/time#minutes");
    public static final Property seconds = MODEL.createProperty("http://www.w3.org/2006/time#seconds");
    public static final Property numericPosition = MODEL.createProperty("http://www.w3.org/2006/time#numericPosition");
    public static final Property nominalPosition = MODEL.createProperty("http://www.w3.org/2006/time#nominalPosition");
    public static final Property timeZone = MODEL.createProperty("http://www.w3.org/2006/time#timeZone");
    public static final Property year = MODEL.createProperty("http://www.w3.org/2006/time#year");
    public static final Property month = MODEL.createProperty("http://www.w3.org/2006/time#month");
    public static final Property day = MODEL.createProperty("http://www.w3.org/2006/time#day");
    public static final Property hour = MODEL.createProperty("http://www.w3.org/2006/time#hour");
    public static final Property minute = MODEL.createProperty("http://www.w3.org/2006/time#minute");
    public static final Property second = MODEL.createProperty("http://www.w3.org/2006/time#second");
    public static final Property week = MODEL.createProperty("http://www.w3.org/2006/time#week");
    public static final Property dayOfYear = MODEL.createProperty("http://www.w3.org/2006/time#dayOfYear");
    public static final Property dayOfWeek = MODEL.createProperty("http://www.w3.org/2006/time#dayOfWeek");
    public static final Property monthOfYear = MODEL.createProperty("http://www.w3.org/2006/time#monthOfYear");
    public static final Property hasTRS = MODEL.createProperty("http://www.w3.org/2006/time#hasTRS");

    /**
     * Returns the URI for this schema
     *
     * @return URI
     */
    public static String getURI() {
        return NS;
    }
}

