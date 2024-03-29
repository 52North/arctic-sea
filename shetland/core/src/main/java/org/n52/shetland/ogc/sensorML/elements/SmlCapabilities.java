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
package org.n52.shetland.ogc.sensorML.elements;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.n52.shetland.ogc.swe.DataRecord;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Lists;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * SOS internal representation of SensorML capabilities
 *
 * @since 1.0.0
 */
public class SmlCapabilities extends AbstractSmlDataComponentContainer<SmlCapabilities> {

    private List<SmlCapability> capabilities = Lists.newArrayList();

    /**
     * default constructor
     */
    public SmlCapabilities() {
        super();
    }

    /**
     * constructor
     *
     * @param name
     *            Type
     */
    public SmlCapabilities(String name) {
        setName(name);
    }

    /**
     * constructor
     *
     * @param name
     *            Type
     * @param dataRecord
     *            DataRecord
     */
    public SmlCapabilities(String name, DataRecord dataRecord) {
        super(dataRecord);
        setName(name);
    }

    /**
     * @return the capabilities
     */
    public List<SmlCapability> getCapabilities() {
        if (!hasCapabilities() && isSetAbstractDataComponents()) {
            return getAbstractDataComponents().stream().map(component -> {
                SmlCapability smlCapability = new SmlCapability();
                if (component.isSetName()) {
                    smlCapability.setName(component.getName().getValue());
                } else if (component.isSetDefinition()) {
                    smlCapability.setName(component.getDefinition());
                }
                smlCapability.setAbstractDataComponent(component);
                return smlCapability;
            }).collect(toList());

        }
        return Collections.unmodifiableList(capabilities);
    }

    /**
     * @param capabilities
     *            the capabilities to set
     * @return this
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SmlCapabilities setCapabilities(List<SmlCapability> capabilities) {
        this.capabilities.clear();
        if (CollectionHelper.isNotEmpty(capabilities)) {
            this.capabilities.addAll(capabilities);
            capabilities.stream().map(SmlCapability::getAbstractDataComponent)
                    .forEach(this::addAbstractDataComponents);

        }
        return this;
    }

    /**
     * @param capabilities
     *            the capabilities to add
     */
    public SmlCapabilities addCapabilities(Collection<SmlCapability> capabilities) {
        if (CollectionHelper.isNotEmpty(capabilities)) {
            this.capabilities.addAll(capabilities);
            capabilities.stream().map(SmlCapability::getAbstractDataComponent)
                    .forEach(this::addAbstractDataComponents);
        }
        return this;
    }

    /**
     * @param capability
     *            the capability to add
     */
    public SmlCapabilities addCapability(SmlCapability capability) {
        if (capability != null) {
            this.capabilities.add(capability);
            addAbstractDataComponents(capability.getAbstractDataComponent());
        }
        return this;
    }

    public boolean isSetCapabilities() {
        return hasCapabilities() || isSetAbstractDataComponents();
    }

    private boolean hasCapabilities() {
        return CollectionHelper.isNotEmpty(capabilities);
    }

}
