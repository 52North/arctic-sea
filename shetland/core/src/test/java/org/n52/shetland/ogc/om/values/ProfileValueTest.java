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
package org.n52.shetland.ogc.om.values;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.ProfileLevel;
import org.n52.shetland.ogc.om.values.ProfileValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.Value;

import com.google.common.collect.Lists;

public class ProfileValueTest {

    private ProfileValue profileValue = createProfileValue(true, true);
    private ProfileValue profileValueFrom = createProfileValue(true, false);
    private ProfileValue profileValueTo = createProfileValue(false, true);

    @Test
    public void testFromLevel() {
        assertThat(profileValue.getFromLevel().getValue(), is(BigDecimal.valueOf(0.0)));
    }

    @Test
    public void testToLevel() {
        assertThat(profileValue.getToLevel().getValue(), is(BigDecimal.valueOf(30.0)));
    }

    @Test
    public void testFromLevel4FromOnly() {
        assertThat(profileValueFrom.getFromLevel().getValue(), is(BigDecimal.valueOf(0.0)));
    }

    @Test
    public void testToLevel4FromOnly() {
        assertThat(profileValueFrom.getToLevel().getValue(), is(BigDecimal.valueOf(20.0)));
    }

    @Test
    public void testFromLevel4ToOnly() {
        assertThat(profileValueTo.getFromLevel().getValue(), is(BigDecimal.valueOf(10.0)));
    }

    @Test
    public void testToLevel4ToOnly() {
        assertThat(profileValueTo.getToLevel().getValue(), is(BigDecimal.valueOf(30.0)));
    }

    private ProfileValue createProfileValue(boolean fromDepth, boolean toDepth) {
        ProfileValue coverage = new ProfileValue("");
        coverage.addValue(createProfileLevel(fromDepth, toDepth, 0.0));
        coverage.addValue(createProfileLevel(fromDepth, toDepth, 10.0));
        coverage.addValue(createProfileLevel(fromDepth, toDepth, 20.0));
        return coverage;
    }

    private ProfileLevel createProfileLevel(boolean fromDepth, boolean toDepth, double start) {
        ProfileLevel profileLevel = new ProfileLevel();
        if (fromDepth) {
            profileLevel.setLevelStart(createQuantity("fromDepth", start, "m"));
        }
        if (toDepth) {
            profileLevel.setLevelEnd(createQuantity("toDepth", start + 10.0, "m"));
        }
        profileLevel.setValue(createProfileLevel());
        return profileLevel;
    }

    private QuantityValue createQuantity(String definition, double value, String unit) {
        QuantityValue quantity = new QuantityValue(value, unit);
        quantity.setValue(value).setUom(unit).setDefinition(definition);
        return quantity;
    }

    private List<Value<?>> createProfileLevel() {
        List<Value<?>> list = Lists.newArrayList();
        CategoryValue category = new CategoryValue("weathered grey brown basalt", "unknown");
        category.setDefinition("http://www.opengis.net/def/gwml/2.0/observedProperty/earthMaterial");
        category.addName(new CodeType("lithology"));
        list.add(category);
        return list;
    }

}
