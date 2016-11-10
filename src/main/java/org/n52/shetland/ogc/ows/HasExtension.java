/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.ows;

import org.n52.shetland.ogc.ows.exception.InvalidParameterValueException;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.Extensions;

/**
 * Interface to identify if the implemented class supportes
 * {@link Extensions}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 * @param <T>
 */
public interface HasExtension<T> {
    /**
     * Get the {@link Extension}s
     *
     * @return {@link Extensions} with {@link Extension}s
     */
    Extensions getExtensions();

    /**
     * Set the {@link Extensions} object
     *
     * @param extensions
     *                   the {@link Extensions} object to set
     *
     * @return this
     */
    T setExtensions(Extensions extensions);

    /**
     * Add a {@link Extensions} to this object
     *
     * @param extension
     *                  the {@link Extensions} to add
     *
     * @return this
     */
    T addExtensions(Extensions extension);

    /**
     * Add a {@link Extension} to this object
     *
     * @param extension
     *                  the {@link Extension} to add
     *
     * @return this
     */
    T addExtension(Extension<?> extension);

    /**
     * Check if {@link Extension}s are set
     *
     * @return <code>true</code>, if {@link Extensions} is not null or
     *         empty
     */
    boolean isSetExtensions();

    /**
     * Check if {@link Extension} for identifier is set
     *
     * @param identifier
     *                   Identifier to check
     *
     * @return <code>true</code>, if {@link Extensions} is available for the
     *         identifier
     */
    boolean hasExtension(
            Enum<?> identifier);

    /**
     * Check if {@link Extension} for identifier is set
     *
     * @param identifier
     *                   Identifier to check
     *
     * @return <code>true</code>, if {@link Extensions} is available for the
     *         identifier
     */
    boolean hasExtension(String identifier);

    /**
     * Get {@link Extension} for identifier
     *
     * @param identifier
     *                   Identifier to get {@link Extension} for
     *
     * @return The requested {@link Extension}
     *
     * @throws InvalidParameterValueException
     *                                        If an error occurs
     */
    Extension<?> getExtension(Enum<?> identifier) throws InvalidParameterValueException;

    /**
     * Get {@link Extension} for identifier
     *
     * @param identifier
     *                   Identifier to get {@link Extension} for
     *
     * @return The requested {@link Extension}
     *
     * @throws InvalidParameterValueException
     *                                        If an error occurs
     */
    Extension<?> getExtension(String identifier) throws InvalidParameterValueException;

}
