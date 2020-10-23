/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.encode.json.sensorML.v20;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public interface SensorML20JsonEncoder {

    JsonNodeFactory jsonFactory = JsonNodeFactory.withExactBigDecimals(false);

    String TYPE = "type";
    String ID = "id";
    String IDENTIFIER = "identifier";
    String DESCRIPTION = "description";
    String TERM = "Term";
    String DEFINITION = "definition";
    String LABEL = "label";
    String VALUE = "value";
    String NAME = "name";
    String CODESPACE = "codeSpace";
    String CLASSIFICATION = "classification";
    String IDENTIFICATION = "identification";
    String HISTORY = "history";
    String PROPERTY = "property";
    String TIME = "time";
}
