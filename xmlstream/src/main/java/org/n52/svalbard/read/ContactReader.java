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
package org.n52.svalbard.read;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.inspire.base2.Contact;
import org.n52.svalbard.decode.exception.DecodingException;

public class ContactReader extends XmlReader<Contact> {

    private Contact contact;

    @Override
    protected void begin()
            throws XMLStreamException, DecodingException {
        this.contact = new Contact();
    }

    @Override
    protected void read(QName name)
            throws XMLStreamException, DecodingException {
        if (name.equals(AqdConstants.QN_BASE2_ADDRESS)) {
            this.contact.setAddress(delegate(new AddressReader()));
        } else if (name.equals(AqdConstants.QN_BASE2_CONTACT_INSTRUCTIONS)) {
            this.contact
                    .setContactInstructions(delegate(new NillableFreeTextReader()));
        } else if (name.equals(AqdConstants.QN_BASE2_ELECTRONIC_MAIL_ADDRESS)) {
            this.contact
                    .setElectronicMailAddress(delegate(new NillableStringReader()));
        } else if (name.equals(AqdConstants.QN_BASE2_HOURS_OF_SERVICE)) {
            this.contact
                    .setHoursOfService(delegate(new NillableFreeTextReader()));
        } else if (name.equals(AqdConstants.QN_BASE2_WEBSITE)) {
            this.contact.setWebsite(delegate(new NillableStringReader()));
        } else if (name.equals(AqdConstants.QN_BASE2_TELEPHONE_FACSIMILE)) {
            this.contact
                    .addTelephoneFacsimile(delegate(new NillableStringReader()));
        } else if (name.equals(AqdConstants.QN_BASE2_TELEPHONE_VOICE)) {
            this.contact.addTelephoneVoice(delegate(new NillableStringReader()));
        } else {
            ignore();
        }
    }

    @Override
    protected Contact finish()
            throws DecodingException {
        return contact;
    }

}
