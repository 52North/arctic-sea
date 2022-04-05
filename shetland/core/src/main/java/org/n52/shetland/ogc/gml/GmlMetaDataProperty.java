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
package org.n52.shetland.ogc.gml;

/**
 * Class represents a GML conform MetaDataProperty element
 * @since 1.0.0
 *
 */
public class GmlMetaDataProperty {

    /**
     * Title
     */
    private String title;

    /**
     * Role
     */
    private String role;

    /**
     * Href
     */
    private String href;

    /**
     * Set title
     * @param title Title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set role
     * @param role Role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Set href
     * @param href Href to set
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * Get title
     * @return Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get role
     * @return Role
     */
    public String getRole() {
        return role;
    }

    /**
     * Get href
     * @return Href
     */
    public String getHref() {
        return href;
    }

    @Override
    public String toString() {
        return String.format("GmlMetaDataProperty [title=%s, role=%s, href=%s]", getTitle(), getRole(), getHref());
    }
}
