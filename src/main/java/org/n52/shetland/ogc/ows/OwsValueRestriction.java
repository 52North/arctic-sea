/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.ows;


import java.util.Comparator;

import org.n52.janmayen.Comparables;


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface OwsValueRestriction extends Comparable<OwsValueRestriction> {
    public static final Comparator<OwsValueRestriction> COMPARATOR = Comparator.nullsLast((o1, o2) -> {
        if (o1.isRange()) {
            if (o2.isRange()) {
                return OwsRange.COMPARATOR.compare(o1.asRange(), o2.asRange());
            } else if (o2.isValue()) {
                return Comparables.GREATER;
            } else {
                throw new AssertionError();
            }
        } else if (o1.isValue()) {
            if (o2.isRange()) {
                return Comparables.LESS;
            } else if (o2.isValue()) {
                return OwsValue.COMPARATOR.compare(o1.asValue(), o2.asValue());
            } else {
                throw new AssertionError();
            }
        } else {
            throw new AssertionError();
        }
    });


    default boolean isRange() {
        return false;
    }

    default boolean isValue() {
        return false;
    }

    default OwsRange asRange() {
        throw new UnsupportedOperationException();
    }

    default OwsValue asValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public default int compareTo(OwsValueRestriction other) {
        return COMPARATOR.compare(this, other);
    }

}
