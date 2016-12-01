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
package org.n52.shetland.ogc.sos;

import org.n52.shetland.ogc.swe.SweAbstractDataComponent;

/**
 * @since 4.0.0
 *
 */
public class SosResultStructure extends ObjectWithXmlString<SweAbstractDataComponent> {
    public SosResultStructure(SweAbstractDataComponent object) {
        super(object);
    }

    public SosResultStructure(String xml) {
        super(xml);
    }

    public SosResultStructure(SweAbstractDataComponent object, String xml) {
        super(object, xml);
    }
}
