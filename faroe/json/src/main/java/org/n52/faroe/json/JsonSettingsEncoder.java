/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.faroe.json;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.joda.time.DateTime;

import org.n52.faroe.JSONSettingConstants;
import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingDefinitionGroup;
import org.n52.faroe.SettingType;
import org.n52.faroe.SettingValue;
import org.n52.faroe.settings.ChoiceSettingDefinition;
import org.n52.faroe.settings.IntegerSettingDefinition;
import org.n52.janmayen.Json;
import org.n52.janmayen.Times;
import org.n52.janmayen.i18n.LocaleHelper;
import org.n52.janmayen.i18n.LocalizedString;
import org.n52.janmayen.i18n.MultilingualString;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class JsonSettingsEncoder {
    private final JsonNodeFactory nodeFactory = Json.nodeFactory();

    public Map<SettingDefinitionGroup, Set<SettingDefinition<?>>> sortByGroup(Set<SettingDefinition<?>> defs) {
        return defs.stream().collect(groupingBy(SettingDefinition::getGroup, toSet()));
    }

    public ObjectNode encode(Map<SettingDefinitionGroup, Set<SettingDefinition<?>>> grouped) {
        ObjectNode json = nodeFactory.objectNode();
        ArrayNode sections = json.putArray(JSONSettingConstants.SECTIONS_KEY);
        grouped.keySet().stream().sorted().forEach(group -> {
            ObjectNode jgroup = sections.addObject();
            jgroup.put(JSONSettingConstants.TITLE_KEY, group.getTitle());
            jgroup.put(JSONSettingConstants.DESCRIPTION_KEY, group.getDescription());
            jgroup.set(JSONSettingConstants.SETTINGS_KEY, encode(grouped.get(group)));
        });
        return json;
    }

    public ObjectNode encode(Set<SettingDefinition<?>> settings) {
        return settings.stream().sorted()
                .collect(nodeFactory::objectNode,
                         (j, def) -> j.set(def.getKey(), encode(def)),
                         ObjectNode::setAll);
    }

    public ObjectNode encode(SettingDefinition<?> def) {
        ObjectNode j = nodeFactory.objectNode();
        j.put(JSONSettingConstants.TITLE_KEY, def.getTitle());
        j.put(JSONSettingConstants.DESCRIPTION_KEY, def.getDescription());
        j.put(JSONSettingConstants.TYPE_KEY, getType(def));
        j.put(JSONSettingConstants.REQUIRED_KEY, !def.isOptional());
        j.set(JSONSettingConstants.DEFAULT_KEY, def.hasDefaultValue() ? encodeDefaultValue(def) : null);

        if (def instanceof IntegerSettingDefinition) {
            IntegerSettingDefinition iDef = (IntegerSettingDefinition) def;
            if (iDef.hasMinimum()) {
                j.put(JSONSettingConstants.MINIMUM_KEY, iDef.getMinimum());
                j.put(JSONSettingConstants.MINIMUM_EXCLUSIVE_KEY, iDef.isExclusiveMinimum());
            }
            if (iDef.hasMaximum()) {
                j.put(JSONSettingConstants.MAXIMUM_KEY, iDef.getMaximum());
                j.put(JSONSettingConstants.MAXIMUM_EXCLUSIVE_KEY, iDef.isExclusiveMaximum());
            }
        }

        if (def instanceof ChoiceSettingDefinition) {
            ChoiceSettingDefinition cDef = (ChoiceSettingDefinition) def;
            ObjectNode options = j.putObject(JSONSettingConstants.OPTIONS_KEY);
            cDef.getOptions().entrySet().forEach(o -> options.put(o.getKey(), o.getValue()));
        }
        return j;
    }

    protected String getType(SettingDefinition<?> def) {
        return def.getType().toString();
    }


    public JsonNode encodeDefaultValue(SettingDefinition<?> def) {
        if (def == null) {
            return nodeFactory.nullNode();
        }
        return JsonSettingsEncoder.this.encodeValue(def.getType(), def.getDefaultValue());
    }

    public JsonNode encodeValue(SettingValue<?> def) {
        if (def == null) {
            return nodeFactory.nullNode();
        }
        return JsonSettingsEncoder.this.encodeValue(def.getType(), def.getValue());
    }

    public JsonNode encodeValue(SettingType type, Object value) {
        if (value == null) {
            return nodeFactory.nullNode();
        }
        switch (type) {
            case TIMEINSTANT:
                return textNode(Times.encodeDateTime((DateTime) value));
            case FILE:
                return textNode(value);
            case URI:
                return textNode(value);
            case CHOICE:
                return textNode(value);
            case STRING:
                return textNode(value);
            case BOOLEAN:
                return nodeFactory.booleanNode((Boolean) value);
            case INTEGER:
                return nodeFactory.numberNode((Integer) value);
            case NUMERIC:
                return nodeFactory.numberNode((Double) value);
            case MULTILINGUAL_STRING: {
                ObjectNode json = nodeFactory.objectNode();
                MultilingualString mls = (MultilingualString) value;
                for (LocalizedString ls : mls) {
                    json.put(LocaleHelper.encode(ls.getLang()), ls.getText());
                }
                return json;
            }
            default:
                throw new IllegalArgumentException(String.format("Unknown Type %s", type));
        }
    }

    private void encodeValue(ObjectNode o, Entry<SettingDefinition<?>, SettingValue<?>> e) {
        SettingDefinition<?> def = e.getKey();
        Object value = Optional.ofNullable(e.getValue())
                .map(v -> (Object) v.getValue())
                .orElseGet(def::getDefaultValue);
        o.set(def.getKey(), encodeValue(def.getType(), value));
    }

    public JsonNode encodeValues(Map<SettingDefinition<?>, SettingValue<?>> settings) {
        return settings.entrySet().stream()
                .sorted(Entry.comparingByKey())
                .collect(nodeFactory::objectNode, this::encodeValue, ObjectNode::setAll);
    }

    private TextNode textNode(Object value) {
        return nodeFactory.textNode(String.valueOf(value));
    }
}
