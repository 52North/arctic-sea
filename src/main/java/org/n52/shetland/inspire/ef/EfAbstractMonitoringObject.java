/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.inspire.ef;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;

/**
 * Class represents an AbstractMonitoringObject
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.3.0
 *
 */
public abstract class EfAbstractMonitoringObject extends AbstractFeature {

    public EfAbstractMonitoringObject() {
        this("");
    }

    /**
     * constructor
     *
     * @param identifier
     *            Feature identifier
     */
    public EfAbstractMonitoringObject(String identifier) {
        super(identifier);
    }

    /**
     * constructor
     *
     * @param identifier
     *            Feature identifier
     */
    public EfAbstractMonitoringObject(CodeWithAuthority identifier) {
        super(identifier);
    }

    /**
     * constructor
     *
     * @param identifier
     *            Feature identifier
     * @param gmlId
     *            GML id
     */
    public EfAbstractMonitoringObject(CodeWithAuthority identifier, String gmlId) {
        super(identifier, gmlId);
    }

}
