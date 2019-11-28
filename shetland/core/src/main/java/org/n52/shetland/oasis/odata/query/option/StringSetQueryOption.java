/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.oasis.odata.query.option;

import java.util.Set;

public abstract class StringSetQueryOption extends QueryOption<Set<String>>  {

 private Set<String> value;

    public StringSetQueryOption(String value) {
        this.value = parse(value);
    }

    @Override
    public Set<String> getValue() {
        return value;
    }

    private Set<String> parse(String value2) {
        // TODO Auto-generated method stub
        return null;
    }
}
