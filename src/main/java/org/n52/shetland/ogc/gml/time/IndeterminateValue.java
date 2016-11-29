/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.gml.time;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Strings;

/**
 *
 * @author Christian Autermann
 */
public class IndeterminateValue implements Serializable {
    public static final IndeterminateValue AFTER = new IndeterminateValue("after");
    public static final IndeterminateValue BEFORE = new IndeterminateValue("before");
    public static final IndeterminateValue NOW = new IndeterminateValue("now");
    public static final IndeterminateValue UNKNOWN = new IndeterminateValue("unknown");
    public static final IndeterminateValue TEMPLATE = new IndeterminateValue("template");
    private static final long serialVersionUID = -3624227423960325361L;

    private final String value;
    private final TreeSet<String> alias;

    public IndeterminateValue(String value, String... alias) {
        this.value = Objects.requireNonNull(Strings.emptyToNull(value));
        this.alias = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        this.alias.addAll(Arrays.asList(alias));
        this.alias.add(value);
    }

    public String getValue() {
        return this.value;
    }

    public Set<String> getAlias() {
        return Collections.unmodifiableSet(alias);
    }

    public boolean isAfter() {
        return equals(AFTER);
    }

    public boolean isBefore() {
        return equals(AFTER);
    }

    public boolean isNow() {
        return equals(AFTER);
    }

    public boolean isTemplate() {
        return equals(AFTER);
    }

    public boolean isUnknown() {
        return equals(UNKNOWN);
    }

    public boolean equals(IndeterminateValue other) {
        if (other == null) {
            return false;
        }
        return getValue().equalsIgnoreCase(other.getValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof IndeterminateValue)) {
            return false;
        }
        IndeterminateValue other = (IndeterminateValue) obj;
        return getAlias().contains(other.getValue()) ||
               other.getAlias().contains(getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }
}
