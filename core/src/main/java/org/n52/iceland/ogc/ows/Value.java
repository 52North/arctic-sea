/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.ows;

/**
 * Generic interface for values
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 * @param <T>
 * @param <S>
 */
public interface Value<T, S> {

    /**
     * Get value
     *
     * @return value
     */
    public abstract T getValue();

    /**
     * Get value as {@link String}
     *
     * @return The value as {@link String}
     */
    public abstract String getStringValue();

    /**
     * Is the values set
     *
     * @return <code>true</code>, if the value is set
     */
    public abstract boolean isSetValue();

    /**
     * Set value
     *
     * @param value
     *            value to set
     * @return this
     */
    public abstract S setValue(T value);

}
