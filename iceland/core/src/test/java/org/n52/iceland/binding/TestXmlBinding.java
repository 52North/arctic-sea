/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.binding;

import java.util.Collections;
import java.util.Set;

import org.n52.iceland.coding.DocumentBuilderProvider;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;

import com.google.common.collect.ImmutableSet;

public class TestXmlBinding extends AbstractXmlBinding<OwsServiceRequest> {

    private static final PathBindingKey PATH_KEY
            = new PathBindingKey("/service/test");
    private static final MediaTypeBindingKey MEDIA_TYPE_KEY
            = new MediaTypeBindingKey(MediaTypes.APPLICATION_XML);
    private static final ImmutableSet<BindingKey> KEYS
            = ImmutableSet.of(PATH_KEY, MEDIA_TYPE_KEY);

    public TestXmlBinding() {
        DocumentBuilderProvider fac = new DocumentBuilderProvider();
        fac.init();
        super.setDocumentFactory(fac);
    }

    @Override
    protected boolean isUseHttpResponseCodes() {
        return false;
    }

    @Override
    public Set<BindingKey> getKeys() {
        return Collections.unmodifiableSet(KEYS);
    }

    @Override
    public Set<String> getConformanceClasses(String service, String version) {
        return Collections.emptySet();
    }

    @Override
    protected MediaType getDefaultContentType() {
        return MEDIA_TYPE_KEY.getMediaType();
    }

}
