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

import org.n52.shetland.oasis.odata.query.option.QueryOptions;

import java.util.Objects;

public class ExpandItem {

    private final String path;

    private final QueryOptions queryOptions;

    public ExpandItem(String path, QueryOptions options) {
        this.path = path;
        this.queryOptions = options;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the filters
     */
    public QueryOptions getQueryOptions() {
        return queryOptions;
    }

    @Override public int hashCode() {
        return Objects.hash(path, Objects.hashCode(queryOptions));
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ExpandItem)) {
            return false;
        }

        return Objects.equals(this.path, ((ExpandItem) o).getPath()) &&
                Objects.equals(this.queryOptions, ((ExpandItem) o).getQueryOptions());
    }
}
