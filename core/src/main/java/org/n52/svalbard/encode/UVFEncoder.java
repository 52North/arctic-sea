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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.AbstractPhenomenon;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.ObservationMergeIndicator;
import org.n52.shetland.ogc.om.ObservationStream;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.TimeValuePair;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.om.series.wml.MeasurementTimeseriesMetadata;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants.InterpolationType;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.MultiValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.AbstractObservationResponse;
import org.n52.shetland.ogc.sos.response.BinaryAttachmentResponse;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.util.DateTimeFormatException;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.util.JavaHelper;
import org.n52.shetland.uvf.UVFConstants;
import org.n52.shetland.uvf.UVFConstants.LineEnding;
import org.n52.shetland.uvf.UVFSettingsProvider;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

/**
 * The UVFEncoder implements the so called <b>U</b>niversal <b>V</b>ariable
 * <b>F</b>ormat.
 *
 * The definitions found on the internet are the following:
 *
 * <ul>
 * <li><a href="http://www.aquaplan.de/public_papers/imex/sectionUVF.html">
 * Aquaplan Specification of UVF</a> (<a href=
 * "https://web.archive.org/web/20080220032227/http://www.aquaplan.de/public_papers/imex/sectionUVF.html">
 * archived Version</a>)</li>
 * <li><a href="http://wiki.bluemodel.org/index.php/UVF-Format">Bluemodel.org
 * updated version</a> (<a href=
 * "https://web.archive.org/web/20080220032227/http://wiki.bluemodel.org/index.php/UVF-Format">
 * archived version</a>)</li>
 * </ul>
 *
 * It is used for exchanging timeseries and looks like the following:
 *
 * <pre>
 *  0: $sb Index-Einheit:
 1: $ib Funktion-Interpretation: Summenlinie
 2: $sb Mess-Groesse: Niederschlag
 3: $sb Mess-Einheit: mm
 4: $sb Mess-Stellennummer: 5242
 5: *Z
 6: Niederschlag: mm             1900 2000
 7: 5242              1231240   1413414     52.52
 8: 73110107301002100636Zeit
 9: 7311010730         0
10: 7311050224         0
11: 7311050240 .30333331
12: 7311050255        .5
13: 7311050414        .5
14: 7311050415 -777
15: 7311050419 -777
16: 7311050420 .80000001
 * </pre>
 *
 * The Lines 0 to 8 specify the header of the dataset. Hereby, the lines 0 to 4
 * provide optional metadata. The lines 5 to 8 are mandatory and are referenced
 * in the specification by "Zeile 1,2,3,4". Line 14 and 15 show the handling of
 * gaps or no data values. They are encoded with <code>-777</code>. Gaps are
 * defined by two values: one for the start and one for the end of the gap. The
 * following assumptions/constraints are implemented:
 * <ul>
 * <li>Only ONE timeseries will be encoded. Hence, the
 * {@code UVFRequestModifier} ensures, that each request for the UVF contains
 * ONE observed property, ONE feature of interest, and ONE procedure.</li>
 * <li>Only observations of type Measurement and Count are supported.</li>
 * <li>The encoder does not check for gaps and encodes only start and end. This
 * MUST be done before inserting the data in the SOS database.</li>
 * <li>Identifiers are shortened via {@link String#substring(int, int)} to the
 * length of {@link UVFConstants#MAX_IDENTIFIER_LENGTH} (15) starting from the
 * end of the given identifier.</li>
 * <li>Values (e.g. measurements and coordinates) are shortened via
 * {@link String#substring(int, int)} to the length of
 * {@link UVFConstants#MAX_VALUE_LENGTH} (10) starting form the beginning of the
 * String representation of the value. No rounding is performed.</li>
 * <li>The UVF requires that all coordinates are in Gauß Krüger, hence a
 * coordinate transformation is performed before the response is encoded. This
 * requires that the EPSG code of the best matching GK band is given. When not
 * present in the request, a default value is used. This value can be specified
 * using the admin WebUI of the SOS. Currently the following EPSG codes are
 * allowed (see {@link UVFConstants#ALLOWED_CRS}):
 * <ul>
 * <li>31466</li>
 * <li>31467</li>
 * <li>31468</li>
 * <li>31469</li>
 * </ul>
 * </ul>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 */
