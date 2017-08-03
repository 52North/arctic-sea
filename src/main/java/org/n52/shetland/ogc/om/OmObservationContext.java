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
package org.n52.shetland.ogc.om;

import java.util.Objects;

import org.n52.shetland.ogc.gml.ReferenceType;


/**
 * Representation of OGC O&M 2.0 ObservationContext
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class OmObservationContext {

    private ReferenceType role;
    private ReferenceType relatedObservation;

    /**
     * constructor
     *
     * @param role
     *            Role of the related observation
     * @param relatedObservation
     *            Reference to related observation
     */
    public OmObservationContext(ReferenceType role, ReferenceType relatedObservation) {
        this.role = role;
        this.relatedObservation = relatedObservation;
    }

    /**
     * @return the role
     */
    public ReferenceType getRole() {
        return role;
    }

    /**
     * @return the relatedObservation
     */
    public ReferenceType getRelatedObservation() {
        return relatedObservation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.role, 234, this.relatedObservation);
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(this, obj);
    }

}
