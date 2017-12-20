/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.iso.gmd;

import java.net.URI;

import org.n52.shetland.w3c.Nillable;

import com.google.common.base.Strings;

/**
 * Internal representation of the ISO GMD OnlineResource.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class CiOnlineResource
        extends AbstractObject {

    /* 1..1 */
    private Nillable<URI> linkage;
    /* 0..1 */
    private Nillable<String> protocol;
    /* 0..1 */
    private String applicationProfile;
    /* 0..1 */
    private String name;
    /* 0..1 */
    private String description;
    /* 0..1 */
    private String function;

    // <xs:element name="linkage" type="gmd:URL_PropertyType"/>
    // <xs:element name="protocol" type="gco:CharacterString_PropertyType"
    // minOccurs="0"/>
    // <xs:element name="applicationProfile"
    // type="gco:CharacterString_PropertyType" minOccurs="0"/>
    // <xs:element name="name" type="gco:CharacterString_PropertyType"
    // minOccurs="0"/>
    // <xs:element name="description" type="gco:CharacterString_PropertyType"
    // minOccurs="0"/>
    // <xs:element name="function" type="gmd:CI_OnLineFunctionCode_PropertyType"
    // minOccurs="0"/>

    public CiOnlineResource(Nillable<URI> linkage) {
        this.linkage = linkage;
    }

    public CiOnlineResource(URI linkage) {
        this.linkage = Nillable.of(linkage);
    }

    public CiOnlineResource(String linkage) {
        this.linkage = Nillable.of(URI.create(linkage));
    }

    /**
     * @return the linkage
     */
    public Nillable<URI> getLinkage() {
        return linkage;
    }

    /**
     * @param linkage
     *            the linkage to set
     */
    public void setLinkage(Nillable<URI> linkage) {
        this.linkage = linkage;
    }

    /**
     * @return the protocol
     */
    public Nillable<String> getProtocol() {
        return protocol;
    }

    /**
     * @param protocol
     *            the protocol to set
     */
    public void setProtocol(Nillable<String> protocol) {
        this.protocol = protocol;
    }

    /**
     * @param protocol
     *            the protocol to set
     */
    public void setProtocol(String protocol) {
        this.protocol = Nillable.of(protocol);
    }

    public boolean isSetProtocol() {
        return getProtocol() != null;
    }

    /**
     * @return the applicationProfile
     */
    public String getApplicationProfile() {
        return applicationProfile;
    }

    /**
     * @param applicationProfile
     *            the applicationProfile to set
     */
    public void setApplicationProfile(String applicationProfile) {
        this.applicationProfile = applicationProfile;
    }

    public boolean isSetApplicationProfile() {
        return !Strings.isNullOrEmpty(getApplicationProfile());
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSetName() {
        return !Strings.isNullOrEmpty(getName());
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSetDescription() {
        return !Strings.isNullOrEmpty(getDescription());
    }

    /**
     * @return the function
     */
    public String getFunction() {
        return function;
    }

    /**
     * @param function
     *            the function to set
     */
    public void setFunction(String function) {
        this.function = function;
    }

    public boolean isSetFunction() {
        return !Strings.isNullOrEmpty(getFunction());
    }

}
