/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.filter;

import java.util.Arrays;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 *
 * @since 1.0.0
 */
public class Envelope implements Geometry {

    private int srid = -1;

    // a coordinate position consisting of all the minimal ordinates for each dimension for all points within the envelope
    private Double[] lowerCorner;

    // a coordinate position consisting of all the maximal ordinates for each dimension for all points within the envelope
    private Double[] upperCorner;

    public Envelope() {
        //
    }

    public Envelope(Double[] lowerCorner, Double[] upperCorner) {
        this.lowerCorner = lowerCorner;
        this.upperCorner = upperCorner;
    }

    public Envelope(Double[] lowerCorner, Double[] upperCorner, int srid) {
        this.lowerCorner = lowerCorner;
        this.upperCorner = upperCorner;
        this.srid = srid;
    }

    public Envelope setLowerCorner(Double[] corner) {
        this.lowerCorner = corner;
        return this;
    }

    public Envelope setUpperCorner(Double[] corner) {
        this.upperCorner = corner;
        return this;
    }

    public Double[] getLowerCorner() {
        return lowerCorner;
    }

    public Double[] getUpperCorner() {
        return upperCorner;
    }

    public Envelope setSrid(int srid) {
        this.srid = srid;
        return this;
    }

    public boolean isSetSrid() {
        return getSRID() > 0;
    }

    @Override
    public int getSRID() {
        return srid;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.srid;
        hash = 43 * hash + Arrays.deepHashCode(this.lowerCorner);
        hash = 43 * hash + Arrays.deepHashCode(this.upperCorner);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Envelope other = (Envelope) obj;
        if (this.srid != other.srid) {
            return false;
        }
        if (!Arrays.deepEquals(this.lowerCorner, other.lowerCorner)) {
            return false;
        }
        if (!Arrays.deepEquals(this.upperCorner, other.upperCorner)) {
            return false;
        }
        return true;
    }

}
