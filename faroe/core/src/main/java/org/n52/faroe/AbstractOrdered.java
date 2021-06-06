/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.faroe;

/**
 * Abstract, generic implementation of {@code Ordered}.
 *
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public abstract class AbstractOrdered implements Ordered {

    private float order;

    public AbstractOrdered(float order) {
        this.order = order;
    }

    public AbstractOrdered() {
        this(0.0f);
    }

    @Override
    public float getOrder() {
        return this.order;
    }

    @Override
    public void setOrder(float order) {
        this.order = order;
    }

    @Override
    public int compareTo(Ordered t) {
        int compare = Float.compare(getOrder(), t.getOrder());
        if (compare == 0 && t instanceof AbstractOrdered) {
            AbstractOrdered ao = (AbstractOrdered) t;
            if (getSuborder() == null) {
                return 1;
            } else if (ao.getSuborder() == null) {
                return -1;
            } else {
                return getSuborder().compareTo(ao.getSuborder());
            }
        }
        return compare;
    }

    protected abstract String getSuborder();

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Float.floatToIntBits(this.order);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractOrdered other = (AbstractOrdered) obj;
        return Float.floatToIntBits(this.order) == Float.floatToIntBits(other.order);
    }
}
