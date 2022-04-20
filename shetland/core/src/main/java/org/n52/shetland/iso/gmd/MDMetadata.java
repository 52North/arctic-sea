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
package org.n52.shetland.iso.gmd;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.n52.shetland.w3c.xlink.Referenceable;

import com.google.common.collect.Sets;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class MDMetadata extends AbstractObject {

    private Set<Referenceable<CiResponsibleParty>> contact = new LinkedHashSet<>();

    private DateTime dateStamp;

    private Set<Referenceable<AbstractMDIdentification>> identificationInfo = new LinkedHashSet<>();

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public MDMetadata(Referenceable<CiResponsibleParty> contact, DateTime dateStamp,
            Referenceable<AbstractMDIdentification> identificationInfo) {
        this(Sets.newHashSet(contact), dateStamp, Sets.newHashSet(identificationInfo));
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public MDMetadata(CiResponsibleParty contact, DateTime dateStamp, AbstractMDIdentification identificationInfo) {
        this(Referenceable.of(contact), dateStamp, Referenceable.of(identificationInfo));
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public MDMetadata(Collection<Referenceable<CiResponsibleParty>> contact, DateTime dateStamp,
            Collection<Referenceable<AbstractMDIdentification>> identificationInfo) {
        super();
        addContacts(contact);
        this.dateStamp = dateStamp;
        addIdentificationInfos(identificationInfo);
    }

    /**
     * @return the contact
     */
    public Set<Referenceable<CiResponsibleParty>> getContact() {
        return Collections.unmodifiableSet(contact);
    }

    public MDMetadata addContact(Referenceable<CiResponsibleParty> contact) {
        this.contact.add(contact);
        return this;
    }

    public MDMetadata addContact(CiResponsibleParty contact) {
        return addContact(Referenceable.of(contact));
    }

    public MDMetadata addContacts(Collection<Referenceable<CiResponsibleParty>> contacts) {
        if (contacts != null) {
            this.contact.addAll(contacts);
        }
        return this;
    }

    /**
     * @return the dateStamp
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public DateTime getDateStamp() {
        return dateStamp;
    }

    /**
     * @return the identificationInfo
     */
    public Set<Referenceable<AbstractMDIdentification>> getIdentificationInfo() {
        return Collections.unmodifiableSet(identificationInfo);
    }

    public MDMetadata addIdentificationInfo(AbstractMDIdentification identificationInfo) {
        return addIdentificationInfo(Referenceable.of(identificationInfo));
    }

    public MDMetadata addIdentificationInfo(Referenceable<AbstractMDIdentification> identificationInfo) {
        if (identificationInfo != null) {
            this.identificationInfo.add(identificationInfo);
        }
        return this;
    }

    public MDMetadata addIdentificationInfos(Collection<Referenceable<AbstractMDIdentification>> identificationInfos) {
        if (identificationInfos != null) {
            this.identificationInfo.addAll(identificationInfos);
        }
        return this;
    }

}
