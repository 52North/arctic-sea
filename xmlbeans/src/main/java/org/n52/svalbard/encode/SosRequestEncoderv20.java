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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.GetResultRequest;
import org.n52.shetland.ogc.sos.request.GetResultTemplateRequest;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import net.opengis.sos.x20.GetResultDocument;
import net.opengis.sos.x20.GetResultTemplateDocument;
import net.opengis.sos.x20.GetResultTemplateType;
import net.opengis.sos.x20.GetResultType;
import net.opengis.sos.x20.GetResultType.SpatialFilter;

/**
 * @since 1.0.0
 *
 */
public class SosRequestEncoderv20 extends AbstractXmlEncoder<XmlObject, OwsServiceRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SosRequestEncoderv20.class);

    private static final Set<EncoderKey> ENCODER_KEYS = CodingHelper.encoderKeysForElements(Sos2Constants.NS_SOS_20,
            OwsServiceRequest.class, GetResultTemplateRequest.class, GetResultRequest.class);

    public SosRequestEncoderv20() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public void addNamespacePrefixToMap(final Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(Sos2Constants.NS_SOS_20, SosConstants.NS_SOS_PREFIX);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(Sos2Constants.SOS_SCHEMA_LOCATION);
    }

    @Override
    public XmlObject encode(OwsServiceRequest communicationObject) throws EncodingException {
        return encode(communicationObject, EncodingContext.empty());
    }

    @Override
    public XmlObject encode(OwsServiceRequest request, EncodingContext additionalValues) throws EncodingException {
        XmlObject encodedObject = encodeRequests(request);
        XmlHelper.validateDocument(encodedObject, EncodingException::new);
        return encodedObject;
    }

    private XmlObject encodeRequests(OwsServiceRequest request) throws EncodingException {
        if (request instanceof GetResultTemplateRequest) {
            return createGetResultTemplateRequest((GetResultTemplateRequest) request);
        } else if (request instanceof GetResultRequest) {
            return createGetResultRequest((GetResultRequest) request);
        }
        throw new UnsupportedEncoderInputException(this, request);
    }

    private XmlObject createGetResultTemplateRequest(final GetResultTemplateRequest request) {
        final GetResultTemplateDocument getResultTemplateDoc =
                GetResultTemplateDocument.Factory.newInstance(getXmlOptions());
        final GetResultTemplateType getResultTemplate = getResultTemplateDoc.addNewGetResultTemplate();
        getResultTemplate.setService(request.getService());
        getResultTemplate.setVersion(request.getVersion());
        getResultTemplate.setOffering(request.getOffering());
        getResultTemplate.setObservedProperty(request.getObservedProperty());
        return getResultTemplateDoc;
    }

    private XmlObject createGetResultRequest(final GetResultRequest request) throws EncodingException {
        final GetResultDocument getResultDoc = GetResultDocument.Factory.newInstance(getXmlOptions());
        final GetResultType getResult = getResultDoc.addNewGetResult();
        getResult.setService(request.getService());
        getResult.setVersion(request.getVersion());
        getResult.setOffering(request.getOffering());
        getResult.setObservedProperty(request.getObservedProperty());
        if (request.isSetFeatureOfInterest()) {
            request.getFeatureIdentifiers().forEach(getResult::addFeatureOfInterest);
        }
        if (request.hasTemporalFilter()) {
            for (final TemporalFilter temporalFilter : request.getTemporalFilter()) {
                createTemporalFilter(getResult.addNewTemporalFilter(), temporalFilter);
            }
        }
        if (request.isSetSpatialFilter()) {
            createSpatialFilter(getResult.addNewSpatialFilter(), request.getSpatialFilter());
        }

        return getResultDoc;
    }

    private void createTemporalFilter(final net.opengis.sos.x20.GetResultType.TemporalFilter temporalFilter,
            final TemporalFilter sosTemporalFilter) throws EncodingException {
        final Encoder<XmlObject, TemporalFilter> encoder = getEncoder(FilterConstants.NS_FES_2, sosTemporalFilter);
        final XmlObject encodedObject = encoder.encode(sosTemporalFilter);
        temporalFilter.set(encodedObject);
    }

    private void createSpatialFilter(final SpatialFilter spatialFilter,
            final org.n52.shetland.ogc.filter.SpatialFilter sosSpatialFilter) throws EncodingException {
        final Encoder<XmlObject, org.n52.shetland.ogc.filter.SpatialFilter> encoder =
                getEncoder(FilterConstants.NS_FES_2, sosSpatialFilter);
        final XmlObject encodedObject = encoder.encode(sosSpatialFilter);
        spatialFilter.set(encodedObject);
    }

}
