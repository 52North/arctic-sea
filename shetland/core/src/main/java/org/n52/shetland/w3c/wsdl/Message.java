/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.w3c.wsdl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.n52.shetland.w3c.wsdl.WSDLConstants.WSDLQNames;

public class Message extends AbstractWsdl {

    private List<Part> parts = new LinkedList<>();

    public Message(String name) {
        super(name);
    }

    @Override
    public QName getQName() {
        return WSDLQNames.QN_WSDL_MESSAGE;
    }

    public Message addPart(Part part) {
        if (part != null) {
            this.parts.add(part);
        }
        return this;
    }

    public Message addParts(Collection<Part> parts) {
        if (parts != null) {
            parts.forEach(p -> {
                addPart(p);
            });
        }
        return this;
    }

    public Message setParts(Collection<Part> parts) {
        this.parts.clear();
        return addParts(parts);
    }

    public List<Part> getParts() {
        return parts;
    }

    public boolean isSetParts() {
        return getParts() != null && !getParts().isEmpty();
    }
}
