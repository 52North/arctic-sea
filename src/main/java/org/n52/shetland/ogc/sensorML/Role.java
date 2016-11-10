/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sensorML;

import org.n52.shetland.iso.CodeList;

/**
 * Class that represents SensorML Role
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.2.0
 *
 */
public class Role {

    private String value;

    private String codeList = CodeList.CI_ROLE_CODE_URL;

    private String codeListValue = CodeList.CiRoleCodes.CI_RoleCode_pointOfContact.name();

    public Role(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public Role setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * @return the codeList
     */
    public String getCodeList() {
        return codeList;
    }

    /**
     * @param codeList
     *            the codeList to set
     */
    public Role setCodeList(String codeList) {
        this.codeList = codeList;
        return this;
    }

    /**
     * @return the codeListValue
     */
    public String getCodeListValue() {
        return codeListValue;
    }

    /**
     * @param codeListValue
     *            the codeListValue to set
     */
    public Role setCodeListValue(String codeListValue) {
        this.codeListValue = codeListValue;
        return this;
    }

    /**
     * @param codeListValue
     *            the codeListValue to set
     */
    public Role setCodeListValue(CodeList.CiRoleCodes codeListValue) {
        this.codeListValue = codeListValue.name();
        return this;
    }

}
