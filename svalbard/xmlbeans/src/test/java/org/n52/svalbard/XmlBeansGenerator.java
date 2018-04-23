/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.encode.Encoder;
import org.reflections.Reflections;

public class XmlBeansGenerator {

    @Ignore
    @Test
    public void test() {
        SortedSet<String> set = new TreeSet<>();
        set.addAll(getClasses("org.n52.svalbard.decode", Decoder.class));
        set.addAll(getClasses("org.n52.svalbard.encode", Encoder.class));
        for (String coder : set) {
            System.out.println("    <bean class=\"" + coder + "\"/>");
        }
    }

    private Set<String> getClasses(String packageName, Class<?> clazz) {
        return new Reflections(packageName).getSubTypesOf(clazz).stream()
                .filter(d -> !Modifier.isAbstract(d.getModifiers())).map(d -> d.getName()).collect(Collectors.toSet());
    }
}
