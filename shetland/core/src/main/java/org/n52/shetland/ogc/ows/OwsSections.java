/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class OwsSections {

    private List<String> sections = new ArrayList<>();

    /**
     * @return the sections
     */
    public List<String> getSections() {
        return Collections.unmodifiableList(sections);
    }

    /**
     * @param sections the sections to set
     *
     * @return this
     */
    public OwsSections setSections(List<String> sections) {
        this.sections.clear();
        this.sections = sections;
        return this;
    }

    /**
     * @param sections the sections to add
     *
     * @return this
     */
    public OwsSections addSections(List<String> sections) {
        this.sections.addAll(sections);
        return this;
    }

    /**
     * @param section the section to add
     *
     * @return this
     */
    public OwsSections addSections(String section) {
        this.sections.add(section);
        return this;
    }

    public boolean isSetSections() {
        return !sections.isEmpty();
    }
}
