/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.util;

import java.util.Comparator;

/**
 * @param <T> the type
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 *
 */
//TODO move to janmayen
public class MinMax<T> {

    private T minimum;
    private T maximum;

    public MinMax(T minimum, T maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
        if (this.maximum == null && this.minimum != null) {
            this.maximum = this.minimum;
        } else if (this.minimum == null && this.maximum != null) {
            this.minimum = this.maximum;
        }
    }

    public MinMax() {
    }

    /**
     * Get the value of minimum
     *
     * @return the value of minimum
     */
    public T getMinimum() {
        return minimum;
    }

    /**
     * Set the value of minimum
     *
     * @param minimum new value of minimum
     *
     * @return this
     */
    public MinMax<T> setMinimum(T minimum) {
        this.minimum = minimum;
        if (this.maximum == null) {
            this.maximum = minimum;
        }
        return this;
    }

    /**
     * Get the value of maximum
     *
     * @return the value of maximum
     */
    public T getMaximum() {
        return maximum;
    }

    /**
     * Set the value of maximum
     *
     * @param maximum new value of maximum
     *
     * @return this
     */
    public MinMax<T> setMaximum(T maximum) {
        this.maximum = maximum;
        if (this.minimum == null) {
            this.minimum = maximum;
        }
        return this;
    }

    /**
     * Extend this {@code MinMax} to include {@code t}.
     *
     * @param t the object to include
     * @param c the comparator used to compare the values
     *
     * @return this
     */
    public MinMax<T> extend(T t, Comparator<? super T> c) {
        if (t != null) {
            if (this.maximum == null || c.compare(t, this.maximum) > 0) {
                this.maximum = t;
            }
            if (this.minimum == null || c.compare(t, this.minimum) < 0) {
                this.minimum = t;
            }
        }
        return this;
    }

    /**
     * *
     * Extend this {@code MinMax} to include {@code minmax}.
     *
     * @param minmax the {@code MinMax} to include
     * @param c      the comparator
     *
     * @return this
     */
    public MinMax<T> extend(MinMax<T> minmax, Comparator<? super T> c) {
        return extend(minmax.getMinimum(), c).extend(minmax.getMaximum(), c);
    }

    /**
     * Checks if this MinMax is empty
     *
     * @return if it is empty
     */
    public boolean isEmpty() {
        return this.minimum == null && this.maximum == null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            final MinMax<T> other = (MinMax<T>) obj;
            return (getMinimum() == null ? other.getMinimum() == null : other.getMinimum().equals(getMinimum())) &&
                   (getMaximum() == null ? other.getMaximum() == null : other.getMaximum().equals(getMaximum()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 97;
        int hash = 7;
        hash = prime * hash + (getMinimum() != null ? getMinimum().hashCode() : 0);
        hash = prime * hash + (getMaximum() != null ? getMaximum().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("MinMax[minimum=%s, maximum=%s]", getMinimum(), getMaximum());
    }

}
