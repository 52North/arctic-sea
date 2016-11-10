/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
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

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface SweDataComponentVisitor<T> {
    T visit(SweField component)
            throws OwsExceptionReport;

    T visit(SweDataRecord component)
            throws OwsExceptionReport;

    T visit(SweDataArray component)
            throws OwsExceptionReport;

    T visit(SweCount component)
            throws OwsExceptionReport;

    T visit(SweCountRange component)
            throws OwsExceptionReport;

    T visit(SweBoolean component)
            throws OwsExceptionReport;

    T visit(SweCategory component)
            throws OwsExceptionReport;

    T visit(SweObservableProperty component)
            throws OwsExceptionReport;

    T visit(SweQuantity component)
            throws OwsExceptionReport;

    T visit(SweQuantityRange component)
            throws OwsExceptionReport;

    T visit(SweText component)
            throws OwsExceptionReport;

    T visit(SweTime component)
            throws OwsExceptionReport;

    T visit(SweTimeRange component)
            throws OwsExceptionReport;

    T visit(SweEnvelope component)
            throws OwsExceptionReport;

    T visit(SweVector component)
            throws OwsExceptionReport;

    T visit(SweSimpleDataRecord component)
            throws OwsExceptionReport;

    T visit(SmlPosition component)
            throws OwsExceptionReport;

    T visit(SmlDataInterface component)
            throws OwsExceptionReport;

    T visit(SmlFeatureOfInterest component)
            throws OwsExceptionReport;

}
