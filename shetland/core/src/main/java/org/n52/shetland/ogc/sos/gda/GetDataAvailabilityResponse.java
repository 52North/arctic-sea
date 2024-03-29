/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.sos.gda;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.n52.janmayen.Copyable;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.ows.service.ResponseFormat;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Response of a {@link GetDataAvailabilityRequest}.
 *
 * @author Christian Autermann
 *
 * @since 1.0.0
 */
public class GetDataAvailabilityResponse extends OwsServiceResponse implements ResponseFormat {
    private final List<DataAvailability> dataAvailabilities = new LinkedList<>();

    private String responseFormat;

    private String namespace = GetDataAvailabilityConstants.NS_GDA_20;

    /**
     * Creates a new {@code GetDataAvailabilityResponse} consisting of zero or more {@code DataAvailability}
     * objects.
     *
     * @param dataAvailabilities
     *            the data availabilities
     */
    public GetDataAvailabilityResponse(DataAvailability... dataAvailabilities) {
        super(null, null, GetDataAvailabilityConstants.OPERATION_NAME);
        Collections.addAll(this.dataAvailabilities, dataAvailabilities);
    }

    public GetDataAvailabilityResponse() {
        super(null, null, GetDataAvailabilityConstants.OPERATION_NAME);
    }

    public GetDataAvailabilityResponse(String service, String version) {
        super(service, version, GetDataAvailabilityConstants.OPERATION_NAME);
    }

