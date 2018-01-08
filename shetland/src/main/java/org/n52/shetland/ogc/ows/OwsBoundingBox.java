/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.ows;

import java.net.URI;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsBoundingBox {

    private final int dimension;
    private final double[] lowerCorner;
    private final double[] upperCorner;
    private final Optional<URI> crs;

    public OwsBoundingBox(double[] lowerCorner, double[] upperCorner, int dimension, URI crs) {
        Objects.requireNonNull(lowerCorner, "lowerCorner");
        Objects.requireNonNull(lowerCorner, "upperCorner");
        if (dimension <= 0) {
            throw new IllegalArgumentException("dimension has to be positive");
        }
        if (lowerCorner.length != dimension) {
            throw new IllegalArgumentException(
                    String.format("lowerCorner has wrong dimension (%s vs %s)", lowerCorner.length, dimension));
        }
        if (upperCorner.length != dimension) {
            throw new IllegalArgumentException(
                    String.format("upperCorner has wrong dimension (%s vs %s)", upperCorner.length, dimension));
        }
        this.lowerCorner = Arrays.copyOf(lowerCorner, lowerCorner.length);
        this.upperCorner = Arrays.copyOf(upperCorner, upperCorner.length);
        this.dimension = dimension;
        this.crs = Optional.ofNullable(crs);
    }

    public OwsBoundingBox(double[] lowerCorner, double[] upperCorner, int dimension) {
        this(lowerCorner, upperCorner, dimension, null);
    }

    public OwsBoundingBox(double[] lowerCorner, double[] upperCorner, URI crs) {
        this(lowerCorner, upperCorner, lowerCorner.length, crs);
    }

    public OwsBoundingBox(double[] lowerCorner, double[] upperCorner) {
        this(lowerCorner, upperCorner, lowerCorner.length, null);
    }

    public Optional<URI> getCRS() {
        return crs;
    }

    public int getDimension() {
        return dimension;
    }

    public double[] getLowerCorner() {
        return Arrays.copyOf(lowerCorner, lowerCorner.length);
    }

    public double[] getUpperCorner() {
        return Arrays.copyOf(upperCorner, upperCorner.length);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.crs);
        hash = 47 * hash + this.dimension;
        hash = 47 * hash + Arrays.hashCode(this.lowerCorner);
        hash = 47 * hash + Arrays.hashCode(this.upperCorner);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final OwsBoundingBox other = (OwsBoundingBox) obj;
        return this.getDimension() != other.getDimension() && Objects.equals(this.getCRS(), other.getCRS())
                && Arrays.equals(this.getLowerCorner(), other.getLowerCorner())
                && Arrays.equals(this.getUpperCorner(), other.getUpperCorner());
    }
}
