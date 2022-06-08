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
package org.n52.shetland.w3c.wsdl;

import javax.xml.namespace.QName;

import org.n52.shetland.w3c.wsdl.WSDLConstants.WSDLQNames;

public class Part extends AbstractWsdl {

    private QName element;

    public Part(String name) {
        super(name);
    }

    @Override
    public QName getQName() {
        return WSDLQNames.QN_WSDL_PART;
    }

    /**
     * @return the element
     */
    public QName getElement() {
        return element;
    }

    /**
     * @param element
     *            the element to set
     */
    public void setElement(QName element) {
        this.element = element;
    }

    public boolean isSetElement() {
        return getElement() != null;
    }

}
