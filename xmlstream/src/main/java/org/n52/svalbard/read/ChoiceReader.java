/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.read;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.svalbard.decode.exception.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class ChoiceReader<T> extends XmlReader<T> {

    private final Map<QName, ? extends XmlReader<? extends T>> delegates;
    private T t;

    public ChoiceReader(Map<QName, ? extends XmlReader<? extends T>> delegates) {
        this.delegates = delegates;
    }

    @Override
    protected void read(QName name)
            throws XMLStreamException, DecodingException {
        XmlReader<? extends T> delegate = delegates.get(name);
        if (this.t == null && delegate != null) {
            this.t = delegate(delegate);
        } else {
            ignore();
        }
    }

    @Override
    protected T finish() {
        return t;
    }

}
