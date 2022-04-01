/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.om.series.DefaultPointMetadata;
import org.n52.shetland.ogc.om.series.Metadata;
import org.n52.shetland.ogc.om.values.Value;

public abstract class AbstractObservationValue<T extends Value<?>>
        implements ObservationValue<T> {
    private String observationID;
    private String observationType;
    private String observableProperty;
    private String tokenSeparator;
    private String tupleSeparator;
    private String decimalSeparator;
    private String unit;
    private DefaultPointMetadata defaultPointMetadata;
    private Metadata metadata;

    public void setValuesForResultEncoding(OmObservation observation) {
        setObservationID(observation.getObservationID());
        setObservableProperty(observation.getObservationConstellation().getObservableProperty().getIdentifier());
        setObservationType(observation.getObservationConstellation().getObservationType());
        setTokenSeparator(observation.getTokenSeparator());
        setTupleSeparator(observation.getTupleSeparator());
        setDecimalSeparator(observation.getDecimalSeparator());
    }

    /**
     * @return the observationID
     */
    public String getObservationID() {
        return observationID;
    }

    /**
     * @param observationID
     *            the observationID to set
     */
    public void setObservationID(String observationID) {
        this.observationID = observationID;
    }

    public boolean isSetObservationID() {
        return getObservationID() != null && !getObservationID().isEmpty();
    }

    /**
     * @return the observationType
     */
    public String getObservationType() {
        return observationType;
    }

    /**
     * @param observationType
     *            the observationType to set
     */
    private void setObservationType(String observationType) {
        this.observationType = observationType;
    }

    public boolean isSetObservationType() {
        return getObservationType() != null && !getObservationType().isEmpty();
    }

    /**
     * @return the observableProperty
     */
    public String getObservableProperty() {
        return observableProperty;
    }

    /**
     * @param observableProperty
     *            the observableProperty to set
     */
    private void setObservableProperty(String observableProperty) {
        this.observableProperty = observableProperty;
    }

    public boolean isSetObservablePropertyD() {
        return getObservableProperty() != null && !getObservableProperty().isEmpty();
    }

    /**
     * @return the tokenSeparator
     */
    public String getTokenSeparator() {
        return tokenSeparator;
    }

    /**
     * @param tokenSeparator
     *            the tokenSeparator to set
     */
    private void setTokenSeparator(String tokenSeparator) {
        this.tokenSeparator = tokenSeparator;
    }

    public boolean isSetTokenSeparator() {
        return getTokenSeparator() != null && !getTokenSeparator().isEmpty();
    }

    /**
     * @return the tupleSeparator
     */
    public String getTupleSeparator() {
        return tupleSeparator;
    }

    /**
     * @param tupleSeparator
     *            the tupleSeparator to set
     */
    private void setTupleSeparator(String tupleSeparator) {
        this.tupleSeparator = tupleSeparator;
    }

    public boolean isSetTupleSeparator() {
        return getTupleSeparator() != null && !getTupleSeparator().isEmpty();
    }

    /**
     * Get decimal separator
     *
     * @return the decimalSeparator
     */
    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    /**
     * Set decimal separator
     *
     * @param decimalSeparator
     *            the decimalSeparator to set
     */
    public void setDecimalSeparator(final String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    /**
     * Check whether decimal separator is set
     *
     * @return <code>true</code>, if decimal separator is set
     */
    public boolean isSetDecimalSeparator() {

        return getDecimalSeparator() != null && !getDecimalSeparator().isEmpty();
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit
     *            the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isSetUnit() {
        return getUnit() != null && !getUnit().isEmpty();
    }

    @Override
    public boolean isSetDefaultPointMetadata() {
        return defaultPointMetadata != null;
    }

    @Override
    public DefaultPointMetadata getDefaultPointMetadata() {
        return defaultPointMetadata;
    }

    @Override
    public void setDefaultPointMetadata(DefaultPointMetadata defaultPointMetadata) {
        this.defaultPointMetadata = defaultPointMetadata;
    }

    @Override
    public boolean isSetMetadata() {
        return metadata != null;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
