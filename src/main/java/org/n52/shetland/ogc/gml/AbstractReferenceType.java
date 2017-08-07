/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.gml;

import java.util.Objects;

import org.n52.janmayen.Comparables;
import org.n52.shetland.w3c.xlink.W3CHrefAttribute;

public class AbstractReferenceType
        implements Comparable<AbstractReferenceType> {

    /**
     * Href
     */
    private W3CHrefAttribute href;

    /**
     * Title
     */
    private String title;

    /**
     * Role
     */
    private String role;

    /**
     * Get href
     *
     * @return Href
     */
    public String getHref() {
        if (href != null) {
            return href.getHref();
        }
        return null;
    }

    /**
     * Get title
     *
     * @return Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get role
     *
     * @return Role
     */
    public String getRole() {
        return role;
    }

    /**
     * Set href
     *
     * @param href
     *            Href to set
     */
    public AbstractReferenceType setHref(String href) {
        this.href = new W3CHrefAttribute(href);
        return this;
    }

    /**
     * Set title
     *
     * @param title
     *            Title to set
     */
    public AbstractReferenceType setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Set role
     *
     * @param role
     *            Role to set
     */
    public AbstractReferenceType setRole(String role) {
        this.role = role;
        return this;
    }

    /**
     * Check whether href is set
     *
     * @return <code>true</code>, if href is set
     */
    public boolean isSetHref() {
        return this.href != null && this.href.isSetHref();
    }

    /**
     * Check whether title is set
     *
     * @return <code>true</code>, if title is set
     */
    public boolean isSetTitle() {
        return this.title != null && !this.title.isEmpty();
    }

    /**
     * Check whether role is set
     *
     * @return <code>true</code>, if role is set
     */
    public boolean isSetRole() {
        return this.role != null && !this.role.isEmpty();
    }

    /**
     * Check whether href, title, and role are set
     *
     * @return <code>true</code>, if href, title, and role are set
     */
    public boolean hasValues() {
        return isSetHref() && isSetRole() && isSetTitle();
    }

    /**
     * Get title from href.<br>
     * Cuts href: <br>
     * - starts with 'http': cuts string at last '/'<br>
     * - starts with 'urn': cuts string at last ':'<br>
     * - contains "#": cuts string at last "#"<br>
     *
     * @return Title from href
     */
    public String getTitleOrFromHref() {
        if (isSetTitle()) {
            return getTitle();
        }
        String t = getHref();
        if (t.startsWith("http")) {
            t = t.substring(t.lastIndexOf('/') + 1, t.length());
        } else if (t.startsWith("urn")) {
            t = t.substring(t.lastIndexOf(':') + 1, t.length());
        }
        if (t.contains("#")) {
            t = t.substring(t.lastIndexOf("#") + 1, t.length());
        }
        return t;
    }

    @Override
    public String toString() {
        return String.format("AbstractReferenceType [title=%s, role=%s, href=%s]", getTitle(), getRole(), getHref());
    }

    @Override
    public int compareTo(AbstractReferenceType o) {
        Objects.requireNonNull(o);
        return Comparables.compare(getHref(), o.getHref());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.href);
        hash = 29 * hash + Objects.hashCode(this.title);
        hash = 29 * hash + Objects.hashCode(this.role);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractReferenceType other = (AbstractReferenceType) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.role, other.role)) {
            return false;
        }
        if (!Objects.equals(this.href, other.href)) {
            return false;
        }
        return true;
    }

}
