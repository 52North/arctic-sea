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
        return value instanceof SweDataArrayValue && ((SweDataArrayValue) value).getValue().getElementType() instanceof SweAbstractDataRecord
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
