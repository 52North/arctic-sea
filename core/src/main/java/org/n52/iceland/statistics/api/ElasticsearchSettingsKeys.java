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
package org.n52.iceland.statistics.api;

import org.n52.iceland.config.SettingDefinitionGroup;

/**
 * Keys for the {@link SettingDefinitionGroup} for the elasticsearch
 * configuration. MUST match with the corresponding xml file.
 */
public class ElasticsearchSettingsKeys {

    public static final String LOGGING_ENABLED = "statistics.elasticsearch.is_logging_enabled";
    public static final String CLUSTER_NAME = "statistics.elasticsearch.cluster_name";
    public static final String INDEX_NAME = "statistics.elasticsearch.index_name";
    public static final String TYPE_NAME = "statistics.elasticsearch.type_name";
    public static final String UUID = "statistics.elasticsearch.uuid";
    public static final String CLUSTER_NODES = "statistics.elasticsearch.cluster_nodes";

    // lanMode vs transportclient mode vs embedded elasticsearch server
    public static final String CONNECTION_MODE = "statistics.elasticsearch.connection_mode";
    public static final String CONNECTION_MODE_NODE = "statistics.elasticsearch.connection_mode.node";
    public static final String CONNECTION_MODE_TRANSPORT_CLIENT = "statistics.elasticsearch.connection_mode.transport_client";
    public static final String CONNECTION_MODE_EMBEDDED_SERVER = "statistics.elasticsearch.connection_mode.embedded_server";

    public static final String KIBANA_CONFIG_PATH = "statistics.elasticsearch.kibana_config_file";
    public static final String KIBANA_CONFIG_ENABLE = "statistics.elasticsearch.kibana_config_enable";
}
