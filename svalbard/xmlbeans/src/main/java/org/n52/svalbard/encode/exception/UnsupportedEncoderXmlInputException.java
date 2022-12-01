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
package org.n52.svalbard.encode.exception;

import org.apache.xmlbeans.XmlObject;

import org.n52.svalbard.encode.Encoder;

import org.w3c.dom.Node;

public class UnsupportedEncoderXmlInputException extends UnsupportedEncoderInputException {
    private static final long serialVersionUID = -7244575661954080973L;

    public UnsupportedEncoderXmlInputException(Encoder<?, ?> encoder, XmlObject o) {
        super(encoder, o == null ? null : getName(o));
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
