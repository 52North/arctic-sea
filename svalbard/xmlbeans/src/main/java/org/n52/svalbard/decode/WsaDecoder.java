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
package org.n52.svalbard.decode;


import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.wsa.WsaActionHeader;
import org.n52.shetland.w3c.wsa.WsaConstants;
import org.n52.shetland.w3c.wsa.WsaHeader;
import org.n52.shetland.w3c.wsa.WsaMessageIDHeader;
import org.n52.shetland.w3c.wsa.WsaRelatesToHeader;
import org.n52.shetland.w3c.wsa.WsaReplyToHeader;
import org.n52.shetland.w3c.wsa.WsaToHeader;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x2005.x08.addressing.ActionDocument;
import org.w3.x2005.x08.addressing.MessageIDDocument;
import org.w3.x2005.x08.addressing.RelatesToDocument;
import org.w3.x2005.x08.addressing.ReplyToDocument;
import org.w3.x2005.x08.addressing.ToDocument;

import com.google.common.base.Joiner;

/**
 * @since 1.0.0
 *
 */
public class WsaDecoder extends AbstractXmlDecoder<XmlObject, WsaHeader> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsaDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS =
            CollectionHelper.union(CodingHelper.decoderKeysForElements(WsaConstants.NS_WSA, ToDocument.class,
                    ActionDocument.class, MessageIDDocument.class, RelatesToDocument.class, ReplyToDocument.class));

    public WsaDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public WsaHeader decode(XmlObject element) throws DecodingException {
       if (element instanceof ToDocument) {
           return parseToDocument((ToDocument) element);
       } else if (element instanceof ActionDocument) {
           return parseActionDocument((ActionDocument) element);
       } else if (element instanceof MessageIDDocument) {
           return parseMessageIDDocument((MessageIDDocument) element);
       } else if (element instanceof RelatesToDocument) {
           return parseRelatesToDocument((RelatesToDocument) element);
       } else if (element instanceof ReplyToDocument) {
           return parseReplyToDocument((ReplyToDocument) element);
       }
       throw new UnsupportedDecoderXmlInputException(this, element);
    }

    private WsaHeader parseToDocument(ToDocument element) {
        return new WsaToHeader(element.getTo().getStringValue());
    }

    private WsaHeader parseActionDocument(ActionDocument element) {
        return new WsaActionHeader(element.getAction().getStringValue());
    }

    private WsaHeader parseMessageIDDocument(MessageIDDocument element) {
        return new WsaMessageIDHeader(element.getMessageID().getStringValue());
    }

    private WsaHeader parseRelatesToDocument(RelatesToDocument element) {
        return new WsaRelatesToHeader(element.getRelatesTo().getStringValue());
    }

    private WsaHeader parseReplyToDocument(ReplyToDocument element) {
        return new WsaReplyToHeader(element.getReplyTo().getAddress().getStringValue());
    }

//    @Override
//    public List<WsaHeader> decode(List<SOAPHeaderElement> list) {
//        List<WsaHeader> wsaHeaders = Lists.newArrayListWithCapacity(list.size());
//        boolean to = false;
//        boolean replyTo = false;
//        boolean messageId = false;
//        boolean action = false;
//        for (SOAPHeaderElement soapHeaderElement : list) {
//            if (soapHeaderElement.getLocalName().equals(WsaConstants.EN_TO)) {
//                wsaHeaders.add(new WsaToHeader(soapHeaderElement.getValue()));
//                to = true;
//            } else if (soapHeaderElement.getLocalName().equals(WsaConstants.EN_ACTION)) {
//                wsaHeaders.add(new WsaActionHeader(soapHeaderElement.getValue()));
//                action = true;
//            } else if (soapHeaderElement.getLocalName().equals(WsaConstants.EN_REPLY_TO)) {
//                Iterator<?> iter = soapHeaderElement.getChildElements();
//                while (iter.hasNext()) {
//                    Node node = (Node) iter.next();
//                    if (node.getLocalName() != null && node.getLocalName().equals(WsaConstants.EN_ADDRESS)) {
//                        wsaHeaders.add(new WsaReplyToHeader(node.getValue()));
//                        replyTo = true;
//                    }
//                }
//            } else if (soapHeaderElement.getLocalName().equals(WsaConstants.EN_MESSAGE_ID)) {
//                wsaHeaders.add(new WsaMessageIDHeader(soapHeaderElement.getValue()));
//                messageId = true;
//            }
//        }
//        if ((to || replyTo || messageId) && !action) {
//            wsaHeaders.add(new WsaActionHeader(WsaConstants.WSA_FAULT_ACTION));
//        }
//        return wsaHeaders;
//    }
}
