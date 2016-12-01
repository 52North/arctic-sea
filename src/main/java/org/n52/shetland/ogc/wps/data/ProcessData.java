/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.wps.data;

import java.util.Objects;

import org.n52.shetland.ogc.ows.OwsCode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class ProcessData {

    private OwsCode id;

    public ProcessData() {
        this(null);
    }

    public ProcessData(OwsCode id) {
        this.id = id;
    }

    public OwsCode getId() {
        return id;
    }

    public void setId(OwsCode id) {
        this.id = Objects.requireNonNull(id);
    }

    public boolean isGroup() {
        return false;
    }

    public GroupProcessData asGroup() {
        throw new UnsupportedOperationException();
    }

    public boolean isReference() {
        return false;
    }

    public ReferenceProcessData asReference() {
        throw new UnsupportedOperationException();
    }

    public boolean isValue() {
        return false;
    }

    public ValueProcessData asValue() {
        throw new UnsupportedOperationException();
    }
}
