/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos.gda;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.ows.service.ResponseFormat;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Response of a {@link GetDataAvailabilityRequest}.
 *
 * @author Christian Autermann
 *
 * @since 4.0.0
 */
public class GetDataAvailabilityResponse extends OwsServiceResponse implements ResponseFormat {
    private final List<DataAvailability> dataAvailabilities = new LinkedList<DataAvailability>();

    private String responseFormat;

    private String namespace = GetDataAvailabilityConstants.NS_GDA_20;

    /**
     * Creates a new {@code GetDataAvailabilityResponse} consisting of zero or
     * more {@code DataAvailability} objects.
     *
     * @param dataAvailabilities
     *            the data availabilities
     */
    public GetDataAvailabilityResponse(DataAvailability... dataAvailabilities) {
        super(null, null, GetDataAvailabilityConstants.OPERATION_NAME);
        Collections.addAll(this.dataAvailabilities, dataAvailabilities);
    }

    public GetDataAvailabilityResponse() {
        super(null,null, GetDataAvailabilityConstants.OPERATION_NAME);
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
     * Adds a new {@code DataAvailability} to the response.
     *
     * @param dataAvailability
     *            the {@code DataAvailability}.
     */
    public void addDataAvailability(DataAvailability dataAvailability) {
        this.dataAvailabilities.add(dataAvailability);
    }

    /**
     * Sets the {@code DataAvailabilities} of the response.
     *
     * @param dataAvailabilities
     *            the {@code DataAvailabilities}
     */
    public void setDataAvailabilities(Collection<? extends DataAvailability> dataAvailabilities) {
        this.dataAvailabilities.clear();
        this.dataAvailabilities.addAll(dataAvailabilities);
    }

    public String getResponseFormat() {
        return responseFormat;
    }

    public void setResponseFormat(String responseFormat) {
        this.responseFormat = responseFormat;
    }

    public boolean isSetResponseFormat() {
        return !Strings.isNullOrEmpty(getResponseFormat());
    }

    public void setNamespace(String namespace) {
        if (!Strings.isNullOrEmpty(namespace)) {
            this.namespace = namespace;
        }
    }

    public String getNamespace() {
        return this.namespace;
    }

    /**
     * Describes the availability of observation with a specified combination of
     * {@code featureOfInterest}, {@code observedProperty} and {@code procedure}
     * .
     */
    public static class DataAvailability {

        private final ReferenceType featureOfInterest;

        private final ReferenceType observedProperty;

        private final ReferenceType procedure;

        private final TimePeriod phenomenonTime;

        private long count = -1;

        private List<TimeInstant> resultTimes = Lists.newArrayList();

        private ReferenceType offering;

        private FormatDescriptor formatDescriptor;

        private Map<String, NamedValue> metadata = Maps.newHashMap();

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
         * @return this.
         */
        public DataAvailability setCount(long count) {
            this.count = count;
            return this;
        }

        public boolean isSetCount() {
            return count >= 0;
        }

        public DataAvailability setResultTimes(List<TimeInstant> resultTimes) {
            if (resultTimes != null) {
                this.resultTimes = resultTimes;
            }
            return this;
        }

        public DataAvailability addResultTime(TimeInstant resultTime) {
            getResultTimes().add(resultTime);
            return this;
        }

        public List<TimeInstant> getResultTimes() {
            return this.resultTimes;
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

        public String getOfferingString() {
            if (isSetOffering()) {
                return getOffering().getHref();
            }
            return null;
        }

        /**
         * @param offering
         *            the offering to set
         */
        public void setOffering(ReferenceType offering) {
            this.offering = offering;
        }

        public boolean isSetOffering() {
            return getOffering() != null && getOffering().isSetHref();
        }

        /**
         * @return the observationTypes
         */
        public FormatDescriptor getFormatDescriptor() {
            return formatDescriptor;
        }

        /**
         * @param observationTypes
         *            the observationTypes to set
         */
        public void setFormatDescriptor(FormatDescriptor formatDescriptor) {
            this.formatDescriptor = formatDescriptor;
        }

        public boolean isSetFormatDescriptors() {
            return getFormatDescriptor() != null;
        }

        /**
         * @return the metadata
         */
        public Map<String, NamedValue> getMetadata() {
            return metadata;
        }

        /**
         * @param metadata
         *            the metadata to set
         */
        public void setMetadata(Map<String, NamedValue> metadata) {
            this.metadata.clear();
            this.metadata.putAll(metadata);
        }

        /**
         * @param metadata
         *            the metadata to add
         */
        public void addMetadata(String key, NamedValue metadata) {
            this.metadata.put(key, metadata);
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

        public DataAvailability clone() {
            DataAvailability dataAvailability = new DataAvailability(procedure, observedProperty, featureOfInterest, offering,
                    new TimePeriod(phenomenonTime.getStart(), phenomenonTime.getEnd()));
            dataAvailability.setOffering(offering);
            dataAvailability.setCount(getCount());
            dataAvailability.setFormatDescriptor(getFormatDescriptor().clone());
            dataAvailability.setMetadata(getMetadata());
            dataAvailability.setResultTimes(getResultTimes());
            return dataAvailability;
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
            for (ObservationFormatDescriptor formatDescriptor : getFormatDescriptor()
                    .getObservationFormatDescriptors()) {
                for (ObservationFormatDescriptor ofdToMerge : fdToMerge.getObservationFormatDescriptors()) {
                    if (formatDescriptor.getResponseFormat().equals(ofdToMerge.getResponseFormat())) {
                        formatDescriptor.getObservationTypes().addAll(ofdToMerge.getObservationTypes());
                    }
                }
            }
        }
    }

    /**
     * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
     * @since 4.4.0
     *
     */
    public static class FormatDescriptor {
        private ProcedureDescriptionFormatDescriptor procedureDescriptionFormatDescriptor;

        private Set<ObservationFormatDescriptor> observationFormatDescriptors;

        /**
         * @param procedureDescriptionFormatDescriptor
         * @param observationFormatDescriptors
         */
        public FormatDescriptor(ProcedureDescriptionFormatDescriptor procedureDescriptionFormatDescriptor,
                Set<ObservationFormatDescriptor> observationFormatDescriptors) {
            super();
            this.procedureDescriptionFormatDescriptor = procedureDescriptionFormatDescriptor;
            this.observationFormatDescriptors = observationFormatDescriptors;
        }

        /**
         * @return the procedureDescriptionFormatDescriptor
         */
        public ProcedureDescriptionFormatDescriptor getProcedureDescriptionFormatDescriptor() {
            return procedureDescriptionFormatDescriptor;
        }

        /**
         * @param procedureDescriptionFormatDescriptor
         *            the procedureDescriptionFormatDescriptor to set
         */
        public void setProcedureDescriptionFormatDescriptor(
                ProcedureDescriptionFormatDescriptor procedureDescriptionFormatDescriptor) {
            this.procedureDescriptionFormatDescriptor = procedureDescriptionFormatDescriptor;
        }

        /**
         * @return the observationFormatDescriptors
         */
        public Set<ObservationFormatDescriptor> getObservationFormatDescriptors() {
            return observationFormatDescriptors;
        }

        /**
         * @param observationFormatDescriptors
         *            the observationFormatDescriptors to set
         */
        public void setObservationFormatDescriptors(Set<ObservationFormatDescriptor> observationFormatDescriptors) {
            this.observationFormatDescriptors.clear();
            this.observationFormatDescriptors.addAll(observationFormatDescriptors);
        }

        public FormatDescriptor clone() {
            return new FormatDescriptor(procedureDescriptionFormatDescriptor,
                    Sets.newHashSet(observationFormatDescriptors));
        }
    }

    /**
     * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
     * @since 4.4.0
     *
     */
    public static class ObservationFormatDescriptor {
        private String responseFormat;

        private Set<String> observationTypes;

        /**
         * @param responseFormat
         * @param observationTypes
         */
        public ObservationFormatDescriptor(String responseFormat, Set<String> observationTypes) {
            super();
            this.responseFormat = responseFormat;
            this.observationTypes = observationTypes;
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
            return observationTypes;
        }

        /**
         * @param observationTypes
         *            the observationTypes to set
         */
        public void setObservationTypes(Set<String> observationTypes) {
            this.observationTypes.clear();
            this.observationTypes.addAll(observationTypes);
        }

        public ObservationFormatDescriptor clone() {
            return new ObservationFormatDescriptor(responseFormat, Sets.newHashSet(observationTypes));
        }
    }

    /**
     * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
     * @since 4.4.0
     *
     */
    public static class ProcedureDescriptionFormatDescriptor {

        private String procedureDescriptionFormat;

        /**
         * @param procedureDescriptionFormat
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

        public ProcedureDescriptionFormatDescriptor clone() {
            return new ProcedureDescriptionFormatDescriptor(procedureDescriptionFormat);
        }
    }

}
