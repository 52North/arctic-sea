/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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

public class DefaultActivationInitializer<K> implements SourceActivationInitializer<K> {

    private ActivationSource<K> source;

    public DefaultActivationInitializer(ActivationSource<K> source) {
        this.source = source;
    }

    public DefaultActivationInitializer() {
        this(null);
    }

    public void setSource(ActivationSource<K> source) {
        this.source = source;
    }

    @Override
    public ActivationSource<K> getSource() {
        return this.source;
    }

}
