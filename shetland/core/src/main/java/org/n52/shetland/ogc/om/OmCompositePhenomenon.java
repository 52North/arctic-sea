/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OmCompositePhenomenon extends AbstractPhenomenon implements Iterable<OmObservableProperty> {

    /**
     * the components of the composite phenomenon
     */
    private List<OmObservableProperty> phenomenonComponents;

    /**
     * standard constructor
     *
     * @param compPhenId
     *                             id of the composite phenomenon
     * @param compPhenDesc
     *                             description of the composite phenomenon
     * @param phenomenonComponents
     *                             components of the composite phenomenon
     */
    public OmCompositePhenomenon(String compPhenId, String compPhenDesc,
                                 List<OmObservableProperty> phenomenonComponents) {
        super(compPhenId, compPhenDesc);
        this.phenomenonComponents = phenomenonComponents;
    }

    public OmCompositePhenomenon(String identifier) {
        super(identifier);
        this.phenomenonComponents = new LinkedList<>();
    }

    public OmCompositePhenomenon(String identifier, String description) {
        super(identifier, description);
        this.phenomenonComponents = new LinkedList<>();
    }

    /**
     * Get observableProperties
     *
     * @return Returns the phenomenonComponents.
     */
    public List<OmObservableProperty> getPhenomenonComponents() {
        return phenomenonComponents;
    }

    /**
     * Set observableProperties
     *
     * @param phenomenonComponents
     *                             The phenomenonComponents to set.
     */
    public void setPhenomenonComponents(List<OmObservableProperty> phenomenonComponents) {
        this.phenomenonComponents = phenomenonComponents;
    }

    public void addPhenomenonComponent(OmObservableProperty observableProperty) {
        if (this.phenomenonComponents == null) {
            this.phenomenonComponents = new LinkedList<>();
        }
        this.phenomenonComponents.add(observableProperty);
    }

    @Override
    public Iterator<OmObservableProperty> iterator() {
        if (getPhenomenonComponents() == null) {
            return Collections.emptyIterator();
        } else {
            return getPhenomenonComponents().iterator();
        }
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
