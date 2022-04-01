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

import java.util.Collection;
import java.util.TreeSet;

import javax.xml.namespace.QName;

public class Binding extends AbstractWsdl {

    private QName type;
    private Collection<BindingOperation> bindingOperations = new TreeSet<>();

    public Binding(String name) {
        super(name);
    }

    public Binding(QName type) {
        this.type = type;
    }

    public Binding(String name, QName type) {
        super(name);
        this.type = type;
    }

    @Override
    public QName getQName() {
        return WSDLConstants.WSDLQNames.QN_WSDL_BINDING;
    }

    /**
     * @return the type
     */
    public QName getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(QName type) {
        this.type = type;
    }

    public Binding addBindingOperation(BindingOperation bindingOperation) {
        if (bindingOperation != null) {
            this.bindingOperations.add(bindingOperation);
        }
        return this;
    }

    public Binding addBindingOperations(Collection<BindingOperation> bindingOperations) {
        if (bindingOperations != null) {
            bindingOperations.forEach(p -> {
                addBindingOperation(p);
            });
        }
        return this;
    }

    public Binding setBindingOperations(Collection<BindingOperation> bindingOperations) {
        this.bindingOperations.clear();
        return addBindingOperations(bindingOperations);
    }

    public Collection<BindingOperation> getBindingOperations() {
        return bindingOperations;
    }

    public boolean isSetBindingOperations() {
        return !getBindingOperations().isEmpty();
    }
}
