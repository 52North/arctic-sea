/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sensorML;

/**
 * Implementation for sml:Person
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SmlPerson extends SmlContact {

    private String affiliation;

    private String email;

    private String name;

    private String phoneNumber;

    private String surname;

    private String userID;

    public SmlPerson() {
    }

    public SmlPerson(final String surname, final String name, final String userID, final String affiliation,
            final String phoneNumber, final String email) {
        this.surname = surname;
        this.name = name;
        this.userID = userID;
        this.affiliation = affiliation;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSurname() {
        return surname;
    }

    public String getUserID() {
        return userID;
    }

    public boolean isSetAffiliation() {
        return affiliation != null && !affiliation.isEmpty();
    }

    public boolean isSetEmail() {
        return email != null && !email.isEmpty();
    }

    public boolean isSetName() {
        return name != null && !name.isEmpty();
    }

    public boolean isSetPhoneNumber() {
        return phoneNumber != null && !phoneNumber.isEmpty();
    }

    public boolean isSetSurname() {
        return surname != null && !surname.isEmpty();
    }

    public boolean isSetUserID() {
        return userID != null && !userID.isEmpty();
    }

    public SmlContact setAffiliation(final String affiliation) {
        this.affiliation = affiliation;
        return this;
    }

    public SmlContact setEmail(final String email) {
        this.email = email;
        return this;
    }

    public SmlContact setName(final String name) {
        this.name = name;
        return this;
    }

    public SmlContact setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public SmlContact setSurname(final String surname) {
        this.surname = surname;
        return this;
    }

    public SmlContact setUserID(final String userID) {
        this.userID = userID;
        return this;
    }

}
