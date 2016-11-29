/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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

import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;

import com.google.common.collect.Sets;

/**
 * @since 1.0.0
 *
 */
public class TestBinding extends SimpleBinding {
    private static final String URL_PATTERN = "/service/test";

    @Override
    protected boolean isUseHttpResponseCodes() {
        return false;
    }

    @Override
    public Set<String> getConformanceClasses(String service, String version) {
        return Collections.emptySet();
    }

    @Override
    protected MediaType getDefaultContentType() {
        return MediaTypes.APPLICATION_XML;
    }

    @Override
    public Set<BindingKey> getKeys() {
        return Sets.<BindingKey>newHashSet(new PathBindingKey(URL_PATTERN));
    }

    @Override
    public String getUrlPattern() {
        return URL_PATTERN;
    }

}
