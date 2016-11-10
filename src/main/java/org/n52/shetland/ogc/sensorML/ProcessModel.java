/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sensorML;

/**
 * @since 4.0.0
 *
 */
public class ProcessModel extends AbstractProcess implements HasProcessMethod {

    private static final long serialVersionUID = -5490781462864023242L;

    private ProcessMethod method;

    public ProcessMethod getMethod() {
        return method;
    }

    public void setMethod(final ProcessMethod method) {
        this.method = method;
    }

    @Override
    public boolean isSetMethod() {
        return method != null;
    }
}
