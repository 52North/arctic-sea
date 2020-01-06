/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.gml.GmlConstants.NilReason;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GmdConformanceResult extends GmdDomainConsistency {

    private final boolean pass;
    private final NilReason passNilReason;
    private final GmdSpecification specification;

    public GmdConformanceResult(boolean pass, GmdSpecification specification) {
        this.pass = pass;
        this.passNilReason = null;
        this.specification = specification;
    }

    public GmdConformanceResult(NilReason passNilReason, GmdSpecification specification) {
        this.pass = false;
        this.passNilReason = passNilReason;
        this.specification = specification;
    }

    public boolean isPass() {
        return pass;
    }

    public NilReason getPassNilReason() {
        return passNilReason;
    }

    public boolean isSetPassNilReason() {
        return getPassNilReason() != null;
    }

    public GmdSpecification getSpecification() {
        return specification;
    }

}
