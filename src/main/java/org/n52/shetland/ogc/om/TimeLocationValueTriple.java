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

import org.apache.commons.lang.builder.CompareToBuilder;

import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.om.values.Value;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Class that hold the time, the location and the value.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.4.0
 *
 */
public class TimeLocationValueTriple extends TimeValuePair {

    /**
     * Time location value triple value
     */
    private Geometry location;

    public TimeLocationValueTriple(Time time, Value<?> value, Geometry location) {
        super(time, value);
        this.location = location;
    }

    /**
     * @return the location
     */
    public Geometry getLocation() {
        return location;
    }

    /**
     * @param location
     *            the location to set
     */
    public void setLocation(Geometry location) {
        this.location = location;
    }

    public boolean isSetLocation() {
        return getLocation() != null && !getLocation().isEmpty();
    }

    @Override
    public int compareTo(TimeValuePair o) {
        CompareToBuilder compareToBuilder = new CompareToBuilder().appendSuper(super.compareTo(o));
        if (o instanceof TimeLocationValueTriple) {
            compareToBuilder.append(this.getLocation(), ((TimeLocationValueTriple) o).getLocation());
        }
        return compareToBuilder.toComparison();
    }

    @Override
    public int hashCode() {
        return 37 * super.hashCode() + Objects.hashCode(this.getLocation());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && Objects.equals(this.location, ((TimeLocationValueTriple) obj).location);
    }
}
