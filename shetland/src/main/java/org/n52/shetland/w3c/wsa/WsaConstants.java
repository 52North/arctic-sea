/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.w3c.wsa;

import javax.xml.namespace.QName;

/**
 * Constants for WS-Addressing
 *
 * @since 1.0.0
 *
 */
public interface WsaConstants {
    /**
     * WSA fault action URI
     */
    String WSA_FAULT_ACTION = "http://www.w3.org/2005/08/addressing/fault";

    /**
     * WSA namespace
     */
    String NS_WSA = "http://www.w3.org/2005/08/addressing";

    /**
     * WSA prefix
     */
    String NS_WSA_PREFIX = "wsa";

    /**
     * WSA to element
     */
    String EN_TO = "To";

    /**
     * WSA action element
     */
    String EN_ACTION = "Action";

    /**
     * WSA replyTo element
     */
    String EN_REPLY_TO = "ReplyTo";

    /**
     * WSA address element
     */
    String EN_ADDRESS = "Address";

    /**
     * WSA messageID element
     */
    String EN_MESSAGE_ID = "MessageID";

    /**
     * WSA relatesTo element
     */
    String EN_RELATES_TO = "RelatesTo";

    QName QN_TO = new QName(NS_WSA, EN_TO, NS_WSA_PREFIX);

    QName QN_ACTION = new QName(NS_WSA, EN_ACTION, NS_WSA_PREFIX);

    QName QN_REPLY_TO = new QName(NS_WSA, EN_REPLY_TO, NS_WSA_PREFIX);

    QName QN_ADDRESS = new QName(NS_WSA, EN_ADDRESS, NS_WSA_PREFIX);

    QName QN_MESSAGE_ID = new QName(NS_WSA, EN_MESSAGE_ID, NS_WSA_PREFIX);

    QName QN_RELATES_TO = new QName(NS_WSA, EN_RELATES_TO, NS_WSA_PREFIX);
}
