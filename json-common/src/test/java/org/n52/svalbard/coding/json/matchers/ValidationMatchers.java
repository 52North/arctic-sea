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
package org.n52.svalbard.coding.json.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.JSONValidator;
import org.n52.svalbard.coding.json.SchemaConstants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 *
 * @since 1.0.0
 */
public class ValidationMatchers {
    @Factory
    public static Matcher<JsonNode> instanceOf(String schemaURI) {
        return new IsValidInstance(schemaURI);
    }

    @Factory
    public static Matcher<JsonNode> validObservation() {
        return new IsValidInstance(SchemaConstants.Observation.OBSERVATION);
    }

    @Factory
    public static Matcher<JsonNode> validSchema() {
        return new IsValidInstance(SchemaConstants.SCHEMA_URI);
    }

    public static class IsValidInstance extends TypeSafeDiagnosingMatcher<JsonNode> {
        private final String schemaURI;

        public IsValidInstance(String schemaURI) {
            this.schemaURI = schemaURI;
        }

        @Override
        protected boolean matchesSafely(JsonNode item, Description mismatchDescription) {
            try {
                JsonSchema jsonSchema = JSONValidator.getInstance().getJsonSchemaFactory().getJsonSchema(schemaURI);
                ProcessingReport report = jsonSchema.validate(item);
                return describeProcessingReport(report, item, mismatchDescription);
            } catch (ProcessingException ex) {
                mismatchDescription.appendText(ex.getMessage());
            } catch (JsonProcessingException ex) {
                mismatchDescription.appendText(ex.getMessage());
            }
            return false;
        }

        protected boolean describeProcessingReport(ProcessingReport report, JsonNode item,
                Description mismatchDescription) throws JsonProcessingException {
            if (!report.isSuccess()) {
                ObjectNode objectNode = JacksonUtils.nodeFactory().objectNode();
                objectNode.set(JSONConstants.INSTANCE, item);
                ArrayNode errors = objectNode.putArray(JSONConstants.ERRORS);
                for (ProcessingMessage m : report) {
                    errors.add(m.asJson());
                }
                mismatchDescription.appendText(JacksonUtils.prettyPrint(objectNode));
            }
            return report.isSuccess();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("valid instance of ").appendText(schemaURI);
        }
    }
}
