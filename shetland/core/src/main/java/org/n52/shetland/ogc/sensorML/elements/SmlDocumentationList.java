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
package org.n52.shetland.ogc.sensorML.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @since 1.0.0
 *
 */
public class SmlDocumentationList extends AbstractSmlDocumentation {

    private List<SmlDocumentationListMember> members = new ArrayList<SmlDocumentationListMember>(0);

    public List<SmlDocumentationListMember> getMember() {
        return Collections.unmodifiableList(members);
    }

    public SmlDocumentationList setMember(Collection<SmlDocumentationListMember> members) {
        this.members.clear();
        if (members != null) {
            this.members.addAll(members);
        }
       return this;
    }

    public SmlDocumentationList addMember(SmlDocumentationListMember member) {
        this.members.add(member);
        return this;
    }

    public boolean isSetMembers() {
        return members != null && !members.isEmpty();
    }
}
