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
package org.n52.iceland.config.spring;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.BeanPostProcessor;

import org.n52.iceland.config.SettingsService;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 *
 */
public class ConfiguringBeanPostProcessor implements BeanPostProcessor {

    private SettingsService settingsManager;

    @Required
    @Autowired
    public void setSettingsManager(SettingsService settingsManager) {
        this.settingsManager = settingsManager;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        try {
            this.settingsManager.configure(bean);
        } catch (Throwable t) {
            throw new BeanInitializationException(
                    "Couldn't set settings on bean " + beanName, t);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

}
