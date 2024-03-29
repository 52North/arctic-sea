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
package org.n52.shetland.ogc.om;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.om.quality.OmResultQuality;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Sets;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Class representing a single value observation value
 *
 * @since 1.0.0
 *
 * @param <T>
 *            value type
 */
public class SingleObservationValue<
        T> extends AbstractObservationValue<Value<T>> {

    /**
     * Phenomenon time
     */
    private Time phenomenonTime;

    /**
     * Measurement value
     */
    private Value<T> value;

    /**
     * Measurment quality
     */
    private Set<OmResultQuality> qualityList = Sets.newHashSet();

    /**
     * constructor
     */
    public SingleObservationValue() {
    }

    /**
     * constructor
     *
     * @param value
     *            Measurement value
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SingleObservationValue(Value<T> value) {
        this.value = value;
    }

    /**
     * constructor
     *
     * @param phenomenonTime
     *            Phenomenon time
     * @param value
     *            Measurement value
     * @param qualityList
     *            Measurment quality
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SingleObservationValue(Time phenomenonTime, Value<T> value, Set<OmResultQuality> qualityList) {
        this.phenomenonTime = phenomenonTime;
        this.value = value;
        this.qualityList = qualityList;
    }

    /**
     * constructor
     *
     * @param phenomenonTime
     *            Phenomenon time
     * @param value
     *            Measurement value
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SingleObservationValue(Time phenomenonTime, Value<T> value) {
        this.phenomenonTime = phenomenonTime;
        this.value = value;
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Time getPhenomenonTime() {
        return phenomenonTime;
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setPhenomenonTime(Time phenomenonTime) {
        this.phenomenonTime = phenomenonTime;
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Value<T> getValue() {
        return value;
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setValue(Value<T> value) {
        this.value = value;
    }

    /**
     * Set measurement quality
     *
     * @param qualityList
     *            Measurement quality to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SingleObservationValue<T> setQualityList(Collection<OmResultQuality> qualityList) {
        this.qualityList.clear();
        if (qualityList != null) {
            this.qualityList.addAll(qualityList);
        }
        return this;
    }

    public SingleObservationValue<T> addQualityList(Collection<OmResultQuality> qualityList) {
        if (qualityList != null) {
            this.qualityList.addAll(qualityList);
        }
        return this;
    }

    public SingleObservationValue<T> addQuality(OmResultQuality qualityList) {
        if (qualityList != null) {
            this.qualityList.add(qualityList);
        }
        return this;
    }

    /**
     * Get measurement quality
     *
     * @return Measurement quality
     */
    public Set<OmResultQuality> getQualityList() {
        return Collections.unmodifiableSet(qualityList);
    }

    public boolean isSetQualityList() {
        return CollectionHelper.isNotEmpty(getQualityList());
    }

    @Override
    public boolean isSetValue() {
        return getValue() != null && getValue().isSetValue();
    }
}
