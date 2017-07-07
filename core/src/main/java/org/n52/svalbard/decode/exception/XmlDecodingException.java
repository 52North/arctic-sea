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
package org.n52.svalbard.decode.exception;

import org.apache.xmlbeans.XmlException;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class XmlDecodingException extends DecodingException {
    private static final long serialVersionUID = -495706406337738990L;

    public XmlDecodingException(String name, String xml, XmlException e) {
        super(String.format("Error while decoding %s:%n%s", name, xml), e);
    }

    public XmlDecodingException(String name, XmlException e) {
        super(String.format("Error while decoding %s", name), e);
    }
}
