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
package org.n52.faroe.settings;

import org.n52.faroe.AbstractSettingDefinition;
import org.n52.faroe.Pair;
import org.n52.faroe.SettingType;
/**
 * {@link SettingDefinition} for {@code Pair}.
 */

public class PairSettingDefinition extends AbstractSettingDefinition<Pair<?, ?>> {

    private Pair<?, ?> pair;

    /**
     * Constructs a new {@code PairSettingDefinition}.
     */
    public PairSettingDefinition () {
        super(SettingType.PAIR);
    }

    /**
     * Sets the pair object
     */
    public <F, S> void setPair(F f, S s) {
        pair = Pair.of(f, s);
    }

    //TODO Confirm if this needs to override default value?
}
