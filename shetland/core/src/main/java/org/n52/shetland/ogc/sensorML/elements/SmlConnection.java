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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class SmlConnection {

    private String name;

    private String title;

    private String href;

    private String role;

    private List<SmlLink> connections = Lists.newArrayList();

    public SmlConnection() {

    }

    /**
     * constructor
     *
     * @param name
     *            Component name
     */
    public SmlConnection(String name) {
        super();
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getHref() {
        return href;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<SmlLink> getConnections() {
        return Collections.unmodifiableList(connections);
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SmlConnection setConnections(Collection<SmlLink> connections) {
        this.connections.clear();
        if (connections != null) {
            this.connections.addAll(connections);
        }
        return this;
    }

    public SmlConnection addConnection(SmlLink connection) {
        if (connection != null) {
            this.connections.add(connection);
        }
        return this;
    }

    public boolean isSetConnections() {
        return getConnections() != null && !getConnections().isEmpty();
    }
}
