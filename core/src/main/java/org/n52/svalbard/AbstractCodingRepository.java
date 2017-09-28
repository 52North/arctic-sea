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
package org.n52.svalbard;

import org.n52.janmayen.component.AbstractSimilarityKeyComponentRepository;
import org.n52.janmayen.component.Component;
import org.n52.janmayen.component.ComponentFactory;
import org.n52.janmayen.similar.Similar;


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <K> the key type
 * @param <C> the component type
 * @param <F> the factory type
 * @deprecated use {@link AbstractSimilarityKeyComponentRepository}
 */
@SuppressWarnings("checkstyle:linelength")
@Deprecated
public abstract class AbstractCodingRepository<K extends Similar<K>, C extends Component<K>, F extends ComponentFactory<K, C>>
        extends AbstractSimilarityKeyComponentRepository<K, C, F> {


}
