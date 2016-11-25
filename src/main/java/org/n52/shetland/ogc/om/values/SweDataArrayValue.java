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
package org.n52.shetland.ogc.om.values;

import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.ogc.swe.SweDataArray;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.simpleType.SweTime;
import org.n52.shetland.ogc.swe.simpleType.SweTimeRange;
import org.n52.shetland.util.DateTimeHelper;

import com.google.common.collect.Sets;

/**
 * Multi value representing a SweDataArray for observations
 *
 * @since 4.0.0
 *
 */
public class SweDataArrayValue implements MultiValue<SweDataArray> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SweDataArrayValue.class);

    /**
     * Measurement values
     */
    private SweDataArray value;

    public SweDataArrayValue() {
        this(null);
    }

    public SweDataArrayValue(SweDataArray value) {
        this.value = value;
    }

    @Override
    public void setValue(SweDataArray value) {
        this.value = value;
    }

    @Override
    public SweDataArray getValue() {
        return value;
    }

    @Override
    public void setUnit(String unit) {
        // do nothing
    }

    @Override
    public String getUnit() {
        return null;
    }

    /**
     * Adds the given block - a {@link List}<{@link String}> - add the end of
     * the current list of blocks
     *
     * @param blockOfTokensToAddAtTheEnd
     *
     * @return <tt>true</tt> (as specified by {@link Collection#add}) <br />
     * <tt>false</tt> if block could not be added
     */
    public boolean addBlock(List<String> blockOfTokensToAddAtTheEnd) {
        if (value != null) {
            return value.add(blockOfTokensToAddAtTheEnd);
        }
        return false;
    }

    @Override
    public String toString() {
        return String
                .format("SweDataArrayValue [value=%s, unit=null]", getValue());
    }

    @Override
    public Time getPhenomenonTime() {
        TimePeriod timePeriod = new TimePeriod();
        Set<Integer> dateTokenIndizes = Sets.newHashSet();
        if (getValue() == null || getValue().getElementType() == null || getValue().getEncoding() == null) {
            LOGGER.error("Value of type \"{}\" not set correct.", SweDataArrayValue.class.getName());
        } else {
            // get index of time token from elementtype
            if (getValue().getElementType() instanceof SweDataRecord) {
                List<SweField> fields = ((SweDataRecord) getValue().getElementType()).getFields();
                for (int i = 0; i < fields.size(); i++) {
                    final SweField sweField = fields.get(i);
                    if (sweField.getElement() instanceof SweTime || sweField.getElement() instanceof SweTimeRange && checkFieldNameAndElementDefinition(sweField)) {
                        dateTokenIndizes.add(i);
                    }
                }

            }
            if (dateTokenIndizes.isEmpty()) {
                LOGGER.error("PhenomenonTime field could not be found in ElementType");
            } else {
                for (List<String> block : value.getValues()) {
                    // check for "/" to identify time periods (Is
                    // conform with ISO8601 (see WP))
                    // datetimehelper to DateTime from joda time
                    for (Integer index : dateTokenIndizes) {
                        String token = null;
                        try {
                            token = block.get(index);
                            timePeriod.extendToContain(DateTimeHelper.parseIsoString2DateTime2Time(token));
                        } catch (final DateTimeParseException dte) {
                            LOGGER.error(String.format("Could not parse ISO8601 string \"%s\"", token), dte);
                            // FIXME throw exception here?
                        }
                    }
                }
            }
        }
        return timePeriod;
    }

    private boolean checkFieldNameAndElementDefinition(SweField sweField) {
        return "StartTime".equals(sweField.getName().getValue()) ||
               "EndTime".equals(sweField.getName().getValue()) ||
               OmConstants.PHENOMENON_TIME.equals(sweField.getElement().getDefinition());

    }

    @Override
    public boolean isSetValue() {
        return getValue() != null && getValue().isEmpty();
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
    }
}
