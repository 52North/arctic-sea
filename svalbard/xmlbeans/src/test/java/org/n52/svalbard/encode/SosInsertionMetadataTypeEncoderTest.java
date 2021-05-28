/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.xmlbeans.XmlOptions;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.sos.SosInsertionMetadata;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.encode.exception.EncodingException;

import net.opengis.sos.x20.SosInsertionMetadataType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class SosInsertionMetadataTypeEncoderTest {

    private SosInsertionMetadata insertionMetadata;

    private SosInsertionMetadataTypeEncoder encoder;

    @BeforeEach
    public void prepare() {
        insertionMetadata = new SosInsertionMetadata();
        insertionMetadata.setObservationTypes(CollectionHelper.list("type-1", "type-2"));
        insertionMetadata.setFeatureOfInterestTypes(CollectionHelper.list("f-type-1", "f-type-2"));
        encoder = new SosInsertionMetadataTypeEncoder();
        encoder.setXmlOptions(() -> new XmlOptions());
    }

    @Test
    public void shouldEncodeObservationTypes() throws EncodingException {
        SosInsertionMetadataType encoded = encoder.encode(insertionMetadata);

        MatcherAssert.assertThat(encoded.getObservationTypeArray().length, Is.is(2));
        List<String> observationTypes = Arrays.asList(encoded.getObservationTypeArray());
        Collections.sort(observationTypes);
        MatcherAssert.assertThat(observationTypes, Matchers.contains("type-1", "type-2"));
    }

    @Test
    public void shouldEncodeFeatureOfInterestTypes() throws EncodingException {

        SosInsertionMetadataTypeEncoder encoder = new SosInsertionMetadataTypeEncoder();
        encoder.setXmlOptions(() -> new XmlOptions());
        SosInsertionMetadataType encoded = encoder.encode(insertionMetadata);

        MatcherAssert.assertThat(encoded.getFeatureOfInterestTypeArray().length, Is.is(2));
        List<String> featureTypes = Arrays.asList(encoded.getFeatureOfInterestTypeArray());
        MatcherAssert.assertThat(featureTypes, Matchers.containsInAnyOrder("f-type-1", "f-type-2"));
    }

}
