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

import org.locationtech.jts.geom.Point;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * SOS internal representation of SensorML location
 *
 * @since 1.0.0
 */
public class SmlLocation {
    // TODO AssociationAttributeGroup values?
    // TODO support _Curve?

    private Point point;

    /**
     * constructor
     *
     * @param point
     *            Point
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SmlLocation(final Point point) {
        super();
        this.point = point;
    }

    /**
     * @return the point
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Point getPoint() {
        return point;
    }

    /**
     * @return if the point is set
     */
    public boolean isSetPoint() {
        return point != null;
    }

    /**
     * @param point
     *            Point
     * @return this
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SmlLocation setPoint(final Point point) {
        this.point = point;
        return this;
    }
}
