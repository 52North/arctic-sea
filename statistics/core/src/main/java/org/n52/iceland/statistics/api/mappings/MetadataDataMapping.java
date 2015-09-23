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
package org.n52.iceland.statistics.api.mappings;

import org.n52.iceland.statistics.api.parameters.AbstractEsParameter;
import org.n52.iceland.statistics.api.parameters.Description;
import org.n52.iceland.statistics.api.parameters.ElasticsearchTypeRegistry;
import org.n52.iceland.statistics.api.parameters.SingleEsParameter;
import org.n52.iceland.statistics.api.parameters.Description.InformationOrigin;
import org.n52.iceland.statistics.api.parameters.Description.Operation;

public class MetadataDataMapping {

    public static final String METADATA_TYPE_NAME = "mt";

    public static final String METADATA_ROW_ID = "1";

    public static final AbstractEsParameter METADATA_CREATION_TIME_FIELD = new SingleEsParameter("mt-creation-time", new Description(
            InformationOrigin.Computed, Operation.Metadata, "Creation time of the Elasticsearch index"), ElasticsearchTypeRegistry.dateField);

    public static final AbstractEsParameter METADATA_UPDATE_TIME_FIELD = new SingleEsParameter("mt-update-time", new Description(
            InformationOrigin.Computed, Operation.Metadata, "Update time of the Elasticsearch metadata type"), ElasticsearchTypeRegistry.dateField);

    public static final AbstractEsParameter METADATA_NAME_FIELD = new SingleEsParameter("mt-name", new Description(
            InformationOrigin.None, Operation.Metadata, "Name of the used metadata type"), ElasticsearchTypeRegistry.stringField);

    public static final AbstractEsParameter METADATA_VERSION_FIELD = new SingleEsParameter("mt-version", new Description(InformationOrigin.Computed,
            Operation.Metadata, "Monoton increasing version field. The deployment schema and the Elasticsearch schema version must match"),
            ElasticsearchTypeRegistry.integerField);

    public static final AbstractEsParameter METADATA_UUIDS_FIELD = new SingleEsParameter("mt-uuids", new Description(InformationOrigin.Computed,
            Operation.Metadata, "List of unique user IDs"), ElasticsearchTypeRegistry.stringField);

}
