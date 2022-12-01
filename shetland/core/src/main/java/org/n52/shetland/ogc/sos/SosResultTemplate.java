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
package org.n52.shetland.ogc.sos;

import org.n52.shetland.ogc.gml.CodeWithAuthority;

/**
 * @since 1.0.0
 *
 */
public class SosResultTemplate {

    private CodeWithAuthority identifier;
    private SosResultStructure resultStructure;
    private SosResultEncoding resultEncoding;

    public CodeWithAuthority getIdentifier() {
        return identifier;
    }

    public void setIdentifier(CodeWithAuthority identifier) {
        this.identifier = identifier;
    }

    public SosResultStructure getResultStructure() {
        return resultStructure;
    }

    public void setResultStructure(SosResultStructure resultStructure) {
        this.resultStructure = resultStructure;
    }

    public SosResultEncoding getResultEncoding() {
        return resultEncoding;
    }

    public void setResultEncoding(SosResultEncoding resultEncoding) {
        this.resultEncoding = resultEncoding;
    }

}
