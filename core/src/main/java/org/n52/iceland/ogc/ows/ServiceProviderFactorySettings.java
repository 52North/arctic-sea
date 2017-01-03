/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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

/**
 * Setting definitions for the OWS Service Provider.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public interface ServiceProviderFactorySettings {

    @Deprecated
    String FILE = "serviceProvider.file";

    String STATE = "serviceProvider.state";

    String PHONE = "serviceProvider.phone";

    String FACSIMILE = "serviceProvider.facsimile";

    String ADDRESS = "serviceProvider.address";

    String SITE = "serviceProvider.site";

    String CITY = "serviceProvider.city";

    String POSITION_NAME = "serviceProvider.positionName";

    String NAME = "serviceProvider.name";

    String INDIVIDUAL_NAME = "serviceProvider.individualName";

    String POSTAL_CODE = "serviceProvider.postalCode";

    String EMAIL = "serviceProvider.email";

    String COUNTRY = "serviceProvider.country";

    String HOURS_OF_SERVICE = "serviceProvider.hoursOfService";

    String CONTACT_INSTRUCTIONS = "serviceProvider.contactInstructions";

    String ONLINE_RESOURCE = "serviceProvider.onlineResource";

    String ROLE_VALUE = "serviceProvider.role.value";

    String ROLE_CODESPACE = "serviceProvider.role.codespace";

}
