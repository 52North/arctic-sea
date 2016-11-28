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
package org.n52.iceland.i18n;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.n52.janmayen.component.AbstractComponentRepository;
import org.n52.iceland.i18n.metadata.AbstractI18NMetadata;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.janmayen.Producer;

import com.google.common.collect.Maps;


/**
 * I18N DAO repository
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 */
@SuppressWarnings("rawtypes")
public class I18NDAORepository extends AbstractComponentRepository<I18NDAOKey, I18NDAO<?>, I18NDAOFactory> implements Constructable {
    @Deprecated
    private static I18NDAORepository instance;
    private final Map<I18NDAOKey, Producer<I18NDAO<?>>> daos = Maps.newHashMap();

    @Autowired(required = false)
    private Collection<I18NDAO<?>> components;
    @Autowired(required = false)
    private Collection<I18NDAOFactory> componentFactories;

    @Override
    public void init() {
        I18NDAORepository.instance = this;
        this.daos.clear();
        this.daos.putAll(getUniqueProviders(this.components, this.componentFactories));
    }

    /**
     * Get the available DAO
     *
     * @param <T> the meta data type
     * @param c the meta data class
     * @return the loaded DAO
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractI18NMetadata> I18NDAO<T> getDAO(Class<T> c) {
        Producer<I18NDAO<?>> producer = daos.get(new I18NDAOKey(c));
        // TODO check for subtypes
        I18NDAO<?> dao = producer == null ? null : producer.get();
        return (I18NDAO<T>) dao;
    }

    /**
     * Get the singleton instance of the I18NDAORepository.
     *
     * @return Returns a singleton instance of the I18NDAORepository.
     */
    @Deprecated
    public static I18NDAORepository getInstance() {
        return I18NDAORepository.instance;
    }

}
