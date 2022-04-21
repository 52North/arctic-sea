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
package org.n52.shetland.ogc.sensorML;

import com.google.common.base.Strings;

/**
 * Abtract class represents SensorML 2.0 TermType.
 *
 * @author Carsten Hollmann
 * @since 1.0.0
 *
 */
public abstract class Term {

    private String name;

    private String label;

    private String definition;

    /**
     * Classifier codeSpace href.
     */
    private String codeSpace;

    private String value;

    public Term() {

    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the Identifier label
     */
    public String getLabel() {
        return name;
    }

    /**
     * @param label
     *            Identifier label
     */
    public void setLabel(final String label) {
        this.label = label;
        this.name = label;
    }

    /**
     * @return the Classifier definition
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * @param definition
     *            Identifier definition
     */
    public void setDefinition(final String definition) {
        this.definition = definition;
    }

    /**
     * @return the Classifier codeSpace href
     */
    public String getCodeSpace() {
        return codeSpace;
    }

    /**
     * @param codeSpace
     *            href Classifier codeSpace href
     */
    public void setCodeSpace(final String codeSpace) {
        this.codeSpace = codeSpace;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * @return <code>true</code>, if the name is set AND not empty
     */
    public boolean isSetName() {
        return !Strings.isNullOrEmpty(name);
    }

    /**
     * @return <code>true</code>, if the label is set AND not empty
     */
    public boolean isSetLabel() {
        return !Strings.isNullOrEmpty(label);
    }

    /**
     * @return <code>true</code>, if the codeSpace is set AND not empty
     */
    public boolean isSetCodeSpace() {
        return !Strings.isNullOrEmpty(codeSpace);
    }

    /**
     * @return <code>true</code>, if the codeSpace is set AND not empty
     */
    public boolean isSetDefinition() {
        return !Strings.isNullOrEmpty(definition);
    }

    /**
     * @return <code>true</code>, if the value is set AND not empty
     */
    public boolean isSetValue() {
        return !Strings.isNullOrEmpty(value);
    }

}
