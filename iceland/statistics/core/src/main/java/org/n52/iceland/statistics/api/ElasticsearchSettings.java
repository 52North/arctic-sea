/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.n52.faroe.ConfigurationError;
import org.n52.faroe.SettingValueFactory;
import org.n52.faroe.SettingsService;
import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.statistics.api.mappings.MetadataDataMapping;

@Configurable
public class ElasticsearchSettings {

    @Inject
    private SettingsService settingsService;
    @Inject
    private SettingValueFactory settingValueFactory;

    /**
     * Is statistics collection enable
     */
    private boolean loggingEnabled;

    /**
     * In LAN mode the clustername to join to.
     */
    private String clusterName;

    /**
     * Is the connection type Remote or LAN or Embedded server
     */
    private String nodeConnectionMode = ElasticsearchSettingsKeys.CONNECTION_MODE;

    /**
     * The ElasticSearch indexId of the date to be persisted under
     */
    private String indexId;

    /**
     * TypeId of the date to be persisted under
     */
    private String typeId = "ogc-type";

    /**
     * List of the nodes to try to connect to during startup
     */
    private List<String> clusterNodes;

    /**
     * Unique id of the running instance.
     */
    private String uuid;

    /**
     * Enables the kibana configuration importing into elasticsearch. Controls the {@link this#kibanaConfPath} process
     */
    private boolean kibanaConfigEnable;

    /**
     * Path to the configuration file for the preconfigured kibana settings.
     */
    private String kibanaConfPath;

    // Getter Setters
    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    @Setting(ElasticsearchSettingsKeys.LOGGING_ENABLED)
    public void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    public String getIndexId() {
        return indexId;
    }

    @Setting(ElasticsearchSettingsKeys.INDEX_NAME)
    public void setIndexId(String indexId) {
        Validation.notNullOrEmpty(ElasticsearchSettingsKeys.INDEX_NAME, indexId);
        this.indexId = indexId;
    }

    public String getTypeId() {
        return typeId;
    }

    @Setting(ElasticsearchSettingsKeys.TYPE_NAME)
    public void setTypeId(String typeId) {
        Validation.notNullOrEmpty(ElasticsearchSettingsKeys.TYPE_NAME, typeId);
        if (typeId.equals(MetadataDataMapping.METADATA_TYPE_NAME)) {
            throw new ConfigurationError("The %s is reserved. Choose another one.", typeId);
        }
        this.typeId = typeId;
    }

    public String getUuid() {
        return uuid;
    }

    @Setting(ElasticsearchSettingsKeys.UUID)
    public void setUuid(String uuid) {
        if (uuid == null || uuid.trim().isEmpty()) {
            this.uuid = UUID.randomUUID().toString();
            saveStringValueToConfigFile(ElasticsearchSettingsKeys.UUID, this.uuid);
        } else {
            this.uuid = uuid;
        }
    }

    public String getClusterName() {
        return clusterName;
    }

    @Setting(ElasticsearchSettingsKeys.CLUSTER_NAME)
    public void setClusterName(String clusterName) {
        Validation.notNullOrEmpty(ElasticsearchSettingsKeys.CLUSTER_NAME, clusterName);
        this.clusterName = clusterName;
    }

    public List<String> getClusterNodes() {
        return clusterNodes;
    }

    /**
     * this variable must not be null and format of host[:port] comma separated if multiple values are given
     *
     * @param clusterNodes list of the clusterNodes
     */
    @Setting(ElasticsearchSettingsKeys.CLUSTER_NODES)
    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = new ArrayList<>();
        Validation.notNullOrEmpty(ElasticsearchSettingsKeys.CLUSTER_NODES, clusterNodes);
        if (clusterNodes.contains(",")) {
            Arrays.stream(clusterNodes.split(","))
                    .peek(this::checkClustorNodeFormat)
                    .forEach(this.clusterNodes::add);
        } else {
            checkClustorNodeFormat(clusterNodes);
            this.clusterNodes.add(clusterNodes);
        }
    }

    private boolean checkClustorNodeFormat(String node) throws ConfigurationError {
        if (node.contains(":")) {
            String[] split = node.split(":");
            if (split.length != 2) {
                throw new ConfigurationError("Illegal format expected host[:port] but found %s", node);
            }
            try {
                Integer.valueOf(split[1]);
            } catch (NumberFormatException e) {
                throw new ConfigurationError("Illegal value for port", e);
            }
        }
        return true;
    }

    public String getNodeConnectionMode() {
        return nodeConnectionMode;
    }

    /**
     * Connection type to the Elasticsearch cluster. NodeClient or TransportClient are supported.
     *
     * @param choice the connection mode
     *
     * @see ElasticsearchSettingsKeys#CONNECTION_MODE_NODE
     * @see ElasticsearchSettingsKeys#CONNECTION_MODE_TRANSPORT_CLIENT
     * @see ElasticsearchSettingsKeys#CONNECTION_MODE_EMBEDDED_SERVER
     */
    @Setting(ElasticsearchSettingsKeys.CONNECTION_MODE)
    public void setNodeConnectionMode(String choice) {
        nodeConnectionMode = choice;
    }

    /**
     * With the settings API saves the new value to the configuration file
     *
     * @param key   key to save the value
     * @param value value to save under the key
     */
    public void saveStringValueToConfigFile(String key, String value) {
        settingsService.changeSetting(this.settingValueFactory.newStringSettingValue(key, value));

    }

    public void saveBooleanValueToConfigFile(String key, Boolean value) {
        settingsService.changeSetting(this.settingValueFactory.newBooleanSettingValue(key, value));

    }

    public String getKibanaConfPath() {
        return kibanaConfPath;
    }

    @Setting(ElasticsearchSettingsKeys.KIBANA_CONFIG_PATH)
    public void setKibanaConfPath(String kibanaConfPath) {
        this.kibanaConfPath = kibanaConfPath;
    }

    public boolean isKibanaConfigEnable() {
        return kibanaConfigEnable;
    }

    @Setting(ElasticsearchSettingsKeys.KIBANA_CONFIG_ENABLE)
    public void setKibanaConfigEnable(boolean kibanaConfigEnable) {
        this.kibanaConfigEnable = kibanaConfigEnable;
    }

    @Override
    public String toString() {
        return "ElasticsearchSettings [loggingEnabled=" + loggingEnabled + ", clusterName=" + clusterName +
               ", nodeConnectionMode=" +
               nodeConnectionMode + ", indexId=" + indexId + ", typeId=" + typeId + ", clusterNodes=" + clusterNodes +
               ", uuid=" + uuid + "]";
    }
}
