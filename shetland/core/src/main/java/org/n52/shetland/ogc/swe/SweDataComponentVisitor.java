/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface SweDataComponentVisitor<T, X extends Throwable> {
    T visit(SweField component) throws X;

    T visit(SweDataRecord component) throws X;

    T visit(SweDataArray component) throws X;

    T visit(SweCount component) throws X;

    T visit(SweCountRange component) throws X;

    T visit(SweBoolean component) throws X;

    T visit(SweCategory component) throws X;

    T visit(SweCategoryRange component) throws X;

    T visit(SweObservableProperty component) throws X;

    T visit(SweQuantity component) throws X;

    T visit(SweQuantityRange component) throws X;

    T visit(SweText component) throws X;

    T visit(SweTime component) throws X;

    T visit(SweTimeRange component) throws X;

    T visit(SweEnvelope component) throws X;

    T visit(SweVector component) throws X;

    T visit(SweSimpleDataRecord component) throws X;

    T visit(SmlPosition component) throws X;

    T visit(SmlDataInterface component) throws X;

    T visit(SmlFeatureOfInterest component) throws X;

    T visit(StreamingSweDataArray component) throws X;

}
