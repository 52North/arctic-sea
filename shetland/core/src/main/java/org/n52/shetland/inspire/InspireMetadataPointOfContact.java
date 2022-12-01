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
package org.n52.shetland.inspire;

/**
 * Service internal representation of INSPIRE metadata point of contact
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class InspireMetadataPointOfContact {

    /* Element OrganisationName 1..1 */
    private String organisationName;

    /* Element EmailAddress 1..1 */
    private String emailAddress;

    /**
     * constructor
     *
     * @param organisationName
     *            the organisation name
     * @param emailAddress
     *            the email address
     */
    public InspireMetadataPointOfContact(String organisationName, String emailAddress) {
        setOrganisationName(organisationName);
        setEmailAddress(emailAddress);
    }

    /**
     * Get the organisation name
     *
     * @return the organisationName
     */
    public String getOrganisationName() {
        return organisationName;
    }

    /**
     * Set the organisation name
     *
     * @param organisationName
     *            the organisationName to set
     */
    private void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    /**
     * Get the email address
     *
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Set the email address
     *
     * @param emailAddress
     *            the emailAddress to set
     */
    private void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return String.format("%s %n[%n organisationName=%s,%n emailAddress=%s%n]", this.getClass().getSimpleName(),
                getOrganisationName(), getEmailAddress());
    }
}
