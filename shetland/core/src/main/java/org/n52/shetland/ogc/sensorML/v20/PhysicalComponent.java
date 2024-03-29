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

import org.n52.shetland.ogc.sensorML.HasProcessMethod;
import org.n52.shetland.ogc.sensorML.ProcessMethod;
import org.n52.shetland.util.IdGenerator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Class that represents SensorML 2.0 PhysicalComponent
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class PhysicalComponent extends AbstractPhysicalProcess implements HasProcessMethod {

    public static final String ID_PREFIX = "pc_";
    private ProcessMethod method;

    public PhysicalComponent() {
        setGmlId(ID_PREFIX + IdGenerator.generate(ID_PREFIX));
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public ProcessMethod getMethod() {
        return method;
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setMethod(ProcessMethod method) {
        this.method = method;
    }

}
