/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.ows;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsRequestMethod extends OwsOnlineResource {

    private final List<OwsDomain> constraints;
    private final String httpMethod;

    public OwsRequestMethod(URI href, String httpMethod, List<OwsDomain> constraints) {
        super(href);
        this.httpMethod = Objects.requireNonNull(Strings.emptyToNull(httpMethod));
        this.constraints = constraints == null ? Collections.emptyList() : constraints;
    }

    public OwsRequestMethod(URI href, List<OwsDomain> constraints, String httpMethod,
                            URI role, URI arcrole, String title, Show show, Actuate actuate) {
        super(href, role, arcrole, title, show, actuate);
        this.httpMethod = Objects.requireNonNull(Strings.emptyToNull(httpMethod));
        this.constraints = constraints == null ? Collections.emptyList() : constraints;
    }

    public List<OwsDomain> getConstraints() {
        return Collections.unmodifiableList(this.constraints);
    }

    public String getHttpMethod() {
        return this.httpMethod;
    }

}
