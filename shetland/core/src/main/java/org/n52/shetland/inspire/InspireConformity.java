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
package org.n52.shetland.inspire;

/**
 * Service internal representation of INSPIRE conformity
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class InspireConformity {

    private InspireConformityCitation inspireSpecification;

    private InspireDegreeOfConformity inspireDegreeOfConformity;

    /**
     * constructor
     *
     * @param citation
     *            the specification {@link InspireConformityCitation}
     * @param degreeOfConformity
     *            the {@link InspireDegreeOfConformity}
     */
    public InspireConformity(InspireConformityCitation citation, InspireDegreeOfConformity degreeOfConformity) {
        setInspireSpecification(citation);
        setInspireDegreeOfConformity(degreeOfConformity);
    }

    /**
     * Get the specification
     *
     * @return the inspireSpecification
     */
    public InspireConformityCitation getInspireSpecification() {
        return inspireSpecification;
    }

    /**
     * Set the specification
     *
     * @param inspireSpecification
     *            the inspireSpecification to set
     */
    private void setInspireSpecification(InspireConformityCitation inspireSpecification) {
        this.inspireSpecification = inspireSpecification;
    }

    /**
     * Check if the specification is set
     *
     * @return <code>true</code>, if the specification is set
     */
    public boolean isSetInspireSpecification() {
        return getInspireSpecification() != null;
    }

    /**
     * set the degree of conformity
     *
     * @param inspireDegreeOfConformity
     *            the degree of conformity to set
     */
    private void setInspireDegreeOfConformity(InspireDegreeOfConformity inspireDegreeOfConformity) {
        this.inspireDegreeOfConformity = inspireDegreeOfConformity;
    }

    /**
     * Get the degree of conformity
     *
     * @return the degree of conformity
     */
    public InspireDegreeOfConformity getInspireDegreeOfConformity() {
        return inspireDegreeOfConformity;
    }

    /**
     * Enum of degree of conformity values
     *
     * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
     * @since 1.0.0
     *
     */
    public enum InspireDegreeOfConformity {
        conformant, notConformant, notEvaluated;
    }

}
