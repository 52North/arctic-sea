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
package org.n52.iceland.config.spring;

import javax.inject.Inject;

import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingsService;
import org.n52.faroe.annotation.Setting;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Bean post processor, that
 * {@linkplain SettingsService#addSetting(org.n52.faroe.SettingDefinition) adds} beans
 * to the {@linkplain SettingsService}.
 *
 * @see Setting
 *
 * @since 7.6.0
 *
 * @author Carsten Hollmann
 */
public class SettingsBeanPostProcessor implements BeanPostProcessor {

    private SettingsService settingsService;

    /**
     * Sets the {@code SettingsManager} used to configure the beans.
     *
     * @param settingsService the settings service
     */
    @Inject
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    /**
     * Adds the {@code bean} using the settings service.
     *
     * @param bean     the bean instance
     * @param beanName the bean name
     *
     * @return the bean
     *
     * @throws BeanInitializationException if the configuration fails
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof SettingDefinition<?>) {
            this.settingsService.addSetting((SettingDefinition<?>) bean);
        }
        return bean;
    }

    /**
     * Noop implementation, will just return {@code bean}.
     *
     * @param bean     the bean instance
     * @param beanName the bean name
     *
     * @return {@code bean}
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}