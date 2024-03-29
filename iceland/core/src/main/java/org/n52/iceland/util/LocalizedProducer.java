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
package org.n52.iceland.util;

import java.util.Locale;
import java.util.Set;

import org.n52.janmayen.Producer;

/**
 * Generic Factory interface.
 *
 * @param <T>
 *            the type to produce
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 *
 */
public interface LocalizedProducer<T> extends Producer<T> {

    /**
     * Get language specific Producer result
     *
     * @param language
     *                 The resulting language
     *
     * @return Result in the specified language
     */
    T get(Locale language);

    /**
     * Gets the {@code Locale}s available to this {@code Producer}.
     *
     * @return the locales
     */
    Set<Locale> getAvailableLocales();
}
