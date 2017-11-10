/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import org.n52.faroe.ConfigurationError;
import org.n52.faroe.SettingsService;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;

/**
 * Bean post processor, that
 * {@linkplain SettingsService#configure(java.lang.Object) configures} beans
 * using a {@link SettingsService} bean. Configuration takes place after all
 * dependencies are injected, but before any initialization methods are called.
 *
 * Note that all beans that are required to create the {@code SettingsService}
 * bean can not be processed.
 *
 * @see Configurable
 * @see Setting
 *
 * @since 1.0.0
 *
 * @author Christian Autermann
 */
public class ConfiguringBeanPostProcessor implements BeanPostProcessor {

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
     * Configures the {@code bean} using the settings service.
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
        try {
            this.settingsService.configure(bean);
        } catch (ConfigurationError t) {
            throw new BeanInitializationException("Couldn't set settings on bean " + beanName, t);
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
