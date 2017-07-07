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

import javax.xml.stream.XMLStreamException;

import org.n52.shetland.w3c.W3CConstants;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.decode.exception.DecodingException;

public abstract class ReferenceableReader<T> extends XmlReader<Referenceable<T>> {

    private Referenceable<T> referenceable;

    @Override
    protected void begin() throws XMLStreamException, DecodingException {
        if (attr(W3CConstants.QN_XLINK_HREF).isPresent()) {
            this.referenceable = Referenceable.of(delegate(new ReferenceReader()));
        } else {
            this.referenceable = Referenceable.of(delegate(new NillableReaderImpl()));
        }
    }

    @Override
    protected Referenceable<T> finish()
            throws DecodingException {
        return referenceable;
    }

    protected abstract XmlReader<T> getDelegate();

    private class NillableReaderImpl extends NillableReader<T> {
        @Override
        protected XmlReader<T> getDelegate() {
            return ReferenceableReader.this.getDelegate();
        }
    }

}
