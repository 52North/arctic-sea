/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class Sections {

    private List<String> sections = Lists.newArrayList();

    /**
     * @return the sections
     */
    public List<String> getSections() {
        return sections;
    }

    /**
     * @param sections the sections to set
     */
    public Sections setSections(List<String> sections) {
        this.sections.clear();
        this.sections = sections;
        return this;
    }

    /**
     * @param sections the sections to add
     */
    public Sections addSections(List<String> sections) {
        this.sections.addAll(sections);
        return this;
    }

    /**
     * @param section the section to add
     */
    public Sections addSections(String section) {
        this.sections.add(section);
        return this;
    }

    public boolean isSetSections() {
        return !sections.isEmpty();
    }
}
