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
package org.n52.svalbard.decode;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.opengis.gml.x32.AbstractTimeObjectType;
import net.opengis.sosgda.x10.DataAvailabilityMemberType;
import net.opengis.sosgda.x10.GetDataAvailabilityResponseDocument;
import net.opengis.sosgda.x10.GetDataAvailabilityResponseType;
import net.opengis.sosgda.x20.FormatDescriptorType;
import net.opengis.sosgda.x20.ObservationFormatDescriptorType;

import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.DataAvailability;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.FormatDescriptor;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.ObservationFormatDescriptor;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.ProcedureDescriptionFormatDescriptor;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * XML {@link Decoder} for {@link GetDataAvailabilityResponse}
 *
 * @author <a href="mailto:j.schulte@52north.org">Jan Schulte</a>
 * @since 5.0.0
 *
 */
public class GetDataAvailabilityResponseDecoder
        extends AbstractXmlDecoder<XmlObject, GetDataAvailabilityResponse>
        implements SosResponseDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetDataAvailabilityResponseDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
            CodingHelper.decoderKeysForElements(GetDataAvailabilityConstants.NS_GDA,
                                                GetDataAvailabilityResponseDocument.class),
            CodingHelper.decoderKeysForElements(GetDataAvailabilityConstants.NS_GDA_20,
                                                net.opengis.sosgda.x20.GetDataAvailabilityResponseDocument.class));

    public GetDataAvailabilityResponseDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                     Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public GetDataAvailabilityResponse decode(XmlObject document) throws DecodingException {
        if (document instanceof GetDataAvailabilityResponseDocument) {
            return decodeV10((GetDataAvailabilityResponseDocument) document);
        } else if (document instanceof net.opengis.sosgda.x20.GetDataAvailabilityResponseDocument) {
            return decodeV20((net.opengis.sosgda.x20.GetDataAvailabilityResponseDocument) document);
        }
        throw new UnsupportedDecoderInputException(this, document);
    }

    private GetDataAvailabilityResponse decodeV10(GetDataAvailabilityResponseDocument document)
            throws DecodingException {
        GetDataAvailabilityResponse response = new GetDataAvailabilityResponse();
        setService(response);
        setVersions(response);
        response.setNamespace(GetDataAvailabilityConstants.NS_GDA);
        response.setDataAvailabilities(parseDataAvalabilities(document.getGetDataAvailabilityResponse()));
        return response;
    }

    private GetDataAvailabilityResponse decodeV20(net.opengis.sosgda.x20.GetDataAvailabilityResponseDocument document)
            throws DecodingException {
        GetDataAvailabilityResponse response = new GetDataAvailabilityResponse();
        setService(response);
        setVersions(response);
        response.setNamespace(GetDataAvailabilityConstants.NS_GDA_20);
        response.setDataAvailabilities(parseDataAvalabilities(document.getGetDataAvailabilityResponse()));
        return response;
    }

    private Collection<DataAvailability> parseDataAvalabilities(
            GetDataAvailabilityResponseType response) throws DecodingException {
        List<DataAvailability> availabilities = Lists.newArrayList();
        if (CollectionHelper.isNotNullOrEmpty(response.getDataAvailabilityMemberArray())) {
            Map<String, TimePeriod> periods = Maps.newHashMap();
            for (DataAvailabilityMemberType damt : response.getDataAvailabilityMemberArray()) {

                ReferenceType procedure = decodeXmlElement(damt.getProcedure());
                ReferenceType featureOfInterest = decodeXmlElement(damt.getFeatureOfInterest());
                ReferenceType observedProperty = decodeXmlElement(damt.getObservedProperty());
                TimePeriod phenomenonTime = getPhenomenonTime(damt.getPhenomenonTime().getAbstractTimeObject(),
                                                              damt.getPhenomenonTime().getHref(), periods);

                availabilities.add(new DataAvailability(procedure, observedProperty, featureOfInterest, null,
                                                        phenomenonTime));
            }
        }
        return availabilities;
    }

    private Collection<DataAvailability> parseDataAvalabilities(
            net.opengis.sosgda.x20.GetDataAvailabilityResponseType response) throws DecodingException {
        List<DataAvailability> availabilities = Lists.newArrayList();
        if (CollectionHelper.isNotNullOrEmpty(response.getDataAvailabilityMemberArray())) {
            Map<String, TimePeriod> periods = Maps.newHashMap();
            for (net.opengis.sosgda.x20.DataAvailabilityMemberType damt : response.getDataAvailabilityMemberArray()) {

                ReferenceType procedure = decodeXmlElement(damt.getProcedure());
                ReferenceType offering = decodeXmlElement(damt.getOffering());
                ReferenceType featureOfInterest = decodeXmlElement(damt.getFeatureOfInterest());
                ReferenceType observedProperty = decodeXmlElement(damt.getObservedProperty());
                TimePeriod phenomenonTime = getPhenomenonTime(damt.getPhenomenonTime().getAbstractTimeObject(),
                                                              damt.getPhenomenonTime().getHref(), periods);

                DataAvailability dataAvailability = new DataAvailability(procedure, observedProperty, featureOfInterest,
                                                                         offering, phenomenonTime);
                FormatDescriptor formatDescriptor = createFormatDescriptor(damt.getFormatDescriptor());
                if (formatDescriptor != null) {
                    dataAvailability.setFormatDescriptor(formatDescriptor);
                }
                availabilities.add(dataAvailability);
            }
        }
        return availabilities;
    }

    private TimePeriod getPhenomenonTime(AbstractTimeObjectType atot, String href, Map<String, TimePeriod> periods)
            throws DecodingException {
        TimePeriod phenomenonTime;
        if (atot != null) {
            phenomenonTime = decodeXmlElement(atot);
            periods.put(phenomenonTime.getGmlId(), phenomenonTime);
        } else {
            String id = href.startsWith("#") ? href.substring(1) : href;
            phenomenonTime = periods.get(id);
        }
        return phenomenonTime;
    }

    private FormatDescriptor createFormatDescriptor(FormatDescriptorType fdt) {
        if (fdt != null) {
            String procDescFormatDescriptor = fdt.getProcedureDescriptionFormatDescriptor()
                    .getProcedureDescriptionFormat();
            Set<ObservationFormatDescriptor> obsFormDescs = Sets.newHashSet();
            for (ObservationFormatDescriptorType obsFormatDescriptor : fdt.getObservationFormatDescriptorArray()) {
                obsFormDescs.add(new ObservationFormatDescriptor(
                        obsFormatDescriptor.getResponseFormat(),
                        Sets.newHashSet(obsFormatDescriptor.getObservationTypeArray())
                ));
            }
            return new FormatDescriptor(new ProcedureDescriptionFormatDescriptor(procDescFormatDescriptor),
                                        obsFormDescs);
        }
        return null;
    }
}
