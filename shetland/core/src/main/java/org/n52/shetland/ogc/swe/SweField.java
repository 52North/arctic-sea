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
package org.n52.shetland.ogc.swe;

import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * SOS internal representation of SWE field
 *
 * @since 1.0.0
 */
// FIXME this is not a AbstractDataComponent according to the SWE standard
public class SweField extends SweAbstractDataComponent {

    /**
     * field element
     */
    private SweAbstractDataComponent element;

    public SweField(final String name) {
        super();
        setName(name);
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SweField(final CodeType name) {
        super();
        setName(name);
    }

    /**
     * constructor
     *
     * @param name
     *            Field name
     * @param element
     *            Field element
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SweField(final String name, final SweAbstractDataComponent element) {
        super();
        setName(name);
        this.element = element;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SweField(final CodeType name, final SweAbstractDataComponent element) {
        super();
        setName(name);
        this.element = element;
    }

    /**
     * @return the element
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public SweAbstractDataComponent getElement() {
        return element;
    }

    /**
     * @param element
     *            the element to set
     * @return This SweField
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SweField setElement(final SweAbstractDataComponent element) {
        this.element = element;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 67;
        int hash = 3;
        hash = prime * hash + super.hashCode();
        hash = prime * hash + (getName() != null ? getName().hashCode() : 0);
        hash = prime * hash + (getElement() != null ? getElement().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SweField other = (SweField) obj;
        if ((getName() == null) ? (other.getName() != null) : !getName().equals(other.getName())) {
            return false;
        }
        if (getElement() != other.getElement() && (getElement() == null || !getElement().equals(other.getElement()))) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return String.format("SosSweField[name=%s, element=%s]", getName(), getElement());
    }

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.Field;
    }

    @Override
    public <
            T,
            X extends Throwable> T accept(SweDataComponentVisitor<T, X> visitor) throws X {
        return visitor.visit(this);
    }

    @Override
    public <
            X extends Throwable> void accept(VoidSweDataComponentVisitor<X> visitor) throws X {
        visitor.visit(this);
    }

    @Override
    public SweField copy() {
        SweField copy = new SweField(getName().copy(), getElement().copy());
        copyValueTo(copy);
        return copy;
    }
}
