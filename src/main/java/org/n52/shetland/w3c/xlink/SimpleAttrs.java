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
package org.n52.shetland.w3c.xlink;


/**
 * Class represents W3C Xlink SimpleAttrs.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class SimpleAttrs {

    private String href;
    private String role;
    private String arcrole;
    private String title;
    private Show show;
    private Actuate actuate;

    /**
     * @return the type
     */
    public Type getType() {
        return Type.SIMPLE;
    }

    /**
     * @return the href
     */
    public String getHref() {
        return href;
    }

    /**
     * @param href
     *             the href to set
     * @return {@code this}
     */
    public SimpleAttrs setHref(String href) {
        this.href = href;
        return this;
    }

    /**
     * @return <code>true</code>, if href is not null or empty
     */
    public boolean isSetHref() {
        return this.href != null && !this.href.isEmpty();
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role
     *             the role to set
     * @return {@code this}
     */
    public SimpleAttrs setRole(String role) {
        this.role = role;
        return this;
    }

    /**
     * @return <code>true</code>, if role is not null or empty
     */
    public boolean isSetRole() {
        return this.role != null && !this.role.isEmpty();
    }

    /**
     * @return the arcrole
     */
    public String getArcrole() {
        return arcrole;
    }

    /**
     * @param arcrole
     *                the arcrole to set
     * @return {@code this}
     */
    public SimpleAttrs setArcrole(String arcrole) {
        this.arcrole = arcrole;
        return this;
    }

    /**
     * @return <code>true</code>, if arcrole is not null or empty
     */
    public boolean isSetArcrole() {
        return this.arcrole != null && !this.arcrole.isEmpty();
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *              the title to set
     * @return {@code this}
     */
    public SimpleAttrs setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * @return <code>true</code>, if title is not null or empty
     */
    public boolean isSetTitle() {
        return this.title != null && !this.title.isEmpty();
    }

    /**
     * @return the show
     */
    public Show getShow() {
        return show;
    }

    /**
     * @param show
     *             the show to set
     * @return {@code this}
     */
    public SimpleAttrs setShow(Show show) {
        this.show = show;
        return this;
    }

    /**
     * @return <code>true</code>, if show is not null
     */
    public boolean isSetShow() {
        return getShow() != null;
    }

    /**
     * @return the actuate
     */
    public Actuate getActuate() {
        return actuate;
    }

    /**
     * @param actuate
     *                the actuate to set
     * @return {@code this}
     */
    public SimpleAttrs setActuate(Actuate actuate) {
        this.actuate = actuate;
        return this;
    }

    /**
     * @return <code>true</code>, if actuate is not null
     */
    public boolean isSetActuate() {
        return getActuate() != null;
    }

}
