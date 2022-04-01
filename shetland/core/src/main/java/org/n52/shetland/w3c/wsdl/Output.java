/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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

import java.net.URI;

import javax.xml.namespace.QName;

public class Output extends Param {

    public Output() {
        super();
    }

    public Output(String name) {
        super(name);
    }

    public Output(String name, QName message) {
        super(name, message);
    }

    public Output(String name, URI action, QName message) {
        super(name, action, message);
    }

    @Override
    public QName getQName() {
        return WSDLConstants.WSDLQNames.QN_WSDL_OUTPUT;
    }
}
