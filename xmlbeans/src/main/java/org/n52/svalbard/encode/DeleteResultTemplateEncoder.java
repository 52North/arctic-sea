/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.encode;

import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.drt.DeleteResultTemplateConstants;
import org.n52.shetland.ogc.sos.drt.DeleteResultTemplateResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import com.google.common.collect.Sets;

import net.opengis.drt.x10.DeleteResultTemplateResponseDocument;
import net.opengis.drt.x10.DeleteResultTemplateResponseType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 1.0.0
 */
public class DeleteResultTemplateEncoder extends AbstractResponseEncoder<DeleteResultTemplateResponse> {
    public static final SchemaLocation SCHEMA_LOCATION = new SchemaLocation(DeleteResultTemplateConstants.NS,
            DeleteResultTemplateConstants.SCHEMA_LOCATION_URL);

    public DeleteResultTemplateEncoder() {
        super(SosConstants.SOS, Sos2Constants.SERVICEVERSION, DeleteResultTemplateConstants.OPERATION_NAME,
                DeleteResultTemplateConstants.NS, DeleteResultTemplateConstants.NS_PREFIX,
                DeleteResultTemplateResponse.class);
    }

    @Override
    protected XmlObject create(DeleteResultTemplateResponse drtr) throws EncodingException {
        if (drtr == null) {
            throw new UnsupportedEncoderInputException(this, DeleteResultTemplateResponse.class);
        }
        DeleteResultTemplateResponseDocument drtrd =
                DeleteResultTemplateResponseDocument.Factory.newInstance(getXmlOptions());
        DeleteResultTemplateResponseType drtrt = drtrd.addNewDeleteResultTemplateResponse();
        if (drtr.isSetResultTemplates()) {
            for (String resultTemplate : drtr.getResultTemplates()) {
                drtrt.addDeletedTemplate(resultTemplate);
            }
        }
        return drtrd;
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(SCHEMA_LOCATION);
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet();
    }

}
