/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.service;

import java.util.Collections;
import java.util.Set;

import org.n52.iceland.config.SettingDefinition;
import org.n52.iceland.config.SettingDefinitionGroup;
import org.n52.iceland.config.SettingDefinitionProvider;
import org.n52.iceland.config.settings.BooleanSettingDefinition;
import org.n52.iceland.config.settings.UriSettingDefinition;

import com.google.common.collect.Sets;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 *
 * @since 4.0.0
 */
public class ServiceSettings implements SettingDefinitionProvider {
    
    public static final String NAME = "Service";

    public static final String SERVICE_URL = "service.url";

    public static final String  VALIDATE_RESPONSE = "service.response.validate";

    public static final SettingDefinitionGroup GROUP = new SettingDefinitionGroup().setTitle(NAME).setOrder(2);
    

    public static final UriSettingDefinition SERVICE_URL_DEFINITION = new UriSettingDefinition()
            .setGroup(GROUP)
            .setOrder(ORDER_0)
            .setKey(SERVICE_URL)
            .setTitle("SOS URL")
            .setDescription(
                    "The endpoint URL of this sos which will be shown in the GetCapabilities response "
                            + "(e.g. <code>http://localhost:8080/52nSOS/sos</code> or <code>http://localhost:8080/52nSOS/service</code>)."
                            + " The path to a specific binding (like <code>/soap</code>) will appended to this URL."
                            + " For detailed information, please read the <a href=\"https://wiki.52north.org/bin/view/SensorWeb/SensorObservationServiceIVDocumentation\">documentation</a>");

    public static final BooleanSettingDefinition VALIDATE_RESPONSE_DEFINITION =
            new BooleanSettingDefinition()
                    .setGroup(GROUP)
                    .setOrder(ORDER_16)
                    .setKey(VALIDATE_RESPONSE)
                    .setDefaultValue(false)
                    .setTitle("Should this SOS validate the XML response in non debug mode?")
                    .setDescription(
                            "Whether the SOS should validate the XML response when the debug mode is disables!");

    private static final Set<SettingDefinition<?, ?>> DEFINITIONS = Sets.<SettingDefinition<?, ?>> newHashSet(
            SERVICE_URL_DEFINITION,
            VALIDATE_RESPONSE_DEFINITION);

    @Override
    public Set<SettingDefinition<?, ?>> getSettingDefinitions() {
        return Collections.unmodifiableSet(DEFINITIONS);
    }
}
