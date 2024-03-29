/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.ows;

import java.net.URI;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsUOM extends OwsValuesUnit {
    public OwsUOM(URI reference, String value) {
        super(reference, value);
    }

    public OwsUOM(String value) {
        super(value);
    }

    public OwsUOM(URI reference) {
        super(reference);
    }

    @Override
    public boolean isUOM() {
        return true;
    }

    @Override
    public OwsUOM asUOM() {
        return this;
    }

}
