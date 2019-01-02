/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.inspire;

import java.util.Collection;
import java.util.Set;

import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Sets;

/**
 * Service internal representation of INSPIRE supported CRS
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class InspireSupportedCRS implements InspireObject {

    /* element DefaultCRS 1..1 */
    private String defaultCRS;

    /* element otherCRS 0..* */
    private Set<String> otherCRS = Sets.newHashSet();

    /**
     * constructor
     *
     * @param defaultCRS
     *            the mandatory default CRS
     */
    public InspireSupportedCRS(String defaultCRS) {
        setDefaultCRS(defaultCRS);
    }

    /**
     * Get the default CRS
     *
     * @return the defaultCRS
     */
    public String getDefaultCRS() {
        return defaultCRS;
    }

    /**
     * Set the default CRS
     *
     * @param defaultCRS
     *            the defaultCRS to set
     */
    private void setDefaultCRS(String defaultCRS) {
        this.defaultCRS = defaultCRS;
    }

    /**
     * Get the other CRSs
     *
     * @return the other CRS
     */
    public Set<String> getOtherCRS() {
        return otherCRS;
    }

    /**
     * Set the other CRSs, clears the existing collection
     *
     * @param otherCRS
     *            the otherCRS to set
     * @return this
     */
    public InspireSupportedCRS setOtherCRS(Collection<String> otherCRS) {
        getOtherCRS().clear();
        if (CollectionHelper.isNotEmpty(otherCRS)) {
            getOtherCRS().addAll(otherCRS);
        }
        return this;
    }

    /**
     * Add the other CRS
     *
     * @param otherCRS
     *            the other CRS to add
     * @return this
     */
    public InspireSupportedCRS addOtherCRS(String otherCRS) {
        getOtherCRS().add(otherCRS);
        return this;
    }

    /**
     * Check if other CRSs are set
     *
     * @return <code>true</code>, if other CRSs are set
     */
    public boolean isSetSupportedCRSs() {
        return CollectionHelper.isNotEmpty(getOtherCRS());
    }

    @Override
    public String toString() {
        return String.format("%s %n[%n defaultCRS=%s,%n otherCRS=%s%n]", this.getClass().getSimpleName(),
                getDefaultCRS(), CollectionHelper.collectionToString(getOtherCRS()));
    }

}
