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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.joda.time.DateTime;

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.Time.TimeFormat;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.DataAvailability;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.EncodingValues;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * GetDataAvailability response stream writer.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 4.0.0
 */
public class GetDataAvailabilityStreamWriter extends XmlStreamWriter<List<DataAvailability>> {
    private static final String TIME_PERIOD_PREFIX = "tp_";
    private static final String DATA_AVAILABILITY_PREFIX = "dam_";
    private static final String RESULT_TIME = "resultTime";
    private List<DataAvailability> gdas;
    private final Map<TimePeriod, String> times;
    private final String version;
    private int dataAvailabilityCount = 1;
    private int timePeriodCount = 1;
    private int resultTimeCount = 1;

    public GetDataAvailabilityStreamWriter(String version, List<DataAvailability> gdas) {
        this.gdas = gdas == null ? Collections.<DataAvailability> emptyList() : gdas;
        this.times = new HashMap<>(this.gdas.size());
        this.version = version == null ? Sos2Constants.SERVICEVERSION : version;
    }

    @Override
    public void write(OutputStream out) throws XMLStreamException, EncodingException {
        init(out);
        start(true);
        writeGetDataAvailabilityResponse();
        end();
        finish();
    }

    @Override
    public void write(OutputStream out, EncodingValues encodingValues) throws XMLStreamException, EncodingException {
        write(out);
    }

    @Override
    public void write(List<DataAvailability> elementToStream, OutputStream out)
            throws XMLStreamException, EncodingException {
        this.gdas = elementToStream;
        write(out);
    }

    @Override
    public void write(List<DataAvailability> elementToStream, OutputStream out, EncodingValues encodingValues)
            throws XMLStreamException, EncodingException {
        this.gdas = elementToStream;
        write(out);
    }

    protected void writeGetDataAvailabilityResponse() throws XMLStreamException, EncodingException {
        start(GetDataAvailabilityConstants.GDA_GET_DATA_AVAILABILITY_RESPONSE);
        namespace(GetDataAvailabilityConstants.NS_GDA_PREFIX, GetDataAvailabilityConstants.NS_GDA);
        namespace(GmlConstants.NS_GML_PREFIX, GmlConstants.NS_GML_32);
        namespace(SweConstants.NS_SWE_PREFIX, SweConstants.NS_SWE_20);
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        for (DataAvailability da : this.gdas) {
            wirteDataAvailabilityMember(da);
        }
        end(GetDataAvailabilityConstants.GDA_GET_DATA_AVAILABILITY_RESPONSE);
    }

    protected void wirteDataAvailabilityMember(DataAvailability da) throws XMLStreamException, EncodingException {
        start(GetDataAvailabilityConstants.GDA_DATA_AVAILABILITY_MEMBER);
        attr(GmlConstants.QN_ID_32, DATA_AVAILABILITY_PREFIX + dataAvailabilityCount++);
        writeProcedure(da);
        writeObservedProperty(da);
        writeFeatureOfInterest(da);
        writePhenomenonTime(da);
        if (da.isSetCount()) {
            writeCount(da.getCount());
        }
        if (da.isSetResultTime()) {
            writeResultTimes(da.getResultTimes());
        }
        end(GetDataAvailabilityConstants.GDA_DATA_AVAILABILITY_MEMBER);
    }

    protected void writePhenomenonTime(DataAvailability da) throws EncodingException, XMLStreamException {
        if (times.containsKey(da.getPhenomenonTime())) {
            empty(GetDataAvailabilityConstants.GDA_PHENOMENON_TIME);
            attr(GetDataAvailabilityConstants.XLINK_HREF, "#" + times.get(da.getPhenomenonTime()));
        } else {
            start(GetDataAvailabilityConstants.GDA_PHENOMENON_TIME);
            da.getPhenomenonTime().setGmlId(TIME_PERIOD_PREFIX + timePeriodCount++);
            times.put(da.getPhenomenonTime(), da.getPhenomenonTime().getGmlId());
            writeTimePeriod(da.getPhenomenonTime());
        }
        end(GetDataAvailabilityConstants.GDA_PHENOMENON_TIME);
    }

    protected void writeFeatureOfInterest(DataAvailability da) throws XMLStreamException {
        empty(GetDataAvailabilityConstants.GDA_FEATURE_OF_INTEREST);
        attr(GetDataAvailabilityConstants.XLINK_HREF, da.getFeatureOfInterest().getHref());
        if (da.getFeatureOfInterest().isSetTitle()) {
            attr(GetDataAvailabilityConstants.XLINK_TITLE, da.getFeatureOfInterest().getTitle());
        } else {
            attr(GetDataAvailabilityConstants.XLINK_TITLE, da.getFeatureOfInterest().getTitleOrFromHref());
        }
        end(GetDataAvailabilityConstants.GDA_FEATURE_OF_INTEREST);
    }

