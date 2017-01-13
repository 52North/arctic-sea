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
package org.n52.svalbard;

import org.apache.xmlbeans.XmlObject;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.w3c.dom.Node;

public class UnsupportedDecoderXmlInputException extends UnsupportedDecoderInputException {
    private static final long serialVersionUID = -7244575661954080973L;

    public UnsupportedDecoderXmlInputException(Decoder<?, ?> decoder, XmlObject o) {
        super(decoder, o == null ? null : getName(o));
    }

    private static String getName(XmlObject o) {
        return getName(o.getDomNode());
    }

    private static String getName(Node n) {
        if (n.getPrefix() == null || n.getPrefix().isEmpty()) {
            return n.getLocalName();
        } else {
            return n.getPrefix() + ":" + n.getLocalName();
        }
    }
}
