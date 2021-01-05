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
package org.n52.iceland.util.activation;

import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *
 * @author Christian Autermann
 */
public interface ActivationSource<K> extends ActivationProvider<K> {

    Set<K> getKeys();

    static <K> ActivationSource<K> create(Predicate<? super K> isActive, Supplier<? extends Set<K>> getKeys) {
        return new ActivationSource<K>() {
            @Override
            public Set<K> getKeys() {
                return getKeys.get();
            }

            @Override
            public boolean isActive(K key) {
                return isActive.test(key);
            }
        };
    }

}
