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

import java.net.URI;

import javax.xml.stream.XMLStreamException;

import org.n52.shetland.w3c.W3CConstants;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.svalbard.decode.exception.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ReferenceReader extends XmlReader<Reference> {

    private Reference reference;

    @Override
    protected void begin()
            throws XMLStreamException, DecodingException {
        this.reference = parseReference();
    }

    protected Reference parseReference() {
        Reference ref = new Reference();
        ref.setHref(URI.create(attr(W3CConstants.QN_XLINK_HREF).get()));
        ref.setActuate(attr(W3CConstants.QN_XLINK_ACTUATE).orNull());
        ref.setArcrole(attr(W3CConstants.QN_XLINK_ARCROLE).orNull());
        ref.setRole(attr(W3CConstants.QN_XLINK_ROLE).orNull());
        ref.setShow(attr(W3CConstants.QN_XLINK_SHOW).orNull());
        ref.setTitle(attr(W3CConstants.QN_XLINK_TITLE).orNull());
        ref.setType(attr(W3CConstants.QN_XLINK_TYPE).orNull());
        return ref;
    }

    @Override
    protected Reference finish() {
        return reference;
    }

}
