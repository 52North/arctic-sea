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

import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.iso.GcoConstants;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class NillableReader<T> extends XmlReader<Nillable<T>> {
    private Nillable<T> nillable;

    protected abstract XmlReader<T> getDelegate();

    protected List<QName> getPossibleNilReasonAttributes() {
        return Arrays.asList(AqdConstants.QN_NIL_REASON,
                             GcoConstants.QN_GCO_NIL_REASON);
    }

    @Override
    protected void begin()
            throws XMLStreamException, DecodingException {
        Optional<String> attr = attr(W3CConstants.QN_XSI_NIL);
        if (attr.isPresent() && attr.get().equals("true")) {
            List<QName> attributeNames = getPossibleNilReasonAttributes();
            Iterable<Optional<String>> attributes = attr(attributeNames);
            Iterable<String> reasons = Optional.presentInstances(attributes);
            this.nillable = Nillable.nil(Iterables.getFirst(reasons, null));
        } else {
            this.nillable = Nillable.of(delegate(getDelegate()));
        }
    }

    @Override
    protected Nillable<T> finish() {
        return nillable;
    }

}
