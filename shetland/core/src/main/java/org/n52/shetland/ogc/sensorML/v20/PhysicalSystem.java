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
package org.n52.shetland.ogc.sensorML.v20;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.ogc.sensorML.HasComponents;
import org.n52.shetland.ogc.sensorML.HasConnections;
import org.n52.shetland.ogc.sensorML.elements.SmlComponent;
import org.n52.shetland.ogc.sensorML.elements.SmlConnection;
import org.n52.shetland.util.IdGenerator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Class that represents SensorML 2.0 PhysicalSystem
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class PhysicalSystem extends AbstractPhysicalProcess
        implements HasComponents<PhysicalSystem>, HasConnections<PhysicalSystem> {

    public static final String ID_PREFIX = "ps_";

    private final List<SmlComponent> components = new LinkedList<>();

    private SmlConnection connections;

    public PhysicalSystem() {
        setGmlId(ID_PREFIX + IdGenerator.generate(ID_PREFIX));
    }

    @Override
    public List<SmlComponent> getComponents() {
        return Collections.unmodifiableList(components);
    }

    @Override
    public PhysicalSystem addComponents(final List<SmlComponent> components) {
        if (components != null) {
            this.components.addAll(components);
        }
        return this;
    }

    @Override
    public PhysicalSystem addComponent(final SmlComponent component) {
        if (component != null) {
            components.add(component);
        }
        return this;
    }

    @Override
    public boolean isAggragation() {
        return true;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public SmlConnection getConnections() {
        return connections;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public PhysicalSystem setConnections(SmlConnection connections) {
        this.connections = connections;
        return this;
    }

}
