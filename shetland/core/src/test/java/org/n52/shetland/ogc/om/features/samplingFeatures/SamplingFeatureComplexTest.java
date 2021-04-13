/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om.features.samplingFeatures;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeatureComplex;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 *
 */
public class SamplingFeatureComplexTest {

    @Test
    public void constructor_should_throw_exception_when_role_is_not_provided_1() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SamplingFeatureComplex(null, new SamplingFeature(new CodeWithAuthority("test-feature")));
        });
    }

    @Test
    public void constructor_should_throw_exception_when_role_is_not_provided_2() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SamplingFeatureComplex("", new SamplingFeature(new CodeWithAuthority("test-feature")));
        });
    }

    @Test
    public void constructor_should_throw_exception_when_feature_is_not_provided_1() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SamplingFeatureComplex("test-role", null);
        });
    }

    @Test
    public void constructor_should_throw_exception_when_feature_is_not_provided_2() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SamplingFeatureComplex("test-role", new SamplingFeature(null));
        });
    }

    @Test
    public void constructor_should_throw_exception_when_feature_is_not_provided_3() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SamplingFeatureComplex(null, new SamplingFeature(new CodeWithAuthority(null)));
        });
    }

    @Test
    public void constructor_should_throw_exception_when_feature_is_not_provided_4() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SamplingFeatureComplex(null, new SamplingFeature(new CodeWithAuthority("")));
        });
    }

    @Test
    public void should_set_role_correct() {
        final String role = "test-role";
        final SamplingFeatureComplex sfc =
                new SamplingFeatureComplex(role, new SamplingFeature(new CodeWithAuthority("test-feature")));

        assertThat(sfc.getRelatedSamplingFeatureRole(), is(role));
    }

    @Test
    public void should_set_relatedSamplingFeature_correct() {
        final SamplingFeature feature = new SamplingFeature(new CodeWithAuthority("test-feature"));
        final SamplingFeatureComplex sfc = new SamplingFeatureComplex("test-role", feature);

        assertThat(sfc.getRelatedSamplingFeature(), is(feature));
    }

}
