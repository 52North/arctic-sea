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
package org.n52.shetland.ogc.sos.drt;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.util.CollectionHelper;

public class DeleteResultTemplateResponse extends OwsServiceResponse {

    private static String OPERATION_NAME = "DeleteResultTemplate";
    private List<String> resultTemplates = new LinkedList<>();

    public DeleteResultTemplateResponse() {
        super(null, null, OPERATION_NAME);
    }

    public DeleteResultTemplateResponse(String service, String version) {
        super(service, version, OPERATION_NAME);
    }

    public DeleteResultTemplateResponse(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    @Override
    public String getOperationName() {
        return OPERATION_NAME;
    }

    public List<String> getResultTemplates() {
        return Collections.unmodifiableList(resultTemplates);
    }

    public boolean isSetResultTemplates() {
        return CollectionHelper.isNotEmpty(resultTemplates);
    }

    public DeleteResultTemplateResponse addDeletedResultTemplates(Collection<String> deletedResultTemplates) {
        this.resultTemplates.clear();
        if (deletedResultTemplates != null) {
            this.resultTemplates.addAll(deletedResultTemplates);
        }
        return this;
    }

}
