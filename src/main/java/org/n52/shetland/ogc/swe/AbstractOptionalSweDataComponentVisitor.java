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
package org.n52.shetland.ogc.swe;

import java.util.Optional;

import org.n52.shetland.ogc.sensorML.elements.SmlPosition;
import org.n52.shetland.ogc.sensorML.v20.SmlDataInterface;
import org.n52.shetland.ogc.sensorML.v20.SmlFeatureOfInterest;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweCategory;
import org.n52.shetland.ogc.swe.simpleType.SweCategoryRange;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweCountRange;
import org.n52.shetland.ogc.swe.simpleType.SweObservableProperty;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweQuantityRange;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.ogc.swe.simpleType.SweTime;
import org.n52.shetland.ogc.swe.simpleType.SweTimeRange;
import org.n52.shetland.ogc.swe.stream.StreamingSweDataArray;

public class AbstractOptionalSweDataComponentVisitor<T, X extends Throwable>
        implements SweDataComponentVisitor<Optional<T>, X> {

    @Override
    public Optional<T> visit(SweField component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweDataRecord component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweDataArray component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweCount component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweCountRange component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweBoolean component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweCategory component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweCategoryRange component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweObservableProperty component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweQuantity component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweQuantityRange component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweText component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweTime component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweTimeRange component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweEnvelope component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweVector component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SweSimpleDataRecord component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SmlPosition component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SmlDataInterface component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(SmlFeatureOfInterest component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    @Override
    public Optional<T> visit(StreamingSweDataArray component) throws X {
        return Optional.ofNullable(_visit(component));
    }

    protected T _visit(SweField component) throws X {
        return null;
    }

    protected T _visit(SweDataRecord component) throws X {
        return null;
    }

    protected T _visit(SweDataArray component) throws X {
        return null;
    }

    protected T _visit(SweCount component) throws X {
        return null;
    }

    protected T _visit(SweCountRange component) throws X {
        return null;
    }

    protected T _visit(SweBoolean component) throws X {
        return null;
    }

    protected T _visit(SweCategory component) throws X {
        return null;
    }

    protected T _visit(SweCategoryRange component) {
        return null;
    }

    protected T _visit(SweObservableProperty component) throws X {
        return null;
    }

    protected T _visit(SweQuantity component) throws X {
        return null;
    }

    protected T _visit(SweQuantityRange component) throws X {
        return null;
    }

    protected T _visit(SweText component) throws X {
        return null;
    }

    protected T _visit(SweTime component) throws X {
        return null;
    }

    protected T _visit(SweTimeRange component) throws X {
        return null;
    }

    protected T _visit(SweEnvelope component) throws X {
        return null;
    }

    protected T _visit(SweVector component) throws X {
        return null;
    }

    protected T _visit(SweSimpleDataRecord component) throws X {
        return null;
    }

    protected T _visit(SmlPosition component) throws X {
        return null;
    }

    protected T _visit(SmlDataInterface component) throws X {
        return null;
    }

    protected T _visit(SmlFeatureOfInterest component) throws X {
        return null;
    }

    protected T _visit(StreamingSweDataArray component) throws X {
        return null;
    }

}
