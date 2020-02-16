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
package org.n52.shetland.filter;

import org.n52.shetland.oasis.odata.query.option.FilterOption;
import org.n52.shetland.ogc.filter.FilterClause;

import java.util.Objects;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class FilterFilter implements FilterOption, FilterClause {

    private final Object filter;

    public FilterFilter(Object filter) {
        this.filter = filter;
    }

    /**
     * @return the value
     */
    public Object getFilter() {
        return filter;
    }

    @Override public int hashCode() {
        // TODO: Implement equals in all subclasses of org.n52.svalbard.odata.expr
        // For now always returns true
        return Objects.hash("FilterFilter");
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof FilterFilter)) {
            return false;
        }

        // TODO: Implement equals in all subclasses of org.n52.svalbard.odata.expr
        // For now always returns true
        return true;
    }
}