    protected void writeProcedure(DataAvailability da) throws XMLStreamException {
        empty(GetDataAvailabilityConstants.GDA_PROCEDURE);
        attr(GetDataAvailabilityConstants.XLINK_HREF, da.getProcedure().getHref());
        if (da.getProcedure().isSetTitle()) {
            attr(GetDataAvailabilityConstants.XLINK_TITLE, da.getProcedure().getTitle());
        } else {
            attr(GetDataAvailabilityConstants.XLINK_TITLE, da.getProcedure().getTitleOrFromHref());
        }
        end(GetDataAvailabilityConstants.GDA_PROCEDURE);
    }

    protected void writeObservedProperty(DataAvailability da) throws XMLStreamException {
        empty(GetDataAvailabilityConstants.GDA_OBSERVED_PROPERTY);
        attr(GetDataAvailabilityConstants.XLINK_HREF, da.getObservedProperty().getHref());
        if (da.getObservedProperty().isSetTitle()) {
            attr(GetDataAvailabilityConstants.XLINK_TITLE, da.getObservedProperty().getTitle());
        } else {
            attr(GetDataAvailabilityConstants.XLINK_TITLE, da.getObservedProperty().getTitleOrFromHref());
        }
        end(GetDataAvailabilityConstants.GDA_OBSERVED_PROPERTY);
    }

    protected void writeTimePeriod(TimePeriod tp) throws XMLStreamException, EncodingException {
        start(GmlConstants.QN_TIME_PERIOD_32);
        attr(GmlConstants.QN_ID_32, tp.getGmlId());
        writeBegin(tp);
        writeEnd(tp);
        end(GmlConstants.QN_TIME_PERIOD_32);
    }

    protected void writeBegin(TimePeriod tp) throws XMLStreamException, EncodingException {
        if (tp.isSetStartIndeterminateValue()) {
            empty(GmlConstants.QN_BEGIN_POSITION_32);
            attr(GmlConstants.AN_INDETERMINATE_POSITION, tp.getStartIndet().getValue());
        } else if (tp.isSetStart()) {
            start(GmlConstants.QN_BEGIN_POSITION_32);
            writeTimeString(tp.getStart(), tp.getTimeFormat());
        } else {
            empty(GmlConstants.QN_BEGIN_POSITION_32);
        }
        end(GmlConstants.QN_BEGIN_POSITION_32);
    }

    protected void writeEnd(TimePeriod tp) throws XMLStreamException, EncodingException {

        if (tp.isSetEndIndeterminateValue()) {
            empty(GmlConstants.QN_END_POSITION_32);
            attr(GmlConstants.AN_INDETERMINATE_POSITION, tp.getEndIndet().getValue());
        } else if (tp.isSetEnd()) {
            start(GmlConstants.QN_END_POSITION_32);
            writeTimeString(tp.getEnd(), tp.getTimeFormat());
        } else {
            empty(GmlConstants.QN_END_POSITION_32);
        }
        end(GmlConstants.QN_END_POSITION_32);

    }

    protected void writeTimeString(DateTime time, TimeFormat format) throws XMLStreamException,
            EncodingException {
        chars(DateTimeHelper.formatDateTime2String(time, format));
    }

    protected void writeCount(long count) throws XMLStreamException {
        start(GetDataAvailabilityConstants.GDA_COUNT);
        chars(Long.toString(count));
        end(GetDataAvailabilityConstants.GDA_COUNT);
    }

    protected void writeResultTimes(List<TimeInstant> resultTimes) throws XMLStreamException, EncodingException {
        start(GetDataAvailabilityConstants.GDA_EXTENSION);
        start(SweConstants.QN_DATA_RECORD_SWE_200);
        attr("definition", RESULT_TIME);
        for (TimeInstant resultTime : resultTimes) {
            start(SweConstants.QN_FIELD_200);
            attr("name", RESULT_TIME + resultTimeCount++);
            writeTime(resultTime);
            end(SweConstants.QN_FIELD_200);
        }
        end(SweConstants.QN_DATA_RECORD_SWE_200);
        end(GetDataAvailabilityConstants.GDA_EXTENSION);
    }

    protected void writeTime(TimeInstant ti) throws XMLStreamException, EncodingException {
        start(SweConstants.QN_TIME_SWE_200);
        writeValue(ti);
        writeUom();
        end(SweConstants.QN_TIME_SWE_200);
    }

    private void writeUom() throws XMLStreamException {
        start(SweConstants.QN_UOM_SWE_200);
        attr(W3CConstants.QN_XLINK_HREF, OmConstants.PHEN_UOM_ISO8601);
        end(SweConstants.QN_UOM_SWE_200);

    }

    protected void writeValue(TimeInstant ti) throws XMLStreamException, EncodingException {
        start(SweConstants.QN_VALUE_SWE_200);
        writeTimeString(ti.getValue(), ti.getTimeFormat());
        end(SweConstants.QN_VALUE_SWE_200);
    }

}
