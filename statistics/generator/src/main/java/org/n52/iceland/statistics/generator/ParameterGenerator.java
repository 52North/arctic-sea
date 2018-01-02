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
package org.n52.iceland.statistics.generator;

import static java.util.stream.Collectors.groupingBy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.n52.iceland.statistics.api.mappings.MetadataDataMapping;
import org.n52.iceland.statistics.api.mappings.ServiceEventDataMapping;
import org.n52.iceland.statistics.api.parameters.AbstractEsParameter;
import org.n52.iceland.statistics.api.parameters.Description.InformationOrigin;
import org.n52.iceland.statistics.api.parameters.Description.Operation;
import org.n52.iceland.statistics.generator.formats.MdFormat;

import com.google.common.io.Files;

public class ParameterGenerator {

    public void processClass(String filePath, List<Class<?>> classes) throws IOException {
        MdFormat formatter = new MdFormat();
        formatter.setParameters(getParameters(classes));
        String printable = formatter.create();
        // System.out.println(printable);
        try {
            Files.write(printable, new File(filePath), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Map<Operation, Map<InformationOrigin, List<AbstractEsParameter>>> getParameters(List<Class<?>> classes) {
        return classes.stream()
                .map(Class::getFields)
                .flatMap(Arrays::stream)
                .map(this::getFieldValue)
                .filter(Objects::nonNull)
                .filter(AbstractEsParameter::hasDescription)
                .collect(groupingBy(l -> l.getDescription().getOperation(),
                                    groupingBy(l -> l.getDescription().getInformationOrigin())));
    }

    private AbstractEsParameter getFieldValue(Field field) {
        if (Modifier.isFinal(field.getModifiers()) &&
            Modifier.isStatic(field.getModifiers()) &&
            Modifier.isPublic(field.getModifiers()) &&
            field.getType().isAssignableFrom(AbstractEsParameter.class)) {
            try {
                return (AbstractEsParameter) field.get(null);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     *
     * Generate the parameters.
     *
     * @param args the arguments
     *
     * @throws Exception if an error occurs
     */
    //CHECKSTYLE:OFF
    public static void main(String[] args) throws Exception {
        if (args.length >= 2) {
            String outputFilePath = args[0];
            ParameterGenerator gen = new ParameterGenerator();
            List<Class<?>> processClasses = new ArrayList<>(args.length + 1);
            for (int i = 1; i < args.length; i++) {
                processClasses.add(Class.forName(args[i]));
            }
            processClasses.add(MetadataDataMapping.class);
            processClasses.add(ServiceEventDataMapping.class);
            gen.processClass(outputFilePath, processClasses);
        } else {
            // throw new IllegalArgumentException(String.format("Usage %s [%s]",
            // "..\\PARAMETERS.MD", "SosDataMapping"));
            System.out.println("Application's DataMapping class is not specified. " +
                               "Add the class name of your data mapping class.");
            System.out.printf("Usage java -jar statistics-kibana %s [%s]\n", "..\\PARAMETERS.MD", "SosDataMapping");
        }
    }
    //CHECKSTYLE:ON
}
