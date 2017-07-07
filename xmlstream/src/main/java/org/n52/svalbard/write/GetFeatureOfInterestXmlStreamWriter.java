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

import java.io.OutputStream;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.Sos2StreamingConstants;
import org.n52.shetland.ogc.sos.response.GetFeatureOfInterestResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.XmlBeansEncodingFlags;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

/**
 * Implementatio of {@link XmlStreamWriter} for
 * {@link GetFeatureOfInterestResponse}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class GetFeatureOfInterestXmlStreamWriter
        extends XmlStreamWriter<GetFeatureOfInterestResponse> {

    public GetFeatureOfInterestXmlStreamWriter(
            OutputStream outputStream, EncodingContext context, GetFeatureOfInterestResponse element)
            throws XMLStreamException {
        super(context, outputStream, element);
    }

    @Override
    public void write() throws XMLStreamException, EncodingException {
        start();
        writeGetFeatureOfInterestResponseDoc();
        end();
        finish();
    }

    private void writeGetFeatureOfInterestResponseDoc() throws XMLStreamException, EncodingException {
        start(Sos2StreamingConstants.QN_GET_FEATURE_OF_INTEREST_RESPONSE);
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        namespace(Sos2Constants.NS_SOS_PREFIX, Sos2Constants.NS_SOS_20);
        // write schemaLocation
        schemaLocation(getSchemaLocation());
        AbstractFeature feature = getElement().getAbstractFeature();
        if (feature instanceof FeatureCollection) {
            for (AbstractFeature f : (FeatureCollection) feature) {
                writeFeatureMember(f);
            }
        } else if (feature instanceof AbstractSamplingFeature) {
            writeFeatureMember(feature);
        }
        end(Sos2StreamingConstants.QN_GET_FEATURE_OF_INTEREST_RESPONSE);
    }

    private Set<SchemaLocation> getSchemaLocation() {
        Set<SchemaLocation> schemaLocations = Sets.newHashSet();
        schemaLocations.add(Sos2Constants.SOS_GET_FEATURE_OF_INTEREST_SCHEMA_LOCATION);
        return schemaLocations;
    }

    private void writeFeatureMember(AbstractFeature af) throws XMLStreamException, EncodingException {
        Object o =
                getEncoder(GmlConstants.NS_GML_32, af).encode(af, EncodingContext.of(XmlBeansEncodingFlags.DOCUMENT));
        if (o != null && o instanceof XmlObject) {
            start(Sos2StreamingConstants.QN_FEATURE_MEMBER);
            rawText(((XmlObject) o).xmlText(getXmlOptions()));
            end(Sos2StreamingConstants.QN_FEATURE_MEMBER);
        }
    }

}
