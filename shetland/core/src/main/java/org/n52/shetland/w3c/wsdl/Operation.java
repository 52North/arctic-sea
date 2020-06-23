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
import java.util.Collections;

import javax.xml.namespace.QName;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 4.0.0
 */
public class Operation {

    private final String name;
    private final String version;
    private final URI requestAction;
    private final URI responseAction;
    private final QName request;
    private final QName response;
    private final Collection<Fault> faults;

    public Operation(String name, String version, URI requestAction, URI responseAction, QName request,
            QName response, Collection<Fault> faults) {
        this.name = name;
        this.version = version;
        this.requestAction = requestAction;
        this.responseAction = responseAction;
        this.request = request;
        this.response = response;
        this.faults = faults;
    }

    public String getName() {
        return name;
    }

    public URI getRequestAction() {
        return requestAction;
    }

    public URI getResponseAction() {
        return responseAction;
    }

    public QName getRequest() {
        return request;
    }

    public QName getResponse() {
        return response;
    }

    public String getVersion() {
        return version;
    }

    public Collection<Fault> getFaults() {
        return Collections.unmodifiableCollection(faults);
    }

    public static OperationBuilder newWSDLOperation() {
        return new OperationBuilder();
    }
}
