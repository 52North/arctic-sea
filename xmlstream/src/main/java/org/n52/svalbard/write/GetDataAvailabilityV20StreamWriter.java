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
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.DataAvailability;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.FormatDescriptor;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.ObservationFormatDescriptor;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.ProcedureDescriptionFormatDescriptor;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

/**
 * GetDataAvailability response stream writer.
 *
 * @author Carsten Hollmann
 *
 * @since 1.0.0
 */
public class GetDataAvailabilityV20StreamWriter extends AbstractGetDataAvailabilityStreamWriter {

    public GetDataAvailabilityV20StreamWriter(
            EncodingContext context,
            OutputStream outputStream,
            List<DataAvailability> element) throws XMLStreamException {
        super(context, outputStream, element);
    }

    @Override
    protected void writeGetDataAvailabilityResponse() throws XMLStreamException, EncodingException {
        start(GetDataAvailabilityConstants.GDA_GET_DATA_AVAILABILITY_20_RESPONSE);
        namespace(GetDataAvailabilityConstants.NS_GDA_PREFIX, GetDataAvailabilityConstants.NS_GDA_20);
        namespace(GmlConstants.NS_GML_PREFIX, GmlConstants.NS_GML_32);
        namespace(SweConstants.NS_SWE_PREFIX, SweConstants.NS_SWE_20);
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        schemaLocation(Sets.newHashSet(GetDataAvailabilityConstants.GET_DATA_AVAILABILITY_20_SCHEMA_LOCATION));
        for (DataAvailability da : getElement()) {
            wirteDataAvailabilityMember(da);
        }
        end(GetDataAvailabilityConstants.GDA_GET_DATA_AVAILABILITY_20_RESPONSE);
    }

    protected void writeOffering(ReferenceType offering) throws XMLStreamException {
        empty(GetDataAvailabilityConstants.GDA_20_OFFERING);
        attr(GetDataAvailabilityConstants.XLINK_HREF, offering.getHref());
        if (offering.isSetTitle()) {
            attr(GetDataAvailabilityConstants.XLINK_TITLE, offering.getTitle());
        } else {
            attr(GetDataAvailabilityConstants.XLINK_TITLE, offering.getTitleOrFromHref());
        }
    }

    @Override
    protected void wirteDataAvailabilityMember(DataAvailability da) throws XMLStreamException, EncodingException {
        start(GetDataAvailabilityConstants.GDA_DATA_AVAILABILITY_20_MEMBER);
        attr(GmlConstants.QN_ID_32, DATA_AVAILABILITY_PREFIX + dataAvailabilityCount++);
        writeProcedure(da, GetDataAvailabilityConstants.GDA_20_PROCEDURE);
        writeObservedProperty(da, GetDataAvailabilityConstants.GDA_20_OBSERVED_PROPERTY);
        writeFeatureOfInterest(da, GetDataAvailabilityConstants.GDA_20_FEATURE_OF_INTEREST);
        writePhenomenonTime(da, GetDataAvailabilityConstants.GDA_20PHENOMENON_TIME);
        if (da.isSetCount()) {
            writeCount(da.getCount(), GetDataAvailabilityConstants.GDA_20_COUNT);
        }
        if (da.isSetResultTime()) {
            writeResultTimes(da.getResultTimes(), GetDataAvailabilityConstants.GDA_20_EXTENSION);
        }
        if (da.isSetOffering()) {
            writeOffering(da.getOffering());
        }
        if (da.isSetFormatDescriptors()) {
            writeFormatDescriptor(da.getFormatDescriptor());
        }
        if (da.isSetMetadata()) {
            writeMetadata(da.getMetadata(), GetDataAvailabilityConstants.GDA_20_EXTENSION);
        }
        end(GetDataAvailabilityConstants.GDA_DATA_AVAILABILITY_20_MEMBER);
    }

    protected void writeFormatDescriptor(FormatDescriptor formatDescriptor) throws XMLStreamException {
        start(GetDataAvailabilityConstants.GDA_20_FORMAT_DESCRIPTOR);
        writeProcedureDescriptionFormatDescriptor(formatDescriptor.getProcedureDescriptionFormatDescriptor());
        for (ObservationFormatDescriptor ofd : formatDescriptor.getObservationFormatDescriptors()) {
            writeObservationFormatDescriptor(ofd);
        }
        end(GetDataAvailabilityConstants.GDA_20_FORMAT_DESCRIPTOR);
    }

    protected void writeProcedureDescriptionFormatDescriptor(ProcedureDescriptionFormatDescriptor formatDescriptor)
            throws XMLStreamException {
        start(GetDataAvailabilityConstants.GDA_20_PROCEDURE_FORMAT_DESCRIPTOR);
        writeProcedureDescriptionFormat(formatDescriptor.getProcedureDescriptionFormat());
        end(GetDataAvailabilityConstants.GDA_20_PROCEDURE_FORMAT_DESCRIPTOR);
    }

    protected void writeObservationFormatDescriptor(ObservationFormatDescriptor formatDescriptor)
            throws XMLStreamException {
        start(GetDataAvailabilityConstants.GDA_20_OBSERVATION_FORMAT_DESCRIPTOR);
        writeResponseFormat(formatDescriptor.getResponseFormat());
        for (String observationType : formatDescriptor.getObservationTypes()) {
            writeObservationType(observationType);
        }
        end(GetDataAvailabilityConstants.GDA_20_OBSERVATION_FORMAT_DESCRIPTOR);
    }

    private void writeProcedureDescriptionFormat(String procedureDescriptionFormat) throws XMLStreamException {
        start(GetDataAvailabilityConstants.GDA_20_PROCEDURE_DESCRIPTION_FORMAT);
        chars(procedureDescriptionFormat);
        end(GetDataAvailabilityConstants.GDA_20_PROCEDURE_DESCRIPTION_FORMAT);
    }

    private void writeObservationType(String observationType) throws XMLStreamException {
        start(GetDataAvailabilityConstants.GDA_20_OBSERVATION_TYPE);
        chars(observationType);
        end(GetDataAvailabilityConstants.GDA_20_OBSERVATION_TYPE);
    }

    private void writeResponseFormat(String responseFormat) throws XMLStreamException {
        start(GetDataAvailabilityConstants.GDA_20_RESPONSE_FORMAT);
        chars(responseFormat);
        end(GetDataAvailabilityConstants.GDA_20_RESPONSE_FORMAT);
    }

}
