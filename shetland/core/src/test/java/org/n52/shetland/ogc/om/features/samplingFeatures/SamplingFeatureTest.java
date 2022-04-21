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
package org.n52.shetland.ogc.om.features.samplingFeatures;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.gml.CodeWithAuthority;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SamplingFeatureTest {

    @Test
    public final void addRelatedSamplingFeature_should_add_a_relatedSamplingFeature() {
        final SamplingFeature feature = new SamplingFeature(null);
        final SamplingFeatureComplex relatedSamplingFeature =
                new SamplingFeatureComplex("test-role", new SamplingFeature(new CodeWithAuthority("test-feature")));
        feature.addRelatedSamplingFeature(relatedSamplingFeature);

        assertThat(feature.isSetRelatedSamplingFeatures(), is(TRUE));
        assertThat(feature.getRelatedSamplingFeatures(), hasSize(1));
        assertThat(feature.getRelatedSamplingFeatures().get(0), is(relatedSamplingFeature));

        final SamplingFeatureComplex relatedSamplingFeature2 =
                new SamplingFeatureComplex("test-role", new SamplingFeature(new CodeWithAuthority("test-feature-2")));
        feature.addRelatedSamplingFeature(relatedSamplingFeature2);

        validate(feature, relatedSamplingFeature, relatedSamplingFeature2);

        feature.addRelatedSamplingFeature(null);

        validate(feature, relatedSamplingFeature, relatedSamplingFeature2);
    }

    @Test
    public final void addAllRelatedSamplingFeatures_should_add_all_elements() {
        final SamplingFeature feature = new SamplingFeature(null);
        final SamplingFeatureComplex relatedSamplingFeature =
                new SamplingFeatureComplex("test-role", new SamplingFeature(new CodeWithAuthority("test-feature")));
        final SamplingFeatureComplex relatedSamplingFeature2 =
                new SamplingFeatureComplex("test-role", new SamplingFeature(new CodeWithAuthority("test-feature-2")));

        List<SamplingFeatureComplex> list = Lists.newArrayList(relatedSamplingFeature, relatedSamplingFeature2);

        feature.addAllRelatedSamplingFeatures(list);

        validate(feature, relatedSamplingFeature, relatedSamplingFeature2);

        final SamplingFeatureComplex relatedSamplingFeature3 =
                new SamplingFeatureComplex("test-role", new SamplingFeature(new CodeWithAuthority("test-feature-3")));
        final SamplingFeatureComplex relatedSamplingFeature4 =
                new SamplingFeatureComplex("test-role", new SamplingFeature(new CodeWithAuthority("test-feature-4")));

        list = Lists.newArrayList(relatedSamplingFeature3, relatedSamplingFeature4);

        feature.addAllRelatedSamplingFeatures(list);

        validate(feature, relatedSamplingFeature, relatedSamplingFeature2, relatedSamplingFeature3,
                relatedSamplingFeature4);
    }

    @Test
    public final void setRelatedSamplingFeatures_should_set_all_elements_and_reset_if_set_before() {
        final SamplingFeature feature = new SamplingFeature(null);
        final SamplingFeatureComplex relatedSamplingFeature =
                new SamplingFeatureComplex("test-role", new SamplingFeature(new CodeWithAuthority("test-feature")));
        final SamplingFeatureComplex relatedSamplingFeature2 =
                new SamplingFeatureComplex("test-role", new SamplingFeature(new CodeWithAuthority("test-feature-2")));

        List<SamplingFeatureComplex> list = Lists.newArrayList(relatedSamplingFeature, relatedSamplingFeature2);

        feature.setRelatedSamplingFeatures(list);

        validate(feature, relatedSamplingFeature, relatedSamplingFeature2);

        final SamplingFeatureComplex relatedSamplingFeature3 =
                new SamplingFeatureComplex("test-role", new SamplingFeature(new CodeWithAuthority("test-feature-3")));
        final SamplingFeatureComplex relatedSamplingFeature4 =
                new SamplingFeatureComplex("test-role", new SamplingFeature(new CodeWithAuthority("test-feature-4")));

        list = Lists.newArrayList(relatedSamplingFeature3, relatedSamplingFeature4);

        feature.setRelatedSamplingFeatures(list);

        validate(feature, relatedSamplingFeature3, relatedSamplingFeature4);
    }

    @Test
    public final void isSetRelatedSamplingFeatures_should_return_false_if_not_set() {
        final SamplingFeature feature = new SamplingFeature(null);
        assertThat(feature.isSetRelatedSamplingFeatures(), is(FALSE));

        feature.addRelatedSamplingFeature(new SamplingFeatureComplex("test-role", new SamplingFeature(
                new CodeWithAuthority("test-feature"))));
        assertThat(feature.isSetRelatedSamplingFeatures(), is(TRUE));

        feature.setRelatedSamplingFeatures(null);
        assertThat(feature.isSetRelatedSamplingFeatures(), is(FALSE));

        feature.setRelatedSamplingFeatures(Lists.<SamplingFeatureComplex> newArrayList());
        assertThat(feature.isSetRelatedSamplingFeatures(), is(FALSE));
    }

    private void validate(final SamplingFeature feature, final SamplingFeatureComplex... relatedSamplingFeatures) {
        assertThat(feature.isSetRelatedSamplingFeatures(), is(TRUE));
        assertThat(feature.getRelatedSamplingFeatures(), hasSize(relatedSamplingFeatures.length));
        for (final SamplingFeatureComplex relatedSamplingFeature : relatedSamplingFeatures) {
            assertThat(feature.getRelatedSamplingFeatures(), hasItem(relatedSamplingFeature));
        }
    }

}
