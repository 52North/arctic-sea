/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.ows;

import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsContact {

    private final Optional<OwsPhone> phone;
    private final Optional<OwsAddress> address;
    private final Optional<OwsOnlineResource> onlineResource;
    private final Optional<String> hoursOfService;
    private final Optional<String> contactInstructions;

    public OwsContact(OwsPhone phone,
                      OwsAddress address,
                      OwsOnlineResource onlineResource,
                      String hoursOfService,
                      String contactInstructions) {
        this.phone = Optional.ofNullable(phone);
        this.address = Optional.ofNullable(address);
        this.onlineResource = Optional.ofNullable(onlineResource);
        this.hoursOfService = Optional.ofNullable(hoursOfService);
        this.contactInstructions = Optional.ofNullable(contactInstructions);
    }

    public Optional<OwsPhone> getPhone() {
        return phone;
    }

    public Optional<OwsAddress> getAddress() {
        return address;
    }

    public Optional<OwsOnlineResource> getOnlineResource() {
        return onlineResource;
    }

    public Optional<String> getHoursOfService() {
        return hoursOfService;
    }

    public Optional<String> getContactInstructions() {
        return contactInstructions;
    }

}
