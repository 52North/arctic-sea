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
import java.util.Random;

/**
 *
 * @author Daniel Nüst <d.nuest@52north.org>
 */
public abstract class ValidatableBigInteger extends BigInteger {

    public ValidatableBigInteger(byte[] val) {
        super(val);
        validate();
    }

    public ValidatableBigInteger(int signum, byte[] magnitude) {
        super(signum, magnitude);
        validate();
    }

    public ValidatableBigInteger(String val, int radix) {
        super(val, radix);
        validate();
    }

    public ValidatableBigInteger(String val) {
        super(val);
        validate();
    }

    public ValidatableBigInteger(int numBits, Random rnd) {
        super(numBits, rnd);
        validate();
    }

    public ValidatableBigInteger(int bitLength, int certainty, Random rnd) {
        super(bitLength, certainty, rnd);
        validate();
    }

    protected abstract void validate();
    
}
