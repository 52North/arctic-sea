/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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

import java.util.List;

import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.response.AbstractServiceResponse;
import org.n52.iceland.util.Producer;
import org.n52.iceland.component.AbstractComponentRepository;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.SetMultimap;

@SuppressWarnings("rawtypes")
public class RequestResponseModifierRepository extends
		      AbstractComponentRepository<RequestResponseModifierKeyType, RequestResponseModifier, RequestResponseModifierFactory> {

    @Deprecated
	private static RequestResponseModifierRepository instance;

	private final ListMultimap<RequestResponseModifierKeyType, Producer<RequestResponseModifier>> requestResponseModifier
            = LinkedListMultimap.create();

    public RequestResponseModifierRepository() {
        super(RequestResponseModifier.class, RequestResponseModifierFactory.class);
        RequestResponseModifierRepository.instance = this;
    }

    @Override
    protected void processImplementations(SetMultimap<RequestResponseModifierKeyType, Producer<RequestResponseModifier>> implementations) {
        this.requestResponseModifier.clear();
        for (RequestResponseModifierKeyType key : implementations.keySet()) {
            requestResponseModifier.putAll(key, implementations.get(key));
        }
    }

    public List<RequestResponseModifier> getRequestResponseModifier(AbstractServiceRequest request) {
        RequestResponseModifierKeyType key = new RequestResponseModifierKeyType(request.getService(), request.getVersion(), request);
        return getRequestResponseModifier(key);
    }

    public List<RequestResponseModifier> getRequestResponseModifier(AbstractServiceRequest request, AbstractServiceResponse response) {
        RequestResponseModifierKeyType key = new RequestResponseModifierKeyType(response.getService(), response.getVersion(), request, response);
        return getRequestResponseModifier(key);
	}

    public List<RequestResponseModifier> getRequestResponseModifier(RequestResponseModifierKeyType key) {
        List<Producer<RequestResponseModifier>> producers
                = this.requestResponseModifier.get(key);
        if (producers == null) {
            return null;
        } else {
            return produce(producers);
        }
	}

    public  boolean hasRequestResponseModifier(AbstractServiceRequest request) {
        return hasRequestResponseModifier(new RequestResponseModifierKeyType(
                request.getService(), request.getVersion(), request));
    }

    public boolean hasRequestResponseModifier(AbstractServiceRequest request, AbstractServiceResponse response) {
        return hasRequestResponseModifier(new RequestResponseModifierKeyType(
                request.getService(), request.getVersion(), request, response))
               && hasRequestResponseModifier(new RequestResponseModifierKeyType(
                       response.getService(), response.getVersion(), request,
						response));
	}

	public boolean hasRequestResponseModifier(RequestResponseModifierKeyType key) {
		return requestResponseModifier.containsKey(key);
	}

    @Deprecated
	public static RequestResponseModifierRepository getInstance() {
		return RequestResponseModifierRepository.instance;
	}

}
