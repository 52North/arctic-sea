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
package org.n52.shetland.ogc;

import java.net.URI;

import com.google.common.base.Objects;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public abstract class AbstractCodeType {

    /**
     * Value/identifier
     */
    private String value;

    private URI codeSpace;

    public AbstractCodeType(final String value) {
        this.value = value;
    }

    public AbstractCodeType(final String value, final URI codespace) {
        this.value = value;
        this.codeSpace = codespace;
    }

    /**
     * Get value
     *
     * @return Value to set
     */
    public String getValue() {
        return value;
    }

    /**
     * Get code space
     *
     * @return Code space
     */
    public URI getCodeSpace() {
        return codeSpace;
    }

    /**
     * Set value and return this CodeType object
     *
     * @param value
     *            Value to set
     * @return This CodeType object
     */
    public AbstractCodeType setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * Set code space and return this CodeType object
     *
     * @param codeSpace
     *            Code space to set
     * @return This CodeType object
     */
    public AbstractCodeType setCodeSpace(URI codeSpace) {
        this.codeSpace = codeSpace;
        return this;
    }

    /**
     * Check whether value is set
     *
     * @return <code>true</code>, if value is set
     */
    public boolean isSetValue() {
        return this.value != null && !this.value.isEmpty();
    }

    /**
     * Check whether code space is set
     *
     * @return <code>true</code>, if code space is set
     */
    public boolean isSetCodeSpace() {
        return getCodeSpace() != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractCodeType) {
            AbstractCodeType that = (AbstractCodeType) obj;
            return Objects.equal(getClass(), that.getClass())
                    && Objects.equal(getValue(), that.getValue())
                    && Objects.equal(getCodeSpace(), that.getCodeSpace());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue(), getCodeSpace());
    }

}
