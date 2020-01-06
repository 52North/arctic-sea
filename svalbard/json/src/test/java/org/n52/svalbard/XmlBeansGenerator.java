/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.encode.Encoder;

public class XmlBeansGenerator extends AbstractXmlBeansGenerator {

    @Test
    public void test() {
        SortedSet<String> set = new TreeSet<>();
        set.addAll(getClasses("org.n52.svalbard.decode.json", Decoder.class));
        set.addAll(getClasses("org.n52.svalbard.encode.json", Encoder.class));
        printBeans(set);
    }


}
