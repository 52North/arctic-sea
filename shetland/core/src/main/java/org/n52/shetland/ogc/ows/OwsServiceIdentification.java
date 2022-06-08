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
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

import org.n52.janmayen.i18n.MultilingualString;
import org.n52.shetland.util.CollectionHelper;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsServiceIdentification extends OwsDescription {

    private OwsCode serviceType;
    private SortedSet<String> serviceTypeVersion;
    private SortedSet<URI> profiles;
    private SortedSet<String> fees;
    private SortedSet<String> accessConstraints;

    public OwsServiceIdentification(OwsCode serviceType, Set<String> serviceTypeVersion, Set<URI> profiles,
            Set<String> fees, Set<String> accessConstraints, MultilingualString title, MultilingualString abstrakt,
            Set<OwsKeyword> keywords) {
        super(title, abstrakt, keywords);
        this.serviceType = Objects.requireNonNull(serviceType);
        this.serviceTypeVersion = CollectionHelper.newSortedSet(serviceTypeVersion);
        this.profiles = CollectionHelper.newSortedSet(profiles);
        this.fees = CollectionHelper.newSortedSet(fees);
        this.accessConstraints = CollectionHelper.newSortedSet(accessConstraints);
    }

    public OwsCode getServiceType() {
        return serviceType;
    }

    public void setServiceType(OwsCode serviceType) {
        this.serviceType = Objects.requireNonNull(serviceType);
    }

    public Set<String> getServiceTypeVersion() {
        return Collections.unmodifiableSet(serviceTypeVersion);
    }

    public void setServiceTypeVersion(Collection<String> serviceTypeVersion) {
        this.serviceTypeVersion = CollectionHelper.newSortedSet(serviceTypeVersion);
    }

    public Set<URI> getProfiles() {
        return Collections.unmodifiableSet(profiles);
    }

    public void setProfiles(Collection<URI> profiles) {
        this.profiles = CollectionHelper.newSortedSet(profiles);
    }

    public Set<String> getFees() {
        return Collections.unmodifiableSet(fees);
    }

    public void setFees(Collection<String> fees) {
        this.fees = CollectionHelper.newSortedSet(fees);
    }

    public Set<String> getAccessConstraints() {
        return Collections.unmodifiableSet(accessConstraints);
    }

    public void setAccessConstraints(Collection<String> accessConstraints) {
        this.accessConstraints = CollectionHelper.newSortedSet(accessConstraints);
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
        if (!Objects.equals(this.serviceType, other.getServiceType())) {
            return false;
        }
        if (!Objects.equals(this.serviceTypeVersion, other.getServiceTypeVersion())) {
            return false;
        }
        if (!Objects.equals(this.profiles, other.getProfiles())) {
            return false;
        }
        if (!Objects.equals(this.fees, other.getFees())) {
            return false;
        }
        return Objects.equals(this.accessConstraints, other.getAccessConstraints());
    }

    @Override
    public String toString() {
        return "OwsServiceIdentification{" + "serviceType=" + serviceType + ", serviceTypeVersion="
                + serviceTypeVersion + ", profiles=" + profiles + ", fees=" + fees + ", accessConstraints="
                + accessConstraints + '}';
    }

}
