/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sensorML;

import java.util.List;

import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Lists;

/**
 * Implementation for sml:ContactList
 *
 * @author Shane StClair
 *
 * @since 1.0.0
 */
public class SmlContactList extends SmlContact {
    private List<SmlContact> members;

    public boolean isSetMembers() {
        return !CollectionHelper.nullEmptyOrContainsOnlyNulls(members);
    }


    public List<SmlContact> getMembers() {
        return members;
    }

    public void setMembers(List<SmlContact> members) {
        if (isSetMembers()) {
            this.members.addAll(members);
        } else {
            this.members = members;
        }
    }

    public void addMember(SmlContact member) {
        if (!isSetMembers()) {
            this.members = Lists.newArrayList();
        }
        this.members.add(member);
    }
}
