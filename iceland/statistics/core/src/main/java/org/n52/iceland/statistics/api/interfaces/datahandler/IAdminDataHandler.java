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
package org.n52.iceland.statistics.api.interfaces.datahandler;

import javax.security.auth.Destroyable;

import org.elasticsearch.client.Client;

import org.n52.iceland.statistics.api.ElasticsearchSettings;
import org.n52.janmayen.lifecycle.Constructable;

public interface IAdminDataHandler extends Constructable, Destroyable {

    void deleteIndex(String index);

    void createSchema();

    Client getElasticsearchClient();

    ElasticsearchSettings getElasticsearchSettings();
}
