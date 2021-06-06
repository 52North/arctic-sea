/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.coding.encode;

import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class OwsEncodingException extends EncodingException {
    private static final long serialVersionUID = 1924135887853730006L;

    public OwsEncodingException(OwsExceptionReport cause) {
        super(cause.getMessage(), cause);
    }

    @Override
    public synchronized OwsExceptionReport getCause() {
        return (OwsExceptionReport) super.getCause();
    }
}
