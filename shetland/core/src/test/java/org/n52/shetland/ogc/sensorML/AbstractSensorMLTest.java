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
package org.n52.shetland.ogc.sensorML;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import org.n52.shetland.ogc.sensorML.v20.PhysicalComponent;

import com.google.common.collect.Lists;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class AbstractSensorMLTest {
    @Test
    public void testAddKeywords() {
        AbstractSensorML sml = new PhysicalComponent();
        sml.setKeywords(Lists.newArrayList("a", "b"));
        assertThat(sml.getKeywords(), hasItems("a", "b"));
        sml.addKeywords(Lists.newArrayList("c", "d"));
        assertThat(sml.getKeywords(), hasItems("a", "b", "c", "d"));
    }

    @Test
    public void testSetKeywords() {
        AbstractSensorML sml = new PhysicalComponent();
        sml.setKeywords(Lists.newArrayList("a", "b"));
        assertThat(sml.getKeywords(), hasItems("a", "b"));
        sml.setKeywords(Lists.newArrayList("c", "d"));
        assertThat(sml.getKeywords(), hasItems("c", "d"));
    }
}
