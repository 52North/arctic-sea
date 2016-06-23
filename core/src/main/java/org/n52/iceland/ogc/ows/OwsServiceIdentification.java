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
import java.util.Set;

import org.n52.iceland.i18n.MultilingualString;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsServiceIdentification extends OwsDescription {

    private final OwsCode serviceType;
    private final Set<String> serviceTypeVersion;
    private final Set<URI> profiles;
    private final Set<String> fees;
    private final Set<String> accessConstraints;

    public OwsServiceIdentification(OwsCode serviceType,
                                    Set<String> serviceTypeVersion,
                                    Set<URI> profiles,
                                    Set<String> fees,
                                    Set<String> accessConstraints,
                                    MultilingualString title,
                                    MultilingualString abstrakt,
                                    Set<OwsKeyword> keywords) {
        super(title, abstrakt, keywords);
        this.serviceType = serviceType;
        this.serviceTypeVersion
                = serviceTypeVersion == null ? Collections.emptySet()
                          : serviceTypeVersion;
        this.profiles = profiles == null ? Collections.emptySet() : profiles;
        this.fees = fees == null ? Collections.emptySet() : fees;
        this.accessConstraints
                = accessConstraints == null ? Collections.emptySet()
                          : accessConstraints;
    }

    public OwsCode getServiceType() {
        return serviceType;
    }

    public Set<String> getServiceTypeVersion() {
        return Collections.unmodifiableSet(serviceTypeVersion);
    }

    public Set<URI> getProfiles() {
        return Collections.unmodifiableSet(profiles);
    }

    public Set<String> getFees() {
        return Collections.unmodifiableSet(fees);
    }

    public Set<String> getAccessConstraints() {
        return Collections.unmodifiableSet(accessConstraints);
    }

}