@Configurable
public class UVFEncoder implements ObservationEncoder<BinaryAttachmentResponse, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UVFEncoder.class);

    private static final Set<SupportedType> SUPPORTED_TYPES =
            ImmutableSet.<SupportedType> builder().add(OmConstants.OBS_TYPE_MEASUREMENT_TYPE).build();

    private final Set<String> MEDIA_TYPES =
            Sets.newHashSet(UVFConstants.CONTENT_TYPE_UVF.toString(), UVFConstants.CONTENT_TYPE_UVF_MAC.toString(),
                    UVFConstants.CONTENT_TYPE_UVF_UNIX.toString(), UVFConstants.CONTENT_TYPE_UVF_WINDOWS.toString());

    private final Map<String, Map<String, Set<String>>> SUPPORTED_RESPONSE_FORMATS =
            Collections.singletonMap(SosConstants.SOS,
                    (Map<String, Set<String>>) new ImmutableMap.Builder<String, Set<String>>()
                            .put(Sos1Constants.SERVICEVERSION, MEDIA_TYPES)
                            .put(Sos2Constants.SERVICEVERSION, MEDIA_TYPES).build());

    private final Set<EncoderKey> ENCODER_KEYS = createEncoderKeys();

    private DateTimeZone timeZone;

    private LineEnding lineEnding = LineEnding.Unix;

    public UVFEncoder() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return ENCODER_KEYS;
    }

    @Override
    public BinaryAttachmentResponse encode(Object objectToEncode) throws EncodingException {
        return encode(objectToEncode, EncodingContext.empty());
    }

    @Override
    public BinaryAttachmentResponse encode(Object objectToEncode, EncodingContext additionalValues)
            throws EncodingException {
        if (objectToEncode instanceof AbstractObservationResponse) {
            AbstractObservationResponse aor = (AbstractObservationResponse) objectToEncode;
            if (aor.getObservationCollection() != null) {
                return encodeGetObsResponse(aor);
            } else {
                return createEmptyFile();
            }
        }
        throw new UnsupportedEncoderInputException(this, objectToEncode);
    }

    @Setting(UVFSettingsProvider.UVF_TIME_ZONE_SETTING_KEY)
    public void setTimeZone(String timeZone) {
        if (!Strings.isNullOrEmpty(timeZone)) {
            this.timeZone = DateTimeZone.forTimeZone(TimeZone.getTimeZone(timeZone.trim()));
        } else {
            this.timeZone = null;
        }
    }

    @Setting(UVFSettingsProvider.UVF_LINE_ENDING_KEY)
    public void setLineEnding(String lineEnding) {
        if (!Strings.isNullOrEmpty(lineEnding)) {
            this.lineEnding = UVFConstants.LineEnding.valueOf(lineEnding.trim());
        } else {
            this.lineEnding = UVFConstants.LineEnding.Unix;
        }
    }

    private Set<EncoderKey> createEncoderKeys() {
        Set<EncoderKey> keys = Sets.newHashSet();
        for (String s : MEDIA_TYPES) {
            MediaType mediaType = MediaType.parse(s);
            keys.add((EncoderKey) new OperationResponseEncoderKey(SosConstants.SOS, Sos1Constants.SERVICEVERSION,
                    SosConstants.Operations.GetObservation, mediaType));
            keys.add((EncoderKey) new OperationResponseEncoderKey(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                    SosConstants.Operations.GetObservation, mediaType));
            keys.add((EncoderKey) new OperationResponseEncoderKey(SosConstants.SOS, Sos1Constants.SERVICEVERSION,
                    SosConstants.Operations.GetObservationById, mediaType));
            keys.add((EncoderKey) new OperationResponseEncoderKey(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                    SosConstants.Operations.GetObservationById, mediaType));
        }
        return keys;
    }

    private BinaryAttachmentResponse createEmptyFile() {
        return new BinaryAttachmentResponse(null, null, null);
    }

    private BinaryAttachmentResponse encodeGetObsResponse(AbstractObservationResponse aor) throws EncodingException {
        File tempDir = Files.createTempDir();
        BinaryAttachmentResponse response = null;
        try {
            File uvfFile = encodeToUvf(aor.getObservationCollection(), tempDir, aor.getContentType());
            response = new BinaryAttachmentResponse(Files.toByteArray(uvfFile), getContentType(),
                    String.format(uvfFile.getName(), makeDateSafe(new DateTime(DateTimeZone.UTC))));
        } catch (IOException e) {
            throw new EncodingException("Couldn't create UVF file", e);
        } finally {
            if (!tempDir.delete()) {
                LOGGER.warn("Temporal file '{}' was not deleted!", tempDir.getName());
            }
        }

        return response;

    }

    private File encodeToUvf(ObservationStream observationStream, File tempDir, MediaType contentType)
            throws IOException, EncodingException {
        List<OmObservation> mergeObservations = mergeTotoList(observationStream);
        String filename = getFilename(mergeObservations);
        File uvfFile = new File(tempDir, filename);
        try (Writer fw = new OutputStreamWriter(new FileOutputStream(uvfFile), "UTF-8");) {
            for (OmObservation o : mergeObservations) {
                if (o.isSetValue() && !checkForSingleObservationValue(o.getValue())
                        && !checkForMultiObservationValue(o.getValue())) {
                    String errorMessage = String.format(
                            "The resulting values are not of numeric type "
                            + "which is only supported by this encoder '%s'.",
                            this.getClass().getName());
                    LOGGER.error(errorMessage);
                    throw new EncodingException(errorMessage);
                }

                /*
                 * HEADER: Metadata
                 */
                writeFunktionInterpretation(fw, o, contentType);
                writeIndex(fw, contentType);
                writeMessGroesse(fw, o, contentType);
                writeMessEinheit(fw, o, contentType);
                writeMessStellennummer(fw, o, contentType);
                writeMessStellenname(fw, o, contentType);
                /*
                 * HEADER: Lines 1 - 4
                 */
                writeLine1(fw, contentType);
                TimePeriod temporalBBox = getTemporalBBoxFromObservations(mergeObservations);
                writeLine2(fw, o, temporalBBox, contentType);
                writeLine3(fw, o, contentType);
                writeLine4(fw, temporalBBox, contentType);
                /*
                 * Observation Data
                 */
                writeObservationValue(fw, o, contentType);
            }
        }
        return uvfFile;
    }

    private boolean checkForSingleObservationValue(ObservationValue<?> value) {
        return value instanceof SingleObservationValue<?> && value.getValue().isSetValue()
                && (value.getValue() instanceof CountValue || value.getValue() instanceof QuantityValue);
    }

    private boolean checkForMultiObservationValue(ObservationValue<?> value) {
        return value instanceof MultiObservationValues<?> && value.getValue().isSetValue()
                && value.getValue() instanceof TVPValue && ((TVPValue) value.getValue()).isSetValue()
                && (((TVPValue) value.getValue()).getValue().get(0).getValue() instanceof CountValue
                        || ((TVPValue) value.getValue()).getValue().get(0).getValue() instanceof QuantityValue);
    }

    private void writeFunktionInterpretation(Writer fw, OmObservation o, MediaType contentType) throws IOException {
        String function = getFunction(o);
        if (!Strings.isNullOrEmpty(function)) {
            writeToFile(fw, String.format("$ib Funktion-Interpretation: %s", function), contentType);
        }
    }

    private void writeIndex(Writer fw, MediaType contentType) throws IOException {
        writeToFile(fw, "$sb Index-Einheit: *** Zeit ***", contentType);
    }

    private void writeMessGroesse(Writer fw, OmObservation o, MediaType contentType) throws IOException {
        String observablePropertyIdentifier = o.getObservationConstellation().getObservablePropertyIdentifier();
        observablePropertyIdentifier =
                ensureIdentifierLength(observablePropertyIdentifier, UVFConstants.MAX_IDENTIFIER_LENGTH);
        writeToFile(fw, String.format("$sb Mess-Groesse: %s", observablePropertyIdentifier), contentType);
    }

    private void writeMessEinheit(Writer fw, OmObservation o, MediaType contentType) throws IOException {
        // $sb Mess-Einheit: m3/s
        // Unit (optional)
        String unit = getUnit(o);
        if (unit != null && !unit.isEmpty()) {
            unit = ensureIdentifierLength(unit, UVFConstants.MAX_IDENTIFIER_LENGTH);
            writeToFile(fw, String.format("$sb Mess-Einheit: %s", unit), contentType);
        }
    }

    private void writeMessStellennummer(Writer fw, OmObservation o, MediaType contentType) throws IOException {
        String featureOfInterestIdentifier = o.getObservationConstellation().getFeatureOfInterestIdentifier();
        if (featureOfInterestIdentifier != null && !featureOfInterestIdentifier.isEmpty()) {
            featureOfInterestIdentifier =
                    ensureIdentifierLength(featureOfInterestIdentifier, UVFConstants.MAX_IDENTIFIER_LENGTH);
        }
        writeToFile(fw, String.format("$sb Mess-Stellennummer: %s", featureOfInterestIdentifier), contentType);
    }

    private void writeMessStellenname(Writer fw, OmObservation o, MediaType contentType) throws IOException {
        if (o.getObservationConstellation().getFeatureOfInterest().isSetName()) {
            final CodeType firstName = o.getObservationConstellation().getFeatureOfInterest().getFirstName();
            String name = ensureIdentifierLength(firstName.isSetValue() ? firstName.getValue() : "",
                    UVFConstants.MAX_IDENTIFIER_LENGTH);
            writeToFile(fw, String.format("$sb Mess-Stellenname: %s", name), contentType);
        }
    }

    private void writeLine1(Writer fw, MediaType contentType) throws IOException {
        writeToFile(fw, "*Z", contentType);
    }

    private void writeLine2(Writer fw, OmObservation o, TimePeriod centuries, MediaType contentType)
            throws IOException {
        // 2.Zeile ABFLUSS m3/s 1900 1900
        StringBuilder sb = new StringBuilder(39);
        // Identifier
        AbstractPhenomenon observableProperty = o.getObservationConstellation().getObservableProperty();
        String observablePropertyIdentifier = observableProperty.getIdentifier();
        if (observablePropertyIdentifier != null && !observablePropertyIdentifier.isEmpty()) {
            observablePropertyIdentifier =
                    ensureIdentifierLength(observablePropertyIdentifier, UVFConstants.MAX_IDENTIFIER_LENGTH);
        }
        sb.append(observablePropertyIdentifier);
        fillWithSpaces(sb, UVFConstants.MAX_IDENTIFIER_LENGTH);
        // Unit (optional)
        String unit = getUnit(o);
        if (unit != null && !unit.isEmpty()) {
            unit = ensureIdentifierLength(unit, UVFConstants.MAX_IDENTIFIER_LENGTH);
            sb.append(" ");
            sb.append(unit);
        }
        fillWithSpaces(sb, 30);
        // Centuries
        sb.append(centuries.getStart().getCenturyOfEra() + "00 " + centuries.getEnd().getCenturyOfEra() + "00");
        writeToFile(fw, sb.toString(), contentType);
    }

    private void writeLine3(Writer fw, OmObservation o, MediaType contentType) throws IOException {
        // 3.Zeile 88888 0 0 0.000
        StringBuilder sb = new StringBuilder(45);
        if (o.getObservationConstellation().isSetIdentifier()) {
            sb.append(ensureIdentifierLength(o.getObservationConstellation().getIdentifier(),
                    UVFConstants.MAX_IDENTIFIER_LENGTH));
        } else if (o.isSetIdentifier()) {
            sb.append(ensureIdentifierLength(o.getIdentifier(), UVFConstants.MAX_IDENTIFIER_LENGTH));
        } else {
            if (!o.isSetObservationID()) {
                o.setObservationID(JavaHelper.generateID(o.toString()));
            }
            sb.append(ensureIdentifierLength(o.getObservationID(), UVFConstants.MAX_IDENTIFIER_LENGTH));
        }
        fillWithSpaces(sb, UVFConstants.MAX_IDENTIFIER_LENGTH);
        AbstractFeature f = o.getObservationConstellation().getFeatureOfInterest();
        if (o.getObservationConstellation().getFeatureOfInterest() instanceof AbstractSamplingFeature
                && ((AbstractSamplingFeature) o.getObservationConstellation().getFeatureOfInterest())
                        .isSetGeometry()) {
            AbstractSamplingFeature sf = (AbstractSamplingFeature) f;
            // Rechtswert
            String xString = sf.isSetGeometry() ? Double.toString(sf.getGeometry().getCoordinate().y) : "";
            xString = ensureValueLength(xString, 10);
            sb.append(xString);
            fillWithSpaces(sb, 25);
            // Hochwert
            String yString = sf.isSetGeometry() ? Double.toString(sf.getGeometry().getCoordinate().x) : "";
            yString = ensureValueLength(yString, 10);
            sb.append(yString);
            fillWithSpaces(sb, 35);
            if (sf.isSetGeometry() && !Double.isNaN(sf.getGeometry().getCoordinate().z)) {
                String zString = Double.toString(sf.getGeometry().getCoordinate().z);
                zString = ensureValueLength(zString, 10);
                sb.append(zString);
            }
        }
        fillWithSpaces(sb, 45);
        writeToFile(fw, sb.toString(), contentType);
    }

    private void writeLine4(Writer fw, TimePeriod temporalBBox, MediaType contentType)
            throws IOException, DateTimeFormatException {
        StringBuilder sb = new StringBuilder(28);
        sb.append(DateTimeHelper.formatDateTime2FormattedString(checkTimeZone(temporalBBox.getStart()),
                UVFConstants.TIME_FORMAT));
        sb.append(DateTimeHelper.formatDateTime2FormattedString(checkTimeZone(temporalBBox.getEnd()),
                UVFConstants.TIME_FORMAT));
        fillWithSpaces(sb, 20);
        sb.append("Zeit");
        fillWithSpaces(sb, 28);
        writeToFile(fw, sb.toString(), contentType);
    }

    private void writeObservationValue(Writer fw, OmObservation omObservation, MediaType contentType)
            throws IOException, EncodingException {
        // yymmddhhmmvvvvvvvvvv
        // ^ date with ten chars
        // ^ observed/measured value with 10 chars
        if (omObservation.getValue() instanceof SingleObservationValue<?>) {
            writeSingleObservationValue(fw, omObservation.getPhenomenonTime(),
                    ((SingleObservationValue<?>) omObservation.getValue()).getValue(), contentType);
        } else if (omObservation.getValue() instanceof MultiObservationValues) {
            writeMultiObservationValues(fw, omObservation, contentType);
        } else {
            handleNotYetImplemented(omObservation.getValue().getClass().getName());
        }
    }

    private String getFunction(OmObservation o) {
        if (o.getObservationConstellation().isSetDefaultPointMetadata()
                || o.getObservationConstellation().isSetMetadata()) {
            if (o.getObservationConstellation().isSetMetadata()
                    && o.getObservationConstellation().getMetadata().isSetTimeseriesMetadata()
                    && o.getObservationConstellation().getMetadata()
                            .getTimeseriesmetadata() instanceof MeasurementTimeseriesMetadata
                    && ((MeasurementTimeseriesMetadata) o.getObservationConstellation().getMetadata()
                            .getTimeseriesmetadata()).isCumulative()) {
                return UVFConstants.FunktionInterpretation.Summenlinie.name();
            }
            if (o.getObservationConstellation().isSetDefaultPointMetadata()
                    && o.getObservationConstellation().getDefaultPointMetadata().isSetDefaultTVPMeasurementMetadata()
                    && o.getObservationConstellation().getDefaultPointMetadata().getDefaultTVPMeasurementMetadata()
                            .isSetInterpolationType()) {
                return getFunctionFor(o.getObservationConstellation().getDefaultPointMetadata()
                        .getDefaultTVPMeasurementMetadata().getInterpolationtype());

            }
        } else if (o.isSetValue() && (o.getValue().isSetMetadata() || o.getValue().isSetDefaultPointMetadata())) {
            if (o.getValue().isSetMetadata() && o.getValue().getMetadata().isSetTimeseriesMetadata()
                    && o.getValue().getMetadata().getTimeseriesmetadata() instanceof MeasurementTimeseriesMetadata
                    && ((MeasurementTimeseriesMetadata) o.getValue().getMetadata().getTimeseriesmetadata())
                            .isCumulative()) {
                return UVFConstants.FunktionInterpretation.Summenlinie.name();
            }
            if (o.getValue().isSetDefaultPointMetadata()
                    && o.getValue().getDefaultPointMetadata().isSetDefaultTVPMeasurementMetadata() && o.getValue()
                            .getDefaultPointMetadata().getDefaultTVPMeasurementMetadata().isSetInterpolationType()) {
                return getFunctionFor(o.getValue().getDefaultPointMetadata().getDefaultTVPMeasurementMetadata()
                        .getInterpolationtype());
            }
        }
        return null;
    }

    private String getFunctionFor(InterpolationType interpolationtype) {
        switch (interpolationtype) {
            case Continuous:
                return UVFConstants.FunktionInterpretation.Linie.name();
            case AveragePrec:
                return UVFConstants.FunktionInterpretation.Blockanfang.name();
            case MaxPrec:
                return UVFConstants.FunktionInterpretation.Blockanfang.name();
            case MinPrec:
                return UVFConstants.FunktionInterpretation.Blockanfang.name();
            case TotalPrec:
                return UVFConstants.FunktionInterpretation.Blockanfang.name();
            case ConstPrec:
                return UVFConstants.FunktionInterpretation.Blockanfang.name();
            case AverageSucc:
                return UVFConstants.FunktionInterpretation.Blockende.name();
            case MaxSucc:
                return UVFConstants.FunktionInterpretation.Blockende.name();
            case MinSucc:
                return UVFConstants.FunktionInterpretation.Blockende.name();
            case TotalSucc:
                return UVFConstants.FunktionInterpretation.Blockende.name();
            case ConstSucc:
                return UVFConstants.FunktionInterpretation.Blockende.name();
            case Discontinuous:
                return null;
            case InstantTotal:
                return null;
            case Statistical:
                return null;
            default:
                return null;
        }
    }

    private String getUnit(OmObservation o) {
        if (o.getObservationConstellation().getObservableProperty() instanceof OmObservableProperty
                && ((OmObservableProperty) o.getObservationConstellation().getObservableProperty()).isSetUnit()) {
            return ((OmObservableProperty) o.getObservationConstellation().getObservableProperty()).getUnit();
        } else if (o.getValue().isSetValue() && o.getValue().getValue().isSetUnit()) {
            return o.getValue().getValue().getUnit();
        }
        return null;
    }

    /*
     * ***********************************************************************
     *
     * Helper methods
     *
     ***********************************************************************/

    private void writeSingleObservationValue(Writer fw, Time phenomenonTime, Value<?> value, MediaType contentType)
            throws IOException, EncodingException {
        StringBuilder sb = new StringBuilder(20);
        if (phenomenonTime instanceof TimeInstant) {
            sb.append(DateTimeHelper.formatDateTime2FormattedString(
                    checkTimeZone(((TimeInstant) phenomenonTime).getValue()), UVFConstants.TIME_FORMAT));
        } else {
            sb.append(DateTimeHelper.formatDateTime2FormattedString(
                    checkTimeZone(((TimePeriod) phenomenonTime).getEnd()), UVFConstants.TIME_FORMAT));
        }
        sb.append(encodeObservationValue(value));

        fillWithSpaces(sb, 20);
        writeToFile(fw, sb.toString(), contentType);
    }

    private void writeMultiObservationValues(Writer fw, OmObservation omObservation, MediaType contentType)
            throws IOException, EncodingException {
        MultiValue<?> values = ((MultiObservationValues<?>) omObservation.getValue()).getValue();
        /*
         * - SweDataArrayValue => not allowed, only CountObservation and
         * Measurement - TLVTValue => not supported, because UVF supports
         * timeseries for one location only - TVPValue
         */
        if (values.getValue() instanceof List<?> && !((List<?>) values.getValue()).isEmpty()) {
            Object object = ((List<?>) values.getValue()).get(0);

            if (object instanceof TimeValuePair) {
                @SuppressWarnings("unchecked")
                List<TimeValuePair> valuesList = (List<TimeValuePair>) values.getValue();
                for (TimeValuePair timeValuePair : valuesList) {
                    writeSingleObservationValue(fw, timeValuePair.getTime(), timeValuePair.getValue(), contentType);
                }
            } else {
                handleNotYetImplemented(object.getClass().getName());
            }
        }
    }

    private String encodeObservationValue(Value<?> value) throws EncodingException {
        if (value == null || !value.isSetValue()) {
            return UVFConstants.NO_DATA_STRING;
        }
        if (!(value instanceof SweQuantity) && !(value instanceof SweCount)) {
            String errorMessage = String.format("SweType '%s' not supported. Only '%s' and '%s'.",
                    value.getClass().getName(), SweQuantity.class.getName(), SweCount.class.getName());
            LOGGER.error(errorMessage);
            throw new EncodingException(errorMessage);
        }
        String encodedValue = JavaHelper.asString(value.getValue());
        if (encodedValue.length() > UVFConstants.MAX_VALUE_LENGTH) {
            encodedValue = encodedValue.substring(0, UVFConstants.MAX_VALUE_LENGTH);
        }
        return encodedValue;
    }

    private TimePeriod getTemporalBBoxFromObservations(List<OmObservation> observationCollection)
            throws EncodingException {
        DateTime start = null;
        DateTime end = null;
        for (OmObservation observation : observationCollection) {
            Time phenomenonTime = observation.getPhenomenonTime();
            if (phenomenonTime instanceof TimeInstant) {
                final DateTime time = ((TimeInstant) phenomenonTime).getTimePosition().getTime();
                if (start == null || time.isBefore(start)) {
                    start = time;
                }
                if (end == null || time.isAfter(end)) {
                    end = time;
                }
            } else {
                final DateTime periodStart = ((TimePeriod) phenomenonTime).getStart();
                if (start == null || periodStart.isBefore(start)) {
                    start = periodStart;
                }
                final DateTime periodEnd = ((TimePeriod) phenomenonTime).getEnd();
                if (end == null || periodEnd.isAfter(end)) {
                    end = periodEnd;
                }
            }
        }
        if (start != null && end != null) {
            return new TimePeriod(start, end);
        } else {
            final String message = "Could not extract centuries from observation collection";
            LOGGER.error(message);
            throw new EncodingException(message);
        }
    }

    private String ensureValueLength(String valueString, int maxLength) {
        if (valueString.length() > maxLength) {
            return valueString.substring(0, maxLength);
        }
        return valueString;
    }

    private String ensureIdentifierLength(String identifier, int maxLength) {
        String modified = null;
        if (identifier.contains("/")) {
            modified = identifier.substring(identifier.lastIndexOf("/") + 1);
        } else {
            modified = identifier;
        }
        if (modified.length() > maxLength) {
            int endIndex = modified.length();
            int beginIndex = endIndex - maxLength;
            return modified.substring(beginIndex, endIndex);
        }
        return identifier;
    }

    private void fillWithSpaces(StringBuilder sb, int i) {
        while (sb.length() < i) {
            sb.append(" ");
        }
        sb.trimToSize();
    }

    private void writeToFile(Writer fw, String string, MediaType contentType) throws IOException {
        fw.write(string + getLineEnding(contentType));
        fw.flush();
    }

    private String getLineEnding(MediaType contentType) {
        if (contentType != null) {
            if (contentType.equals(UVFConstants.CONTENT_TYPE_UVF_WINDOWS)) {
                return UVFConstants.LINE_ENDING_WINDOWS;
            } else if (contentType.equals(UVFConstants.CONTENT_TYPE_UVF_WINDOWS)) {
                return UVFConstants.LINE_ENDING_WINDOWS;
            } else if (contentType.equals(UVFConstants.CONTENT_TYPE_UVF_WINDOWS)) {
                return UVFConstants.LINE_ENDING_WINDOWS;
            }
        }
        return getDefaultLineEnding();
    }

    private String getDefaultLineEnding() {
        switch (lineEnding) {
            case Unix:
                return UVFConstants.LINE_ENDING_UNIX;
            case Windows:
                return UVFConstants.LINE_ENDING_WINDOWS;
            case Mac:
                return UVFConstants.LINE_ENDING_MAC;
            default:
                return UVFConstants.LINE_ENDING_UNIX;
        }
    }

    private String getFilename(List<OmObservation> observations) {
        Set<String> identifiers = new HashSet<>();
        for (OmObservation o : observations) {
            if (o.getObservationConstellation().isSetIdentifier()) {
                identifiers.add(o.getObservationConstellation().getIdentifier());
            }
        }
        StringBuffer pathBuffer = new StringBuffer();
        if (!identifiers.isEmpty()) {
            for (String identifier : identifiers) {
                pathBuffer.append(identifier).append("_");
            }
            pathBuffer.replace(pathBuffer.lastIndexOf("_"), pathBuffer.length(), "");
        } else {
            pathBuffer.append("_").append(Long.toString(java.lang.System.nanoTime()));
        }
        pathBuffer.append(".uvf");
        return pathBuffer.toString();
    }

    private String makeDateSafe(DateTime dt) {
        return dt.toString().replace(":", "");
    }

    private DateTime checkTimeZone(DateTime time) {
        if (timeZone != null) {
            return time.toDateTime(timeZone);
        }
        return time;
    }

    private List<OmObservation> mergeTotoList(ObservationStream observationStream) throws EncodingException {
        List<OmObservation> observations = new ArrayList<>();
        try {
            ObservationStream merge =
                    observationStream.merge(ObservationMergeIndicator.sameObservationConstellation());
            while (merge.hasNext()) {
                observations.add(merge.next());
            }
        } catch (OwsExceptionReport e) {
            throw new EncodingException(e);
        }
        return observations;
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.unmodifiableSet(SUPPORTED_TYPES);
    }

    @Override
    public MediaType getContentType() {
        return UVFConstants.CONTENT_TYPE_UVF;
    }

    @Override
    public boolean isObservationAndMeasurmentV20Type() {
        return false;
    }

    @Override
    public boolean shouldObservationsWithSameXBeMerged() {
        return true;
    }

    @Override
    public boolean supportsResultStreamingForMergedValues() {
        return false;
    }

    @Override
    public Set<String> getSupportedResponseFormats(String service, String version) {
        if (SUPPORTED_RESPONSE_FORMATS.get(service) != null) {
            if (SUPPORTED_RESPONSE_FORMATS.get(service).get(version) != null) {
                return SUPPORTED_RESPONSE_FORMATS.get(service).get(version);
            }
        }
        return Collections.emptySet();
    }

    @Override
    public Map<String, Set<SupportedType>> getSupportedResponseFormatObservationTypes() {
        return Collections.emptyMap();
    }

    private void handleNotYetImplemented(String name) throws EncodingException {
        throw new EncodingException("Support for '%s' not yet implemented.",
                name);
    }
}
