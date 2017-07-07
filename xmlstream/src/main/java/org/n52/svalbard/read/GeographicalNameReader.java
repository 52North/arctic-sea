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
package org.n52.svalbard.read;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.inspire.GeographicalName;
import org.n52.svalbard.decode.exception.DecodingException;

public class GeographicalNameReader extends XmlReader<GeographicalName> {
    private GeographicalName geographicalName;

    @Override
    protected void begin() {
        this.geographicalName = new GeographicalName();
    }

    @Override
    protected void read(QName name)
            throws XMLStreamException, DecodingException {
        if (name.equals(AqdConstants.QN_GN_LANGUAGE)) {
            this.geographicalName.setLanguage(delegate(new NillableStringReader()));
        } else if (name.equals(AqdConstants.QN_GN_NATIVENESS)) {
            this.geographicalName.setNativeness(delegate(new NillableCodeTypeReader()));
        } else if (name.equals(AqdConstants.QN_GN_NAME_STATUS)) {
            this.geographicalName.setNameStatus(delegate(new NillableCodeTypeReader()));
        } else if (name.equals(AqdConstants.QN_GN_SOURCE_OF_NAME)) {
            this.geographicalName.setSourceOfName(delegate(new NillableStringReader()));
        } else if (name.equals(AqdConstants.QN_GN_PRONUNCIATION)) {
            this.geographicalName.setPronunciation(delegate(new PronounciationReader()));
        } else if (name.equals(AqdConstants.QN_GN_SPELLING)) {
            this.geographicalName.addSpelling(delegate(new SpellingReader()));
        } else if (name.equals(AqdConstants.QN_GN_GRAMMATICAL_GENDER)) {
            this.geographicalName.setGrammaticalGender(delegate(new NillableCodeTypeReader()));
        } else if (name.equals(AqdConstants.QN_GN_GRAMMATICAL_NUMBER)) {
            this.geographicalName.setGrammaticalNumber(delegate(new NillableCodeTypeReader()));
        } else {
            ignore();
        }
    }

    @Override
    protected GeographicalName finish() {
        return this.geographicalName;
    }



}
