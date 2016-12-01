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
package org.n52.shetland.ogc.wps.description;

public interface ProcessDescriptionBuilderFactory<
        PD extends ProcessDescription,
        GID extends GroupInputDescription,
        GOD extends GroupOutputDescription,
        LID extends LiteralInputDescription,
        LOD extends LiteralOutputDescription,
        CID extends ComplexInputDescription,
        COD extends ComplexOutputDescription,
        BID extends BoundingBoxInputDescription,
        BOD extends BoundingBoxOutputDescription,
        LDD extends LiteralDataDomain
        > {

    ProcessDescription.Builder<PD, ?> process();
    GroupOutputDescription.Builder<GOD, ?> groupOutput();
    GroupInputDescription.Builder<GID, ?> groupInput();
    LiteralInputDescription.Builder<LID, ?> literalInput();
    LiteralOutputDescription.Builder<LOD, ?> literalOutput();
    ComplexInputDescription.Builder<CID, ?> complexInput();
    ComplexOutputDescription.Builder<COD, ?> complexOutput();
    BoundingBoxInputDescription.Builder<BID, ?> boundingBoxInput();
    BoundingBoxOutputDescription.Builder<BOD, ?> boundingBoxOutput();
    LiteralDataDomain.Builder<LDD, ?> literalDataDomain();
}