    public GetDataAvailabilityResponse(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    /**
     * @return the {@code DataAvailabilities}.
     */
    public List<DataAvailability> getDataAvailabilities() {
        return Collections.unmodifiableList(dataAvailabilities);
    }

    /**
     * Sets the {@code DataAvailabilities} of the response.
     *
     * @param dataAvailabilities
     *            the {@code DataAvailabilities}
     * @return this
     */
    public GetDataAvailabilityResponse setDataAvailabilities(
            Collection<? extends DataAvailability> dataAvailabilities) {
        this.dataAvailabilities.clear();
        if (dataAvailabilities != null) {
            this.dataAvailabilities.addAll(dataAvailabilities);
        }
        return this;
    }

    /**
     * Adds a new {@code DataAvailability} to the response.
     *
     * @param dataAvailability
     *            the {@code DataAvailability}.
     * @return this
     */
    public GetDataAvailabilityResponse addDataAvailability(DataAvailability dataAvailability) {
        this.dataAvailabilities.add(dataAvailability);
        return this;
    }

    @Override
    public String getResponseFormat() {
        return Strings.isNullOrEmpty(responseFormat) ? getNamespace() : responseFormat;
    }

    @Override
    public void setResponseFormat(String responseFormat) {
        this.responseFormat = responseFormat;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public GetDataAvailabilityResponse setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    /**
     * Describes the availability of observation with a specified combination of {@code featureOfInterest},
     * {@code observedProperty} and {@code procedure} .
     */
    public static class DataAvailability implements Copyable<DataAvailability> {

        private final ReferenceType featureOfInterest;
        private final ReferenceType observedProperty;
        private final ReferenceType procedure;
        private final TimePeriod phenomenonTime;
        private long count = -1;
        private List<TimeInstant> resultTimes = Lists.newArrayList();
        private ReferenceType offering;
        private FormatDescriptor formatDescriptor;
        private Map<String, NamedValue<?>> metadata = Maps.newHashMap();

        /**
         * Creates a new {@code DataAvailability}.
         *
         * @param procedure
         *            the {@code procedure}
         * @param observedProperty
         *            the {@code observedProperty}
         * @param featureOfInterest
         *            the {@code featureOfInterest}
         * @param offering
         *            the {@code offering}
         * @param phenomenonTime
         *            the {@code phenomenonTime} for which data is available.
         */
        @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
        public DataAvailability(ReferenceType procedure, ReferenceType observedProperty,
                ReferenceType featureOfInterest, ReferenceType offering, TimePeriod phenomenonTime) {
            this.observedProperty = observedProperty;
            this.procedure = procedure;
            this.featureOfInterest = featureOfInterest;
            this.offering = offering;
            this.phenomenonTime = phenomenonTime;
        }

        /**
         * Creates a new {@code DataAvailability}.
         *
         * @param procedure
         *            the {@code procedure}
         * @param observedProperty
         *            the {@code observedProperty}
         * @param featureOfInterest
         *            the {@code featureOfInterest}
         * @param offering
         *            the {@code offering}
         * @param phenomenonTime
         *            the {@code phenomenonTime} for which data is available.
         * @param valueCount
         *            the {@code valueCount} for this combination.
         */
        @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
        public DataAvailability(ReferenceType procedure, ReferenceType observedProperty,
                ReferenceType featureOfInterest, ReferenceType offering, TimePeriod phenomenonTime, long valueCount) {
            this.observedProperty = observedProperty;
            this.procedure = procedure;
            this.featureOfInterest = featureOfInterest;
            this.offering = offering;
            this.phenomenonTime = phenomenonTime;
            this.count = valueCount;
        }

        /**
         * @return the {@code featureOfInterest}
         */
        public ReferenceType getFeatureOfInterest() {
            return featureOfInterest;
        }

        /**
         * @return the {@code observedProperty}
         */
        public ReferenceType getObservedProperty() {
            return observedProperty;
        }

        /**
         * @return the {@code procedure}
         */
        public ReferenceType getProcedure() {
            return procedure;
        }

        /**
         * @return the {@code phenomenonTime} for which data is available.
         */
        @SuppressFBWarnings({ "EI_EXPOSE_REP" })
        public TimePeriod getPhenomenonTime() {
            return phenomenonTime;
        }

        /**
         * @return the {@code count} for this combination.
         */
        public long getCount() {
            return count;
        }

        /**
         * Set the {@code count} for this combination
         *
         * @param count
         *            the count
         *
         * @return this.
         */
        public DataAvailability setCount(long count) {
            this.count = count;
            return this;
        }

        public boolean isSetCount() {
            return count >= 0;
        }

        public DataAvailability setResultTimes(Collection<TimeInstant> resultTimes) {
            this.resultTimes.clear();
            if (resultTimes != null) {
                this.resultTimes.addAll(resultTimes);
            }
            return this;
        }

        public DataAvailability addResultTime(TimeInstant resultTime) {
            getResultTimes().add(resultTime);
            return this;
        }

        public List<TimeInstant> getResultTimes() {
            return Collections.unmodifiableList(resultTimes);
        }

        public boolean isSetResultTime() {
            return CollectionHelper.isNotEmpty(getResultTimes());
        }

        /**
         * @return the offering
         */
        public ReferenceType getOffering() {
            return offering;
        }

        /**
         * @param offering
         *            the offering to set
         */
        public void setOffering(ReferenceType offering) {
            this.offering = offering;
        }

        public String getOfferingString() {
            if (isSetOffering()) {
                return getOffering().getHref();
            }
            return null;
        }

        public boolean isSetOffering() {
            return getOffering() != null && getOffering().isSetHref();
        }

        /**
         * @return the observationTypes
         */
        @SuppressFBWarnings({ "EI_EXPOSE_REP" })
        public FormatDescriptor getFormatDescriptor() {
            return formatDescriptor;
        }

        @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
        public void setFormatDescriptor(FormatDescriptor formatDescriptor) {
            this.formatDescriptor = formatDescriptor;
        }

        public boolean isSetFormatDescriptors() {
            return getFormatDescriptor() != null;
        }

        /**
         * @return the metadata
         */
        public Map<String, NamedValue<?>> getMetadata() {
            return Collections.unmodifiableMap(metadata);
        }

        /**
         * @param metadata
         *            the metadata to set
         * @return this
         */
        public DataAvailability setMetadata(Map<String, NamedValue<?>> metadata) {
            this.metadata.clear();
            if (metadata != null) {
                this.metadata.putAll(metadata);
            }
            return this;
        }

        /**
         * @param key
         *            the key of the metadata
         * @param metadata
         *            the metadata to add
         * @return this
         */
        public DataAvailability addMetadata(String key, NamedValue<?> metadata) {
            this.metadata.put(key, metadata);
            return this;
        }

        public boolean isSetMetadata() {
            return getMetadata() != null && !getMetadata().isEmpty();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof DataAvailability) {
                return hashCode() == o.hashCode();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.procedure, 19, this.observedProperty, 43, this.featureOfInterest, 37,
                    this.offering);
        }

        public boolean sameConstellation(Object o) {
            if (o instanceof DataAvailability) {
                return sameConstellationHashCode() == ((DataAvailability) o).sameConstellationHashCode();
            }
            return false;
        }

        public int sameConstellationHashCode() {
            return Objects.hashCode(this.procedure, 19, this.observedProperty, 43, this.featureOfInterest);
        }

        @Override
        public DataAvailability copy() {
            DataAvailability copy = new DataAvailability(procedure, observedProperty, featureOfInterest, offering,
                    new TimePeriod(phenomenonTime.getStart(), phenomenonTime.getEnd()));
            copy.setOffering(offering);
            copy.setCount(getCount());
            copy.setFormatDescriptor(getFormatDescriptor().copy());
            copy.setMetadata(getMetadata());
            copy.setResultTimes(getResultTimes());
            return copy;
        }

        public boolean merge(DataAvailability toMerge, boolean differentOfferings) {
            if (differentOfferings && sameConstellation(toMerge)) {
                getPhenomenonTime().extendToContain(toMerge.getPhenomenonTime());
                mergeFormatDescriptors(toMerge.getFormatDescriptor());
                return true;
            } else if (equals(toMerge)) {
                getPhenomenonTime().extendToContain(toMerge.getPhenomenonTime());
                mergeFormatDescriptors(toMerge.getFormatDescriptor());
                return true;
            }
            return false;
        }

        private void mergeFormatDescriptors(FormatDescriptor fdToMerge) {
            getFormatDescriptor().getObservationFormatDescriptors()
                    .forEach(fd -> fdToMerge.getObservationFormatDescriptors().stream()
                            .filter(fd2 -> fd.getResponseFormat().equals(fd2.getResponseFormat()))
                            .forEachOrdered(fd2 -> fd.getObservationTypes().addAll(fd2.getObservationTypes())));
        }
    }

    /**
     * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
     * @since 1.0.0
     *
     */
    public static class FormatDescriptor implements Copyable<FormatDescriptor> {
        private ProcedureDescriptionFormatDescriptor procedureDescriptionFormatDescriptor;

        private final Set<ObservationFormatDescriptor> observationFormatDescriptors = new LinkedHashSet<>();

        /**
         * @param procedureDescriptionFormatDescriptor
         *            the {@link ProcedureDescriptionFormatDescriptor}
         * @param observationFormatDescriptors
         *            the {@link ObservationFormatDescriptor}s
         */
        @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
        public FormatDescriptor(ProcedureDescriptionFormatDescriptor procedureDescriptionFormatDescriptor,
                Set<ObservationFormatDescriptor> observationFormatDescriptors) {
            super();
            this.procedureDescriptionFormatDescriptor = procedureDescriptionFormatDescriptor;
            if (observationFormatDescriptors != null) {
                this.observationFormatDescriptors.addAll(observationFormatDescriptors);
            }
        }

        /**
         * @return the procedureDescriptionFormatDescriptor
         */
        @SuppressFBWarnings({ "EI_EXPOSE_REP" })
        public ProcedureDescriptionFormatDescriptor getProcedureDescriptionFormatDescriptor() {
            return procedureDescriptionFormatDescriptor;
        }

        /**
         * @param procedureDescriptionFormatDescriptor
         *            the procedureDescriptionFormatDescriptor to set
         */
        @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
        public void setProcedureDescriptionFormatDescriptor(
                ProcedureDescriptionFormatDescriptor procedureDescriptionFormatDescriptor) {
            this.procedureDescriptionFormatDescriptor = procedureDescriptionFormatDescriptor;
        }

        /**
         * @return the observationFormatDescriptors
         */
        public Set<ObservationFormatDescriptor> getObservationFormatDescriptors() {
            return Collections.unmodifiableSet(observationFormatDescriptors);
        }

        /**
         * @param observationFormatDescriptors
         *            the observationFormatDescriptors to set
         * @return this
         */
        public FormatDescriptor setObservationFormatDescriptors(
                Collection<ObservationFormatDescriptor> observationFormatDescriptors) {
            this.observationFormatDescriptors.clear();
            if (observationFormatDescriptors != null) {
                this.observationFormatDescriptors.addAll(observationFormatDescriptors);
            }
            return this;
        }

        @Override
        public FormatDescriptor copy() {
            return new FormatDescriptor(procedureDescriptionFormatDescriptor,
                    Sets.newHashSet(observationFormatDescriptors));
        }
    }

    /**
     * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
     * @since 1.0.0
     *
     */
    public static class ObservationFormatDescriptor implements Copyable<ObservationFormatDescriptor> {
        private String responseFormat;

        private final Set<String> observationTypes = new LinkedHashSet<>();

        /**
         * @param responseFormat
         *            the response format
         * @param observationTypes
         *            the observation types
         */
        @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
        public ObservationFormatDescriptor(String responseFormat, Collection<String> observationTypes) {
            super();
            this.responseFormat = responseFormat;
            if (observationTypes != null) {
                this.observationTypes.addAll(observationTypes);
            }
        }

        /**
         * @return the responseFormat
         */
        public String getResponseFormat() {
            return responseFormat;
        }

        /**
         * @param responseFormat
         *            the responseFormat to set
         */
        public void setResponseFormat(String responseFormat) {
            this.responseFormat = responseFormat;
        }

        /**
         * @return the observationTypes
         */
        public Set<String> getObservationTypes() {
            return Collections.unmodifiableSet(observationTypes);
        }

        /**
         * @param observationTypes
         *            the observationTypes to set
         * @return this
         */
        public ObservationFormatDescriptor setObservationTypes(Collection<String> observationTypes) {
            this.observationTypes.clear();
            if (observationTypes != null) {
                this.observationTypes.addAll(observationTypes);
            }
            return this;
        }

        @Override
        public ObservationFormatDescriptor copy() {
            return new ObservationFormatDescriptor(responseFormat, Sets.newHashSet(observationTypes));
        }
    }

    /**
     * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
     * @since 1.0.0
     *
     */
    // FIXME: replace with org.n52.shetland.ogc.sos.ProcedureDescriptionFormat
    public static class ProcedureDescriptionFormatDescriptor
            implements Copyable<ProcedureDescriptionFormatDescriptor> {

        private String procedureDescriptionFormat;

        /**
         * @param procedureDescriptionFormat
         *            the procedure desciption format
         */
        public ProcedureDescriptionFormatDescriptor(String procedureDescriptionFormat) {
            super();
            this.procedureDescriptionFormat = procedureDescriptionFormat;
        }

        /**
         * @return the procedureDescriptionFormat
         */
        public String getProcedureDescriptionFormat() {
            return procedureDescriptionFormat;
        }

        /**
         * @param procedureDescriptionFormat
         *            the procedureDescriptionFormat to set
         */
        public void setProcedureDescriptionFormat(String procedureDescriptionFormat) {
            this.procedureDescriptionFormat = procedureDescriptionFormat;
        }

        @Override
        public ProcedureDescriptionFormatDescriptor copy() {
            return new ProcedureDescriptionFormatDescriptor(procedureDescriptionFormat);
        }
    }

}
