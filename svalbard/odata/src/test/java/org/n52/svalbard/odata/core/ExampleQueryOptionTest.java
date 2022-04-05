/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.svalbard.odata.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests all examples directly referenced in the official STA Standard.
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
@SuppressWarnings("unchecked")
public class ExampleQueryOptionTest extends QueryOptionTests implements TestConstants {

    @Test
    public void testValidExamplesFromSpecSyntax() {
        for (String exampleFunction : EXAMPLE_FUNCTIONS) {
            init(exampleFunction);
            Assertions.assertDoesNotThrow(() -> parser.queryOptions().accept(new STAQueryOptionVisitor()),
                                          "Syntax of " + exampleFunction + "was not recognized");
        }
    }
}
