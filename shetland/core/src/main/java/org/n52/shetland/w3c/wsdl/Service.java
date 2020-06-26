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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.n52.shetland.w3c.wsdl.WSDLConstants.WSDLQNames;

public class Service extends AbstractWsdl {

    private List<Port> ports = new LinkedList<>();

    public Service(String name) {
        super(name);
    }

    @Override
    public QName getQName() {
        return WSDLQNames.QN_WSDL_SERVICE;
    }

    public Service addPort(Port port) {
        if (port != null) {
            this.ports.add(port);
        }
        return this;
    }

    public Service addPorts(Collection<Port> ports) {
        if (ports != null) {
            ports.forEach(p -> {
                addPort(p);
            });
        }
        return this;
    }

    public Service setPorts(Collection<Port> ports) {
        this.ports.clear();
        return addPorts(ports);
    }

    public List<Port> getPorts() {
        return ports;
    }

    public boolean isSetPorts() {
        return !getPorts().isEmpty();
    }
}
