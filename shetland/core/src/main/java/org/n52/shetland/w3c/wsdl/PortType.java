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

public class PortType extends AbstractWsdl {

    private Collection<Operation> operations = new TreeSet<>();

    public PortType(String name) {
        super(name);
    }

    @Override
    public QName getQName() {
        return  WSDLQNames.QN_WSDL_PORT_TYPE;
    }

    public PortType addOperation(Operation operation) {
        if (operation != null) {
            this.operations.add(operation);
        }
        return this;
    }

    public PortType addOperations(Collection<Operation> operations) {
        if (operations != null) {
            operations.forEach(p -> {
                addOperation(p);
            });
        }
        return this;
    }

    public PortType setOperations(Collection<Operation> operations) {
        this.operations.clear();
        return addOperations(operations);
    }

    public Collection<Operation> getOperations() {
        return operations;
    }

    public boolean isSetOperations() {
        return !getOperations().isEmpty();
    }

    public Operation getOperation(String name, String request, String response) {
        for (Operation operation : operations) {
            if (operation.getName()
                    .equals(name)
                    && operation.getInput()
                            .getName()
                            .equals(request)
                    && operation.getOutput()
                            .getName()
                            .equals(response)) {
                return operation;
            }
        }
        return null;
    }

}
