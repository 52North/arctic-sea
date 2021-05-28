/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.iceland.util;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public class ValidatableBigIntegers {

    @Test
    public void validPositive() {
        PositiveBigInteger pbi = new PositiveBigInteger("52");
        assertThat(pbi.intValue(), is(equalTo(52)));
    }

    @Test
    public void validNonNegative() {
        NonNegativeBigInteger nnbi = new NonNegativeBigInteger("52");
        assertThat(nnbi.intValue(), is(equalTo(52)));

        nnbi = new NonNegativeBigInteger("0");
        assertThat(nnbi.intValue(), is(equalTo(0)));
    }

    @Test
    public void validNonNegativeZero() {
        NonNegativeBigInteger nnbi = new NonNegativeBigInteger("0");
        assertThat(nnbi.intValue(), is(equalTo(0)));
    }

    @Test
    public void invalidPositive() {
        assertThrows(IllegalArgumentException.class, () -> {
            PositiveBigInteger pbi = new PositiveBigInteger("-1");
        });
    }

    @Test
    public void invalidPositiveZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            PositiveBigInteger pbi = new PositiveBigInteger("0");
        });
    }

    @Test
    public void invalidNonNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            NonNegativeBigInteger nnbi = new NonNegativeBigInteger("-1");
        });
    }

}
