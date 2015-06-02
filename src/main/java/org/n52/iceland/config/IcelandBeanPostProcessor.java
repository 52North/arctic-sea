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
package org.n52.iceland.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.lifecycle.Destroyable;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 *
 */
public class IcelandBeanPostProcessor
        implements BeanPostProcessor, DestructionAwareBeanPostProcessor {

    private static final Logger LOG = LoggerFactory
            .getLogger(IcelandBeanPostProcessor.class);

    private SettingsManager settingsManager;

    @Required
    @Autowired
    public void setSettingsManager(SettingsManager settingsManager) {
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

        if (bean instanceof Constructable) {
            try {
                ((Constructable) bean).init();
            } catch (Throwable t) {
                throw new BeanInitializationException(
                        "Couldn't counstruct bean " + beanName, t);
            }
        }

        return bean;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName)
            throws BeansException {

        if (bean instanceof Destroyable) {
            try {
                ((Destroyable) bean).destroy();
            } catch (Throwable t) {
                LOG.error("Couldn't invoke destroy method on " + beanName, t);
            }
        }
    }

}
