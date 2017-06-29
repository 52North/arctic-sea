/*
 * Copyright 2017 52°North Initiative for Geospatial Open Source
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
package org.n52.faroe.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that should be applied to a method that takes a single Setting as a parameter. The parameter of this
 * method should be of the same type as the type of the associated {@code org.n52.faroe.SettingDefinition}.
 *
 * It is needed to apply the {@code Configurable} annotation to a class with a method annotated with this annotations
 * for the {@code SettingsManager} to recognize it.
 *
 * <b>Example usage:</b>
 *
 * <pre>
 * &#064;Setting(MiscellaneousSettingDefinitions.TOKEN_SEPERATOR_KEY)
 * public void setTokenSeperator(String separator) {
 *     this.separator = separator;
 * }
 * </pre>
 *
 * @see Configurable
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Setting {

    /**
     * The key of the setting.
     *
     * @return the key
     */
    String value();

    /**
     * If this setting is required.
     *
     * @return if it is required
     */
    boolean required() default true;
}
