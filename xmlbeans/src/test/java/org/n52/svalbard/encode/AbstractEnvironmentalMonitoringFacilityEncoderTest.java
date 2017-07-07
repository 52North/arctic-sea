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
package org.n52.svalbard.encode;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.inspire.ef.EnvironmentalMonitoringFacility;
import org.n52.shetland.ogc.gml.ReferenceType;

public abstract class AbstractEnvironmentalMonitoringFacilityEncoderTest {

//    @BeforeClass
//    public static void initSettingsManager() {
//        SettingsManager.getInstance();
//    }
//
//    @AfterClass
//    public static void cleanupSettingManager() {
//        SettingsManager.getInstance().cleanup();
//    }

    protected EnvironmentalMonitoringFacility getEnvironmentalMonitoringFacility() {
        return new EnvironmentalMonitoringFacility(new Identifier("localId", "namespace"), new ReferenceType("mediaMonitored"));
    }
}
