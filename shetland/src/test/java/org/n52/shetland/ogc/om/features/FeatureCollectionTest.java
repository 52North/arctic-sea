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
package org.n52.shetland.ogc.om.features;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class FeatureCollectionTest {

    @Test
    public final void should_remove_member_from_feature_collection() {
        final FeatureCollection features = new FeatureCollection();
        final String feature1Id = "feature-1";
        final SamplingFeature feature1 = new SamplingFeature(new CodeWithAuthority(feature1Id));
        features.addMember(feature1);
        final String feature2Id = "feature-2";
        final SamplingFeature feature2 = new SamplingFeature(new CodeWithAuthority(feature2Id));
        features.addMember(feature2);

        final SamplingFeature removedFeature = (SamplingFeature) features.removeMember(feature2Id);

        assertThat(removedFeature, is(equalTo(feature2)));
        assertThat(features.getMembers().size(), is(1));
        assertThat(features.getMembers().containsKey(feature2Id), is(FALSE));
        assertThat(features.getMembers().containsValue(feature2), is(FALSE));
        assertThat(features.getMembers().containsKey(feature1Id), is(TRUE));
        assertThat(features.getMembers().containsValue(feature1), is(TRUE));
    }

}
