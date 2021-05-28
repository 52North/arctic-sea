/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.sensorML;

import org.n52.shetland.ogc.gml.AbstractReferenceType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public abstract class SmlContact extends AbstractReferenceType {

    private Role role;

    @Override
    public String getRole() {
        if (role != null) {
            return role.getValue();
        }
        if (super.isSetRole()) {
            return super.getRole();
        }
        return null;
    }

    public Role getRoleObject() {
        return role;
    }

    @Override
    public AbstractReferenceType setRole(String role) {
        this.role = new Role(role);
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
