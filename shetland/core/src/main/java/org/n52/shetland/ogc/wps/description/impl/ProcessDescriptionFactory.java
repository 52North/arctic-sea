/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.wps.description.impl;

import org.n52.shetland.ogc.wps.description.BoundingBoxInputDescription;
import org.n52.shetland.ogc.wps.description.BoundingBoxOutputDescription;
import org.n52.shetland.ogc.wps.description.ComplexInputDescription;
import org.n52.shetland.ogc.wps.description.ComplexOutputDescription;
import org.n52.shetland.ogc.wps.description.GroupInputDescription;
import org.n52.shetland.ogc.wps.description.GroupOutputDescription;
import org.n52.shetland.ogc.wps.description.LiteralDataDomain;
import org.n52.shetland.ogc.wps.description.LiteralInputDescription;
import org.n52.shetland.ogc.wps.description.LiteralOutputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;

public class ProcessDescriptionFactory
        implements ProcessDescriptionBuilderFactory<
                                                           ProcessDescription,
                                                           GroupInputDescription,
                                                           GroupOutputDescription,
                                                           LiteralInputDescription,
                                                           LiteralOutputDescription,
                                                           ComplexInputDescription,
                                                           ComplexOutputDescription,
                                                           BoundingBoxInputDescription,
                                                           BoundingBoxOutputDescription,
                                                           LiteralDataDomain
                                                           > {

    private static final ProcessDescriptionFactory INSTANCE = new ProcessDescriptionFactory();

    @Override
    public ProcessDescriptionImpl.Builder process() {
        return new ProcessDescriptionImpl.Builder(this);
    }

    @Override
    public ProcessDescriptionImpl.Builder process(ProcessDescription entity) {
        return new ProcessDescriptionImpl.Builder(this, entity);
    }

    @Override
    public GroupInputDescriptionImpl.Builder groupInput() {
        return new GroupInputDescriptionImpl.Builder(this);
    }

    @Override
    public GroupInputDescriptionImpl.Builder groupInput(GroupInputDescription entity) {
        return new GroupInputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public GroupOutputDescriptionImpl.Builder groupOutput() {
        return new GroupOutputDescriptionImpl.Builder(this);
    }

    @Override
    public GroupOutputDescriptionImpl.Builder groupOutput(GroupOutputDescription entity) {
        return new GroupOutputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public LiteralInputDescriptionImpl.Builder literalInput() {
        return new LiteralInputDescriptionImpl.Builder(this);
    }

    @Override
    public LiteralInputDescriptionImpl.Builder literalInput(LiteralInputDescription entity) {
        return new LiteralInputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public LiteralOutputDescriptionImpl.Builder literalOutput() {
        return new LiteralOutputDescriptionImpl.Builder(this);
    }

    @Override
    public LiteralOutputDescriptionImpl.Builder literalOutput(LiteralOutputDescription entity) {
        return new LiteralOutputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public ComplexInputDescriptionImpl.Builder complexInput() {
        return new ComplexInputDescriptionImpl.Builder(this);
    }

    @Override
    public ComplexInputDescriptionImpl.Builder complexInput(ComplexInputDescription entity) {
        return new ComplexInputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public ComplexOutputDescriptionImpl.Builder complexOutput() {
        return new ComplexOutputDescriptionImpl.Builder(this);
    }

    @Override
    public ComplexOutputDescriptionImpl.Builder complexOutput(ComplexOutputDescription entity) {
        return new ComplexOutputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public BoundingBoxInputDescriptionImpl.Builder boundingBoxInput() {
        return new BoundingBoxInputDescriptionImpl.Builder(this);
    }

    @Override
    public BoundingBoxInputDescriptionImpl.Builder boundingBoxInput(BoundingBoxInputDescription entity) {
        return new BoundingBoxInputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public BoundingBoxOutputDescriptionImpl.Builder boundingBoxOutput() {
        return new BoundingBoxOutputDescriptionImpl.Builder(this);
    }

    @Override
    public BoundingBoxOutputDescriptionImpl.Builder boundingBoxOutput(BoundingBoxOutputDescription entity) {
        return new BoundingBoxOutputDescriptionImpl.Builder(this, entity);
    }

    @Override
    public LiteralDataDomainImpl.Builder literalDataDomain() {
        return new LiteralDataDomainImpl.Builder(this);
    }

    @Override
    public LiteralDataDomainImpl.Builder literalDataDomain(LiteralDataDomain entity) {
        return new LiteralDataDomainImpl.Builder(this, entity);
    }

    public static ProcessDescriptionFactory instance() {
        return INSTANCE;
    }

}
