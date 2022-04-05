/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.iceland.statistics.impl;

import javax.inject.Inject;

import org.n52.iceland.statistics.api.interfaces.StatisticsServiceEventResolver;
import org.n52.iceland.statistics.impl.resolvers.CountingOutputStreamEventResolver;
import org.n52.iceland.statistics.impl.resolvers.DefaultServiceEventResolver;
import org.n52.iceland.statistics.impl.resolvers.ExceptionEventResolver;
import org.n52.iceland.statistics.impl.resolvers.OutgoingResponseEventResolver;
import org.springframework.context.ApplicationContext;

public class StatisticsResolverFactory {

    @Inject
    private ApplicationContext ctx;

    // prototype instance dependencies
    public <T extends StatisticsServiceEventResolver<?>> T getPrototypeBean(Class<T> resolverClass) {
        return ctx.getBean(resolverClass);
    }

    public ExceptionEventResolver getExceptionEventResolver() {
        return ctx.getBean(ExceptionEventResolver.class);
    }

    public DefaultServiceEventResolver getDefaultServiceEventResolver() {
        return ctx.getBean(DefaultServiceEventResolver.class);
    }

    public OutgoingResponseEventResolver getOutgoingResponseEventResolver() {
        return ctx.getBean(OutgoingResponseEventResolver.class);
    }

    public CountingOutputStreamEventResolver getCountingOutputstreamEventResolver() {
        return ctx.getBean(CountingOutputStreamEventResolver.class);
    }

}
