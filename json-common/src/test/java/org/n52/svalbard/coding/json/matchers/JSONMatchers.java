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

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class JSONMatchers {
    private JSONMatchers() {
    }

    @Factory
    public static Matcher<JsonNode> isArray() {
        return new IsArrayNode();
    }

    @Factory
    public static Matcher<JsonNode> arrayOfLength(final int size) {
        return new ArrayOfLength(size);
    }

    @Factory
    public static Matcher<JsonNode> isObject() {
        return new IsObjectNode();
    }

    @Factory
    public static Matcher<JsonNode> isString() {
        return new IsTextNode();
    }

    @Factory
    public static Matcher<JsonNode> isBoolean() {
        return new IsBooleanNode();
    }

    @Factory
    public static Matcher<JsonNode> isNumber() {
        return new IsNumberNode();
    }

    @Factory
    public static Matcher<JsonNode> exist() {
        return new IsExistingNode();
    }

    @Factory
    public static Matcher<JsonNode> isTrue() {
        return new IsBooleanEqualsNode(true);
    }

    @Factory
    public static Matcher<JsonNode> isFalse() {
        return new IsBooleanEqualsNode(false);
    }

    @Factory
    public static Matcher<JsonNode> equalTo(final String value) {
        return new IsStringEqualsNode(value);
    }

    @Factory
    public static Matcher<JsonNode> equalTo(final Number value) {
        return new IsNumberEqualsNode(value);
    }

    @Factory
    public static Matcher<JsonNode> equalTo(final boolean value) {
        return new IsBooleanEqualsNode(value);
    }

    protected static boolean describeNodeType(JsonNode item, Description desc) {
        if (item.isTextual()) {
            desc.appendText("was a text node");
        } else if (item.isNumber()) {
            desc.appendText("was a number node");
        } else if (item.isBoolean()) {
            desc.appendText("was a boolean node");
        } else if (item.isValueNode()) {
            desc.appendText("was a value node");
        } else if (item.isObject()) {
            desc.appendText("was a object node");
        } else if (item.isArray()) {
            desc.appendText("was a array node");
        } else if (item.isMissingNode()) {
            desc.appendText("was a missing node");
        } else if (item.isNull()) {
            desc.appendText("was a null node");
        }
        return false;
    }

    private static class IsArrayNode extends TypeSafeDiagnosingMatcher<JsonNode> {
        @Override
        protected boolean matchesSafely(JsonNode item, Description desc) {
            if (item.isArray()) {
                return true;
            } else {
                return describeNodeType(item, desc);
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a JSON array");
        }
    }

    private static class IsObjectNode extends TypeSafeDiagnosingMatcher<JsonNode> {
        @Override
        protected boolean matchesSafely(JsonNode item, Description desc) {
            if (item.isObject()) {
                return true;
            } else {
                return describeNodeType(item, desc);
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a JSON object");
        }
    }

    private static class IsTextNode extends TypeSafeDiagnosingMatcher<JsonNode> {
        @Override
        protected boolean matchesSafely(JsonNode item, Description desc) {
            if (item.isTextual()) {
                return true;
            } else {
                return describeNodeType(item, desc);
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a string");
        }
    }

    private static class IsBooleanNode extends TypeSafeDiagnosingMatcher<JsonNode> {
        @Override
        protected boolean matchesSafely(JsonNode item, Description desc) {
            if (item.isBoolean()) {
                return true;
            } else {
                return describeNodeType(item, desc);
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a boolean");
        }
    }

    private static class IsNumberNode extends TypeSafeDiagnosingMatcher<JsonNode> {
        @Override
        protected boolean matchesSafely(JsonNode item, Description desc) {
            if (item.isNumber()) {
                return true;
            } else {
                return describeNodeType(item, desc);
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a number");
        }
    }

    private static class IsExistingNode extends TypeSafeDiagnosingMatcher<JsonNode> {
        @Override
        protected boolean matchesSafely(JsonNode item, Description desc) {
            if (item.isMissingNode()) {
                return describeNodeType(item, desc);
            }
            if (item.isNull()) {
                return describeNodeType(item, desc);
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("exists");
        }
    }

    private static class IsStringEqualsNode extends TypeSafeDiagnosingMatcher<JsonNode> {
        private final String value;

        IsStringEqualsNode(String value) {
            this.value = value;
        }

        @Override
        protected boolean matchesSafely(JsonNode item, Description desc) {
            if (!item.isTextual()) {
                return describeNodeType(item, desc);
            }
            if (!item.textValue().equals(value)) {
                desc.appendText("was ").appendValue(item.textValue());
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(value);
        }
    }

    private static class IsNumberEqualsNode extends TypeSafeDiagnosingMatcher<JsonNode> {
        private final Number value;

        IsNumberEqualsNode(Number value) {
            this.value = value;
        }

        @Override
        protected boolean matchesSafely(JsonNode item, Description desc) {
            if (!item.isNumber()) {
                return describeNodeType(item, desc);
            }
            if (!item.numberValue().equals(value)) {
                desc.appendText("was ").appendValue(item.numberValue());
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(value);
        }
    }

    private static class IsBooleanEqualsNode extends TypeSafeDiagnosingMatcher<JsonNode> {
        private final boolean value;

        IsBooleanEqualsNode(boolean value) {
            this.value = value;
        }

        @Override
        protected boolean matchesSafely(JsonNode item, Description desc) {
            if (!item.isBoolean()) {
                return describeNodeType(item, desc);
            }
            if (item.booleanValue() != value) {
                desc.appendText("was ").appendValue(item.booleanValue());
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(value);
        }
    }

    private static class ArrayOfLength extends TypeSafeDiagnosingMatcher<JsonNode> {
        private final int size;

        ArrayOfLength(int size) {
            this.size = size;
        }

        @Override
        protected boolean matchesSafely(JsonNode item, Description desc) {
            if (!item.isArray()) {
                return describeNodeType(item, desc);
            }
            if (item.size() != size) {
                desc.appendText("was array of length").appendValue(size);
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("array of length ").appendValue(size);
        }
    }
}
