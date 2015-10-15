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

import java.math.BigInteger;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public class NonNegativeBigInteger extends BigInteger {

    private static final long serialVersionUID = 4583082216155045978L;

    public NonNegativeBigInteger(byte[] val) {
        super(val);
        validate();
    }

    public NonNegativeBigInteger(String val) {
        super(val);
        validate();
    }

    public NonNegativeBigInteger(int i) {
        super(Integer.toString(i));
        validate();
    }

    public NonNegativeBigInteger(long l) {
        super(Long.toString(l));
        validate();
    }

    private void validate() {
        if (compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("Negative value");
        }
    }

}
