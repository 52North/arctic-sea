/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.statistics.generator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.n52.iceland.statistics.api.mappings.MetadataDataMapping;
import org.n52.iceland.statistics.api.mappings.ServiceEventDataMapping;
import org.n52.iceland.statistics.api.parameters.AbstractEsParameter;
import org.n52.iceland.statistics.api.parameters.Description.InformationOrigin;
import org.n52.iceland.statistics.api.parameters.Description.Operation;
import org.n52.iceland.statistics.generator.formats.MdFormat;

import com.google.common.io.Files;

public class ParameterGenerator {

    private static String outputFilePath = "PARAMETER.MD";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length >= 2) {
            outputFilePath = args[0];
            ParameterGenerator gen = new ParameterGenerator();
            List<Class<?>> processClasses = new ArrayList<>();
            for (int i = 1; i < args.length; i++) {
                processClasses.add(Class.forName(args[i]));
            }
            processClasses.add(MetadataDataMapping.class);
            processClasses.add(ServiceEventDataMapping.class);
            gen.processClass(processClasses);
        } else {
            // throw new IllegalArgumentException(String.format("Usage %s [%s]",
            // "..\\PARAMETERS.MD", "SosDataMapping"));
            System.out.println("Application's DataMapping class is not specified. Add the class name of your data mapping class.");
            System.out.println(String.format("Usage java -jar statistics-kibana %s [%s]", "..\\PARAMETERS.MD", "SosDataMapping"));
        }
    }

    private Map<Operation, Map<InformationOrigin, List<AbstractEsParameter>>> parameters;

    public void processClass(List<Class<?>> classes) throws IOException {
        parameters = new HashMap<>();
        for (Class<?> klass : classes) {
            organize(klass.getFields());
        }
        MdFormat formatter = new MdFormat();
        formatter.setParameters(parameters);
        String printable = formatter.create();
        // System.out.println(printable);
        try {
            Files.write(printable, new File(outputFilePath), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }

    private void organize(Field[] fields) {
        Map<Operation, List<AbstractEsParameter>> mid =
                Arrays.asList(fields).stream().map(this::getFieldValue).filter(l -> l != null && l.getDescription() != null)
                        .collect(Collectors.groupingBy(l -> ((AbstractEsParameter) l).getDescription().getOperation()));

        for (Operation op : mid.keySet()) {
            Map<InformationOrigin, List<AbstractEsParameter>> collect =
                    mid.get(op).stream().collect(Collectors.groupingBy(l -> ((AbstractEsParameter) l).getDescription().getInformationOrigin()));
            parameters.put(op, collect);
        }

    }

    private AbstractEsParameter getFieldValue(Field field) {
        boolean bool = Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers());
        bool = bool && field.getType().isAssignableFrom((AbstractEsParameter.class));
        if (bool) {
            try {
                return (AbstractEsParameter) field.get(null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
