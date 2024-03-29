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

import org.n52.shetland.ogc.gml.time.TimeInstant;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @since 1.0.0
 *
 */
public class SmlDocumentation extends AbstractSmlDocumentation {

    private String version;

    private TimeInstant date;

    private String contact;

    private String format;

    private String onlineResource;

    public String getVersion() {
        return version;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public TimeInstant getDate() {
        return date;
    }

    public String getContact() {
        return contact;
    }

    public String getFormat() {
        return format;
    }

    public String getOnlineResource() {
        return onlineResource;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SmlDocumentation setDate(TimeInstant date) {
        this.date = date;
        return this;
    }

    public SmlDocumentation setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public SmlDocumentation setFormat(String format) {
        this.format = format;
        return this;
    }

    public SmlDocumentation setOnlineResource(String onlineResource) {
        this.onlineResource = onlineResource;
        return this;
    }

    public boolean isSetVersion() {
        return version != null && !version.isEmpty();
    }

    public boolean isSetDate() {
        return date != null;
    }

    public boolean isSetContact() {
        return contact != null && !contact.isEmpty();
    }

    public boolean isSetFormat() {
        return format != null && !format.isEmpty();
    }

    public boolean isSeOnlineResource() {
        return onlineResource != null && !onlineResource.isEmpty();
    }

}
