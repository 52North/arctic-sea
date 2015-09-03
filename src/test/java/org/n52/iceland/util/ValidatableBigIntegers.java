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
package org.n52.iceland.util;

import java.security.InvalidParameterException;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Daniel Nüst <d.nuest@52north.org>
 */
public class ValidatableBigIntegers {

    @Test
    public void validPositive() {
        PositiveBigInteger pbi = new PositiveBigInteger("52");
        Assert.assertThat(pbi.intValue(), is(equalTo(52)));
    }
    
    @Test
    public void validNonNegative() {
        NonNegativeBigInteger nnbi = new NonNegativeBigInteger("52");
        Assert.assertThat(nnbi.intValue(), is(equalTo(52)));
        
        nnbi = new NonNegativeBigInteger("0");
        Assert.assertThat(nnbi.intValue(), is(equalTo(0)));
    }
    
    @Test
    public void validNonNegativeZero() {
        NonNegativeBigInteger nnbi = new NonNegativeBigInteger("0");
        Assert.assertThat(nnbi.intValue(), is(equalTo(0)));
    }
    
    @Test (expected = IllegalArgumentException.class )
    public void invalidPositive() {
        PositiveBigInteger pbi = new PositiveBigInteger("-1");
    }
    
    @Test (expected = IllegalArgumentException.class )
    public void invalidPositiveZero() {
        PositiveBigInteger pbi = new PositiveBigInteger("0");
    }
    
    @Test (expected = IllegalArgumentException.class )
    public void invalidNonNegative() {
        NonNegativeBigInteger nnbi = new NonNegativeBigInteger("-1");
    }

}
