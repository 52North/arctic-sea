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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class OwsAcceptVersions {

    private List<String> acceptVersions = new ArrayList<>();

    /**
     * @return the acceptVersions
     */
    public List<String> getAcceptVersions() {
        return Collections.unmodifiableList(acceptVersions);
    }

    /**
     * @param acceptVersions
     *            the acceptVersions to set
     * @return this
     */
    public OwsAcceptVersions setAcceptVersions(Collection<String> acceptVersions) {
        this.acceptVersions.clear();
        this.acceptVersions.addAll(Objects.requireNonNull(acceptVersions));
        return this;
    }

    /**
     * @param acceptVersions
     *            the acceptVersions to add
     * @return this
     */
    public OwsAcceptVersions addAcceptVersions(Collection<String> acceptVersions) {
        if (acceptVersions != null) {
            this.acceptVersions.addAll(acceptVersions);
        }
        return this;
    }

    /**
     * @param acceptVersion
     *            the acceptVersion to add
     * @return this
     */
    public OwsAcceptVersions addAcceptVersions(String acceptVersion) {
        if (acceptVersion != null) {
            this.acceptVersions.add(acceptVersion);
        }
        return this;
    }

    public boolean isSetAcceptVersions() {
        return !acceptVersions.isEmpty();
    }

}
