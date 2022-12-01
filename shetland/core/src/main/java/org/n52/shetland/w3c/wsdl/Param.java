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

import java.net.URI;

import javax.xml.namespace.QName;

public abstract class Param extends AbstractWsdl {

    private QName message;
    private URI action;

    public Param() {
    }

    public Param(String name) {
        super(name);
    }

    public Param(String name, QName message) {
        super(name);
        this.message = message;
    }

    public Param(String name, URI action, QName message) {
        super(name);
        this.action = action;
        this.message = message;
    }

    /**
     * @return the message
     */
    public QName getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(QName message) {
        this.message = message;
    }

    public boolean isSetMessage() {
        return getMessage() != null;
    }

    /**
     * @return the action
     */
    public URI getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(URI action) {
        this.action = action;
    }

    public boolean isSetAction() {
        return getAction() != null;
    }
}
