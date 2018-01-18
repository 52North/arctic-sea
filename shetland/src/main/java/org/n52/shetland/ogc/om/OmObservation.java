/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.time.IndeterminateValue;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.quality.OmResultQuality;
import org.n52.shetland.ogc.om.values.NilTemplateValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.ogc.swe.SweDataArray;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.locationtech.jts.geom.Geometry;

/**
 * Class represents a SOS/O&M observation
 *
 * @since 1.0.0
 */
public class OmObservation
        extends AbstractFeature {

    /**
     * ID of this observation; in the standard 52n SOS PostgreSQL database, this
     * is implemented through a sequence type.
     */
    private String observationID;

    /**
     * result time of the observation.
     */
    private TimeInstant resultTime;

    /**
     * valid time of the observation.
     */
    private TimePeriod validTime;

    /**
     * constellation of procedure, obervedProperty, offering and
     * observationType.
     */
    private OmObservationConstellation observationConstellation;

    /**
     * type of the value or the result the value points to.
     */
    private String resultType;

    /**
     * O&M parameter.
     */
    private final ParameterHolder parameterHolder = new ParameterHolder();

    /**
     * Map with observation values for each obsservableProeprty.
     */
    private ObservationValue<?> value;

    /**
     * token separator for the value tuples contained in the result element of
     * the generic observation.
     */
    private String tokenSeparator;

    /**
     * no data value for the values contained in the result element.
     */
    private String noDataValue;

    /**
     * separator of value tuples, which are contained in the resulte element.
     */
    private String tupleSeparator;

    /**
     * separator of decimal values, which are contained in the resulte element.
     */
    private String decimalSeparator;

    /**
     * Measurment quality.
     */
    private Set<OmResultQuality> qualityList = Sets.newHashSet();

    private String additionalMergeIndicator;

    private String seriesType;

    private final Set<OmObservationContext> relatedObservations = new HashSet<>();

    /**
     * constructor.
     */
    public OmObservation() {
        this("");
    }

    /**
     * constructor.
     *
     * @param identifier
     *            Feature identifier
     */
    public OmObservation(String identifier) {
        super(identifier);
    }

    /**
     * constructor.
     *
     * @param identifier
     *            Feature identifier
     */
    public OmObservation(CodeWithAuthority identifier) {
        super(identifier);
    }

    /**
     * constructor.
     *
     * @param identifier
     *            Feature identifier
     * @param gmlId
     *            GML id
     */
    public OmObservation(CodeWithAuthority identifier, String gmlId) {
        super(identifier, gmlId);
    }

    /**
     * Get the observation constellation.
     *
     * @return the observationConstellation
     */
    public OmObservationConstellation getObservationConstellation() {
        return observationConstellation;
    }

    /**
     * Set the observation constellation.
     *
     * @param observationConstellation
     *            the observationConstellation to set
     */
    public OmObservation setObservationConstellation(OmObservationConstellation observationConstellation) {
        this.observationConstellation = observationConstellation;
        return this;
    }

    /**
     * Get observation ID.
     *
     * @return the observationID
     */
    public String getObservationID() {
        return observationID;
    }

    /**
     * Set observation ID.
     *
     * @param observationID
     *            the observationID to set
     */
    public void setObservationID(final String observationID) {
        this.observationID = observationID;
    }

    /**
     * Get phenomenon time.
     *
     * @return the phenomenonTime
     */
    public Time getPhenomenonTime() {
        if (isSetValue()) {
            return value.getPhenomenonTime();
        }
        return null;
    }

    public boolean isSetPhenomenonTime() {
        return getPhenomenonTime() != null && !getPhenomenonTime().isEmpty();
    }

    /**
     * Get result time.
     *
     * @return the resultTime
     */
    public TimeInstant getResultTime() {
        return resultTime;
    }

    /**
     * Set result time.
     *
     * @param resultTime
     *            the resultTime to set
     */
    public void setResultTime(final TimeInstant resultTime) {
        this.resultTime = resultTime;
    }

    /**
     * Get valid time.
     *
     * @return the validTime
     */
    public TimePeriod getValidTime() {
        return validTime;
    }

    /**
     * Set valid time.
     *
     * @param validTime
     *            the validTime to set
     */
    public void setValidTime(final TimePeriod validTime) {
        this.validTime = validTime;
    }

    /**
     * Get result type.
     *
     * @return the resultType
     */
    public String getResultType() {
        return resultType;
    }

    /**
     * Set result type.
     *
     * @param resultType
     *            the resultType to set
     */
    public void setResultType(final String resultType) {
        this.resultType = resultType;
    }

    /**
     * Get token separator.
     *
     * @return the tokenSeparator
     */
    public String getTokenSeparator() {
        return tokenSeparator;
    }

    /**
     * Set token separator.
     *
     * @param tokenSeparator
     *            the tokenSeparator to set
     */
    public void setTokenSeparator(final String tokenSeparator) {
        this.tokenSeparator = tokenSeparator;
    }

    /**
     * Get noData value.
     *
     * @return the noDataValue
     */
    public String getNoDataValue() {
        return noDataValue;
    }

    /**
     * Set noData value.
     *
     * @param noDataValue
     *            the noDataValue to set
     */
    public void setNoDataValue(final String noDataValue) {
        this.noDataValue = noDataValue;
    }

    /**
     * Get tuple separator.
     *
     * @return the tupleSeparator
     */
    public String getTupleSeparator() {
        return tupleSeparator;
    }

    /**
     * Set tuple separator.
     *
     * @param tupleSeparator
     *            the tupleSeparator to set
     */
    public void setTupleSeparator(final String tupleSeparator) {
        this.tupleSeparator = tupleSeparator;
    }

    /**
     * Get decimal separator.
     *
     * @return the decimalSeparator
     */
    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    /**
     * Set decimal separator.
     *
     * @param decimalSeparator
     *            the decimalSeparator to set
     */
    public void setDecimalSeparator(final String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    /**
     * Get observation values.
     *
     * @return the values
     */
    public ObservationValue<?> getValue() {
        return value;
    }

    /**
     * Set observation values.
     *
     * @param value
     *            the values to set
     */
    public void setValue(final ObservationValue<?> value) {
        this.value = value;
    }

    public boolean isSetValue() {
        return getValue() != null && getValue().isSetValue();
    }

    /**
     * Merge this observation with passed observation.
     *
     * @param sosObservation
     *            Observation to merge
     */
    public void mergeWithObservation(final OmObservation sosObservation) {
        mergeValues(sosObservation.getValue());
        mergeResultTimes(sosObservation);
    }

    /**
     * Merge this observation with passed observation.
     *
     * @param observationValue
     *            Observation to merge
     */
    public void mergeWithObservation(ObservationValue<?> observationValue) {
        mergeValues(observationValue);
    }

    private void mergeObservationValues(OmObservation merged, OmObservation observation) {
        mergeValues(merged, observation);
        mergeResultTimes(merged, observation);
    }

    private void mergeValues(OmObservation merged, OmObservation observation) {
        SweDataArray combinedValue = (SweDataArray) merged.getValue().getValue().getValue();
        SweDataArray v = (SweDataArray) observation.getValue().getValue().getValue();
        if (v.isSetValues()) {
            combinedValue.addAll(v.getValues());
        }
    }

    /**
     * Merge observation values with passed observation values.
     *
     * @param observationValue
     *            Observation to merge
     */
    private void mergeValues(final ObservationValue<?> observationValue) {
        TVPValue tvpValue;
        if (getValue() instanceof SingleObservationValue) {
            tvpValue = convertSingleValueToMultiValue((SingleObservationValue<?>) value);
        } else {
            tvpValue = (TVPValue) ((MultiObservationValues<?>) value).getValue();
        }
        if (observationValue instanceof SingleObservationValue) {
            final SingleObservationValue<?> singleValue = (SingleObservationValue<?>) observationValue;
            if (!(singleValue.getValue() instanceof NilTemplateValue)) {
                tvpValue.addValue(new TimeValuePair(singleValue.getPhenomenonTime(), singleValue.getValue()));
            }
        } else if (observationValue instanceof MultiObservationValues) {
            tvpValue.addValues(((TVPValue) ((MultiObservationValues<?>) observationValue).getValue()).getValue());
        }
    }

    /**
     * Merge result time with passed observation result time
     *
     * @param sosObservation
     *            Observation to merge
     * @param merged
     *            the observation to merge into
     */
    private void mergeResultTimes(OmObservation merged, OmObservation sosObservation) {
        if (merged.isSetResultTime() && sosObservation.isSetResultTime()) {
            if (merged.getResultTime().getValue().isBefore(sosObservation.getResultTime().getValue())) {
                merged.setResultTime(sosObservation.getResultTime());
            }
        } else if (!merged.isSetResultTime() && sosObservation.isSetResultTime()) {
            merged.setResultTime(sosObservation.getResultTime());
        }
    }

    /**
     * Merge result time with passed observation result time.
     *
     * @param sosObservation
     *            Observation to merge
     */
    private void mergeResultTimes(final OmObservation sosObservation) {
        if (isSetResultTime() && sosObservation.isSetResultTime()) {
            if (getResultTime().getValue().isBefore(sosObservation.getResultTime().getValue())) {
                resultTime = sosObservation.getResultTime();
            }
        } else if (!isSetResultTime() && sosObservation.isSetResultTime()) {
            resultTime = sosObservation.getResultTime();
        }
    }

    /**
     * Convert {@link SingleObservationValue} to {@link TVPValue}.
     *
     * @param singleValue
     *            Single observation value
     *
     * @return Converted TVPValue value
     */
    public TVPValue convertSingleValueToMultiValue(final SingleObservationValue<?> singleValue) {
        MultiObservationValues<List<TimeValuePair>> multiValue = new MultiObservationValues<>();
        TVPValue tvpValue = new TVPValue();
        if (singleValue.isSetUnit()) {
            tvpValue.setUnit(singleValue.getUnit());
        } else if (singleValue.getValue().isSetUnit()) {
            tvpValue.setUnit(singleValue.getValue().getUnit());
        }
        if (singleValue.isSetMetadata()) {
            multiValue.setMetadata(singleValue.getMetadata());
        }
        if (singleValue.isSetDefaultPointMetadata()) {
            multiValue.setDefaultPointMetadata(singleValue.getDefaultPointMetadata());
        }
        TimeValuePair timeValuePair = new TimeValuePair(singleValue.getPhenomenonTime(), singleValue.getValue());
        tvpValue.addValue(timeValuePair);
        multiValue.setValue(tvpValue);
        value = multiValue;
        return tvpValue;
    }

    /**
     * Check whether observation id is set.
     *
     * @return <code>true</code>, if observation id is set
     */
    public boolean isSetObservationID() {
        return getObservationID() != null && !getObservationID().isEmpty();
    }

    /**
     * Check whether tuple separator is set.
     *
     * @return <code>true</code>, if tuple separator is set
     */
    public boolean isSetTupleSeparator() {
        return getTupleSeparator() != null && !getTupleSeparator().isEmpty();
    }

    /**
     * Check whether token separator is set.
     *
     * @return <code>true</code>, if token separator is set
     */
    public boolean isSetTokenSeparator() {
        return getTokenSeparator() != null && !getTokenSeparator().isEmpty();
    }

    /**
     * Check whether decimal separator is set.
     *
     * @return <code>true</code>, if decimal separator is set
     */
    public boolean isSetDecimalSeparator() {
        return getDecimalSeparator() != null && !getDecimalSeparator().isEmpty();
    }

    /**
     * Check whether result time is set.
     *
     * @return <code>true</code>, if result time is set
     */
    public boolean isSetResultTime() {
        return resultTime != null && resultTime.isSetValue();
    }

    /**
     * Check whether result time is template is set.
     *
     * @return <code>true</code>, if result time is template is set
     */
    public boolean isTemplateResultTime() {
        return isSetResultTime() && (getResultTime().isIndeterminateValueEqualTo(IndeterminateValue.TEMPLATE)
                || getResultTime().isNilReasonEqualTo(Time.NilReason.template));
    }

    /**
     * Check whether result type is set.
     *
     * @return <code>true</code>, if result type is set
     */
    public boolean isSetResultType() {
        return getResultType() != null && !getResultType().isEmpty();
    }

    /**
     * Check whether valid time is set.
     *
     * @return <code>true</code>, if valid time is set
     */
    public boolean isSetValidTime() {
        return validTime != null && !validTime.isEmpty();
    }

    /**
     * Get parameter
     *
     * @return the parameter
     */
    public Collection<NamedValue<?>> getParameter() {
        return parameterHolder.getParameter();
    }

    /**
     * Set parameter
     *
     * @param parameter
     *            the parameter to set
     */
    public void setParameter(Collection<NamedValue<?>> parameter) {
        this.parameterHolder.addParameter(parameter);
    }

    /**
     * Add parameter
     *
     * @param namedValue
     *            the namedValue to add to parameter
     */
    public void addParameter(NamedValue<?> namedValue) {
        parameterHolder.addParameter(namedValue);
    }

    public ParameterHolder getParameterHolder() {
        return parameterHolder;
    }

    /**
     * Check whether parameter is set
     *
     * @return <code>true</code>, if parameter is set
     */
    public boolean isSetParameter() {
        return parameterHolder != null && CollectionHelper.isNotEmpty(getParameter());
    }

    /**
     * Check whether spatial filtering profile parameter is set
     *
     * @return <code>true</code>, if spatial filtering profile parameter is set
     */
    public boolean isSetSpatialFilteringProfileParameter() {
        return parameterHolder.isSetSpatialFilteringProfileParameter();
    }

    /**
     * Remove spatial filtering profile parameter
     */
    public void removeSpatialFilteringProfileParameter() {
        if (isSetSpatialFilteringProfileParameter()) {
            removeParameter(getSpatialFilteringProfileParameter());
        }
    }

    /**
     * Remove parameter from list
     *
     * @param parameter
     *            Parameter to remove
     */
    public void removeParameter(NamedValue<?> parameter) {
        getParameterHolder().removeParameter(parameter);
    }

    /**
     * Add sampling geometry to observation
     *
     * @param samplingGeometry
     *            The sampling geometry to set
     * @return this
     */
    public OmObservation addSpatialFilteringProfileParameter(Geometry samplingGeometry) {
        parameterHolder.addSpatialFilteringProfileParameter(samplingGeometry);
        return this;
    }

    /**
     * Get spatial filtering profile parameter
     *
     * @return Spatial filtering profile parameter
     */
    public NamedValue<Geometry> getSpatialFilteringProfileParameter() {
        return parameterHolder.getSpatialFilteringProfileParameter();
    }

    /**
     * Check whether height parameter is set
     *
     * @return <code>true</code>, if height parameter is set
     */
    public boolean isSetHeightParameter() {
        return parameterHolder.isSetHeightParameter();
    }

    /**
     * Get height parameter
     *
     * @return Height parameter
     */
    public NamedValue<BigDecimal> getHeightParameter() {
        return parameterHolder.getHeightParameter();
    }

    /**
     * Check whether depth parameter is set.
     *
     * @return <code>true</code>, if depth parameter is set
     */
    public boolean isSetDepthParameter() {
        return parameterHolder.isSetDepthParameter();
    }

    /**
     * Get depth parameter
     *
     * @return Depth parameter
     */
    public NamedValue<BigDecimal> getDepthParameter() {
        return parameterHolder.getDepthParameter();
    }

    public boolean isSetHeightDepthParameter() {
        return parameterHolder.isSetHeightDepthParameter();
    }

    public NamedValue<BigDecimal> getHeightDepthParameter() {
        return parameterHolder.getHeightDepthParameter();
    }

    public OmObservation cloneTemplate() {
        return cloneTemplate(new OmObservation());
    }

    public OmObservation cloneTemplate(boolean withIdentifierNameDesription) {
        OmObservation clonedTemplate = cloneTemplate(new OmObservation());
        if (withIdentifierNameDesription) {
            if (this.getObservationConstellation().isSetIdentifier()) {
                clonedTemplate.setIdentifier(this.getObservationConstellation().getIdentifier());
                clonedTemplate.setName(this.getObservationConstellation().getName());
                clonedTemplate.setDescription(this.getObservationConstellation().getDescription());
            } else {
                clonedTemplate.setIdentifier(this.getIdentifier());
                clonedTemplate.setName(this.getName());
                clonedTemplate.setDescription(this.getDescription());
            }
        }
        return clonedTemplate;
    }

    protected OmObservation cloneTemplate(OmObservation clone) {
        clone.setObservationConstellation(this.getObservationConstellation());
        clone.setMetaDataProperty(this.getMetaDataProperty());
        if (this.getParameter() != null) {
            clone.setParameter(Sets.newTreeSet(this.getParameter()));
        }
        clone.setRelatedObservations(this.getRelatedObservations());
        clone.setResultType(this.getResultType());
        clone.setTokenSeparator(this.getTokenSeparator());
        clone.setTupleSeparator(this.getTupleSeparator());
        clone.setDecimalSeparator(this.getDecimalSeparator());
        clone.setSeriesType(this.getSeriesType());
        return clone;
    }

    public OmObservation copyTo(OmObservation copyOf) {
        super.copyTo(copyOf);
        copyOf.setObservationID(getObservationID());
        copyOf.setResultTime(getResultTime());
        copyOf.setValidTime(getValidTime());
        copyOf.setObservationConstellation(getObservationConstellation());
        copyOf.setResultType(getResultType());
        copyOf.setParameter(getParameter());
        copyOf.setValue(getValue());
        copyOf.setTokenSeparator(getTokenSeparator());
        copyOf.setNoDataValue(getNoDataValue());
        copyOf.setTupleSeparator(getTupleSeparator());
        copyOf.setDecimalSeparator(getDecimalSeparator());
        copyOf.setResultQuality(getResultQuality());
        copyOf.setRelatedObservations(getRelatedObservations());
        copyOf.setAdditionalMergeIndicator(getAdditionalMergeIndicator());
        return copyOf;
    }

    @Override
    public String getGmlId() {
        if (Strings.isNullOrEmpty(super.getGmlId()) && isSetObservationID()) {
            setGmlId("o_" + getObservationID());
        }
        return super.getGmlId();
    }

    /**
     * Set result quality.
     *
     * @param qualityList
     *            Result quality to set
     *
     * @return {@code this}
     */
    public OmObservation setResultQuality(Set<OmResultQuality> qualityList) {
        this.qualityList = qualityList;
        return this;
    }

    public OmObservation addResultQuality(Set<OmResultQuality> qualityList) {
        this.qualityList.addAll(qualityList);
        return this;
    }

    public OmObservation addResultQuality(OmResultQuality qualityList) {
        this.qualityList.add(qualityList);
        return this;
    }

    /**
     * Get result quality.
     *
     * @return Result quality
     */
    public Set<OmResultQuality> getResultQuality() {
        return qualityList;
    }

    public boolean isSetResultQuality() {
        return CollectionHelper.isNotEmpty(getResultQuality());
    }

    /**
     * Get related observations
     *
     * @return the relatedObservations
     */
    public Set<OmObservationContext> getRelatedObservations() {
        return relatedObservations;
    }

    /**
     * Set related observations
     *
     * @param relatedObservations
     *            the relatedObservations to set
     */
    public void setRelatedObservations(Set<OmObservationContext> relatedObservations) {
        this.relatedObservations.clear();
        this.relatedObservations.addAll(relatedObservations);
    }

    /**
     * Add related observations
     *
     * @param relatedObservations
     *            the relatedObservations to set
     */
    public void addRelatedObservations(Set<OmObservationContext> relatedObservations) {
        this.relatedObservations.addAll(relatedObservations);
    }

    /**
     * Add a related observation
     *
     * @param relatedObservation
     *            the relatedObservation to add
     */
    public void addRelatedObservation(OmObservationContext relatedObservation) {
        this.relatedObservations.add(relatedObservation);
    }

    /**
     * Check if related observations are set
     *
     * @return <code>true</code>, if related observations are set
     */
    public boolean isSetRelatedObservations() {
        return CollectionHelper.isNotEmpty(getRelatedObservations());
    }

    public OmObservation setAdditionalMergeIndicator(String additionalMergeIndicator) {
        this.additionalMergeIndicator = additionalMergeIndicator;
        return this;
    }

    public String getAdditionalMergeIndicator() {
        return additionalMergeIndicator;
    }

    public boolean isSetAdditionalMergeIndicator() {
        return getAdditionalMergeIndicator() != null && !getAdditionalMergeIndicator().isEmpty();
    }

    /**
     * @return the seriesType
     */
    public String getSeriesType() {
        return seriesType;
    }

    /**
     * @param seriesType
     *            the seriesType to set
     */
    public void setSeriesType(String seriesType) {
        this.seriesType = seriesType;
    }

    public boolean isSetSeriesType() {
        return !Strings.isNullOrEmpty(getSeriesType());
    }

    public boolean checkForMerge(OmObservation observation) {
        return checkForMerge(observation, ObservationMergeIndicator.sameObservationConstellation());
    }

    public boolean checkForMerge(OmObservation observation, ObservationMergeIndicator indicator) {
        return checkMergeIndicator(observation) && checkObservationTypeForMerging(observation)
                && checkProcedure(indicator, observation) && checkOfferings(indicator, observation)
                && checkFeatureOfInterest(indicator, observation) && checkObservableProperty(indicator, observation)
                && checkPhenomenonTime(indicator, observation) && checkResultTime(indicator, observation)
                && checkSamplingGeometry(indicator, observation);
    }

    /**
     * TODO change if currently not supported types could be merged.
     *
     * @param observation
     *            the observation
     *
     * @return <code>true</code>, if the observation can be merged
     */
    private boolean checkObservationTypeForMerging(OmObservation observation) {
        OmObservationConstellation oc = observation.getObservationConstellation();
        return oc.isSetObservationType() && !OmConstants.OBS_TYPE_SWE_ARRAY_OBSERVATION.equals(oc.getObservationType())
                && !OmConstants.OBS_TYPE_COMPLEX_OBSERVATION.equals(oc.getObservationType())
                && !OmConstants.OBS_TYPE_OBSERVATION.equals(oc.getObservationType())
                && !OmConstants.OBS_TYPE_UNKNOWN.equals(oc.getObservationType());
    }

    private boolean checkProcedure(ObservationMergeIndicator indicator, OmObservation observation) {
        return !indicator.isProcedure() || getObservationConstellation().getProcedure()
                .equals(observation.getObservationConstellation().getProcedure());
    }

    private boolean checkOfferings(ObservationMergeIndicator indicator, OmObservation observation) {
        return !indicator.isOfferings() || getObservationConstellation().getOfferings()
                .equals(observation.getObservationConstellation().getOfferings());
    }

    private boolean checkFeatureOfInterest(ObservationMergeIndicator indicator, OmObservation observation) {
        return !indicator.isFeatureOfInterest() || getObservationConstellation().getFeatureOfInterest()
                .equals(observation.getObservationConstellation().getFeatureOfInterest());
    }

    private boolean checkObservableProperty(ObservationMergeIndicator indicator, OmObservation observation) {
        return !indicator.isObservableProperty() || getObservationConstellation().getObservableProperty()
                .equals(observation.getObservationConstellation().getObservableProperty());
    }

    private boolean checkPhenomenonTime(ObservationMergeIndicator indicator, OmObservation observation) {
        return !indicator.isPhenomenonTime() || getPhenomenonTime().equals(observation.getPhenomenonTime());
    }

    private boolean checkResultTime(ObservationMergeIndicator indicator, OmObservation observation) {
        return !indicator.isSetResultTime() || getResultTime().equals(observation.getResultTime());
    }

    private boolean checkSamplingGeometry(ObservationMergeIndicator indicator, OmObservation observation) {
        return !indicator.isSamplingGeometry()
                || (isSetSpatialFilteringProfileParameter() && observation.isSetSpatialFilteringProfileParameter()
                        && getSpatialFilteringProfileParameter().getValue().getValue()
                                .equals(observation.getSpatialFilteringProfileParameter().getValue().getValue()));
    }

    private boolean checkMergeIndicator(OmObservation observation) {
        return Objects.equals(getAdditionalMergeIndicator(), observation.getAdditionalMergeIndicator());
    }

}
