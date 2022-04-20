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

import org.n52.shetland.util.CollectionHelper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Implementation for sml:ContactList
 *
 * @author Shane StClair
 *
 * @since 1.0.0
 */
public class SmlContactList extends SmlContact {
    private List<SmlContact> members = new LinkedList<>();

    public boolean isSetMembers() {
        return !CollectionHelper.nullEmptyOrContainsOnlyNulls(members);
    }

    public List<SmlContact> getMembers() {
        return Collections.unmodifiableList(members);
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SmlContactList setMembers(Collection<SmlContact> members) {
        this.members.clear();
        if (CollectionHelper.isNotEmpty(members)) {
            this.members.addAll(members);
        }
        return this;
    }

    public SmlContactList addMember(SmlContact member) {
        if (member != null) {
        this.members.add(member);
        }
        return this;
    }
}
