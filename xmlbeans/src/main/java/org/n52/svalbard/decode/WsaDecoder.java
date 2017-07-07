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
package org.n52.svalbard.decode;


import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.soap.Node;
import javax.xml.soap.SOAPHeaderElement;

import org.n52.shetland.w3c.wsa.WsaActionHeader;
import org.n52.shetland.w3c.wsa.WsaConstants;
import org.n52.shetland.w3c.wsa.WsaHeader;
import org.n52.shetland.w3c.wsa.WsaMessageIDHeader;
import org.n52.shetland.w3c.wsa.WsaReplyToHeader;
import org.n52.shetland.w3c.wsa.WsaToHeader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * @since 1.0.0
 *
 */
public class WsaDecoder implements Decoder<List<WsaHeader>, List<SOAPHeaderElement>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsaDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS = Collections
            .<DecoderKey> singleton(new XmlNamespaceDecoderKey(WsaConstants.NS_WSA, SOAPHeaderElement.class));

    public WsaDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public List<WsaHeader> decode(List<SOAPHeaderElement> list) {
        List<WsaHeader> wsaHeaders = Lists.newArrayListWithCapacity(list.size());
        boolean to = false;
        boolean replyTo = false;
        boolean messageId = false;
        boolean action = false;
        for (SOAPHeaderElement soapHeaderElement : list) {
            if (soapHeaderElement.getLocalName().equals(WsaConstants.EN_TO)) {
                wsaHeaders.add(new WsaToHeader(soapHeaderElement.getValue()));
                to = true;
            } else if (soapHeaderElement.getLocalName().equals(WsaConstants.EN_ACTION)) {
                wsaHeaders.add(new WsaActionHeader(soapHeaderElement.getValue()));
                action = true;
            } else if (soapHeaderElement.getLocalName().equals(WsaConstants.EN_REPLY_TO)) {
                Iterator<?> iter = soapHeaderElement.getChildElements();
                while (iter.hasNext()) {
                    Node node = (Node) iter.next();
                    if (node.getLocalName() != null && node.getLocalName().equals(WsaConstants.EN_ADDRESS)) {
                        wsaHeaders.add(new WsaReplyToHeader(node.getValue()));
                        replyTo = true;
                    }
                }
            } else if (soapHeaderElement.getLocalName().equals(WsaConstants.EN_MESSAGE_ID)) {
                wsaHeaders.add(new WsaMessageIDHeader(soapHeaderElement.getValue()));
                messageId = true;
            }
        }
        if ((to || replyTo || messageId) && !action) {
            wsaHeaders.add(new WsaActionHeader(WsaConstants.WSA_FAULT_ACTION));
        }
        return wsaHeaders;
    }
}
