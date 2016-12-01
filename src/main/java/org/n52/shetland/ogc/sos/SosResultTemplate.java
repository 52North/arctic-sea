/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos;

import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.encoding.SweAbstractEncoding;
import org.n52.shetland.ogc.swe.encoding.SweTextEncoding;
import org.n52.sos.util.CodingHelper;
import org.n52.svalbard.decode.exception.DecoderResponseUnsupportedException;
import org.n52.svalbard.decode.exception.DecodingException;

/**
 * @since 4.0.0
 *
 */
public class SosResultTemplate {

    private CodeWithAuthority identifier;
    private String xmlResultStructure;
    private String xmResultEncoding;
    private SweAbstractDataComponent resultStructure;
    private SweAbstractEncoding resultEncoding;

    public CodeWithAuthority getIdentifier() {
        return identifier;
    }

    public void setIdentifier(CodeWithAuthority identifier) {
        this.identifier = identifier;
    }

    public String getXmlResultStructure() {
        return xmlResultStructure;
    }

    public void setXmlResultStructure(String xmlResultStructure) {
        this.xmlResultStructure = xmlResultStructure;
    }
    public String getXmResultEncoding() {
        return xmResultEncoding;
    }

    public void setXmResultEncoding(String xmResultEncoding) {
        this.xmResultEncoding = xmResultEncoding;
    }
    public SweAbstractDataComponent getResultStructure() throws DecodingException {
        if (resultStructure == null) {
            this.resultStructure = parseResultStructure();
        }
        return resultStructure;
    }

    public void setResultStructure(SweAbstractDataComponent resultStructure) {
        this.resultStructure = resultStructure;
    }
    public SweAbstractEncoding getResultEncoding() throws DecodingException {
        if (resultEncoding == null) {
            this.resultEncoding = parseResultEncoding();
        }
        return resultEncoding;
    }

    public void setResultEncoding(SweAbstractEncoding resultEncoding) {
        this.resultEncoding = resultEncoding;
    }

    private SweAbstractDataComponent parseResultStructure() throws DecodingException {
        Object decodedObject = CodingHelper.decodeXmlObject(xmlResultStructure);
        if (decodedObject instanceof SweDataRecord) {
            return (SweDataRecord) decodedObject;
        }
        throw new DecoderResponseUnsupportedException(xmlResultStructure, decodedObject);
    }

    private SweAbstractEncoding parseResultEncoding() throws DecodingException {
        Object decodedObject = CodingHelper.decodeXmlObject(xmResultEncoding);
        if (decodedObject instanceof SweTextEncoding) {
            return (SweTextEncoding) decodedObject;
        }
        throw new DecoderResponseUnsupportedException(xmResultEncoding, decodedObject);
    }
}
