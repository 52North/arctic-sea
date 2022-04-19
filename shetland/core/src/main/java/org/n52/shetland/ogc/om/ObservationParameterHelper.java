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

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.om.values.TextValue;

public interface ObservationParameterHelper<T extends AbstractFeature> {

    ParameterHolder getParameterHolder();

    /**
     * Check whether category parameter is set
     *
     * @return <code>true</code>, if category parameter is set
     */
    default boolean isSetCategoryParameter() {
        return getParameterHolder().hasParameter(OmConstants.PARAMETER_NAME_CATEGORY);
    }

    /**
     * Remove category parameter
     */
    default void removeCategoryParameter() {
        if (isSetCategoryParameter()) {
            removeParameter(getCategoryParameter());
        }
    }

    /**
     * Add category to observation
     *
     * @param category
     *            The category to set
     * @return this
     */
    default T addCategoryParameter(String category) {
        return addCategoryParameter(new TextValue(category));
    }

    default T addCategoryParameter(TextValue category) {
        return addCategoryParameter(new NamedValue<String>(new ReferenceType(OmConstants.PARAMETER_NAME_CATEGORY),
                category));
    }

    default T addCategoryParameter(NamedValue<String> categoryParameter) {
        getParameterHolder().addParameter(categoryParameter);
        return (T) this;
    }

    /**
     * Get category parameter
     *
     * @return category parameter
     */
    default NamedValue<String> getCategoryParameter() {
        if (getParameterHolder().isSetParameter()) {
            for (NamedValue<?> namedValue : getParameterHolder().getParameter()) {
                if (namedValue.getName().getHref().equalsIgnoreCase(OmConstants.PARAMETER_NAME_CATEGORY)) {
                    return (NamedValue<String>) namedValue;
                }
            }
        }
        return null;
    }

    /**
     * Check whether platform parameter is set
     *
     * @return <code>true</code>, if platform parameter is set
     */
    default boolean isSetPlatformParameter() {
        return getParameterHolder().hasParameter(OmConstants.PARAMETER_NAME_PLATFORM);
    }

    /**
     * Remove platform parameter
     */
    default void removePlatformParameter() {
        if (isSetPlatformParameter()) {
            removeParameter(getPlatformParameter());
        }
    }

    /**
     * Add platform to observation
     *
     * @param platform
     *            The platform to set
     * @return this
     */
    default T addPlatformParameter(String platform) {
        return addPlatformParameter(new TextValue(platform));
    }

    default T addPlatformParameter(TextValue platform) {
        return addPlatformParameter(new NamedValue<String>(new ReferenceType(OmConstants.PARAMETER_NAME_PLATFORM),
                platform));
    }

    default T addPlatformParameter(NamedValue<String> platformParameter) {
        getParameterHolder().addParameter(platformParameter);
        return (T) this;
    }

    /**
     * Get platform parameter
     *
     * @return platform parameter
     */
    default NamedValue<String> getPlatformParameter() {
        if (getParameterHolder().isSetParameter()) {
            for (NamedValue<?> namedValue : getParameterHolder().getParameter()) {
                if (namedValue.getName().getHref().equalsIgnoreCase(OmConstants.PARAMETER_NAME_PLATFORM)) {
                    return (NamedValue<String>) namedValue;
                }
            }
        }
        return null;
    }

    /**
     * Remove parameter from list
     *
     * @param parameter
     *            Parameter to remove
     */
    default void removeParameter(NamedValue<?> parameter) {
        getParameterHolder().removeParameter(parameter);
    }
}
