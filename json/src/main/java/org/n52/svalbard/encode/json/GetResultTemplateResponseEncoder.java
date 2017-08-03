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
package org.n52.svalbard.encode.json;

import javax.inject.Inject;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosResultEncoding;
import org.n52.shetland.ogc.sos.SosResultStructure;
import org.n52.shetland.ogc.sos.response.GetResultTemplateResponse;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.encoding.SweAbstractEncoding;
import org.n52.shetland.ogc.swe.encoding.SweTextEncoding;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.DecoderRepository;
import org.n52.svalbard.decode.XmlNamespaceDecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.NoDecoderForKeyException;
import org.n52.svalbard.encode.exception.EncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GetResultTemplateResponseEncoder
        extends AbstractSosResponseEncoder<GetResultTemplateResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(GetResultTemplateResponseEncoder.class);

    private DecoderRepository decoderRepository;

    public GetResultTemplateResponseEncoder() {
        super(GetResultTemplateResponse.class, Sos2Constants.Operations.GetResultTemplate);
    }

    @Inject
    public void setDecoderRepository(DecoderRepository decoderRepository) {
        this.decoderRepository = decoderRepository;
    }

    @Override
    protected void encodeResponse(ObjectNode json, GetResultTemplateResponse t)
            throws EncodingException {
        encodeResultEncoding(t, json);
        encodeResultStructure(t, json);
    }

    private void encodeResultStructure(GetResultTemplateResponse t, ObjectNode json)
            throws EncodingException {
        ObjectNode jrs = json.putObject(JSONConstants.RESULT_STRUCTURE);
        SweAbstractDataComponent structure;
        SosResultStructure rs = t.getResultStructure();
        if (rs.isDecoded()) {
            structure = t.getResultStructure().get().get();
        } else {
            try {
                XmlNamespaceDecoderKey key =
                        new XmlNamespaceDecoderKey(SweConstants.NS_SWE_20, SweAbstractDataComponent.class);
                Decoder<SweAbstractDataComponent, XmlObject> decoder = this.decoderRepository.getDecoder(key);
                if (decoder == null) {
                    throw new NoDecoderForKeyException(key);
                }
                structure = decoder.decode(XmlObject.Factory.parse(rs.getXml().get()));
            } catch (XmlException | DecodingException ex) {
                throw new EncodingException(ex);
            }
        }
        if (structure instanceof SweDataRecord) {
            encodeSweDataRecord(structure, jrs);
        } else {
            LOG.warn("Unsupported structure: {}", structure == null ? null : structure.getClass());
        }
    }

    private void encodeResultEncoding(GetResultTemplateResponse t, ObjectNode json)
            throws EncodingException {
        ObjectNode jre = json.putObject(JSONConstants.RESULT_ENCODING);
        SweAbstractEncoding encoding = null;
        SosResultEncoding re = t.getResultEncoding();

        if (re.isDecoded()) {
            encoding = t.getResultEncoding().get().get();
        } else {
            try {
                XmlNamespaceDecoderKey key =
                        new XmlNamespaceDecoderKey(SweConstants.NS_SWE_20, SweAbstractEncoding.class);
                Decoder<SweAbstractEncoding, XmlObject> decoder = this.decoderRepository.getDecoder(key);
                if (decoder == null) {
                    throw new NoDecoderForKeyException(key);
                }
                encoding = decoder.decode(XmlObject.Factory.parse(re.getXml().get()));
            } catch (XmlException | DecodingException ex) {
                throw new EncodingException(ex);
            }
        }

        if (encoding instanceof SweTextEncoding) {
            encodeSweTextEncoding(encoding, jre);
        } else {
            LOG.warn("Unsupported encoding: {}", encoding == null ? null : encoding.getClass());
        }
    }

    private void encodeSweTextEncoding(SweAbstractEncoding encoding, ObjectNode node) {
        SweTextEncoding sweTextEncoding = (SweTextEncoding) encoding;
        String ts = sweTextEncoding.getTokenSeparator();
        if (ts != null && !ts.isEmpty()) {
            node.put(JSONConstants.TOKEN_SEPARATOR, ts);
        }
        String bs = sweTextEncoding.getBlockSeparator();
        if (bs != null && !bs.isEmpty()) {
            node.put(JSONConstants.BLOCK_SEPARATOR, bs);
        }
        String ds = sweTextEncoding.getDecimalSeparator();
        if (ds != null && !ds.isEmpty()) {
            node.put(JSONConstants.DECIMAL_SEPARATOR, ds);
        }
    }

    private void encodeSweDataRecord(SweAbstractDataComponent structure, ObjectNode node)
            throws EncodingException {
        SweDataRecord sweDataRecord = (SweDataRecord) structure;
        ArrayNode fields = node.putArray(JSONConstants.FIELDS);
        for (SweField field : sweDataRecord.getFields()) {
            fields.add(encodeObjectToJson(field));
        }
    }

}
