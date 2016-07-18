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
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

import org.n52.iceland.i18n.MultilingualString;
import org.n52.iceland.util.CollectionHelper;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsServiceIdentification extends OwsDescription {

    private final OwsCode serviceType;
    private final SortedSet<String> serviceTypeVersion;
    private final SortedSet<URI> profiles;
    private final SortedSet<String> fees;
    private final SortedSet<String> accessConstraints;

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
        this.serviceTypeVersion = CollectionHelper.newSortedSet(serviceTypeVersion);
        this.profiles = CollectionHelper.newSortedSet(profiles);
        this.fees = CollectionHelper.newSortedSet(fees);
        this.accessConstraints = CollectionHelper.newSortedSet(accessConstraints);
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.serviceType);
        hash = 53 * hash + Objects.hashCode(this.serviceTypeVersion);
        hash = 53 * hash + Objects.hashCode(this.profiles);
        hash = 53 * hash + Objects.hashCode(this.fees);
        hash = 53 * hash + Objects.hashCode(this.accessConstraints);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OwsServiceIdentification other = (OwsServiceIdentification) obj;
        if (!Objects.equals(this.serviceType, other.serviceType)) {
            return false;
        }
        if (!Objects.equals(this.serviceTypeVersion, other.serviceTypeVersion)) {
            return false;
        }
        if (!Objects.equals(this.profiles, other.profiles)) {
            return false;
        }
        if (!Objects.equals(this.fees, other.fees)) {
            return false;
        }
        if (!Objects.equals(this.accessConstraints, other.accessConstraints)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OwsServiceIdentification{" + "serviceType=" + serviceType +
               ", serviceTypeVersion=" + serviceTypeVersion + ", profiles=" +
               profiles + ", fees=" + fees + ", accessConstraints=" +
               accessConstraints + '}';
    }


}
