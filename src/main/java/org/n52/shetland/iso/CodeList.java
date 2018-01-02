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
package org.n52.shetland.iso;

public interface CodeList {

    String CODE_LIST_URL = "http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml";

    String CI_ROLE_CODE_URL = CODE_LIST_URL + "#CI_RoleCode/";

    enum CiRoleCodes {
        CI_RoleCode_resourceProvider("resourceProvider"),
        CI_RoleCode_custodian("custodian"),
        CI_RoleCode_owner("owner"),
        CI_RoleCode_user("user"),
        CI_RoleCode_distributor("distributor"),
        CI_RoleCode_originator("originator"),
        CI_RoleCode_pointOfContact("pointOfContact"),
        CI_RoleCode_principalInvestigator("principalInvestigator"),
        CI_RoleCode_processor("processor"),
        CI_RoleCode_publisher("publisher"),
        CI_RoleCode_author("author");

        private String identifier;

        CiRoleCodes(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;
        }
    }


}
