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
package org.n52.svalbard.encode;

import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosResultEncoding;
import org.n52.shetland.ogc.sos.SosResultStructure;
import org.n52.shetland.ogc.sos.response.GetResultTemplateResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.XmlHelper;

import com.google.common.collect.Sets;

import net.opengis.sos.x20.GetResultTemplateResponseDocument;
import net.opengis.sos.x20.GetResultTemplateResponseType;
import net.opengis.sos.x20.GetResultTemplateResponseType.ResultEncoding;
import net.opengis.sos.x20.GetResultTemplateResponseType.ResultStructure;
import net.opengis.swe.x20.DataRecordDocument;
import net.opengis.swe.x20.TextEncodingDocument;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GetResultTemplateResponseEncoder
        extends AbstractSosResponseEncoder<GetResultTemplateResponse> {

    public GetResultTemplateResponseEncoder() {
        super(Sos2Constants.Operations.GetResultTemplate.name(), GetResultTemplateResponse.class);
    }

    @Override
    protected XmlObject create(GetResultTemplateResponse response)
            throws EncodingException {
        GetResultTemplateResponseDocument doc = GetResultTemplateResponseDocument.Factory.newInstance(getXmlOptions());
        GetResultTemplateResponseType xbResponse = doc.addNewGetResultTemplateResponse();
        xbResponse.setResultEncoding(createResultEncoding(response.getResultEncoding()));
        xbResponse.setResultStructure(createResultStructure(response.getResultStructure()));
        return doc;
    }

    private ResultEncoding createResultEncoding(SosResultEncoding resultEncoding)
            throws EncodingException {
        // TODO move encoding to SWECommonEncoder
        final TextEncodingDocument xbEncoding;
        if (resultEncoding.isEncoded()) {
            try {
                xbEncoding = TextEncodingDocument.Factory.parse(resultEncoding.getXml().get());
            } catch (XmlException ex) {
                throw unsupportedResultEncoding(ex);
            }
        } else {
            XmlObject xml = encodeSwe(EncodingContext.of(XmlBeansEncodingFlags.DOCUMENT), resultEncoding.get().get());
            if (xml instanceof TextEncodingDocument) {
                xbEncoding = (TextEncodingDocument) xml;
            } else {
                throw unsupportedResultEncoding();
            }

        }
        ResultEncoding xbResultEncoding = ResultEncoding.Factory.newInstance(getXmlOptions());
        xbResultEncoding.addNewAbstractEncoding().set(xbEncoding.getTextEncoding());
        XmlHelper.substituteElement(xbResultEncoding.getAbstractEncoding(), xbEncoding.getTextEncoding());
        return xbResultEncoding;
    }

    private ResultStructure createResultStructure(SosResultStructure resultStructure)
            throws EncodingException {
        // TODO move encoding to SWECommonEncoder
        final DataRecordDocument dataRecordDoc;
        if (resultStructure.isEncoded()) {
            try {
                dataRecordDoc = DataRecordDocument.Factory.parse(resultStructure.getXml().get());
            } catch (XmlException ex) {
                throw unsupportedResultStructure(ex);
            }
        } else {
            XmlObject xml = encodeSwe(EncodingContext.of(XmlBeansEncodingFlags.DOCUMENT), resultStructure.get().get());
            if (xml instanceof DataRecordDocument) {
                dataRecordDoc = (DataRecordDocument) xml;
            } else {
                throw unsupportedResultStructure();
            }
        }
        ResultStructure xbResultStructure = ResultStructure.Factory.newInstance(getXmlOptions());
        xbResultStructure.addNewAbstractDataComponent().set(dataRecordDoc.getDataRecord());
        XmlHelper.substituteElement(xbResultStructure.getAbstractDataComponent(), dataRecordDoc.getDataRecord());
        return xbResultStructure;
    }

    @Override
    public Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(Sos2Constants.SOS_GET_RESULT_TEMPLATE_SCHEMA_LOCATION);
    }

    private static EncodingException unsupportedResultEncoding(Throwable cause) {
        return new EncodingException("ResultEncoding element encoding is not supported!", cause);
    }

    private static EncodingException unsupportedResultEncoding() {
        return unsupportedResultEncoding(null);
    }

    private static EncodingException unsupportedResultStructure(Throwable cause) {
        return new EncodingException("ResultStructure element encoding is not supported!", cause);
    }

    private static EncodingException unsupportedResultStructure() {
        return unsupportedResultStructure(null);
    }
}
