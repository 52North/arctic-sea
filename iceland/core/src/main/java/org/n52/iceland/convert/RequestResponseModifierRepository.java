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
package org.n52.iceland.convert;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.n52.janmayen.Producer;
import org.n52.janmayen.component.AbstractComponentRepository;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;

public class RequestResponseModifierRepository
        extends
        AbstractComponentRepository<RequestResponseModifierKey, RequestResponseModifier, RequestResponseModifierFactory>
        implements Constructable {

    private final Map<RequestResponseModifierKey, Set<Producer<RequestResponseModifier>>> requestResponseModifier
            = new HashMap<>();

    @Inject
    private Optional<Collection<RequestResponseModifier>> components = Optional.of(Collections.emptyList());

    @Inject
    private Optional<Collection<RequestResponseModifierFactory>> componentFactories =
            Optional.of(Collections.emptyList());

    @Override
    public void init() {
        Map<RequestResponseModifierKey, Set<Producer<RequestResponseModifier>>> implementations
                = getProviders(this.components, this.componentFactories);
        this.requestResponseModifier.clear();
        this.requestResponseModifier.putAll(implementations);
    }

    public List<RequestResponseModifier> getRequestResponseModifier(OwsServiceRequest request) {
        RequestResponseModifierKey key
                = new RequestResponseModifierKey(request.getService(), request.getVersion(), request);
        return getRequestResponseModifier(key);
    }

    public List<RequestResponseModifier> getRequestResponseModifier(OwsServiceRequest request,
                                                                    OwsServiceResponse response) {
        RequestResponseModifierKey key
                = new RequestResponseModifierKey(response.getService(), response.getVersion(), request, response);
        return getRequestResponseModifier(key);
    }

    public List<RequestResponseModifier> getRequestResponseModifier(RequestResponseModifierKey key) {
        Set<Producer<RequestResponseModifier>> producers = this.requestResponseModifier.get(key);
        if (producers == null) {
            return null;
        } else {
            return producers.stream().map(Producer::get).collect(toList());
        }
    }

    public boolean hasRequestResponseModifier(OwsServiceRequest request) {
        return hasRequestResponseModifier(new RequestResponseModifierKey(
                request.getService(), request.getVersion(), request));
    }

    public boolean hasRequestResponseModifier(OwsServiceRequest request, OwsServiceResponse response) {
        return hasRequestResponseModifier(new RequestResponseModifierKey(
                request.getService(), request.getVersion(), request, response)) &&
               hasRequestResponseModifier(new RequestResponseModifierKey(
                       response.getService(), response.getVersion(), request,
                       response));
    }

    public boolean hasRequestResponseModifier(RequestResponseModifierKey key) {
        return requestResponseModifier.containsKey(key);
    }

}
