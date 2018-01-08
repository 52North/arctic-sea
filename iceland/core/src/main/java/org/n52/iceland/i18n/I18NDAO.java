/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.i18n;

import java.util.Collection;
import java.util.Locale;

import org.n52.iceland.i18n.metadata.AbstractI18NMetadata;
import org.n52.janmayen.component.Component;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

/**
 * Interface for the I18N DAOs
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 *
 */
public interface I18NDAO<T extends AbstractI18NMetadata>
        extends Component<I18NDAOKey> {

    boolean isSupported();

    T getMetadata(String id)
            throws OwsExceptionReport;

    T getMetadata(String id, Locale locale)
            throws OwsExceptionReport;

    Collection<T> getMetadata()
            throws OwsExceptionReport;

    Collection<T> getMetadata(Collection<String> id)
            throws OwsExceptionReport;

    Collection<T> getMetadata(Collection<String> id, Locale locale)
            throws OwsExceptionReport;

    void saveMetadata(T i18n)
            throws OwsExceptionReport;

    Collection<Locale> getAvailableLocales()
            throws OwsExceptionReport;
}
