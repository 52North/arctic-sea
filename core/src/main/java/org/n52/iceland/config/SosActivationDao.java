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
package org.n52.iceland.config;

import java.util.Set;

import org.n52.iceland.ogc.sos.SosOfferingExtensionKey;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public interface SosActivationDao extends ActivationDao {

/**
     * Checks if the offering extension is active.
     *
     * @param key
     *            the offering extension key
     *
     * @return if the offering extension is active
     */
    boolean isOfferingExtensionActive(SosOfferingExtensionKey key);

    void setOfferingExtensionStatus(SosOfferingExtensionKey key, boolean active);

    Set<SosOfferingExtensionKey> getOfferingExtensionKeys();
}
