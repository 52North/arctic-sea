/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.n52.janmayen.Copyable;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.om.series.DefaultPointMetadata;
import org.n52.shetland.ogc.om.series.Metadata;
import org.n52.shetland.w3c.Nillable;

import com.google.common.base.Objects;

/**
 * @since 1.0.0
 */
public class OmObservationConstellation
        extends AbstractFeature
        implements Copyable<OmObservationConstellation> {

    /**
     * Identifier of the procedure by which the observation is made
     */
    private Nillable<AbstractFeature> procedure = Nillable.<AbstractFeature> nil();

    /**
     * Identifier of the observableProperty to which the observation accords to
     */
    private AbstractPhenomenon observableProperty;

    /**
     * Identifiers of the offerings to which this observation belongs
     */
    private Set<String> offerings;

    /**
     * Identifier of the featureOfInterest to which this observation belongs
     */
    private Nillable<AbstractFeature> featureOfInterest = Nillable.<AbstractFeature> nil();

    /**
     * type of the observation
     */
    private String observationType;

    private DefaultPointMetadata defaultPointMetadata;

    private Metadata metadata;

    /**
     * default constructor
     */
    public OmObservationConstellation() {
        super("");
    }

    /**
     * constructor
     *
     * @param procedure
     *            Procedure by which the observation is made
     * @param observableProperty
     *            observableProperty to which the observation accords to
     * @param featureOfInterest
     *            featureOfInterest to which this observation belongs
     */
    public OmObservationConstellation(
            AbstractFeature procedure, AbstractPhenomenon observableProperty, AbstractFeature featureOfInterest) {
        this(procedure, observableProperty, null, featureOfInterest, null);
    }

    /**
     * constructor
     *
     * @param procedure
     *            Procedure by which the observation is made
     * @param observableProperty
     *            observableProperty to which the observation accords to
     * @param featureOfInterest
     *            featureOfInterest to which this observation belongs
     * @param observationType
     *            the observation type
     */
    public OmObservationConstellation(
            AbstractFeature procedure, AbstractPhenomenon observableProperty, AbstractFeature featureOfInterest,
            String observationType) {
        this(procedure, observableProperty, null, featureOfInterest, observationType);
    }

    /**
     * constructor
     *
     * @param procedure
     *            Procedure by which the observation is made
     * @param observableProperty
     *            observableProperty to which the observation accords to
     * @param offerings
     *            offering to which this observation belongs
     * @param featureOfInterest
     *            featureOfInterest to which this observation belongs
     * @param observationType
     *            the observation type
     */
    public OmObservationConstellation(
            AbstractFeature procedure, AbstractPhenomenon observableProperty, Set<String> offerings,
            AbstractFeature featureOfInterest, String observationType) {
        super("");
        this.procedure = Nillable.of(procedure);
        this.observableProperty = observableProperty;
        this.offerings = offerings;
        this.featureOfInterest = Nillable.of(featureOfInterest);
        this.observationType = observationType;
    }

    /**
     * constructor
     *
     * @param procedure
     *            Procedure by which the observation is made
     * @param observableProperty
     *            observableProperty to which the observation accords to
     * @param featureOfInterest
     *            featureOfInterest to which this observation belongs
     * @param offerings
     *            offering to which this observation belongs
     */
    public OmObservationConstellation(
            AbstractFeature procedure, AbstractPhenomenon observableProperty, AbstractFeature featureOfInterest,
            Set<String> offerings) {
        this(procedure, observableProperty, featureOfInterest);
        this.offerings = offerings;
    }

    /**
     * Get the procedure
     *
     * @return the procedure
     */
    public AbstractFeature getProcedure() {
        return procedure.isPresent() ? procedure.get() : null;
    }

    /**
     * Get the procedure
     *
     * @return the procedure
     */
    public Nillable<AbstractFeature> getNillableProcedure() {
        return procedure;
    }

    public String getProcedureIdentifier() {
        return procedure.isPresent() ? procedure.get().getIdentifier() : null;
    }

    /**
     * Set the procedure
     *
     * @param procedure
     *            the procedure to set
     *
     * @return this
     */
    public OmObservationConstellation setProcedure(AbstractFeature procedure) {
        if (procedure == null) {
            return setProcedure(Nillable.<AbstractFeature> nil());
        }
        return setProcedure(Nillable.of(procedure));
    }

    /**
     * Set the procedure
     *
     * @param procedure
     *            the procedure to set
     * @return this
     */
    public OmObservationConstellation setProcedure(Nillable<AbstractFeature> procedure) {
        this.procedure = procedure;
        return this;
    }

    /**
     * Get observableProperty
     *
     * @return the observableProperty
     */
    public AbstractPhenomenon getObservableProperty() {
        return observableProperty;
    }

    /**
     * Set observableProperty
     *
     * @param observableProperty
     *            the observableProperty to set
     *
     * @return this
     */
    public OmObservationConstellation setObservableProperty(AbstractPhenomenon observableProperty) {
        this.observableProperty = observableProperty;
        return this;
    }

    public String getObservablePropertyIdentifier() {
        return getObservableProperty().getIdentifier();
    }

    /**
     * Get offering
     *
     * @return the offering
     */
    public Set<String> getOfferings() {
        return offerings;
    }

    /**
     * Set offering
     *
     * @param offerings
     *            the offering to set
     *
     * @return this
     */
    public OmObservationConstellation setOfferings(Set<String> offerings) {
        this.offerings = offerings;
        return this;
    }

    public OmObservationConstellation setOfferings(List<String> offerings) {
        if (this.offerings == null) {
            this.offerings = new HashSet<>(offerings.size());
        }
        this.offerings.addAll(offerings);
        return this;
    }

    public OmObservationConstellation addOffering(String offering) {
        if (offerings == null) {
            offerings = new HashSet<>(1);
        }
        offerings.add(offering);
        return this;
    }

    /**
     * Get featureOfInterest
     *
     * @return the featureOfInterest
     */
    public AbstractFeature getFeatureOfInterest() {
        return featureOfInterest.isPresent() ? featureOfInterest.get() : null;
    }

    /**
     * Get featureOfInterest
     *
     * @return the featureOfInterest
     */
    public Nillable<AbstractFeature> getNillableFeatureOfInterest() {
        return featureOfInterest;
    }

    /**
     * Set featureOfInterest
     *
     * @param featureOfInterest
     *            the featureOfInterest to set
     * @return this
     */
    public OmObservationConstellation setFeatureOfInterest(AbstractFeature featureOfInterest) {
        if (featureOfInterest == null) {
            return setFeatureOfInterest(Nillable.<AbstractFeature> nil());
        }
        return setFeatureOfInterest(Nillable.of(featureOfInterest));
    }

    /**
     * Set featureOfInterest
     *
     * @param featureOfInterest
     *            the featureOfInterest to set
     * @return this
     */
    public OmObservationConstellation setFeatureOfInterest(Nillable<AbstractFeature> featureOfInterest) {
        this.featureOfInterest = featureOfInterest;
        return this;
    }

    public String getFeatureOfInterestIdentifier() {
        return featureOfInterest.isPresent() ? featureOfInterest.get().getIdentifier() : null;
    }

    /**
     * Get observation type
     *
     * @return the observationType
     */
    public String getObservationType() {
        return observationType;
    }

    /**
     * Set observation type
     *
     * @param observationType
     *            the observationType to set
     *
     * @return this
     */
    public OmObservationConstellation setObservationType(String observationType) {
        this.observationType = observationType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OmObservationConstellation) {
            return hashCode() == o.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.procedure, 19, this.observableProperty, this.offerings, 43,
                this.featureOfInterest);
    }

    /**
     * TODO change if currently not supported types could be merged.
     *
     * @return <code>true</code>, if the observation can be merged
     */
    public boolean checkObservationTypeForMerging() {
        return isSetObservationType() && !OmConstants.OBS_TYPE_SWE_ARRAY_OBSERVATION.equals(observationType)
                && !OmConstants.OBS_TYPE_COMPLEX_OBSERVATION.equals(observationType)
                && !OmConstants.OBS_TYPE_OBSERVATION.equals(observationType)
                && !OmConstants.OBS_TYPE_UNKNOWN.equals(observationType);
    }

    public boolean isSetObservationType() {
        return observationType != null && !observationType.isEmpty();
    }

    public boolean isSetOfferings() {
        return offerings != null && !offerings.isEmpty();
    }

    public boolean isSetProcedure() {
        return getNillableProcedure() != null && getNillableProcedure().isPresent();
    }

    public boolean isSetFeatureOfInterest() {
        return getNillableFeatureOfInterest() != null && getNillableFeatureOfInterest().isPresent();
    }

    @Override
    public OmObservationConstellation copy() {
        OmObservationConstellation copy = new OmObservationConstellation();
        copy.setFeatureOfInterest(getFeatureOfInterest());
        copy.setObservableProperty(getObservableProperty());
        copy.setObservationType(getObservationType());
        copy.setOfferings(new HashSet<>(getOfferings()));
        copy.setProcedure(getProcedure());
        copy.setIdentifier(this.getIdentifier());
        copy.setName(this.getName());
        copy.setDescription(this.getDescription());
        return copy;
    }

    public boolean isEmpty() {
        return !isSetOfferings() && !hasProcedure() && !hasObservableProperty() && hasFeatureOfInterest();
    }

    private boolean hasFeatureOfInterest() {
        return getFeatureOfInterest() != null && getFeatureOfInterest().isSetIdentifier();
    }

    private boolean hasObservableProperty() {
        return getObservableProperty() != null && getObservableProperty().isSetIdentifier();
    }

    private boolean hasProcedure() {
        return getProcedure() != null && getProcedure().isSetIdentifier();
    }

    public boolean isSetDefaultPointMetadata() {
        return defaultPointMetadata != null;
    }

    public DefaultPointMetadata getDefaultPointMetadata() {
        return defaultPointMetadata;
    }

    public void setDefaultPointMetadata(DefaultPointMetadata defaultPointMetadata) {
        this.defaultPointMetadata = defaultPointMetadata;
    }

    public boolean isSetMetadata() {
        return metadata != null;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("OmObservationConstellation [");
        builder.append("procedure=").append(getProcedure().getIdentifierCodeWithAuthority());
        builder.append(", observableProperty=").append(getObservableProperty().getIdentifierCodeWithAuthority());
        builder.append(", featureOfInterest=").append(getFeatureOfInterest().getIdentifierCodeWithAuthority());
        if (isSetOfferings()) {
            builder.append(", offerings=[");
            boolean first = true;
            for (String offering : getOfferings()) {
                if (!first) {
                    builder.append(", ");
                }
                first = false;
                builder.append(offering);
            }
            builder.append("]");
        }
        if (isSetObservationType()) {
            builder.append(", observationType=").append(getObservationType());
        }
        builder.append("]");
        return builder.toString();
    }
}
