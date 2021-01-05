/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.janmayen.lifecycle;

import javax.annotation.PreDestroy;

/**
 * @see PreDestroy
 * @since 1.0.0
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 */
public interface Destroyable {
    /**
     * Destroys this object.
     */
    void destroy();
}
