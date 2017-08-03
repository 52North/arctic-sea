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
package org.n52.shetland.inspire;

/**
 * Service internal representation of INSPIRE mandatory keyword
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class InspireMandatoryKeyword extends AbstractInspireKeyword<InspireMandatoryKeywordValue> {

    private InspireMandatoryKeywordValue keywordValue;

    /**
     * constructor
     *
     * @param keywordValue
     *            the keyword value
     */
    public InspireMandatoryKeyword(InspireMandatoryKeywordValue keywordValue) {
        super();
        setKeywordValue(keywordValue);
    }

    /**
     * constructor
     *
     * @param keywordValue
     *            the keyword value
     * @param originatingControlledVocabulary
     *            the originating controlled vocabulary
     */
    public InspireMandatoryKeyword(InspireMandatoryKeywordValue keywordValue,
            InspireOriginatingControlledVocabulary originatingControlledVocabulary) {
        super(originatingControlledVocabulary);
        setKeywordValue(keywordValue);
    }

    @Override
    public InspireMandatoryKeywordValue getKeywordValue() {
        return keywordValue;
    }

    @Override
    protected void setKeywordValue(InspireMandatoryKeywordValue keywordValue) {
        this.keywordValue = keywordValue;
    }

}
