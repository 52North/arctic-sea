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
package org.n52.shetland.ogc.filter;

import org.n52.shetland.ogc.filter.FilterConstants.ComparisonOperator;
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

/**
 * OGC Filter class for comparison filter
 *
 * @since 1.0.0
 */
public class ComparisonFilter extends Filter<ComparisonOperator> {

    /**
     * Filter operator
     */
    private ComparisonOperator operator;

    /**
     * filter value
     */
    private String value;

    /**
     * filter value for between filter, value contains the lower value then
     */
    private String valueUpper;

    /**
     * escape character
     */
    private String escapeString;

    /**
     * wild card character
     */
    private String wildCard;

    /**
     * single char character
     */
    private String singleChar;

    /**
     * match case flag
     */
    private boolean matchCase = true;

    /**
     * default constructor
     */
    public ComparisonFilter() {
    }

    /**
     * constructor
     *
     * @param operator
     *            Filter operator
     * @param valueReference
     *            valueReference
     * @param value
     *            value
     */
    public ComparisonFilter(ComparisonOperator operator, String valueReference, String value) {
        super(valueReference);
        this.operator = operator;
        this.value = value;
    }

    /**
     * constructor for {@link ComparisonOperator#PropertyIsBetween} filter
     *
     * @param operator
     *            Filter operator
     * @param valueReference
     *            valueReference
     * @param value
     *            value
     * @param valueUpper
     *            upper value
     *
     * @throws OwsExceptionReport
     *             If operator is not {@link ComparisonOperator#PropertyIsBetween}
     */
    public ComparisonFilter(ComparisonOperator operator, String valueReference, String value, String valueUpper)
            throws OwsExceptionReport {
        super(valueReference);
        if (operator == ComparisonOperator.PropertyIsBetween) {
            this.operator = operator;
            this.value = value;
            this.valueUpper = valueUpper;
        } else {
            throw new NoApplicableCodeException()
                    .withMessage("Use other constructor for ComparisonFilter! This constructor can only "
                            + "be used for operator 'PropertyIsBetween'");
        }
    }

    /**
     * constructor for {@link ComparisonOperator#PropertyIsLike} filter
     *
     * @param operator
     *            Filter operator
     * @param valueReference
     *            valueReference
     * @param value
     *            value
     * @param valueUpper
     *            upper value for between filter
     * @param escapeString
     *            Escape characters
     *
     * @throws OwsExceptionReport
     *             If operator is not {@link ComparisonOperator#PropertyIsLike}
     */
    public ComparisonFilter(ComparisonOperator operator, String valueReference, String value, String valueUpper,
            String escapeString) throws OwsExceptionReport {
        super(valueReference);
        if (operator == ComparisonOperator.PropertyIsLike) {
            this.operator = operator;
            this.value = value;
            this.valueUpper = valueUpper;
            this.escapeString = escapeString;
        } else {
            throw new NoApplicableCodeException().withMessage("Use other constructor for ComparisonFilter! "
                    + "This constructor can only be used for operator 'PropertyIsLike'");
        }
    }

    @Override
    public ComparisonOperator getOperator() {
        return operator;
    }

    @Override
    public ComparisonFilter setOperator(ComparisonOperator operator) {
        this.operator = operator;
        return this;
    }

    /**
     * Get filter value
     *
     * @return filter value
     */
    public String getValue() {
        return value;
    }

    /**
     * Get upper filter value
     *
     * @return upper filter value
     */
    public String getValueUpper() {
        return valueUpper;
    }

    /**
     * Set filter value
     *
     * @param value
     *            filter value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Set upper filter value
     *
     * @param valueUpper
     *            upper filter value
     */
    public void setValueUpper(String valueUpper) {
        this.valueUpper = valueUpper;
    }

    /**
     * Get escape characters
     *
     * @return escape characters
     */
    public String getEscapeString() {
        return escapeString;
    }

    /**
     * Set escape characters
     *
     * @param escapeString
     *            escape characters
     */
    public void setEscapeString(String escapeString) {
        this.escapeString = escapeString;
    }

    /**
     * Get wild card character
     *
     * @return wild card character
     */
    public String getWildCard() {
        return wildCard;
    }

    /**
     * Set wild card character
     *
     * @param wildCard
     *            wild card character
     */
    public void setWildCard(String wildCard) {
        this.wildCard = wildCard;
    }

    /**
     * Get single char character
     *
     * @return single char character
     */
    public String getSingleChar() {
        return singleChar;
    }

    /**
     * Set single char character
     *
     * @param singleChar
     *            single char character
     */
    public void setSingleChar(String singleChar) {
        this.singleChar = singleChar;
    }

    /**
     * Check if value is not null or empty
     *
     * @return <code>true</code>, if value is not empty
     */
    public boolean isSetValue() {
        return this.value != null && !this.value.isEmpty();
    }

    /**
     * Check if value upper is not null or empty
     *
     * @return <code>true</code>, if value upper is not empty
     */
    public boolean isSetValueUpper() {
        return this.valueUpper != null && !this.valueUpper.isEmpty();
    }

    /**
     * Check if escape string is not null or empty
     *
     * @return <code>true</code>, if escape string is not empty
     */
    public boolean isSetEscapeString() {
        return this.escapeString != null && !this.escapeString.isEmpty();
    }

    /**
     * Check if wild card is not null or empty
     *
     * @return <code>true</code>, if wild card is not empty
     */
    public boolean isSetWildCard() {
        return this.wildCard != null && !this.wildCard.isEmpty();
    }

    /**
     * Check if single char is not null or empty
     *
     * @return <code>true</code>, if single char is not empty
     */
    public boolean isSetSingleChar() {
        return this.singleChar != null && !this.singleChar.isEmpty();
    }

    /**
     * @return the matchCase
     */
    public boolean isMatchCase() {
        return matchCase;
    }

    /**
     * @param matchCase
     *            the matchCase to set
     */
    public void setMatchCase(boolean matchCase) {
        this.matchCase = matchCase;
    }

    public ComparisonFilter copy() {
        ComparisonFilter copy = new ComparisonFilter();
        if (isSetEscapeString()) {
            copy.setEscapeString(getEscapeString());
        }
        if (isSetOperator()) {
            copy.setOperator(getOperator());
        }
        if (isSetSingleChar()) {
            copy.setSingleChar(getSingleChar());
        }
        if (isSetValue()) {
            copy.setValue(getValue());
        }
        if (isSetValueUpper()) {
            copy.setValueUpper(getValueUpper());
        }
        if (isSetWildCard()) {
            copy.setWildCard(getWildCard());
        }

        copy.setValueReference(getValueReference());
        copy.setMatchCase(isMatchCase());
        return copy;
    }

    @Override
    public String toString() {
        String result = "ComparisonFilter: ";
        if (isSetValueUpper()) {
            return result + getValueReference() + " " + getValue() + " " + getOperator().name() + " "
                    + getValueUpper();
        } else {
            return result + getValueReference() + " " + getOperator().name() + " " + getValue();
        }
    }

}
