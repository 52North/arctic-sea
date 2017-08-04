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

import static java.util.stream.Collectors.toSet;

import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.DataAvailability;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.ObservationFormatDescriptor;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;


/**
 * GetDataAvailability response stream writer.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GetDataAvailabilityStreamWriter
        extends AbstractGetDataAvailabilityStreamWriter {

    public GetDataAvailabilityStreamWriter(
            EncodingContext context, OutputStream outputStream, List<DataAvailability> element)
            throws XMLStreamException {
        super(context, outputStream, Optional.ofNullable(element).orElseGet(Collections::emptyList));
    }

    @Override
    public void write() throws XMLStreamException, EncodingException {
        start();
        writeGetDataAvailabilityResponse();
        end();
        finish();
    }

    @Override
    protected void writeGetDataAvailabilityResponse() throws XMLStreamException, EncodingException {
        start(GetDataAvailabilityConstants.GDA_GET_DATA_AVAILABILITY_RESPONSE);
        namespace(GetDataAvailabilityConstants.NS_GDA_PREFIX, GetDataAvailabilityConstants.NS_GDA);
        namespace(GmlConstants.NS_GML_PREFIX, GmlConstants.NS_GML_32);
        namespace(SweConstants.NS_SWE_PREFIX, SweConstants.NS_SWE_20);
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        schemaLocation(Sets.newHashSet(GetDataAvailabilityConstants.GET_DATA_AVAILABILITY_SCHEMA_LOCATION));
        for (DataAvailability da : getElement()) {
            wirteDataAvailabilityMember(da);
        }
        end(GetDataAvailabilityConstants.GDA_GET_DATA_AVAILABILITY_RESPONSE);
    }

    @Override
    protected void wirteDataAvailabilityMember(DataAvailability da) throws XMLStreamException, EncodingException {
        start(GetDataAvailabilityConstants.GDA_DATA_AVAILABILITY_MEMBER);
        attr(GmlConstants.QN_ID_32, DATA_AVAILABILITY_PREFIX + dataAvailabilityCount++);
        writeProcedure(da, GetDataAvailabilityConstants.GDA_PROCEDURE);
        writeObservedProperty(da, GetDataAvailabilityConstants.GDA_OBSERVED_PROPERTY);
        writeFeatureOfInterest(da, GetDataAvailabilityConstants.GDA_FEATURE_OF_INTEREST);
        writePhenomenonTime(da, GetDataAvailabilityConstants.GDA_PHENOMENON_TIME);
        if (da.isSetCount()) {
            writeCount(da.getCount(), GetDataAvailabilityConstants.GDA_COUNT);
        }
        if (da.isSetResultTime()) {
            writeResultTimes(da.getResultTimes(), GetDataAvailabilityConstants.GDA_EXTENSION);
        }
        if (da.isSetOffering()) {
            writeOffering(da.getOffering(), GetDataAvailabilityConstants.GDA_EXTENSION);
        }
        if (da.isSetFormatDescriptors()) {
            Set<String> observationTypes = da.getFormatDescriptor().getObservationFormatDescriptors().stream()
                    .map(ObservationFormatDescriptor::getObservationTypes).flatMap(Set::stream).collect(toSet());
            writeObservationTypes(observationTypes);
        }
        if (da.isSetMetadata()) {
            writeMetadata(da.getMetadata(), GetDataAvailabilityConstants.GDA_EXTENSION);
        }
        end(GetDataAvailabilityConstants.GDA_DATA_AVAILABILITY_MEMBER);
    }

    protected void writeOffering(ReferenceType offering, QName element) throws XMLStreamException {
        start(GetDataAvailabilityConstants.GDA_EXTENSION);
        writeSweText("offering", offering.getHref());
        end(GetDataAvailabilityConstants.GDA_EXTENSION);
    }

    protected void writeObservationTypes(Set<String> observationTypes) throws XMLStreamException {
        start(GetDataAvailabilityConstants.GDA_EXTENSION);
        start(SweConstants.QN_DATA_RECORD_SWE_200);
        attr(AN_DEFINITION, "observationTypes");
        int observationTypeCount = 1;
        for (String observationType : observationTypes) {
            start(SweConstants.QN_FIELD_200);
            attr("name", "observationType_" + observationTypeCount++);
            writeSweText("observationType", observationType);
            end(SweConstants.QN_FIELD_200);
        }
        end(SweConstants.QN_DATA_RECORD_SWE_200);
        end(GetDataAvailabilityConstants.GDA_EXTENSION);
    }

    private void writeSweText(String definition, String value) throws XMLStreamException {
        start(SweConstants.QN_TEXT_SWE_200);
        attr(AN_DEFINITION, definition);
        writeSweValue(value);
        end(SweConstants.QN_TEXT_SWE_200);
    }

}
