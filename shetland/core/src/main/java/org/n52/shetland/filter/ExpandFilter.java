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

import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.oasis.odata.query.option.ExpandOption;
import org.n52.shetland.ogc.filter.FilterClause;

public class ExpandFilter implements FilterClause, ExpandOption {

    private List<ExpandItem> items = new LinkedList<>();


    public ExpandFilter(ExpandItem item) {
        if (item != null) {
            items.add(item);
        }
    }

    public ExpandFilter(List<ExpandItem> items) {
        setItems(items);
    }

    /**
     * @return the items
     */
    public List<ExpandItem> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<ExpandItem> items) {
        this.items.clear();
        if (items != null) {
            this.items = items;
        }
    }

}
