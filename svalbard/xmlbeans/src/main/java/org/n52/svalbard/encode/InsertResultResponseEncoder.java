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
package org.n52.svalbard.encode;

import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.response.InsertResultResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import net.opengis.sos.x20.InsertResultResponseDocument;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class InsertResultResponseEncoder extends AbstractSosResponseEncoder<InsertResultResponse> {
    public InsertResultResponseEncoder() {
        super(Sos2Constants.Operations.InsertResult.name(), InsertResultResponse.class);
    }

    @Override
    protected XmlObject create(InsertResultResponse response) throws EncodingException {
        final InsertResultResponseDocument doc = InsertResultResponseDocument.Factory.newInstance(getXmlOptions());
        doc.addNewInsertResultResponse();
        if (response.hasExtensions()) {
            createExtension(doc.getInsertResultResponse(), response.getExtensions());
        }
        return doc;
    }

    @Override
    public Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(Sos2Constants.SOS_INSERT_RESULT_SCHEMA_LOCATION);
    }
}
