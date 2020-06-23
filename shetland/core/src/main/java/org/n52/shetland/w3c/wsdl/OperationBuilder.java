/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.namespace.QName;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 4.0.0
 */
public class OperationBuilder {
    private String name;
    private String version;
    private URI requestAction;
    private URI responseAction;
    private QName request;
    private QName response;
    private Collection<Fault> faults = new LinkedList<>();

    public OperationBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public OperationBuilder setVersion(String version) {
        this.version = version;
        return this;
    }

    public OperationBuilder setRequestAction(URI requestAction) {
        this.requestAction = requestAction;
        return this;
    }

    public OperationBuilder setResponseAction(URI responseAction) {
        this.responseAction = responseAction;
        return this;
    }

    public OperationBuilder setRequest(QName request) {
        this.request = request;
        return this;
    }

    public OperationBuilder setRequest(String namespace, String localpart) {
        return setRequest(new QName(namespace, localpart));
    }

    public OperationBuilder setResponse(QName response) {
        this.response = response;
        return this;
    }

    public OperationBuilder setResponse(String namespace, String localpart) {
        return setResponse(new QName(namespace, localpart));
    }

    public OperationBuilder setFaults(Collection<Fault> faults) {
        this.faults = new LinkedList<>(faults);
        return this;
    }

    public OperationBuilder addFault(Fault fault) {
        this.faults.add(fault);
        return this;
    }

    public OperationBuilder addFault(String name, URI action) {
        return addFault(new Fault(name, action));
    }

    public Operation build() {
        return new Operation(name, version, requestAction, responseAction, request, response, faults);
    }
}
