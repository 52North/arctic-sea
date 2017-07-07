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
import org.n52.shetland.inspire.ad.AddressRepresentation;
import org.n52.svalbard.decode.exception.DecodingException;

public class AddressRepresentationReader extends XmlReader<AddressRepresentation> {
    private AddressRepresentation address;

    @Override
    protected void begin()
            throws XMLStreamException, DecodingException {
        this.address = new AddressRepresentation();
    }

    @Override
    protected void read(QName name)
            throws XMLStreamException, DecodingException {
        if (name.equals(AqdConstants.QN_AD_ADMIN_UNIT)) {
            address.addAdminUnit(delegate(new AdminUnitReader()));
        } else if (name.equals(AqdConstants.QN_AD_LOCATOR_DESIGNATOR)) {
            address.addLocatorDesignator(chars());
        } else if (name.equals(AqdConstants.QN_AD_LOCATOR_NAME)) {
            address.addLocatorName(delegate(new LocatorNameReader()));
        } else if (name.equals(AqdConstants.QN_AD_ADDRESS_AREA)) {
            address.addAddressArea(delegate(new AddressAreaReader()));
        } else if (name.equals(AqdConstants.QN_AD_POST_NAME)) {
            address.addPostName(delegate(new PostNameReader()));
        } else if (name.equals(AqdConstants.QN_AD_POST_CODE)) {
            address.setPostCode(delegate(new NillableStringReader()));
        } else if (name.equals(AqdConstants.QN_AD_THOROUGHFARE)) {
            address.addThoroughfare(delegate(new ThoroughfareReader()));
        } else if (name.equals(AqdConstants.QN_AD_ADDRESS_FEATURE)) {
            address.setAddressFeature(delegate(new NillableReferenceReader()));
        } else {
            ignore();
        }
    }

    @Override
    protected AddressRepresentation finish()
            throws DecodingException {
        return this.address;
    }

}
