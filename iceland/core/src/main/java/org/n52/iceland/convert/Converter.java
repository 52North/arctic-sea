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
package org.n52.iceland.convert;

import org.n52.janmayen.component.Component;

/**
 * Interface to convert an object into another object, e.g. SensorML 1.0.1 to
 * SensorML 2.0
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 * @param <T>
 *            Target object
 * @param <S>
 *            Source object
 */
public interface Converter<T, S> extends Component<ConverterKey> {

    /**
     * Convert object to target
     *
     * @param objectToConvert
     *            Object to convert
     * @return The converted object
     * @throws ConverterException
     *             If an error occurs during the conversion
     */
    T convert(S objectToConvert) throws ConverterException;

}
