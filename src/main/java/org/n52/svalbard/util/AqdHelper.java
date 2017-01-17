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
package org.n52.svalbard.util;

import java.util.Set;

import org.n52.faroe.ConfigurationError;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.shetland.aqd.AbstractEReportingHeader;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.JavaHelper;
import org.n52.svalbard.EReportingSetting;

import com.google.common.base.Strings;

@Configurable
public class AqdHelper {

    private String namespace;
    private String observationPrefix;
    private Set<Integer> validityFlags;
    private Set<Integer> verificationFlags;

    /**
     * @return the validityFlags
     */
    public Set<Integer> getValidityFlags() {
        return validityFlags;
    }

    /**
     * @param validityFlags the validityFlags to set
     */
    @Setting(EReportingSetting.EREPORTING_VALIDITY_FLAGS)
    public void setValidityFlags(String validityFlags) {
        this.validityFlags = JavaHelper.getIntegerSetFromString(validityFlags);
    }

    public String getEReportingNamespace() {
        return namespace;
    }

    @Setting(EReportingSetting.EREPORTING_NAMESPACE)
    public void setEReportingNamespace(String namespace) throws ConfigurationError {
        this.namespace = namespace;
    }

    public boolean isSetEReportingNamespace() {
        return !Strings.isNullOrEmpty(getEReportingNamespace());
    }

    @Setting(EReportingSetting.EREPORTING_OBSERVATION_PREFIX)
    public void setEReportingObservationPrefix(String observationPrefix) throws ConfigurationError {
        this.observationPrefix = observationPrefix;
    }

    public String getEReportingObservationPrefix() {
        return observationPrefix;
    }

    public boolean isSetEReportingObservationPrefix() {
        return !Strings.isNullOrEmpty(getEReportingObservationPrefix());
    }

    public boolean isSetValidityFlags() {
        return CollectionHelper.isNotEmpty(getValidityFlags());
    }

    /**
     * @return the verificationFlags
     */
    public Set<Integer> getVerificationFlags() {
        return verificationFlags;
    }

    /**
     * @param verificationFlags the verificationFlags to set
     */
    @Setting(EReportingSetting.EREPORTING_VERIFICATION_FLAGS)
    public void setVerificationFlags(String verificationFlags) {
        this.verificationFlags = JavaHelper.getIntegerSetFromString(verificationFlags);
    }

    public boolean isSetVerificationFlags() {
        return CollectionHelper.isNotEmpty(getVerificationFlags());
    }

    public void processObservation(OmObservation observation, TimePeriod timePeriod, TimeInstant resultTime,
                                   FeatureCollection featureCollection, AbstractEReportingHeader eReportingHeader,
                                   int counter) {
        if (observation.isSetPhenomenonTime()) {
            // generate gml:id
            observation.setGmlId(getObservationId(counter));
            // add xlink:href to eReportingHeader.content
            eReportingHeader.addContent((AbstractFeature) new OmObservation()
                    .setIdentifier(new CodeWithAuthority(getObservationXlink(observation.getGmlId()))));
            timePeriod.extendToContain(observation.getPhenomenonTime());
            observation.setResultTime(resultTime);
            featureCollection.addMember(observation);
        }
    }

    public String getObservationXlink(String gmlId) {
        StringBuilder id = new StringBuilder();
        if (isSetEReportingNamespace()) {
            id.append(getEReportingNamespace());
            if (!getEReportingNamespace().endsWith("/")) {
                id.append("/");
            }
        } else {
            id.append("#");
        }
        id.append(gmlId);
        return id.toString();

    }

    public String getObservationId(int counter) {
        return (isSetEReportingObservationPrefix() ? getEReportingObservationPrefix() : "o_")
                .concat(Integer.toString(counter));
    }
}