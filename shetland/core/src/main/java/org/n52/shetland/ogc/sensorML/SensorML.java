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
package org.n52.shetland.ogc.sensorML;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.ogc.UoM;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * SOS internal representation of a sensor description
 *
 * @since 1.0.0
 */
public class SensorML extends AbstractSensorML {

    private String version;
    private final List<AbstractProcess> members = new LinkedList<AbstractProcess>();

    /**
     * default constructor
     */
    public SensorML() {
    }

    public String getVersion() {
        return version;
    }

    public SensorML setVersion(final String version) {
        this.version = version;
        return this;
    }

    public List<AbstractProcess> getMembers() {
        return Collections.unmodifiableList(members);
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SensorML setMembers(final Collection<AbstractProcess> members) {
        if (members != null) {
            for (final AbstractProcess member : members) {
                addMember(member);
            }
        }
        return this;
    }

    public SensorML addMember(final AbstractProcess member) {
        if (isEmpty() && !isSetIdentifier() && member.isSetIdentifier()) {
            setIdentifier(member.getIdentifierCodeWithAuthority());
        }
        members.add(member);
        return this;
    }

    /**
     * @return <tt>true</tt>, if everything from the super class is not set
     */
    private boolean isEmpty() {
        // don't check validTime
        return !isSetKeywords() && !isSetIdentifications() && !isSetClassifications() && !isSetCapabilities()
                && !isSetCharacteristics() && !isSetContact() && !isSetDocumentation() && !isSetHistory();
    }

    /**
     * @return <tt>true</tt>, if this instance contains only members and everything else is not set
     */
    public boolean isWrapper() {
        return isEmpty() && isSetMembers();
    }

    public boolean isSetMembers() {
        return !members.isEmpty();
    }

    @Override
    public String getObservablePropertyName(String observableProperty) {
        if (isWrapper()) {
            return getMembers().iterator().next().getObservablePropertyName(observableProperty);
        }
        return null;
    }

    @Override
    public String getObservablePropertyDescription(String observableProperty) {
        if (isWrapper()) {
            return getMembers().iterator().next().getObservablePropertyDescription(observableProperty);
        }
        return null;
    }

    @Override
    public UoM getObservablePropertyUnit(String observableProperty) {
        if (isWrapper()) {
            return getMembers().iterator().next().getObservablePropertyUnit(observableProperty);
        }
        return null;
    }

    @Override
    public String getDefaultElementEncoding() {
        return SensorMLConstants.NS_SML;
    }
}
