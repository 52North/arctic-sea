/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Abstract service internal representation of INSPIRE keywords
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class AbstractInspireKeyword<
        T> {

    private InspireOriginatingControlledVocabulary originatingControlledVocabulary;

    /**
     * constructor
     */
    public AbstractInspireKeyword() {
    }

    /**
     * constructor
     *
     * @param originatingControlledVocabulary
     *            the keyword
     */
    public AbstractInspireKeyword(InspireOriginatingControlledVocabulary originatingControlledVocabulary) {
        setOriginatingControlledVocabulary(originatingControlledVocabulary);
    }

    /**
     * Get the keyword
     *
     * @return the originatingControlledVocabulary
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public InspireOriginatingControlledVocabulary getOriginatingControlledVocabulary() {
        return originatingControlledVocabulary;
    }

    /**
     * Set the keyword
     *
     * @param originatingControlledVocabulary
     *            the originatingControlledVocabulary to set
     */
    private void setOriginatingControlledVocabulary(
            InspireOriginatingControlledVocabulary originatingControlledVocabulary) {
        this.originatingControlledVocabulary = originatingControlledVocabulary;
    }

    /**
     * Check if the keyword is set
     *
     * @return <code>true</code>, if the keyword is set
     */
    public boolean isSetOriginatingControlledVocabulary() {
        return getOriginatingControlledVocabulary() != null;
    }

    /**
     * Get the keyword value
     *
     * @return the keyword value
     */
    public abstract T getKeywordValue();

    /**
     * Set the keyword value
     *
     * @param keywordValue
     *            the keyword value to set
     */
    protected abstract void setKeywordValue(T keywordValue);

    @Override
    public String toString() {
        return String.format("%s %n[%n originatingControlledVocabulary=%s,%n keywordValue=%s%n]",
                this.getClass().getSimpleName(), getOriginatingControlledVocabulary(), getKeywordValue());
    }

}
