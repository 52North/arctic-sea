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
import org.n52.shetland.ogc.wps.description.impl.BoundingBoxInputDescriptionImpl;
import org.n52.shetland.ogc.wps.description.impl.BoundingBoxOutputDescriptionImpl;
import org.n52.shetland.ogc.wps.description.impl.ComplexInputDescriptionImpl;
import org.n52.shetland.ogc.wps.description.impl.ComplexOutputDescriptionImpl;
import org.n52.shetland.ogc.wps.description.impl.GroupInputDescriptionImpl;
import org.n52.shetland.ogc.wps.description.impl.GroupOutputDescriptionImpl;
import org.n52.shetland.ogc.wps.description.impl.LiteralInputDescriptionImpl;
import org.n52.shetland.ogc.wps.description.impl.LiteralOutputDescriptionImpl;
import org.n52.shetland.ogc.wps.description.impl.ProcessDescriptionImpl;

public class ProcessDescriptionFactory implements ProcessDescriptionBuilderFactory
        <
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

    @Override
    public ProcessDescriptionImpl.Builder process() {
        return new ProcessDescriptionImpl.Builder();
    }

    @Override
    public GroupOutputDescriptionImpl.Builder groupOutput() {
        return new GroupOutputDescriptionImpl.Builder();
    }

    @Override
    public GroupInputDescriptionImpl.Builder groupInput() {
        return new GroupInputDescriptionImpl.Builder();
    }

    @Override
    public LiteralInputDescriptionImpl.Builder literalInput() {
        return new LiteralInputDescriptionImpl.Builder();
    }

    @Override
    public LiteralOutputDescriptionImpl.Builder literalOutput() {
        return new LiteralOutputDescriptionImpl.Builder();
    }

    @Override
    public ComplexInputDescriptionImpl.Builder complexInput() {
        return new ComplexInputDescriptionImpl.Builder();
    }

    @Override
    public ComplexOutputDescriptionImpl.Builder complexOutput() {
        return new ComplexOutputDescriptionImpl.Builder();
    }

    @Override
    public BoundingBoxInputDescriptionImpl.Builder boundingBoxInput() {
        return new BoundingBoxInputDescriptionImpl.Builder();
    }

    @Override
    public BoundingBoxOutputDescriptionImpl.Builder boundingBoxOutput() {
        return new BoundingBoxOutputDescriptionImpl.Builder();
    }

    @Override
    public LiteralDataDomainImpl.Builder literalDataDomain() {
        return new LiteralDataDomainImpl.Builder();
    }

}
