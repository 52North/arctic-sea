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

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import org.n52.shetland.w3c.wsdl.WSDLConstants.WSDLQNames;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class BindingOperation extends AbstractWsdl {

    private BindingInput input;
    private BindingOutput output;
    private final Collection<BindingFault> faults = new TreeSet<>();

    public BindingOperation(String name) {
        super(name);
    }

    @Override
    public QName getQName() {
        return WSDLQNames.QN_WSDL_OPERATION;
    }

    /**
     * @return the input
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public BindingInput getInput() {
        return input;
    }

    /**
     * @param input
     *            the input to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setInput(BindingInput input) {
        this.input = input;
    }

    public boolean isSetInput() {
        return getInput() != null;
    }

    /**
     * @return the output
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public BindingOutput getOutput() {
        return output;
    }

    /**
     * @param output
     *            the output to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setOutput(BindingOutput output) {
        this.output = output;
    }

    public boolean isSetOutput() {
        return getOutput() != null;
    }

    public Collection<BindingFault> getBindingFaults() {
        return Collections.unmodifiableCollection(faults);
    }

    public BindingOperation addBindingFault(BindingFault fault) {
        if (fault != null) {
            this.faults.add(fault);
        }
        return this;
    }

    public BindingOperation addBindingFaults(Collection<BindingFault> faults) {
        if (faults != null) {
            faults.forEach(p -> {
                addBindingFault(p);
            });
        }
        return this;
    }

    public BindingOperation setBindingFaults(Collection<BindingFault> faults) {
        this.faults.clear();
        return addBindingFaults(faults);
    }

    public boolean isSetBindingFaults() {
        return !getBindingFaults().isEmpty();
    }
}
