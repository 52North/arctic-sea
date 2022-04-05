/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
import java.util.TreeSet;

import javax.xml.namespace.QName;

import org.n52.shetland.w3c.wsdl.WSDLConstants.WSDLQNames;

public class Service extends AbstractWsdl {

    private Collection<Port> ports = new TreeSet<>();

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

    public Collection<Port> getPorts() {
        return ports;
    }

    public boolean isSetPorts() {
        return !getPorts().isEmpty();
    }
}
