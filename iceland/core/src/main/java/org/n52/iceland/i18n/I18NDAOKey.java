/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.iceland.i18n;

import org.n52.iceland.i18n.metadata.AbstractI18NMetadata;
import org.n52.janmayen.component.ClassBasedComponentKey;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class I18NDAOKey extends ClassBasedComponentKey<AbstractI18NMetadata> {

    public I18NDAOKey(Class<? extends AbstractI18NMetadata> type) {
        super(type);
    }

}
