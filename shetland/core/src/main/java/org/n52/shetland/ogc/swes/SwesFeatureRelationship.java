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
package org.n52.shetland.ogc.swes;

import org.n52.shetland.ogc.gml.AbstractFeature;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @since 1.0.0
 *
 */
public class SwesFeatureRelationship {
    private String role;

    private AbstractFeature feature;

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SwesFeatureRelationship(String role, AbstractFeature feature) {
        this.role = role;
        this.feature = feature;
    }

    public SwesFeatureRelationship() {
        this(null, null);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public AbstractFeature getFeature() {
        return feature;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setFeature(AbstractFeature feature) {
        this.feature = feature;
    }

    @Override
    public String toString() {
        return String.format("SosFeatureRelationship [role=%s, feature=%s]", role, feature);
    }
}
