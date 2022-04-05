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
package org.n52.shetland.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.n52.janmayen.http.QueryBuilder;
import org.n52.shetland.ogc.ows.OWSConstants;



public class OwsQueryBuilder extends QueryBuilder {

    public OwsQueryBuilder(URL url) {
        super(url);
    }

    public OwsQueryBuilder(URL url, Charset charset) {
        super(url, charset);
    }

    public OwsQueryBuilder(String url) throws MalformedURLException {
        super(url);
    }

    public OwsQueryBuilder addService(String service) {
        add(OWSConstants.RequestParams.service, service);
        return this;
    }

    public OwsQueryBuilder addVersion(String version) {
        add(OWSConstants.RequestParams.version, version);
        return this;
    }

    public OwsQueryBuilder addRequest(String request) {
        add(OWSConstants.RequestParams.request, request);
        return this;
    }

    public OwsQueryBuilder addRequest(Enum<?> request) {
        add(OWSConstants.RequestParams.request, request);
        return this;
    }

}
