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
package org.n52.iceland.convert;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.n52.iceland.component.AbstractComponentRepository;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.response.AbstractServiceResponse;
import org.n52.iceland.util.Producer;
import org.n52.iceland.util.Producers;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.SetMultimap;

@SuppressWarnings("rawtypes")
public class RequestResponseModifierRepository extends
              AbstractComponentRepository<RequestResponseModifierKey, RequestResponseModifier, RequestResponseModifierFactory> implements Constructable {

    @Deprecated
    private static RequestResponseModifierRepository instance;

    private final ListMultimap<RequestResponseModifierKey, Producer<RequestResponseModifier>> requestResponseModifier
            = LinkedListMultimap.create();

    @Autowired(required = false)
    private Collection<RequestResponseModifier> components;

    @Autowired(required = false)
    private Collection<RequestResponseModifierFactory> componentFactories;

    @Override
    public void init() {
        RequestResponseModifierRepository.instance = this;
        SetMultimap<RequestResponseModifierKey, Producer<RequestResponseModifier>> implementations
                = getProviders(this.components, this.componentFactories);
        this.requestResponseModifier.clear();
        for (RequestResponseModifierKey key : implementations.keySet()) {
            requestResponseModifier.putAll(key, implementations.get(key));
        }
    }

    public List<RequestResponseModifier> getRequestResponseModifier(AbstractServiceRequest request) {
        RequestResponseModifierKey key = new RequestResponseModifierKey(request.getService(), request.getVersion(), request);
        return getRequestResponseModifier(key);
    }

    public List<RequestResponseModifier> getRequestResponseModifier(AbstractServiceRequest request, AbstractServiceResponse response) {
        RequestResponseModifierKey key = new RequestResponseModifierKey(response.getService(), response.getVersion(), request, response);
        return getRequestResponseModifier(key);
    }

    public List<RequestResponseModifier> getRequestResponseModifier(RequestResponseModifierKey key) {
        List<Producer<RequestResponseModifier>> producers
                = this.requestResponseModifier.get(key);
        if (producers == null) {
            return null;
        } else {
            return Producers.produce(producers);
        }
    }

    public  boolean hasRequestResponseModifier(AbstractServiceRequest request) {
        return hasRequestResponseModifier(new RequestResponseModifierKey(
                request.getService(), request.getVersion(), request));
    }

    public boolean hasRequestResponseModifier(AbstractServiceRequest request, AbstractServiceResponse response) {
        return hasRequestResponseModifier(new RequestResponseModifierKey(
                request.getService(), request.getVersion(), request, response))
               && hasRequestResponseModifier(new RequestResponseModifierKey(
                       response.getService(), response.getVersion(), request,
                        response));
    }

    public boolean hasRequestResponseModifier(RequestResponseModifierKey key) {
        return requestResponseModifier.containsKey(key);
    }

    @Deprecated
    public static RequestResponseModifierRepository getInstance() {
        return RequestResponseModifierRepository.instance;
    }

}
