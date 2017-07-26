/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.iso.gmd;

import org.n52.shetland.iso.gco.Role;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.AttributeSimpleAttrs;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.shetland.w3c.xlink.SimpleAttrs;

/**
 * Internal representation of the ISO GMD ResponsibleParty.
 *
 * @author Carsten Hollmann <c.hollmann@52north.org>
 * @since 1.0.0
 *
 */
public class CiResponsibleParty extends AbstractObject implements AttributeSimpleAttrs {

    private SimpleAttrs simpleAttrs;

    private String individualName;

    private String organizationName;

    private String positionName;

    private Referenceable<CiContact> contactInfo;

    private Nillable<Role> role;

    public CiResponsibleParty(SimpleAttrs simpleAttrs) {
        this.simpleAttrs = simpleAttrs;
        this.role = Nillable.<Role>missing();
    }

    public CiResponsibleParty(Role role) {
        this.role = Nillable.<Role>of(role);
    }

    public CiResponsibleParty(Nillable<Role> role) {
        this.role = role;
    }

    @Override
    public void setSimpleAttrs(SimpleAttrs simpleAttrs) {
        this.simpleAttrs = simpleAttrs;
    }

    @Override
    public SimpleAttrs getSimpleAttrs() {
        return simpleAttrs;
    }

    public boolean isSetIndividualName() {
        return individualName != null && !individualName.isEmpty();
    }

    public String getIndividualName() {
        return individualName;
    }

    public CiResponsibleParty setIndividualName(final String invidualName) {
        individualName = invidualName;
        return this;
    }

    public boolean isSetOrganizationName() {
        return organizationName != null && !organizationName.isEmpty();
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public CiResponsibleParty setOrganizationName(final String organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    public boolean isSetPositionName() {
        return positionName != null && !positionName.isEmpty();
    }

    public String getPositionName() {
        return positionName;
    }

    public CiResponsibleParty setPositionName(final String positionName) {
        this.positionName = positionName;
        return this;
    }

    /**
     * @return the contactInfo
     */
    public Referenceable<CiContact> getContactInfo() {
        return contactInfo;
    }

    /**
     * @param contactInfo the contactInfo to set
     *
     * @return {@code this}
     */
    public CiResponsibleParty setContactInfo(CiContact contactInfo) {
        if (contactInfo != null) {
            this.contactInfo = Referenceable.of(contactInfo);
        }
        return this;
    }

    /**
     * @param contactInfo the contactInfo to set
     *
     * @return {@code this}
     */
    public CiResponsibleParty setContactInfo(Referenceable<CiContact> contactInfo) {
        this.contactInfo = contactInfo;
        return this;
    }

    public boolean isSetContactInfo() {
        return contactInfo != null;
    }

    /**
     * @return the role
     */
    public Role getRole() {
        if (role.isPresent()) {
            return role.get();
        }
        return new Role("");
    }

    /**
     * @return the role
     */
    public Nillable<Role> getRoleNillable() {
        return role;
    }

}
