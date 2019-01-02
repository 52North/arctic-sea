/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sensorML.v20;

import org.n52.shetland.ogc.sensorML.HasProcessMethod;
import org.n52.shetland.ogc.sensorML.ProcessMethod;
import org.n52.shetland.util.JavaHelper;

/**
 * Class that represents SensorML 2.0 SimpleProcess
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class SimpleProcess
        extends DescribedObject
        implements HasProcessMethod {

    public static final String ID_PREFIX = "sp_";
    private ProcessMethod method;

    public SimpleProcess() {
        setGmlId(ID_PREFIX + JavaHelper.generateID(ID_PREFIX));
    }

    @Override
    public ProcessMethod getMethod() {
        return method;
    }

    @Override
    public void setMethod(ProcessMethod method) {
        this.method = method;
    }

}
