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
package org.n52.shetland.ogc.swe;

import java.util.Objects;
import java.util.function.Supplier;

import org.n52.shetland.ogc.sensorML.elements.SmlPosition;
import org.n52.shetland.ogc.sensorML.v20.SmlDataInterface;
import org.n52.shetland.ogc.sensorML.v20.SmlFeatureOfInterest;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweCategory;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweCountRange;
import org.n52.shetland.ogc.swe.simpleType.SweObservableProperty;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweQuantityRange;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.ogc.swe.simpleType.SweTime;
import org.n52.shetland.ogc.swe.simpleType.SweTimeRange;

public abstract class AbstractThrowingSweDataComponentVisitor<T, X extends Throwable>
        implements SweDataComponentVisitor<T, X> {

    private final Supplier<X> exceptionSupplier;

    public AbstractThrowingSweDataComponentVisitor(Supplier<X> exceptionSupplier) {
        this.exceptionSupplier = Objects.requireNonNull(exceptionSupplier);
    }

    @Override
    public T visit(SweField component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweDataRecord component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweDataArray component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweCount component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweCountRange component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweBoolean component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweCategory component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweObservableProperty component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweQuantity component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweQuantityRange component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweText component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweTime component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweTimeRange component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweEnvelope component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweVector component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SweSimpleDataRecord component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SmlPosition component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SmlDataInterface component) throws X {
        throw this.exceptionSupplier.get();
    }

    @Override
    public T visit(SmlFeatureOfInterest component) throws X {
        throw this.exceptionSupplier.get();
    }

}
