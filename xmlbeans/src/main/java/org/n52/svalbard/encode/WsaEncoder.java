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

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.w3c.wsa.WsaActionHeader;
import org.n52.shetland.w3c.wsa.WsaConstants;
import org.n52.shetland.w3c.wsa.WsaHeader;
import org.n52.shetland.w3c.wsa.WsaMessageIDHeader;
import org.n52.shetland.w3c.wsa.WsaRelatesToHeader;
import org.n52.shetland.w3c.wsa.WsaReplyToHeader;
import org.n52.shetland.w3c.wsa.WsaToHeader;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlOptionsHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x2005.x08.addressing.ActionDocument;
import org.w3.x2005.x08.addressing.MessageIDDocument;
import org.w3.x2005.x08.addressing.RelatesToDocument;
import org.w3.x2005.x08.addressing.ReplyToDocument;
import org.w3.x2005.x08.addressing.ToDocument;

import com.google.common.base.Joiner;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @since 1.0.0
 *
 */
public class WsaEncoder implements Encoder<XmlObject, WsaHeader> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsaEncoder.class);

    private static final Set<EncoderKey> ENCODER_KEYS =
            CodingHelper.encoderKeysForElements(WsaConstants.NS_WSA, WsaHeader.class);

    public WsaEncoder() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public MediaType getContentType() {
        return MediaTypes.TEXT_XML;
    }

    @Override
    public XmlObject encode(WsaHeader wsaHeader) throws EncodingException {
        return encode(wsaHeader, EncodingContext.empty());
    }

    @Override
    @SuppressFBWarnings("NP_LOAD_OF_KNOWN_NULL_VALUE")
    public XmlObject encode(WsaHeader wsaHeader, EncodingContext additionalValues) throws EncodingException {
        if (wsaHeader == null) {
            throw new UnsupportedEncoderInputException(this, wsaHeader);
        }
        if (!wsaHeader.isSetValue()) {
            return null;
        }
        if (wsaHeader instanceof WsaReplyToHeader) {
            return encodeReplyToHeader((WsaReplyToHeader) wsaHeader);
        } else if (wsaHeader instanceof WsaMessageIDHeader) {
            return encodeMessageIDHeader((WsaMessageIDHeader) wsaHeader);
        } else if (wsaHeader instanceof WsaActionHeader) {
            return encodeActionHeader((WsaActionHeader) wsaHeader);
        } else if (wsaHeader instanceof WsaToHeader) {
            return encodeToHeader((WsaToHeader) wsaHeader);
        } else if (wsaHeader instanceof WsaRelatesToHeader) {
            return encodeRelatesToHeader((WsaRelatesToHeader) wsaHeader);
        } else {
            throw new UnsupportedEncoderInputException(this, wsaHeader);
        }
    }

    private XmlObject encodeReplyToHeader(WsaReplyToHeader wsaHeader) {
        ReplyToDocument replyToDoc = ReplyToDocument.Factory.newInstance(getXmlOptions());
        replyToDoc.addNewReplyTo().addNewAddress().setStringValue(wsaHeader.getValue());
        return replyToDoc;
    }

    private XmlObject encodeRelatesToHeader(WsaRelatesToHeader wsaHeader) {
        RelatesToDocument relatesToDoc = RelatesToDocument.Factory.newInstance(getXmlOptions());
        relatesToDoc.addNewRelatesTo().setStringValue(wsaHeader.getValue());
        return relatesToDoc;
    }

    private XmlObject encodeMessageIDHeader(WsaMessageIDHeader wsaHeader) {
        MessageIDDocument messageIDDoc = MessageIDDocument.Factory.newInstance(getXmlOptions());
        messageIDDoc.addNewMessageID().setStringValue(wsaHeader.getValue());
        return null;
    }

    private XmlObject encodeActionHeader(WsaActionHeader wsaHeader) {
        ActionDocument actionDoc = ActionDocument.Factory.newInstance(getXmlOptions());
        actionDoc.addNewAction().setStringValue(wsaHeader.getValue());
        return actionDoc;
    }

    private XmlObject encodeToHeader(WsaToHeader wsaHeader) {
        ToDocument toDoc = ToDocument.Factory.newInstance(getXmlOptions());
        toDoc.addNewTo().setStringValue(wsaHeader.getValue());
        return toDoc;
    }

    private static XmlOptions getXmlOptions() {
        return XmlOptionsHelper.getInstance().getXmlOptions();
    }

}
