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
package org.n52.shetland.ogc.swe;

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


public class AbstractVoidSweDataComponentVisitor<X extends Throwable> extends VoidSweDataComponentVisitor<X> {

    @Override
    protected void _visit(SmlDataInterface component) throws X {
    }

    @Override
    protected void _visit(SmlFeatureOfInterest component) throws X {
    }

    @Override
    protected void _visit(SweField component) throws X {
    }

    @Override
    protected void _visit(SweDataRecord component) throws X {
    }

    @Override
    protected void _visit(SweDataArray component) throws X {
    }

    @Override
    protected void _visit(SweCount component) throws X {
    }

    @Override
    protected void _visit(SweCountRange component) throws X {
    }

    @Override
    protected void _visit(SweBoolean component) throws X {
    }

    @Override
    protected void _visit(SweCategory component) throws X {
    }

    @Override
    protected void _visit(SweCategoryRange component) throws X {
    }

    @Override
    protected void _visit(SweObservableProperty component) throws X {
    }

    @Override
    protected void _visit(SweQuantity component) throws X {
    }

    @Override
    protected void _visit(SweQuantityRange component) throws X {
    }

    @Override
    protected void _visit(SweText component) throws X {
    }

    @Override
    protected void _visit(SweTime component) throws X {
    }

    @Override
    protected void _visit(SweTimeRange component) throws X {
    }

    @Override
    protected void _visit(SweEnvelope component) throws X {
    }

    @Override
    protected void _visit(SweVector component) throws X {
    }

    @Override
    protected void _visit(SweSimpleDataRecord component) throws X {
    }

    @Override
    protected void _visit(SmlPosition component) throws X {
    }

    @Override
    protected void _visit(StreamingSweDataArray component) throws X {
    }

}
