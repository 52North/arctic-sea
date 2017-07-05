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

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.DataAvailability;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.FormatDescriptor;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.ObservationFormatDescriptor;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.ProcedureDescriptionFormatDescriptor;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.w3c.W3CConstants;

/**
 * GetDataAvailability response stream writer.
 *
 * @author Carsten Hollmann
 *
 * @since 4.4.0
 */
public class GetDataAvailabilityStreamWriter extends AbstractGetDataAvailabilityStreamWriter {

    public GetDataAvailabilityStreamWriter(String version, List<DataAvailability> gdas) {
        super(version, gdas);
    }

    @Override
    protected void writeGetDataAvailabilityResponse() throws XMLStreamException, OwsExceptionReport {
        start(GetDataAvailabilityConstants.GDA_GET_DATA_AVAILABILITY_20_RESPONSE);
        namespace(GetDataAvailabilityConstants.NS_GDA_PREFIX, GetDataAvailabilityConstants.NS_GDA_20);
        namespace(GmlConstants.NS_GML_PREFIX, GmlConstants.NS_GML_32);
        namespace(SweConstants.NS_SWE_PREFIX, SweConstants.NS_SWE_20);
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        for (DataAvailability da : getGDAs()) {
            wirteDataAvailabilityMember(da);
        }
        end(GetDataAvailabilityConstants.GDA_GET_DATA_AVAILABILITY_20_RESPONSE);
    }

    protected void writeOffering(DataAvailability da, QName element) throws XMLStreamException {
        start(element);
        attr(GetDataAvailabilityConstants.XLINK_HREF, da.getOffering().getHref());
        if (da.getOffering().isSetTitle()) {
            attr(GetDataAvailabilityConstants.XLINK_TITLE, da.getOffering().getTitle());
        } else {
            attr(GetDataAvailabilityConstants.XLINK_TITLE, da.getOffering().getTitleFromHref());
        }
        end(element);
    }

    protected void wirteDataAvailabilityMember(DataAvailability da) throws XMLStreamException, OwsExceptionReport {
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
            writeOffering(da, GetDataAvailabilityConstants.GDA_20_OFFERING);
        }
        if (da.isSetFormatDescriptors()) {
           writeFormatDescriptor(da.getFormatDescriptor(), GetDataAvailabilityConstants.GDA_20_FORMAT_DESCRIPTOR);
        }
        if (da.isSetMetadata()) {
            writeMetadata(da.getMetadata(), GetDataAvailabilityConstants.GDA_20_EXTENSION);
        }
        end(GetDataAvailabilityConstants.GDA_DATA_AVAILABILITY_20_MEMBER);
    }

    protected void writeFormatDescriptor(FormatDescriptor formatDescriptor, QName element) throws XMLStreamException {
        start(element);
        writeProcedureDescriptionFormatDescriptor(formatDescriptor.getProcedureDescriptionFormatDescriptor(), GetDataAvailabilityConstants.GDA_20_PROCEDURE_FORMAT_DESCRIPTOR);
        for (ObservationFormatDescriptor observationFormatDescriptor : formatDescriptor.getObservationFormatDescriptors()) {
            writeObservationFormatDescriptor(observationFormatDescriptor, GetDataAvailabilityConstants.GDA_20_OBSERVATION_FORMAT_DESCRIPTOR);
        }
        end(element);
    }

    protected void writeProcedureDescriptionFormatDescriptor(ProcedureDescriptionFormatDescriptor formatDescriptor, QName element) throws XMLStreamException {
        start(element);
        writeElementWithStringValue(formatDescriptor.getProcedureDescriptionFormat(), GetDataAvailabilityConstants.GDA_20_PROCEDURE_DESCRIPTION_FORMAT);
        end(element);
    }

    protected void writeObservationFormatDescriptor(ObservationFormatDescriptor formatDescriptor, QName element) throws XMLStreamException {
        start(element);
        writeElementWithStringValue(formatDescriptor.getResponseFormat(), GetDataAvailabilityConstants.GDA_20_RESPONSE_FORMAT);
        for (String observationType : formatDescriptor.getObservationTypes()) {
            writeElementWithStringValue(observationType, GetDataAvailabilityConstants.GDA_20_OBSERVATION_TYPE);
        }
        end(element);
    }

    protected void writeElementWithStringValue(String value, QName element) throws XMLStreamException {
        start(element);
        chars(value);
        end(element);
    }



}
