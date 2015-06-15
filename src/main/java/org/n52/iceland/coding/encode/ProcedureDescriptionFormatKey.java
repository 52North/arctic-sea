/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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

import org.n52.iceland.service.operator.ServiceOperatorKey;

import com.google.common.base.Objects;

/**
 * @since 4.0.0
 *
 */
public class ProcedureDescriptionFormatKey {
    private ServiceOperatorKey serviceOperatorKey;

    private String procedureDescriptionFormat;

    public ProcedureDescriptionFormatKey(ServiceOperatorKey serviceOperatorKey, String responseFormat) {
        this.serviceOperatorKey = serviceOperatorKey;
        this.procedureDescriptionFormat = responseFormat;
    }

    public ProcedureDescriptionFormatKey() {
        this(null, null);
    }

    @Deprecated
    public ServiceOperatorKey getServiceOperatorKeyType() {
        return getServiceOperatorKey();
    }

    @Deprecated
    public void setServiceOperatorKeyType(ServiceOperatorKey serviceOperatorKeyType) {
        setServiceOperatorKey(serviceOperatorKeyType);
    }


     public ServiceOperatorKey getServiceOperatorKey() {
        return serviceOperatorKey;
    }

    public void setServiceOperatorKey(ServiceOperatorKey serviceOperatorKey) {
        this.serviceOperatorKey = serviceOperatorKey;
    }

    public String getProcedureDescriptionFormat() {
        return procedureDescriptionFormat;
    }

    public void setProcedureDescriptionFormat(String responseFormat) {
        this.procedureDescriptionFormat = responseFormat;
    }

    public String getService() {
        return getServiceOperatorKey() != null ? getServiceOperatorKey().getService() : null;
    }

    public String getVersion() {
        return getServiceOperatorKey() != null ? getServiceOperatorKey().getVersion() : null;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getServiceOperatorKey(), getProcedureDescriptionFormat());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProcedureDescriptionFormatKey) {
            ProcedureDescriptionFormatKey o = (ProcedureDescriptionFormatKey) obj;
            return Objects.equal(getServiceOperatorKey(), o.getServiceOperatorKey())
                    && Objects.equal(getProcedureDescriptionFormat(), o.getProcedureDescriptionFormat());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s[serviceOperatorKeyType=%s, procedureDescriptionFormat=%s]", getClass()
                .getSimpleName(), getServiceOperatorKey(), getProcedureDescriptionFormat());
    }
}
