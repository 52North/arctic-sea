/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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


/**
 * Class represents a GML conform ReferenceType element
 *
 * @since 1.0.0
 *
 */
public class ReferenceType extends AbstractReferenceType {

    /**
     * constructor
     */
    public ReferenceType() {
    }

    /**
     * constructor
     *
     * @param href
     *            Href
     */
    public ReferenceType(String href) {
        setHref(href);
    }

    /**
     * constructor
     *
     * @param href
     *            Href
     * @param title
     *            Title
     */
    public ReferenceType(String href, String title) {
        setHref(href);
        setTitle(title);
    }

    @Override
    public String toString() {
        return String.format("ReferenceType [title=%s, role=%s, href=%s]", getTitle(), getRole(), getHref());
    }

    public boolean isEmpty() {
        return !isSetHref() && !isSetTitle() && !isSetRole();
    }
}
