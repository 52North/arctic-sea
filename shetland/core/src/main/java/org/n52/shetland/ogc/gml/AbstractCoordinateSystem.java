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
package org.n52.shetland.ogc.gml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.n52.shetland.w3c.xlink.Referenceable;

/**
 * Internal representation of the OGC GML AbstractCoordinateSystem.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class AbstractCoordinateSystem extends IdentifiedObject {

    private final List<Referenceable<CoordinateSystemAxis>> coordinateSystemAxis = new ArrayList<>();

    private Aggregation aggregation;

    public AbstractCoordinateSystem(CodeWithAuthority identifier,
            Referenceable<CoordinateSystemAxis> coordinateSystemAxis) {
        super(identifier);
        addCoordinateSystemAxis(coordinateSystemAxis);
    }

    public AbstractCoordinateSystem(CodeWithAuthority identifier,
            Collection<Referenceable<CoordinateSystemAxis>> coordinateSystemAxis) {
        super(identifier);
        setCoordinateSystemAxis(coordinateSystemAxis);
    }

    /**
     * @return the coordinateSystemAxis
     */
    public List<Referenceable<CoordinateSystemAxis>> getCoordinateSystemAxis() {
        return Collections.unmodifiableList(coordinateSystemAxis);
    }

    /**
     * @param coordinateSystemAxis
     *            the coordinateSystemAxis to set
     *
     * @return {@code this}
     */
    public AbstractCoordinateSystem setCoordinateSystemAxis(
            Collection<Referenceable<CoordinateSystemAxis>> coordinateSystemAxis) {
        this.coordinateSystemAxis.clear();
        if (coordinateSystemAxis != null) {
            this.coordinateSystemAxis.addAll(coordinateSystemAxis);
        }
        return this;
    }

    /**
     * @param coordinateSystemAxis
     *            the coordinateSystemAxis to set
     *
     * @return {@code this}
     */
    public AbstractCoordinateSystem addCoordinateSystemAxis(
            Collection<Referenceable<CoordinateSystemAxis>> coordinateSystemAxis) {
        if (coordinateSystemAxis != null) {
            this.coordinateSystemAxis.addAll(coordinateSystemAxis);
        }
        return this;
    }

    /**
     * @param coordinateSystemAxis
     *            the coordinateSystemAxis to set
     *
     * @return {@code this}
     */
    public AbstractCoordinateSystem addCoordinateSystemAxis(Referenceable<CoordinateSystemAxis> coordinateSystemAxis) {
        if (coordinateSystemAxis != null) {
            this.coordinateSystemAxis.add(coordinateSystemAxis);
        }
        return this;
    }

    /**
     * @return the aggregation
     */
    public Aggregation getAggregation() {
        return aggregation;
    }

    /**
     * @param aggregation
     *            the aggregation to set
     *
     * @return {@code this}
     */
    public AbstractCoordinateSystem setAggregation(Aggregation aggregation) {
        this.aggregation = aggregation;
        return this;
    }

    public boolean isSetAggregation() {
        return getAggregation() != null;
    }

}
