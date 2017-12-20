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
package org.n52.shetland.ogc.swes;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import org.n52.shetland.ogc.ows.extension.Extensions;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweText;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SwesExtensionsTest {

    private static final String DEFINITION_1 = TestDefinitions.definition1.name();
    private static final String DEFINITION_2 = TestDefinitions.definition2.name();
    private static final String DEFINITION_3= TestDefinitions.definition3.name();
    private static final SweText VALUE_1 = new SweText().setValue("");
    private static final SweCount VALUE_2 = new SweCount().setValue(1);
    private static final SweBoolean VALUE_3 = new SweBoolean().setValue(false);

    @Test
    public void isEmpty_should_return_false_if_extensions_are_null_or_empty() {
        assertThat(new Extensions().isEmpty(), is(TRUE));
    }

    @Test
    public void isEmpty_should_return_false_if_at_least_one_extension_is_set() {
        final Extensions extensions = new Extensions();
        extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_1));
        assertThat(extensions.isEmpty(), is(FALSE));
    }

    @Test
    public void isBooleanExtensionSet_should_return_true_if_set_to_true() {
        final Extensions extensions = new Extensions();
        extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_1).setValue(new SweBoolean().setValue(TRUE)));
        assertThat(extensions.getBooleanExtension(DEFINITION_1), is(TRUE));
    }

   @Test
   public void containsExtension_for_string_schould_return_true() {
       final Extensions extensions = new Extensions();
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_1).setValue(new SweText()));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_2).setValue(new SweText()));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_3).setValue(new SweText()));
       assertThat(extensions.containsExtension(DEFINITION_1), is(TRUE));
       assertThat(extensions.containsExtension(DEFINITION_2), is(TRUE));
       assertThat(extensions.containsExtension(DEFINITION_3), is(TRUE));
   }

   @Test
   public void containsExtension_for_enum_schould_return_true() {
       final Extensions extensions = new Extensions();
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_1).setValue(new SweText()));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_2).setValue(new SweText()));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_3).setValue(new SweText()));
       assertThat(extensions.containsExtension(DEFINITION_1), is(TRUE));
       assertThat(extensions.containsExtension(DEFINITION_2), is(TRUE));
       assertThat(extensions.containsExtension(DEFINITION_3), is(TRUE));
   }

   @Test
   public void containsExtension_for_enum_created_with_string_schould_return_true() {
       final Extensions extensions = new Extensions();
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_1).setValue(new SweText()));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_2).setValue(new SweText()));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_3).setValue(new SweText()));
       assertThat(extensions.containsExtension(DEFINITION_1), is(TRUE));
       assertThat(extensions.containsExtension(DEFINITION_2), is(TRUE));
       assertThat(extensions.containsExtension(DEFINITION_3), is(TRUE));
   }

   @Test
   public void containsExtension_for_string_created_with_enum_schould_return_true() {
       final Extensions extensions = new Extensions();
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_1).setValue(new SweText()));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_2).setValue(new SweText()));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_3).setValue(new SweText()));
       assertThat(extensions.containsExtension(DEFINITION_1), is(TRUE));
       assertThat(extensions.containsExtension(DEFINITION_2), is(TRUE));
       assertThat(extensions.containsExtension(DEFINITION_3), is(TRUE));
   }


   @Test
   public void getExtension_for_string_schould_return_true() {
       final Extensions extensions = new Extensions();
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_1).setValue(VALUE_1));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_2).setValue(VALUE_2));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_3).setValue(VALUE_3));
       assertThat(extensions.getExtension(DEFINITION_1).orElse(null).getValue(), instanceOf(VALUE_1.getClass()));
       assertThat(extensions.getExtension(DEFINITION_2).orElse(null).getValue(), instanceOf(VALUE_2.getClass()));
       assertThat(extensions.getExtension(DEFINITION_3).orElse(null).getValue(), instanceOf(VALUE_3.getClass()));
   }

   @Test
   public void getExtension_for_enum_schould_return_true() {
       final Extensions extensions = new Extensions();
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_1).setValue(VALUE_1));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_2).setValue(VALUE_2));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_3).setValue(VALUE_3));
       assertThat(extensions.getExtension(DEFINITION_1).orElse(null).getValue(), instanceOf(VALUE_1.getClass()));
       assertThat(extensions.getExtension(DEFINITION_2).orElse(null).getValue(), instanceOf(VALUE_2.getClass()));
       assertThat(extensions.getExtension(DEFINITION_3).orElse(null).getValue(), instanceOf(VALUE_3.getClass()));
   }

   @Test
   public void getExtension_for_enum_created_with_string_schould_return_true() {
       final Extensions extensions = new Extensions();
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_1).setValue(VALUE_1));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_2).setValue(VALUE_2));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_3).setValue(VALUE_3));
       assertThat(extensions.getExtension(DEFINITION_1).orElse(null).getValue(), instanceOf(VALUE_1.getClass()));
       assertThat(extensions.getExtension(DEFINITION_2).orElse(null).getValue(), instanceOf(VALUE_2.getClass()));
       assertThat(extensions.getExtension(DEFINITION_3).orElse(null).getValue(), instanceOf(VALUE_3.getClass()));
   }

   @Test
   public void getExtension_for_string_created_with_enum_schould_return_true() {
       final Extensions extensions = new Extensions();
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_1).setValue(VALUE_1));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_2).setValue(VALUE_2));
       extensions.addExtension(new SwesExtension<>().setDefinition(DEFINITION_3).setValue(VALUE_3));
       assertThat(extensions.getExtension(DEFINITION_1).orElse(null).getValue(), instanceOf(VALUE_1.getClass()));
       assertThat(extensions.getExtension(DEFINITION_2).orElse(null).getValue(), instanceOf(VALUE_2.getClass()));
       assertThat(extensions.getExtension(DEFINITION_3).orElse(null).getValue(), instanceOf(VALUE_3.getClass()));
   }
   protected static enum TestDefinitions {
       definition1, definition2, definition3;
   }
}
