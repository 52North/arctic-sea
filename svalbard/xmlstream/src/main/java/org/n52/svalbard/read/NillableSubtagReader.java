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
package org.n52.svalbard.read;

import javax.xml.namespace.QName;

public abstract class NillableSubtagReader<T> extends NillableReader<T> {

    protected abstract QName getSubtagName();

    protected abstract XmlReader<T> getSubtagDelegate();

    @Override
    public XmlReader<T> getDelegate() {
        return new NillableSubtagReaderDelegate();
    }

    private class NillableSubtagReaderDelegate extends SubtagReader<T> {

        @Override
        protected QName getSubtagName() {
            return NillableSubtagReader.this.getSubtagName();
        }

        @Override
        protected XmlReader<T> getSubtagDelegate() {
            return NillableSubtagReader.this.getSubtagDelegate();
        }
    }

}
