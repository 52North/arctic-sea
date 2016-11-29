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
package org.n52.shetland.ogc.sos;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.time.Time;

/**
 * @since 4.0.0
 *
 */
public class SosProcedureDescription<T extends AbstractFeature> extends AbstractFeature {
    private Set<SosOffering> offerings = new LinkedHashSet<>();

    private final T description;

    private Time validTime;

    public SosProcedureDescription(T description) {
        super(description.getIdentifier());
        this.description = description;
    }

    public T getProcedureDescription() {
        return this.description;
    }

    public Set<SosOffering> getOfferings() {
        return offerings;
    }

    public void setOfferings(Collection<SosOffering> offering) {
        this.offerings = Optional.ofNullable(offering).map(HashSet::new).orElseGet(HashSet::new);
    }

    public void addOffering(SosOffering offering) {
        this.offerings.add(offering);
    }

    public boolean isSetOfferings(){
        return this.offerings != null && !this.offerings.isEmpty();
    }

    public Time getValidTime() {
        return validTime;
    }

    public void setValidTime(Time validTime) {
        this.validTime = validTime;
    }

    public boolean isSetValidTime() {
        return this.validTime != null && !this.validTime.isEmpty();
    }
}
