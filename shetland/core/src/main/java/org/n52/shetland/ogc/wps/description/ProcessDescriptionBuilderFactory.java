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
package org.n52.shetland.ogc.wps.description;

@SuppressWarnings("checkstyle:interfacetypeparametername")
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
                                                         LDD extends LiteralDataDomain> {

    ProcessDescription.Builder<? extends PD, ?> process();

    ProcessDescription.Builder<? extends PD, ?> process(ProcessDescription entity);

    GroupOutputDescription.Builder<? extends GOD, ?> groupOutput();

    GroupOutputDescription.Builder<? extends GOD, ?> groupOutput(GroupOutputDescription entity);

    GroupInputDescription.Builder<? extends GID, ?> groupInput();

    GroupInputDescription.Builder<? extends GID, ?> groupInput(GroupInputDescription entity);

    LiteralInputDescription.Builder<? extends LID, ?> literalInput();

    LiteralInputDescription.Builder<? extends LID, ?> literalInput(LiteralInputDescription entity);

    LiteralOutputDescription.Builder<? extends LOD, ?> literalOutput();

    LiteralOutputDescription.Builder<? extends LOD, ?> literalOutput(LiteralOutputDescription entity);

    ComplexInputDescription.Builder<? extends CID, ?> complexInput();

    ComplexInputDescription.Builder<? extends CID, ?> complexInput(ComplexInputDescription entity);

    ComplexOutputDescription.Builder<? extends COD, ?> complexOutput();

    ComplexOutputDescription.Builder<? extends COD, ?> complexOutput(ComplexOutputDescription entity);

    BoundingBoxInputDescription.Builder<? extends BID, ?> boundingBoxInput();

    BoundingBoxInputDescription.Builder<? extends BID, ?> boundingBoxInput(BoundingBoxInputDescription entity);

    BoundingBoxOutputDescription.Builder<? extends BOD, ?> boundingBoxOutput();

    BoundingBoxOutputDescription.Builder<? extends BOD, ?> boundingBoxOutput(BoundingBoxOutputDescription entity);

    LiteralDataDomain.Builder<? extends LDD, ?> literalDataDomain();

    LiteralDataDomain.Builder<? extends LDD, ?> literalDataDomain(LiteralDataDomain entity);
}