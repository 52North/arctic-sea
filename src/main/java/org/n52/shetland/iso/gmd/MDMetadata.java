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

import java.util.Set;

import org.joda.time.DateTime;

import org.n52.shetland.w3c.xlink.AttributeSimpleAttrs;
import org.n52.shetland.w3c.xlink.SimpleAttrs;

import com.google.common.collect.Sets;

public class MDMetadata extends AbstractObject implements AttributeSimpleAttrs {

    private SimpleAttrs simpleAttrs;

    private Set<CiResponsibleParty> contact = Sets.newHashSet();

    private DateTime dateStamp;

    private Set<AbstractMDIdentification> identificationInfo = Sets.newHashSet();

    public MDMetadata(SimpleAttrs simpleAttrs) {
        this.simpleAttrs = simpleAttrs;
    }

    public MDMetadata(CiResponsibleParty contact, DateTime dateStamp, AbstractMDIdentification identificationInfo) {
        this(Sets.newHashSet(contact), dateStamp, Sets.newHashSet(identificationInfo));
    }

    public MDMetadata(Set<CiResponsibleParty> contact, DateTime dateStamp, Set<AbstractMDIdentification> identificationInfo) {
        super();
        this.contact = contact;
        this.dateStamp = dateStamp;
        this.identificationInfo = identificationInfo;
    }


    @Override
    public void setSimpleAttrs(SimpleAttrs simpleAttrs) {
       this.simpleAttrs = simpleAttrs;
    }

    @Override
    public SimpleAttrs getSimpleAttrs() {
        return simpleAttrs;
    }

    /**
     * @return the contact
     */
    public Set<CiResponsibleParty> getContact() {
        return contact;
    }

    public MDMetadata addContact(CiResponsibleParty contact) {
        this.contact.add(contact);
        return this;
    }

    public MDMetadata addContacts(Set<CiResponsibleParty> contacts) {
        this.contact.addAll(contacts);
        return this;
    }

    /**
     * @return the dateStamp
     */
    public DateTime getDateStamp() {
        return dateStamp;
    }

    /**
     * @return the identificationInfo
     */
    public Set<AbstractMDIdentification> getIdentificationInfo() {
        return identificationInfo;
    }

    public MDMetadata addIdentificationInfo(AbstractMDIdentification identificationInfo) {
        this.identificationInfo.add(identificationInfo);
        return this;
    }

    public MDMetadata addIdentificationInfos(Set<AbstractMDIdentification> identificationInfos) {
        this.identificationInfo.addAll(identificationInfos);
        return this;
    }

}
