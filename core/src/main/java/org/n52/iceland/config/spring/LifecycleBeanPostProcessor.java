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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import org.n52.janmayen.lifecycle.Constructable;
import org.n52.janmayen.lifecycle.Destroyable;

/**
 * Bean post processor that calls {@link Constructable#init() } and
 * {@link Destroyable#destroy()} for every bean that implements these
 * interfaces. In contrast to the {@link javax.annotation.PostConstruct} and
 * {@link javax.annotation.PreDestroy} annotations, these methods will also be
 * called if they are declared in a super class of the bean.
 *
 * This postprocess will be called at the same stages,
 * {@link CommonAnnotationBeanPostProcessor} would be called:
 * Constructor
 * Bean-Injections
 * Settings-Injections
 * init()
 * ...
 * destroy()
 *
 * @see Constructable
 * @see Destroyable
 *
 * @since 1.0.0
 *
 * @author Christian Autermann
 */
public class LifecycleBeanPostProcessor implements DestructionAwareBeanPostProcessor, PriorityOrdered {
    private static final Logger LOG = LoggerFactory.getLogger(LifecycleBeanPostProcessor.class);

    private int order = Ordered.LOWEST_PRECEDENCE;

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof Constructable) {
            try {
                ((Constructable) bean).init();
            } catch (Throwable t) {
                throw new BeanInitializationException("Couldn't counstruct bean " + beanName, t);
            }
        }
        return bean;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) {
        if (bean instanceof Destroyable) {
            try {
                ((Destroyable) bean).destroy();
            } catch (Throwable t) {
                LOG.error("Couldn't invoke destroy method on " + beanName, t);
            }
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }
}
