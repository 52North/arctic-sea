/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.coding.encode;

import org.n52.shetland.ogc.ows.service.OwsServiceKey;

import com.google.common.base.Objects;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class ResponseFormatKey {
    private OwsServiceKey serviceOperatorKey;

    private String responseFormat;

    public ResponseFormatKey(OwsServiceKey serviceOperatorKey, String responseFormat) {
        this.serviceOperatorKey = serviceOperatorKey;
        this.responseFormat = responseFormat;
    }

    public ResponseFormatKey() {
        this(null, null);
    }

    public OwsServiceKey getServiceOperatorKey() {
        return this.serviceOperatorKey;
    }

    public void setServiceOperatorKey(OwsServiceKey serviceOperatorKey) {
        this.serviceOperatorKey = serviceOperatorKey;
    }

    public String getResponseFormat() {
        return responseFormat;
    }

    public void setResponseFormat(String responseFormat) {
        this.responseFormat = responseFormat;
    }

    public String getService() {
        return getServiceOperatorKey() != null ? getServiceOperatorKey().getService() : null;
    }

    public String getVersion() {
        return getServiceOperatorKey() != null ? getServiceOperatorKey().getVersion() : null;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getServiceOperatorKey(), getResponseFormat());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ResponseFormatKey) {
            ResponseFormatKey o = (ResponseFormatKey) obj;
            return Objects.equal(getServiceOperatorKey(), o.getServiceOperatorKey())
                    && Objects.equal(getResponseFormat(), o.getResponseFormat());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s[serviceOperatorKeyType=%s, responseFormat=%s]", getClass().getSimpleName(),
                getServiceOperatorKey(), getResponseFormat());
    }
}
