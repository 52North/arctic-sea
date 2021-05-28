/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
import java.util.Collections;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import org.n52.shetland.w3c.wsdl.WSDLConstants.WSDLQNames;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 4.0.0
 */
public class Operation extends AbstractWsdl {

    private Input input;
    private Output output;
    private final Collection<Fault> faults = new TreeSet<>();

    public Operation(String name) {
        super(name);
    }

    @Override
    public QName getQName() {
        return WSDLQNames.QN_WSDL_OPERATION;
    }

    /**
     * @return the input
     */
    public Input getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
    public void setInput(Input input) {
        this.input = input;
    }

    public boolean isSetInput() {
        return getInput() != null;
    }

    /**
     * @return the output
     */
    public Output getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(Output output) {
        this.output = output;
    }

    public boolean isSetOutput() {
        return getOutput() != null;
    }

    public Collection<Fault> getFaults() {
        return Collections.unmodifiableCollection(faults);
    }

    public Operation addFault(Fault fault) {
        if (fault != null) {
            this.faults.add(fault);
        }
        return this;
    }

    public Operation addFaults(Collection<Fault> faults) {
        if (faults != null) {
            faults.forEach(p -> {
                addFault(p);
            });
        }
        return this;
    }

    public Operation setFaults(Collection<Fault> faults) {
        this.faults.clear();
        return addFaults(faults);
    }

    public boolean isSetFaults() {
        return !getFaults().isEmpty();
    }
}
