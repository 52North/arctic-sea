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
package org.n52.svalbard.encode;

import org.n52.shetland.ogc.om.values.SweDataArrayValue;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.shetland.ogc.swe.SweAbstractDataRecord;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweTime;
import org.n52.shetland.ogc.swe.simpleType.SweTimeRange;

public interface WmlTmlHelper {

    default boolean checkSweDataArray(Value<?> value) {
        return value instanceof SweDataArrayValue
                && ((SweDataArrayValue) value).getValue().getElementType() instanceof SweAbstractDataRecord
                && checkFields((SweAbstractDataRecord) ((SweDataArrayValue) value).getValue().getElementType());
    }

    default boolean checkFields(SweAbstractDataRecord sweAbstractDataRecord) {
        return sweAbstractDataRecord.getFields().size() == 2
                && (sweAbstractDataRecord.getFields().get(0).getElement() instanceof SweTime
                        || sweAbstractDataRecord.getFields().get(0).getElement() instanceof SweTimeRange)
                && (sweAbstractDataRecord.getFields().get(0).getElement() instanceof SweQuantity
                        || sweAbstractDataRecord.getFields().get(0).getElement() instanceof SweCount);
    }

}
