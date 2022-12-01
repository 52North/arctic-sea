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
package org.n52.iceland.statistics.api;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.n52.faroe.ConfigurationError;
import org.n52.svalbard.encode.exception.EncodingException;

public class ElasticsearchSettingsTest {

    @Test
    public void checkSingleClusterNodes() {
        ElasticsearchSettings s = new ElasticsearchSettings();
        s.setClusterNodes("localhost");
        Assertions.assertEquals(1, s.getClusterNodes().size());

        s.setClusterNodes("localhost:1234");
        Assertions.assertEquals(1, s.getClusterNodes().size());
    }

    @Test
    public void emptyClusterNodesException() {
        assertThrows(ConfigurationError.class, () -> {
            ElasticsearchSettings s = new ElasticsearchSettings();
            s.setClusterNodes("");
        });
    }

    @Test
    public void multipleClusterNodes() {
        ElasticsearchSettings s = new ElasticsearchSettings();
        s.setClusterNodes("picsanadrag.ru,localhost:7894,lofaszjanos:789");
        Assertions.assertEquals(3, s.getClusterNodes().size());
    }

    @Test
    public void multipleClusterNodesException() {
        assertThrows(ConfigurationError.class, () -> {
            ElasticsearchSettings s = new ElasticsearchSettings();
            s.setClusterNodes("abcd,localhost:notnumber,lofaszjanos:789");
        });
    }

}
