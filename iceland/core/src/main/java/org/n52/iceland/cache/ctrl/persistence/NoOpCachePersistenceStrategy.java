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
package org.n52.iceland.cache.ctrl.persistence;

import java.util.Optional;

import org.n52.iceland.cache.ContentCache;
import org.n52.iceland.cache.ContentCachePersistenceStrategy;
import org.n52.iceland.cache.WritableContentCache;

/**
 * @author Christian Autermann
 */
public class NoOpCachePersistenceStrategy
        implements ContentCachePersistenceStrategy {

    @Override
    public void persistOnPartialUpdate(ContentCache cache) {
    }

    @Override
    public void persistOnCompleteUpdate(ContentCache cache) {
    }

    @Override
    public void persistOnShutdown(ContentCache cache) {
    }

    @Override
    public Optional<WritableContentCache> load() {
        return Optional.empty();
    }

    @Override
    public void remove() {
    }

}
