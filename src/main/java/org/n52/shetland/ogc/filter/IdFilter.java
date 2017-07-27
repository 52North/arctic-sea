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
package org.n52.shetland.ogc.filter;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.n52.shetland.ogc.filter.FilterConstants.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ID filter class
 *
 * @since 1.0.0
 *
 */
public class IdFilter
        extends Filter<Id> {

    private static final Logger log = LoggerFactory.getLogger(IdFilter.class);

    private Id operator;

    private Set<String> ids;

    public IdFilter() {
        this(Sets.<String> newHashSet());
    }

    public IdFilter(String id) {
        this(Sets.newHashSet(id));
    }

    public IdFilter(Set<String> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }

    public Collection<String> getIds() {
        return ids;
    }

    public Filter<Id> addId(String id) {
        this.ids.add(id);
        return this;
    }

    @Override
    public Id getOperator() {
        return operator;
    }

    @Override
    public Filter<Id> setOperator(Id operator) throws RuntimeException {
        if (Optional.ofNullable(this.operator).isPresent() && !this.operator.equals(operator)) {
            log.warn(
                    "Combination of different ID filters not supported, "
                    + "ignoring new operator '{}' in favour of already set '{}'",
                    operator, this.operator);
        }
        this.operator = operator;
        return this;
    }

}
