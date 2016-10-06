/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;

import org.n52.iceland.util.CollectionHelper;
import org.n52.iceland.util.Optionals;
import org.n52.iceland.w3c.xlink.Actuate;
import org.n52.iceland.w3c.xlink.Show;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsRequestMethod extends OwsOnlineResource implements Comparable<OwsRequestMethod> {
    private static final Comparator<OwsRequestMethod> COMPARATOR
            = Comparator.nullsLast(Comparator.comparing(OwsRequestMethod::getHttpMethod)
                    .thenComparing(OwsRequestMethod::getHref, Optionals.nullsLast())
                    .thenComparing(OwsRequestMethod::getTitle, Optionals.nullsLast()));

    private final SortedSet<OwsDomain> constraints;
    private final String httpMethod;

    public OwsRequestMethod(URI href, String httpMethod, Collection<OwsDomain> constraints) {
        super(href);
        this.httpMethod = Objects.requireNonNull(Strings.emptyToNull(httpMethod));
        this.constraints = CollectionHelper.newSortedSet(constraints);
    }

    public OwsRequestMethod(URI href, Collection<OwsDomain> constraints, String httpMethod,
                            URI role, URI arcrole, String title, Show show, Actuate actuate) {
        super(href, role, arcrole, title, show, actuate);
        this.httpMethod = Objects.requireNonNull(Strings.emptyToNull(httpMethod));
        this.constraints = CollectionHelper.newSortedSet(constraints);
    }

    public SortedSet<OwsDomain> getConstraints() {
        return Collections.unmodifiableSortedSet(this.constraints);
    }

    public String getHttpMethod() {
        return this.httpMethod;
    }

    @Override
    public int compareTo(OwsRequestMethod o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.constraints);
        hash = 47 * hash + Objects.hashCode(this.httpMethod);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        final OwsRequestMethod other = (OwsRequestMethod) obj;
        return Objects.equals(this.httpMethod, other.httpMethod) &&
               Objects.equals(this.constraints, other.constraints);
    }

    @Override
    public String toString() {
        return "OwsRequestMethod{" + "constraints=" + constraints +
               ", httpMethod=" + httpMethod + '}';
    }

}
