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
package org.n52.shetland.ogc.sos.response;

import java.util.Map;
import java.util.NoSuchElementException;

import org.n52.shetland.ogc.om.AbstractObservationValue;
import org.n52.shetland.ogc.om.ObservationStream;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.shetland.ogc.ows.OWSConstants.AdditionalRequestParams;
import org.n52.shetland.ogc.ows.exception.CodedException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.exception.ResponseExceedsSizeLimitException;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Maps;

public abstract class AbstractStreaming extends AbstractObservationValue<Value<ObservationStream>>
        implements ObservationStream {
    private final Map<AdditionalRequestParams, Object> additionalRequestParams = Maps.newHashMap();

    private String responseFormat;
    private int maxNumberOfValues = Integer.MIN_VALUE;
    private int currentNumberOfValues = 0;

    @Override
    public abstract OmObservation next() throws NoSuchElementException, OwsExceptionReport;

    @Override
    public abstract boolean hasNext() throws OwsExceptionReport;

    /**
     * Check and modify observation for Spatial Filtering Profile and requested crs
     *
     * @param observation {@link OmObservation} to check
     *
     * @throws OwsExceptionReport If an error occurs when modifying the {@link OmObservation}
     */
    protected abstract void checkForModifications(OmObservation observation) throws OwsExceptionReport;

    public void add(AdditionalRequestParams parameter, Object object) {
        additionalRequestParams.put(parameter, object);
    }

    public boolean contains(AdditionalRequestParams parameter) {
        return additionalRequestParams.containsKey(parameter);
    }

    public boolean isSetAdditionalRequestParams() {
        return CollectionHelper.isNotEmpty(additionalRequestParams);
    }

    protected Object getAdditionalRequestParams(AdditionalRequestParams parameter) {
        return additionalRequestParams.get(parameter);
    }

    @Override
    public boolean isSetValue() {
        return true;
    }

    public void setResponseFormat(String responseFormat) {
        this.responseFormat = responseFormat;
    }

    public String getResponseFormat() {
        return responseFormat;
    }

    /**
     * @return the maxNumberOfValues
     */
    public int getMaxNumberOfValues() {
        return maxNumberOfValues;
    }

    /**
     * @param maxNumberOfValues the maxNumberOfValues to set
     */
    public void setMaxNumberOfValues(int maxNumberOfValues) {
        this.maxNumberOfValues = maxNumberOfValues;
    }

    /**
     * Check if the max number of returned values is exceeded
     *
     * @param size Max number count
     *
     * @throws CodedException If the size limit is exceeded
     */
    protected void checkMaxNumberOfReturnedValues(int size) throws OwsExceptionReport {
        if (getMaxNumberOfValues() > 0) {
            currentNumberOfValues += size;
            if (currentNumberOfValues > getMaxNumberOfValues()) {
                throw new ResponseExceedsSizeLimitException().at("maxNumberOfReturnedValues");
            }
        }
    }

}
