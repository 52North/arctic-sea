/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.shetland.w3c.wsdl;

import javax.xml.namespace.QName;

public class Include extends AbstractWsdl {

    private String namespace;
    private String schemaLocation;

    public Include(String namespace, String schemaLocation) {
        this.namespace = namespace;
        this.schemaLocation = schemaLocation;
    }

    @Override
    public QName getQName() {
        return WSDLConstants.QN_XSD_INCLUDE;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }
}
