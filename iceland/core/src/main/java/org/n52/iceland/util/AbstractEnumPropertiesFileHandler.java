/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.util;

import javax.servlet.ServletContext;

import org.n52.faroe.ConfigurationError;


/**
 * @since 2.0.0
 *
 * @param <E> Enum type
 */
public abstract class AbstractEnumPropertiesFileHandler<E extends Enum<E>> extends
        ServletContextPropertyFileHandler {

    protected AbstractEnumPropertiesFileHandler(ServletContext ctx, String name) {
        super(ctx, name);
    }

    public String get(E e) throws ConfigurationError {
        return get(e.name());
    }

    public void save(E e, String value) throws ConfigurationError {
        save(e.name(), value);
    }
}
