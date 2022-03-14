/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.om.values.visitor;

import org.n52.shetland.ogc.om.values.BooleanValue;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.ComplexValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.CvDiscretePointCoverage;
import org.n52.shetland.ogc.om.values.GeometryValue;
import org.n52.shetland.ogc.om.values.HrefAttributeValue;
import org.n52.shetland.ogc.om.values.MultiPointCoverage;
import org.n52.shetland.ogc.om.values.NilTemplateValue;
import org.n52.shetland.ogc.om.values.ProfileValue;
import org.n52.shetland.ogc.om.values.QuantityRangeValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.RectifiedGridCoverage;
import org.n52.shetland.ogc.om.values.ReferenceValue;
import org.n52.shetland.ogc.om.values.SweDataArrayValue;
import org.n52.shetland.ogc.om.values.TLVTValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.om.values.TimeRangeValue;
import org.n52.shetland.ogc.om.values.TimeValue;
import org.n52.shetland.ogc.om.values.TrajectoryValue;
import org.n52.shetland.ogc.om.values.UnknownValue;
import org.n52.shetland.ogc.om.values.XmlValue;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <T> the return type
 * @param <X> the exception type
 */
public interface ValueVisitor<T, X extends Exception> {
    T visit(BooleanValue value)
            throws X;

    T visit(CategoryValue value)
            throws X;

    T visit(ComplexValue value)
            throws X;

    T visit(CountValue value)
            throws X;

    T visit(GeometryValue value)
            throws X;

    T visit(HrefAttributeValue value)
            throws X;

    T visit(NilTemplateValue value)
            throws X;

    T visit(QuantityValue value)
            throws X;

    T visit(QuantityRangeValue value)
            throws X;

    T visit(ReferenceValue value)
            throws X;

    T visit(SweDataArrayValue value)
            throws X;

    T visit(TVPValue value)
            throws X;

    T visit(TLVTValue value)
            throws X;

    T visit(TextValue value)
            throws X;

    T visit(CvDiscretePointCoverage value)
            throws X;

    T visit(MultiPointCoverage value)
            throws X;

    T visit(RectifiedGridCoverage value)
            throws X;

    T visit(ProfileValue value)
            throws X;

    T visit(TrajectoryValue value)
            throws X;

    T visit(UnknownValue value)
            throws X;

    T visit(TimeValue value)
            throws X;

    T visit(TimeRangeValue value)
            throws X;

    T visit(XmlValue<?> value)
            throws X;

}
