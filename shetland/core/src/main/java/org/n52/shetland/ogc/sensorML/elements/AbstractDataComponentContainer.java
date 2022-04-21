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

import org.n52.shetland.ogc.gml.AbstractReferenceType;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Abstract container class for {@link SweAbstractDataComponent}s
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 * @param <T>
 *            Implemented class
 */
public class AbstractDataComponentContainer<
        T> extends AbstractReferenceType {

    private String name;

    private SweAbstractDataComponent abstractDataComponent;

    /**
     * default constructor
     */
    public AbstractDataComponentContainer() {
        super();
    }

    /**
     * constructor
     *
     * @param name
     *            name
     */
    public AbstractDataComponentContainer(String name) {
        this.name = name;
    }

    /**
     * constructor
     *
     * @param name
     *            name
     * @param abstractDataComponent
     *            data component
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public AbstractDataComponentContainer(String name, SweAbstractDataComponent abstractDataComponent) {
        this.name = name;
        this.abstractDataComponent = abstractDataComponent;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public boolean isSetName() {
        return getName() != null && !getName().isEmpty();
    }

    /**
     * @return the abstractDataComponent
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public SweAbstractDataComponent getAbstractDataComponent() {
        return abstractDataComponent;
    }

    /**
     * @param abstractDataComponent
     *            the abstractDataComponent to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setAbstractDataComponent(SweAbstractDataComponent abstractDataComponent) {
        this.abstractDataComponent = abstractDataComponent;
    }

    public boolean isSetAbstractDataComponent() {
        return getAbstractDataComponent() != null;
    }
}
