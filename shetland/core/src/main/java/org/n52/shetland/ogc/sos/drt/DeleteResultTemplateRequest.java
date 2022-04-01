/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos.drt;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class DeleteResultTemplateRequest extends OwsServiceRequest {

    private static String OPERATION_NAME = "DeleteResultTemplate";
    private List<String> resultTemplates;
    private List<AbstractMap.SimpleEntry<String, String>> observedPropertyOfferingPairs;

    public DeleteResultTemplateRequest() {
        super(null, null, OPERATION_NAME);
    }

    public DeleteResultTemplateRequest(String service, String version) {
        super(service, version, OPERATION_NAME);
    }

    public DeleteResultTemplateRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    @Override
    public String getOperationName() {
        return OPERATION_NAME;
    }

    public DeleteResultTemplateRequest addResultTemplate(String resultTemplateId) {
        if (!isSetResultTemplates()) {
            resultTemplates = Lists.newArrayList();
        }
        if (!Strings.isNullOrEmpty(resultTemplateId)) {
            resultTemplates.add(resultTemplateId);
        }
        return this;
    }

    public boolean isSetResultTemplates() {
        return CollectionHelper.isNotEmpty(resultTemplates);
    }

    public List<String> getResultTemplates() {
        if (isSetResultTemplates()) {
            return resultTemplates;
        } else {
            return Collections.emptyList();
        }
    }

    public DeleteResultTemplateRequest setObservableProperty(String observedProperty) {
        return addObservedPropertyOfferingPair(observedProperty, "");
    }

    public DeleteResultTemplateRequest setOffering(String offering) {
        return addObservedPropertyOfferingPair("", offering);
    }

    public DeleteResultTemplateRequest addObservedPropertyOfferingPair(String observedProperty, String offering) {
        if (!isSetObservedPropertyOfferingPairs()) {
            observedPropertyOfferingPairs = Lists.newArrayList();
        }
        observedPropertyOfferingPairs.add(new AbstractMap.SimpleEntry<>(observedProperty, offering));
        return this;
    }

    public boolean isSetObservedPropertyOfferingPairs() {
        return observedPropertyOfferingPairs != null && !observedPropertyOfferingPairs.isEmpty();
    }

    public List<AbstractMap.SimpleEntry<String, String>> getObservedPropertyOfferingPairs() {
        if (isSetObservedPropertyOfferingPairs()) {
            return observedPropertyOfferingPairs;
        } else {
            return Collections.emptyList();
        }
    }

}
