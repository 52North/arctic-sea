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
package org.n52.janmayen;

import java.util.Set;


public final class ClassHelper {

    private ClassHelper() {
    }

    /**
     * Calculates class similarity based on hierarchy depth.
     *
     * @param superClass the super class
     * @param clazz the class
     * @return 0 for equality, -1 for non-hierarchy classes, &gt;0 the lower the
     *         more similiar
     */
    public static int getSimiliarity(Class<?> superClass, Class<?> clazz) {
        return Classes.getSimiliarity(superClass, clazz);
    }

    public static <T> Set<Class<? extends T>> flattenPartialHierachy(Class<T> limitingClass,
                                                                     Class<? extends T> actualClass) {
        return Classes.flattenPartialHierachy(limitingClass, actualClass);
    }
}
