/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.ows;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.n52.iceland.service.operator.ServiceOperatorRepository;
import org.n52.janmayen.i18n.MultilingualString;
import org.n52.shetland.ogc.ows.OwsServiceIdentification;

import java.util.Collections;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;

class OwsServiceIdentificationFactoryTest {

    public static final String SERVICE = "service";

    @Test
    void testNullLocale() {
        ServiceOperatorRepository serviceOperatorRepository = Mockito.mock(ServiceOperatorRepository.class);
        Mockito.when(serviceOperatorRepository.getSupportedVersions(SERVICE))
               .thenReturn(Collections.singleton("1.0.0"));

        OwsServiceIdentificationFactory factory = new OwsServiceIdentificationFactory(SERVICE, serviceOperatorRepository);

        MultilingualString title = new MultilingualString();
        title.addLocalization("en", "Title");
        title.addLocalization("de", "Titel");
        factory.setTitle(title);

        MultilingualString description = new MultilingualString();
        description.addLocalization("en", "Description");
        description.addLocalization("de", "Beschreibung");
        factory.setAbstract(description);

        assertThat(factory.create(null), is(notNullValue()));
        assertThat(factory.create(Locale.ENGLISH), is(notNullValue()));
        assertThat(factory.create(Locale.ENGLISH).getTitle().get().getLocales(), is(Matchers.contains(Locale.ENGLISH)));
        assertThat(factory.create(Locale.GERMAN), is(notNullValue()));
        assertThat(factory.create(Locale.GERMAN).getTitle().get().getLocales(), is(Matchers.contains(Locale.GERMAN)));
        assertThat(factory.create(Locale.CHINESE), is(notNullValue()));
        assertThat(factory.create(Locale.CHINESE).getTitle().get().getLocales(), is(Matchers.contains(Locale.ENGLISH)));
    }
}
