/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.service;

import java.net.URI;
import java.util.Locale;

import org.n52.faroe.ConfigurationError;
import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.i18n.I18NSettings;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.svalbard.CodingSettings;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @deprecated inject settings in classes that actually use the setting
 *
 * @since 1.0.0
 */
@Configurable
@Deprecated
public class ServiceConfiguration implements Constructable {
    private static ServiceConfiguration instance;

    /**
     * character encoding for responses.
     */
    private boolean encodeFullChildrenInDescribeSensor;
    private boolean addOutputsToSensorML;
    private boolean strictSpatialFilteringProfile;
    private boolean validateResponse;
    private boolean useHttpStatusCodesInKvpAndPoxBinding;

    /**
     * URL of this service.
     */
    private String serviceURL;

    /**
     * directory of sensor descriptions in SensorML format.
     */
    private String sensorDirectory;
    private boolean deregisterJdbcDriver;
    private Locale defaultLanguage;
    private boolean showAllLanguageValues;
    private int maxNumberOfReturnedTimeSeries = Integer.MAX_VALUE;
    private int maxNumberOfReturnedValues = Integer.MAX_VALUE;
    private boolean overallExtrema = true;
    private boolean streamingEncoding = true;

    @Override
    public void init() {
        ServiceConfiguration.instance = this;
    }

    @Deprecated
    public String getDefaultOfferingPrefix() {
        return null;
    }

    @Deprecated
    public boolean isEncodeFullChildrenInDescribeSensor() {
        return encodeFullChildrenInDescribeSensor;
    }

    @Deprecated
    public void setEncodeFullChildrenInDescribeSensor(final boolean encodeFullChildrenInDescribeSensor) {
        this.encodeFullChildrenInDescribeSensor = encodeFullChildrenInDescribeSensor;
    }

    @Deprecated
    public boolean isAddOutputsToSensorML() {
        return addOutputsToSensorML;
    }

    @Deprecated
    public void setAddOutputsToSensorML(final boolean addOutputsToSensorML) {
        this.addOutputsToSensorML = addOutputsToSensorML;
    }

    @Deprecated
    public boolean isStrictSpatialFilteringProfile() {
        return strictSpatialFilteringProfile;
    }

    @Deprecated
    public void setStrictSpatialFilteringProfile(final boolean strictSpatialFilteringProfile) {
        this.strictSpatialFilteringProfile = strictSpatialFilteringProfile;
    }

    public boolean isValidateResponse() {
        return validateResponse;
    }

    @Setting(CodingSettings.VALIDATE_RESPONSE)
    public void setValidateResponse(final boolean validateResponse) {
        this.validateResponse = validateResponse;
    }

    @Deprecated
    public boolean isSupportsQuality() {
        return true;
    }

    public boolean isUseHttpStatusCodesInKvpAndPoxBinding() {
        return useHttpStatusCodesInKvpAndPoxBinding;
    }

    @Setting(MiscSettings.HTTP_STATUS_CODE_USE_IN_KVP_POX_BINDING)
    public void setUseHttpStatusCodesInKvpAndPoxBinding(final boolean useHttpStatusCodesInKvpAndPoxBinding) {
        Validation.notNull(MiscSettings.HTTP_STATUS_CODE_USE_IN_KVP_POX_BINDING, useHttpStatusCodesInKvpAndPoxBinding);
        this.useHttpStatusCodesInKvpAndPoxBinding = useHttpStatusCodesInKvpAndPoxBinding;
    }

    @Deprecated
    public String getSensorDir() {
        return sensorDirectory;
    }

    @Deprecated
    public void setSensorDirectory(final String sensorDirectory) {
        this.sensorDirectory = sensorDirectory;
    }

    /**
     * Get service URL.
     *
     * @return the service URL
     */
    public String getServiceURL() {
        return serviceURL;
    }

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(final URI serviceURL) throws ConfigurationError {
        Validation.notNull("Service URL", serviceURL);
        String url = serviceURL.toString();
        if (url.contains("?")) {
            url = url.split("[?]")[0];
        }
        this.serviceURL = url;
    }

    @Deprecated
    public void setDeregisterJdbcDriver(final boolean deregisterJdbcDriver) {
        this.deregisterJdbcDriver = deregisterJdbcDriver;
    }

    @Deprecated
    public boolean isDeregisterJdbcDriver() {
        return deregisterJdbcDriver;
    }

    @Setting(I18NSettings.I18N_DEFAULT_LANGUAGE)
    public void setDefaultLanguage(final String defaultLanguage) {
        Validation.notNullOrEmpty("Default language as three character string", defaultLanguage);
        this.defaultLanguage = new Locale(defaultLanguage);
    }

    public Locale getDefaultLanguage() {
        return defaultLanguage;
    }

    @Setting(I18NSettings.I18N_SHOW_ALL_LANGUAGE_VALUES)
    public void setShowAllLanguageValues(final boolean showAllLanguageValues) {
        this.showAllLanguageValues = showAllLanguageValues;
    }

    public boolean isShowAllLanguageValues() {
        return showAllLanguageValues;
    }

    public boolean isSetDefaultLanguage() {
        return this.defaultLanguage != null;
    }

    @Setting(MiscSettings.HYDRO_MAX_NUMBER_OF_RETURNED_TIME_SERIES)
    public void setMaxNumberOfReturnedTimeSeries(Integer value) {
        this.maxNumberOfReturnedTimeSeries = value;
    }

    public int getMaxNumberOfReturnedTimeSeries() {
        return maxNumberOfReturnedTimeSeries;
    }

    @Setting(MiscSettings.HYDRO_MAX_NUMBER_OF_RETURNED_VALUES)
    public void setMaxNumberOfReturnedValues(Integer value) {
        this.maxNumberOfReturnedValues = value;
    }

    public int getMaxNumberOfReturnedValues() {
        return maxNumberOfReturnedValues;
    }

    @Setting(MiscSettings.RETURN_OVERALL_EXTREMA_FOR_FIRST_LATEST)
    public void setOverallExtrema(boolean overallExtrema) {
        this.overallExtrema = overallExtrema;
    }

    public boolean isOverallExtrema() {
        return overallExtrema;
    }

    @Setting(StreamingSettings.FORCE_STREAMING_ENCODING)
    public void setStreamingEncoding(boolean streamingEncoding) {
        this.streamingEncoding = streamingEncoding;
    }

    public boolean isForceStreamingEncoding() {
        return streamingEncoding;
    }

    /**
     * @return Returns a singleton instance of the ServiceConfiguration.
     *
     * @deprecated use injection
     */
    @Deprecated
    public static synchronized ServiceConfiguration getInstance() {
        return ServiceConfiguration.instance;
    }
}
