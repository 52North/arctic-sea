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
package org.n52.shetland.ogc.om;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class OmCompositePhenomenon extends AbstractPhenomenon implements Iterable<OmObservableProperty> {

    /**
     * the components of the composite phenomenon
     */
    private List<OmObservableProperty> phenomenonComponents = new LinkedList<>();

    /**
     * standard constructor
     *
     * @param compPhenId
     *            id of the composite phenomenon
     * @param compPhenDesc
     *            description of the composite phenomenon
     * @param phenomenonComponents
     *            components of the composite phenomenon
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public OmCompositePhenomenon(String compPhenId, String compPhenDesc,
            Collection<OmObservableProperty> phenomenonComponents) {
        super(compPhenId, compPhenDesc);
        setPhenomenonComponents(phenomenonComponents);
    }

    public OmCompositePhenomenon(String identifier) {
        super(identifier);
    }

    public OmCompositePhenomenon(String identifier, String description) {
        super(identifier, description);
    }

    /**
     * Get observableProperties
     *
     * @return Returns the phenomenonComponents.
     */
    public List<OmObservableProperty> getPhenomenonComponents() {
        return Collections.unmodifiableList(phenomenonComponents);
    }

    /**
     * Set observableProperties
     *
     * @param phenomenonComponents
     *            The phenomenonComponents to set.
     * @return this
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public OmCompositePhenomenon setPhenomenonComponents(Collection<OmObservableProperty> phenomenonComponents) {
        this.phenomenonComponents.clear();
        if (phenomenonComponents != null) {
            this.phenomenonComponents.addAll(phenomenonComponents);
        }
        return this;
    }

    public OmCompositePhenomenon addPhenomenonComponent(OmObservableProperty observableProperty) {
        if (observableProperty != null) {
            this.phenomenonComponents.add(observableProperty);
        }
        return this;
    }

    @Override
    public Iterator<OmObservableProperty> iterator() {
        return getPhenomenonComponents().iterator();
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    @Override
    public boolean isObservableProperty() {
        return false;
    }
}
