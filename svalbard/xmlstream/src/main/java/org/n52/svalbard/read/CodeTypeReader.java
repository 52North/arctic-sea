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

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.stream.XMLStreamException;

import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.svalbard.decode.exception.DecodingException;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class CodeTypeReader extends XmlReader<CodeType> {

    private CodeType codeType;

    @Override
    protected void begin() throws XMLStreamException, DecodingException {
        String codeSpace = attr(AqdConstants.AN_CODE_SPACE).orNull();
        try {
            if (!Strings.isNullOrEmpty(codeSpace)) {
                this.codeType = new CodeType(chars(), new URI(codeSpace));
            } else {
                this.codeType = new CodeType(chars());
            }
        } catch (URISyntaxException e) {
            throw new DecodingException(e, "Error while creating URI from '{}'",
                    codeSpace);
        }
    }

    @Override
    protected CodeType finish() {
        return this.codeType;
    }

}
