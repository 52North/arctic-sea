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
package org.n52.svalbard.write;

//import static org.n52.svalbard.util.CodingHelper.encodeObjectToXml;

import java.io.OutputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.Sos2StreamingConstants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.GetFeatureOfInterestResponse;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.HelperValues;
import org.n52.svalbard.encode.EncodingValues;
import org.n52.svalbard.encode.StreamingDataEncoder;
import org.n52.svalbard.util.XmlOptionsHelper;

import com.google.common.collect.Sets;

/**
 * Implementatio of {@link XmlStreamWriter} for {@link GetFeatureOfInterestResponse}
 *
 * @author Carsten Hollmann <c.hollmann@52north.org>
 * @since 4.4.0
 *
 */
public class GetFeatureOfInterestXmlStreamWriter extends XmlStreamWriter<GetFeatureOfInterestResponse> implements StreamingDataEncoder {

    private GetFeatureOfInterestResponse response;

    /**
     * constructor
     */
    public GetFeatureOfInterestXmlStreamWriter() {
    }

    /**
     * constructor
     *
     * @param response
     *            {@link GetObservationResponse} to write to stream
     */
    public GetFeatureOfInterestXmlStreamWriter(GetFeatureOfInterestResponse response) {
        setResponse(response);
    }

    @Override
    public void write(OutputStream out) throws XMLStreamException, OwsExceptionReport {
        write(getResponse(), out);
    }

    @Override
    public void write(OutputStream out, EncodingValues encodingValues) throws XMLStreamException, OwsExceptionReport {
        write(getResponse(), out, encodingValues);
    }

    @Override
    public void write(GetFeatureOfInterestResponse response, OutputStream out) throws XMLStreamException, OwsExceptionReport {
        write(response, out, new EncodingValues());
    }

    @Override
    public void write(GetFeatureOfInterestResponse response, OutputStream out, EncodingValues encodingValues)
            throws XMLStreamException, OwsExceptionReport {
        try {
            init(out, encodingValues);
            start(encodingValues.isEmbedded());
            writeGetFeatureOfInterestResponseDoc(response, encodingValues);
            end();
            finish();
        } catch (XMLStreamException xmlse) {
            throw new NoApplicableCodeException().causedBy(xmlse);
        }
    }

    /**
     * Set the {@link GetFeatureOfInterestResponse} to be written to stream
     *
     * @param response
     *            {@link GetFeatureOfInterestResponse} to write to stream
     */
    protected void setResponse(GetFeatureOfInterestResponse response) {
        this.response = response;
    }

    /**
     * Get the {@link GetFeatureOfInterestResponse} to write to stream
     *
     * @return {@link GetFeatureOfInterestResponse} to write
     */
    protected GetFeatureOfInterestResponse getResponse() {
        return response;
    }

    private void writeGetFeatureOfInterestResponseDoc(GetFeatureOfInterestResponse response, EncodingValues encodingValues)
            throws XMLStreamException, OwsExceptionReport {
        start(Sos2StreamingConstants.QN_GET_FEATURE_OF_INTEREST_RESPONSE);
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        namespace(Sos2StreamingConstants.NS_SOS_PREFIX, Sos2StreamingConstants.NS_SOS_20);
        // get observation encoder
        encodingValues.getAdditionalValues().put(HelperValues.DOCUMENT, null);
        // write schemaLocation
        schemaLocation(getSchemaLocation(encodingValues));
        writeNewLine();
        AbstractFeature feature = response.getAbstractFeature();
        if (feature instanceof FeatureCollection) {
            for (AbstractFeature f : (FeatureCollection) feature) {
                writeFeatureMember(f, encodingValues);
                writeNewLine();
            }
        } else if (feature instanceof AbstractSamplingFeature) {
            writeFeatureMember(feature, encodingValues);
            writeNewLine();
        }
        indent--;
        end(Sos2StreamingConstants.QN_GET_FEATURE_OF_INTEREST_RESPONSE);
    }

    private Set<SchemaLocation> getSchemaLocation(EncodingValues encodingValue) {
        Set<SchemaLocation> schemaLocations = Sets.newHashSet();
        if (encodingValue.isSetEncoder()
                && CollectionHelper.isNotEmpty(encodingValue.getEncoder().getSchemaLocations())) {
            schemaLocations.addAll(encodingValue.getEncoder().getSchemaLocations());
        } else {
            schemaLocations.add(Sos2Constants.SOS_GET_FEATURE_OF_INTEREST_SCHEMA_LOCATION);
        }
        return schemaLocations;
    }

    private void writeFeatureMember(AbstractFeature af, EncodingValues encodingValues) throws XMLStreamException, OwsExceptionReport {
        start(Sos2StreamingConstants.QN_FEATURE_MEMBER);
        writeNewLine();

        Map<HelperValues, String> additionalValues =
                new EnumMap<SosConstants.HelperValues, String>(HelperValues.class);
        Profile activeProfile = getActiveProfile();
        if (activeProfile.isSetEncodeFeatureOfInterestNamespace()) {
            additionalValues.put(HelperValues.ENCODE_NAMESPACE,
                    activeProfile.getEncodingNamespaceForFeatureOfInterest());
        }
        rawText(encodeGml(encodingValues.getAdditionalValues(), af)
                .xmlText(XmlOptionsHelper.getInstance().getXmlOptions()));
        indent--;
        writeNewLine();
        end(Sos2StreamingConstants.QN_FEATURE_MEMBER);
        indent++;
    }

    protected XmlObject encodeGml(Map<HelperValues, String> helperValues, Object o) throws OwsExceptionReport {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o, helperValues);
    }

    protected Profile getActiveProfile() {
        return Configurator.getInstance().getProfileHandler().getActiveProfile();
    }
}
