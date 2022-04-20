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
package org.n52.shetland.ogc.sensorML;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.n52.shetland.ogc.sensorML.elements.SmlComponent;
import org.n52.shetland.ogc.sensorML.elements.SmlConnection;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class ProcessChain extends AbstractProcess implements HasComponents<ProcessChain>, HasConnections<ProcessChain> {

    private final List<SmlComponent> components = new ArrayList<>(0);
    private SmlConnection connections;

    @Override
    public List<SmlComponent> getComponents() {
        return Collections.unmodifiableList(components);
    }

    @Override
    public ProcessChain addComponents(final List<SmlComponent> components) {
        if (components != null) {
            this.components.addAll(components);
        }
        return this;
    }

    @Override
    public ProcessChain addComponent(final SmlComponent component) {
        if (component != null) {
            components.add(component);
        }
        return this;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public SmlConnection getConnections() {
        return connections;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public ProcessChain setConnections(SmlConnection connections) {
        this.connections = connections;
        return this;
    }

    @Override
    public boolean isAggragation() {
        return true;
    }

    @Override
    public String getDefaultElementEncoding() {
        return SensorMLConstants.NS_SML;
    }
}
