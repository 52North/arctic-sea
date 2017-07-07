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

import static java.util.stream.Collectors.toList;

import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gmlcov.GmlCoverageConstants;
import org.n52.shetland.ogc.om.AbstractObservationValue;
import org.n52.shetland.ogc.om.AbstractPhenomenon;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.ObservationType;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.TimeValuePair;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.write.WmlTDREncoderv20XmlStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.opengis.gml.x32.MeasureOrNilReasonListType;
import net.opengis.gml.x32.QuantityListDocument;
import net.opengis.om.x20.OMObservationType;
import net.opengis.watermlDr.x20.MeasurementTimeseriesCoverageType;
import net.opengis.watermlDr.x20.MeasurementTimeseriesDomainRangeDocument;
import net.opengis.watermlDr.x20.TimePositionListDocument;
import net.opengis.watermlDr.x20.TimePositionListType;

/**
 * Encoder class for WaterML 2.0 TimeseriesDomainRange (TDR)
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class WmlTDREncoderv20
        extends AbstractWmlEncoderv20 {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmlTDREncoderv20.class);

    // TODO: change to correct conformance class
    private static final Set<String> CONFORMANCE_CLASSES = ImmutableSet.of();

    private static final Set<EncoderKey> ENCODER_KEYS = createEncoderKeys();

    private static final ImmutableSet<SupportedType> SUPPORTED_TYPES = ImmutableSet.<SupportedType> builder()
            .add(new ObservationType(WaterMLConstants.OBSERVATION_TYPE_MEASURMENT_TDR)).build();

    private static final Map<String, Map<String, Set<String>>> SUPPORTED_RESPONSE_FORMATS =
            Collections.singletonMap(SosConstants.SOS, Collections.singletonMap(Sos2Constants.SERVICEVERSION,
                    Collections.singleton(WaterMLConstants.NS_WML_20_DR)));

    private static final String TIMESERIES_ID_PREFIX = "timeseries_";

    private static final String DATA_RECORD_ID_PREFIX = "datarecord_";

    private static final String TIME_POSITION_LIST_ID_PREFIX = "timepositionList_";

    public WmlTDREncoderv20() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.unmodifiableSet(SUPPORTED_TYPES);
    }

    @Override
    public Map<String, Set<SupportedType>> getSupportedResponseFormatObservationTypes() {
        return Collections.singletonMap(WaterMLConstants.NS_WML_20_DR, getSupportedTypes());
    }

    @Override
    public Set<String> getConformanceClasses(String service, String version) {
        if (SosConstants.SOS.equals(service) && Sos2Constants.SERVICEVERSION.equals(version)) {
            return Collections.unmodifiableSet(CONFORMANCE_CLASSES);
        }
        return Collections.emptySet();
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        super.addNamespacePrefixToMap(nameSpacePrefixMap);
        nameSpacePrefixMap.put(WaterMLConstants.NS_WML_20_DR, WaterMLConstants.NS_WML_20_DR_PREFIX);
        nameSpacePrefixMap.put(GmlCoverageConstants.NS_GML_COV, GmlCoverageConstants.NS_GML_COV_PREFIX);
    }

    @Override
    public Set<String> getSupportedResponseFormats(String service, String version) {
        if (SUPPORTED_RESPONSE_FORMATS.get(service) != null
                && SUPPORTED_RESPONSE_FORMATS.get(service).get(version) != null) {
            return SUPPORTED_RESPONSE_FORMATS.get(service).get(version);
        }
        return Collections.emptySet();
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(WaterMLConstants.WML_20_SCHEMA_LOCATION, WaterMLConstants.WML_20_DR_SCHEMA_LOCATION,
                GmlCoverageConstants.GML_COVERAGE_10_SCHEMA_LOCATION);
    }

    @Override
    public boolean supportsResultStreamingForMergedValues() {
        return false;
    }

    @Override
    public XmlObject encode(Object element, EncodingContext additionalValues) throws EncodingException {
        if (element instanceof ObservationValue) {
            return encodeResult((ObservationValue<?>) element);
        } else {
            return super.encode(element, additionalValues);
        }
    }

    @Override
    public void encode(Object objectToEncode, OutputStream outputStream, EncodingContext ctx)
            throws EncodingException {
        if (objectToEncode instanceof OmObservation) {
            try {
                new WmlTDREncoderv20XmlStreamWriter(ctx.with(StreamingEncoderFlags.ENCODER, this), outputStream,
                        (OmObservation) objectToEncode).write();
            } catch (XMLStreamException xmlse) {
                throw new EncodingException("Error while writing element to stream!", xmlse);
            }
        } else {
            super.encode(objectToEncode, ctx);
        }
    }

    @Override
    protected XmlObject createResult(OmObservation sosObservation) throws EncodingException {
        return createMeasurementDomainRange(sosObservation);
    }

    @Override
    protected XmlObject encodeResult(ObservationValue<?> observationValue) throws EncodingException {
        return createMeasurementDomainRange((AbstractObservationValue<?>) observationValue);
    }

    @Override
    protected void addObservationType(OMObservationType xbObservation, String observationType) {
        if (!Strings.isNullOrEmpty(observationType)) {
            if (observationType.equals(OmConstants.OBS_TYPE_MEASUREMENT)
                    || observationType.equals(WaterMLConstants.OBSERVATION_TYPE_MEASURMENT_TDR)) {
                xbObservation.addNewType().setHref(WaterMLConstants.OBSERVATION_TYPE_MEASURMENT_TDR);
            } else if (observationType.equals(OmConstants.OBS_TYPE_CATEGORY_OBSERVATION)
                    || observationType.equals(WaterMLConstants.OBSERVATION_TYPE_CATEGORICAL_TDR)) {
                xbObservation.addNewType().setHref(WaterMLConstants.OBSERVATION_TYPE_CATEGORICAL_TDR);
            }
        }
    }

    protected OMObservationType createOmObservationType() {
        return OMObservationType.Factory.newInstance(getXmlOptions());
    }

    /**
     * Create a XML MeasurementTimeseriesDomainRange object from SOS observation
     * for om:result
     *
     * @param sosObservation
     *            SOS observation
     * @return XML MeasurementTimeseriesDomainRange object for om:result
     * @throws EncodingException
     *             If an error occurs
     */
    private XmlObject createMeasurementDomainRange(OmObservation sosObservation) throws EncodingException {
        if (!sosObservation.getObservationConstellation().isSetObservationType()
                || (sosObservation.getObservationConstellation().isSetObservationType() && isInvalidObservationType(
                        sosObservation.getObservationConstellation().getObservationType()))) {
            throw new UnsupportedEncoderInputException(this,
                    sosObservation.getObservationConstellation().isSetObservationType());
        }
        MeasurementTimeseriesDomainRangeDocument xbMearuementTimeseriesDomainRangeDoc =
                MeasurementTimeseriesDomainRangeDocument.Factory.newInstance();
        MeasurementTimeseriesCoverageType xbMeasurementTimeseriesDomainRange =
                xbMearuementTimeseriesDomainRangeDoc.addNewMeasurementTimeseriesDomainRange();
        xbMeasurementTimeseriesDomainRange.setId(TIMESERIES_ID_PREFIX + sosObservation.getObservationID());

        // set time position list
        xbMeasurementTimeseriesDomainRange.addNewDomainSet().set(getTimePositionList(sosObservation));
        // initialize unit
        AbstractPhenomenon observableProperty = sosObservation.getObservationConstellation().getObservableProperty();
        String unit = null;
        // create quantity list from values
        QuantityListDocument quantityListDoc = QuantityListDocument.Factory.newInstance();
        MeasureOrNilReasonListType quantityList = quantityListDoc.addNewQuantityList();
        if (sosObservation.getValue() instanceof MultiObservationValues) {
            MultiObservationValues<?> observationValue = (MultiObservationValues<?>) sosObservation.getValue();
            TVPValue tvpValue = (TVPValue) observationValue.getValue();
            List<TimeValuePair> timeValuePairs = tvpValue.getValue();
            if (Strings.isNullOrEmpty(unit) && CollectionHelper.isNotEmpty(timeValuePairs)
                    && timeValuePairs.get(0).getValue().isSetUnit()) {
                unit = timeValuePairs.get(0).getValue().getUnit();
            }
            quantityList.setListValue(getValueList(timeValuePairs));
        }

        if (Strings.isNullOrEmpty(unit)) {
            unit = OGCConstants.UNKNOWN;
        }
        quantityList.setUom(unit);
        // set unit to SosObservableProperty if not set.
        if (observableProperty instanceof OmObservableProperty
                && !((OmObservableProperty) observableProperty).isSetUnit()) {
            ((OmObservableProperty) observableProperty).setUnit(unit);
        }
        // set up range set
        xbMeasurementTimeseriesDomainRange.addNewRangeSet().set(quantityListDoc);
        // set up rangeType
        xbMeasurementTimeseriesDomainRange.addNewRangeType().set(createDataRecord(sosObservation));

        // set om:Result
        return xbMearuementTimeseriesDomainRangeDoc;
    }

    private XmlObject createMeasurementDomainRange(AbstractObservationValue<?> observationValue)
            throws EncodingException {
        if (!observationValue.isSetObservationType() || (observationValue.isSetObservationType()
                && isInvalidObservationType(observationValue.getObservationType()))) {
            return null;
        }

        MeasurementTimeseriesDomainRangeDocument xbMearuementTimeseriesDomainRangeDoc =
                MeasurementTimeseriesDomainRangeDocument.Factory.newInstance();
        MeasurementTimeseriesCoverageType xbMeasurementTimeseriesDomainRange =
                xbMearuementTimeseriesDomainRangeDoc.addNewMeasurementTimeseriesDomainRange();
        xbMeasurementTimeseriesDomainRange.setId(TIMESERIES_ID_PREFIX + observationValue.getObservationID());

        // set time position list
        xbMeasurementTimeseriesDomainRange.addNewDomainSet().set(getTimePositionList(observationValue));
        // initialize unit
        // AbstractPhenomenon observableProperty =
        // observationValue.getObservableProperty();
        String unit = null;
        // create quantity list from values
        QuantityListDocument quantityListDoc = QuantityListDocument.Factory.newInstance();
        MeasureOrNilReasonListType quantityList = quantityListDoc.addNewQuantityList();
        if (observationValue instanceof MultiObservationValues) {
            TVPValue tvpValue = (TVPValue) ((MultiObservationValues<?>) observationValue).getValue();
            List<TimeValuePair> timeValuePairs = tvpValue.getValue();
            if (Strings.isNullOrEmpty(unit) && CollectionHelper.isNotEmpty(timeValuePairs)
                    && timeValuePairs.get(0).getValue().isSetUnit()) {
                unit = timeValuePairs.get(0).getValue().getUnit();
            }
            quantityList.setListValue(getValueList(timeValuePairs));
        }

        if (Strings.isNullOrEmpty(unit)) {
            unit = OGCConstants.UNKNOWN;
        }
        quantityList.setUom(unit);
        // set unit to SosObservableProperty if not set.
        // if (observableProperty instanceof OmObservableProperty
        // && !((OmObservableProperty) observableProperty).isSetUnit()) {
        // ((OmObservableProperty) observableProperty).setUnit(unit);
        // }
        // set up range set
        xbMeasurementTimeseriesDomainRange.addNewRangeSet().set(quantityListDoc);
        // set up rangeType
        xbMeasurementTimeseriesDomainRange.addNewRangeType().set(createDataRecord(observationValue, unit));

        // set om:Result
        return xbMearuementTimeseriesDomainRangeDoc;
    }

    /**
     * Create a SOS DataRecord object from SOS observation and encode to
     * XmlBeans object
     *
     * @param sosObservation
     *            SOS observation
     * @return XML DataRecord object
     * @throws EncodingException
     *             If an error occurs
     */
    private XmlObject createDataRecord(OmObservation sosObservation) throws EncodingException {
        AbstractPhenomenon observableProperty = sosObservation.getObservationConstellation().getObservableProperty();
        SweQuantity quantity = new SweQuantity();
        quantity.setDefinition(observableProperty.getIdentifier());
        quantity.setDescription(observableProperty.getDescription());
        if (observableProperty instanceof OmObservableProperty
                && ((OmObservableProperty) observableProperty).isSetUnit()) {
            quantity.setUom(((OmObservableProperty) observableProperty).getUnit());
        }
        return createDataRecord(quantity, sosObservation.getObservationID());
    }

    private XmlObject createDataRecord(AbstractObservationValue<?> observationValue, String unit)
            throws EncodingException {
        // AbstractPhenomenon observableProperty =
        // sosObservation.getObservationConstellation().getObservableProperty();
        SweQuantity quantity = new SweQuantity();
        quantity.setDefinition(observationValue.getObservableProperty());
        quantity.setUom(unit);
        return createDataRecord(quantity, observationValue.getObservationID());
    }

    private XmlObject createDataRecord(SweQuantity quantity, String observationId) throws EncodingException {
        SweField field = new SweField("observed_value", quantity);
        SweDataRecord dataRecord = new SweDataRecord();
        dataRecord.setIdentifier(DATA_RECORD_ID_PREFIX + observationId);
        dataRecord.addField(field);
        return encodeObjectToXml(SweConstants.NS_SWE_20, dataRecord,
                EncodingContext.of(XmlBeansEncodingFlags.FOR_OBSERVATION));
    }

    /**
     * Create a TimePositionList XML object from time values
     *
     * @param sosObservation
     *            SOS observation
     * @return XML TimePositionList object
     * @throws EncodingException
     *             If an error occurs
     */
    private TimePositionListDocument getTimePositionList(OmObservation sosObservation) throws EncodingException {
        TimePositionListDocument timePositionListDoc = TimePositionListDocument.Factory.newInstance();
        TimePositionListType timePositionList = timePositionListDoc.addNewTimePositionList();
        timePositionList.setId(TIME_POSITION_LIST_ID_PREFIX + sosObservation.getObservationID());
        if (sosObservation.getValue() instanceof SingleObservationValue<?>) {
            timePositionList.setTimePositionList(
                    Lists.newArrayList(getTimeString(sosObservation.getValue().getPhenomenonTime())));
        } else if (sosObservation.getValue() instanceof MultiObservationValues<?>) {
            timePositionList.setTimePositionList(getTimeArray((MultiObservationValues<?>) sosObservation.getValue()));
        }
        return timePositionListDoc;
    }

    private TimePositionListDocument getTimePositionList(AbstractObservationValue<?> observationValue)
            throws EncodingException {
        TimePositionListDocument timePositionListDoc = TimePositionListDocument.Factory.newInstance();
        TimePositionListType timePositionList = timePositionListDoc.addNewTimePositionList();
        timePositionList.setId(TIME_POSITION_LIST_ID_PREFIX + observationValue.getObservationID());
        timePositionList.setTimePositionList(getTimeArray((MultiObservationValues<?>) observationValue));
        return timePositionListDoc;
    }

    /**
     * Create a array from time values
     *
     * @param sosObservationValues
     *            SOS multi value observation object
     * @return List with string representations of time values
     * @throws EncodingException
     *             If an error occurs
     */
    private List<String> getTimeArray(MultiObservationValues<?> sosObservationValues) throws EncodingException {
        return ((TVPValue) sosObservationValues.getValue()).getValue().stream().map(TimeValuePair::getTime)
                .map(this::getTimeString).collect(toList());
    }

    /**
     * Get a value list from SOS TimeValuePair objects
     *
     * @param timeValuePairs
     *            SOS TimeValuePair objects
     * @return List with value objects
     * @throws EncodingException
     *             If an error occurs
     */
    private List<Object> getValueList(List<TimeValuePair> timeValuePairs) throws EncodingException {
        return timeValuePairs.stream().map(TimeValuePair::getValue).map(value -> {
            if (value != null && (value instanceof CountValue || value instanceof QuantityValue)) {
                return value.getValue();
            } else {
                return "";
            }
        }).collect(toList());
    }

    private boolean isInvalidObservationType(String observationType) {
        return !(OmConstants.OBS_TYPE_COUNT_OBSERVATION.equals(observationType)
                || OmConstants.OBS_TYPE_MEASUREMENT.equals(observationType)
                || OmConstants.OBS_TYPE_SWE_ARRAY_OBSERVATION.equals(observationType));
    }

    private static Set<EncoderKey> createEncoderKeys() {
        return CollectionHelper.union(getDefaultEncoderKeys(),
                CodingHelper.encoderKeysForElements(WaterMLConstants.NS_WML_20_DR, GetObservationResponse.class,
                        OmObservation.class, AbstractFeature.class, SingleObservationValue.class,
                        MultiObservationValues.class));
    }
}
