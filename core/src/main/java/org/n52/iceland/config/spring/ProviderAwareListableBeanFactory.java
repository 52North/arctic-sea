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

import static java.util.stream.Collectors.toMap;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.inject.Provider;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ProviderAwareListableBeanFactory extends DefaultListableBeanFactory implements Serializable {
    private static final long serialVersionUID = -6826027137321052707L;

    public ProviderAwareListableBeanFactory() {
    }

    public ProviderAwareListableBeanFactory(BeanFactory parentBeanFactory) {
        super(parentBeanFactory);
    }

    @Override
    protected Map<String, Object> findAutowireCandidates(String beanName, Class<?> requiredType,
                                                         DependencyDescriptor descriptor) {

        if (!requiredType.equals(Provider.class)) {
            return super.findAutowireCandidates(beanName, requiredType, descriptor);
        }

        DependencyDescriptor providedDescriptor = new DependencyDescriptor(descriptor);
        providedDescriptor.increaseNestingLevel();
        Class<?> type = providedDescriptor.getDependencyType();
        Set<String> candidates = findAutowireCandidates(beanName, type, providedDescriptor).keySet();
        return candidates.stream()
                .collect(toMap(Function.identity(), name -> new DependencyProvider(descriptor, name)));

    }

    private static class OptionalDependencyDescriptor extends DependencyDescriptor {
        private static final long serialVersionUID = -6623409266686702545L;

        OptionalDependencyDescriptor(DependencyDescriptor original) {
            super(original);
            super.increaseNestingLevel();
        }

        @Override
        public boolean isRequired() {
            return false;
        }
    }

    private class DependencyProvider implements ObjectFactory<Object>, Provider<Object>, Serializable {
        private static final long serialVersionUID = 2498323681896163119L;
        private final DependencyDescriptor descriptor;
        private final boolean optional;
        private final String beanName;

        DependencyProvider(DependencyDescriptor descriptor, String beanName) {
            DependencyDescriptor d = new DependencyDescriptor(descriptor);
            d.increaseNestingLevel();
            this.optional = d.getDependencyType().equals(Optional.class);
            this.descriptor = this.optional ? new OptionalDependencyDescriptor(d) : d;
            this.beanName = beanName;
        }

        @Override
        public Object getObject()
                throws BeansException {
            Object resolved = getBean(beanName);
            //Object resolved = doResolveDependency(this.descriptor, beanName, null, null);
            return this.optional ? Optional.ofNullable(resolved) : Objects.requireNonNull(resolved);
        }

        @Override
        public Object get() throws BeansException {
            return getObject();
        }
    }

}
