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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @since 1.0.0
 *
 */
public class SmlDocumentationListMember {

    private String name;

    private SmlDocumentation documentation;

    public String getName() {
        return name;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public SmlDocumentation getDocumentation() {
        return documentation;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SmlDocumentationListMember setDocumentation(SmlDocumentation documentation) {
        this.documentation = documentation;
        return this;
    }

}
