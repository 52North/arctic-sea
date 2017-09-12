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
package org.n52.svalbard.encode;

import static java.util.stream.Collectors.toList;

import java.util.List;

import net.opengis.gml.x32.BooleanListDocument;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.CountListDocument;
import net.opengis.gml.x32.DataBlockType;
import net.opengis.gml.x32.DiscreteCoverageType;
import net.opengis.gml.x32.MeasureOrNilReasonListType;
import net.opengis.gml.x32.QuantityListDocument;
import net.opengis.gml.x32.RangeSetType;

import org.n52.shetland.ogc.om.values.BooleanValue;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.DiscreteCoverage;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.base.Joiner;

/**
 * Abstract {@link Encoder} implementation for {@link DiscreteCoverage}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 * @param <T> Target
 * @param <S> Source
 */
public abstract class AbstractCoverageEncoder<T, S>
        extends AbstractXmlEncoder<T, S> {

    /**
     * Encode range set of {@link DiscreteCoverageType} from
     * {@link DiscreteCoverage}
     *
     * @param dct
     *            {@link DiscreteCoverageType} to encode range se for
     * @param discreteCoverage
     *            The {@link DiscreteCoverage} with the range set
     * @return {@link DiscreteCoverageType} with range set
     * @throws EncodingException If an error occurs
     */
    protected RangeSetType encodeRangeSet(DiscreteCoverageType dct, DiscreteCoverage<?> discreteCoverage)
            throws EncodingException {
        RangeSetType rst = dct.addNewRangeSet();
        encodeValueList(rst, discreteCoverage);
        return dct.getRangeSet();
    }

    /**
     * Encode value list of {@link RangeSetType} from {@link DiscreteCoverage}
     *
     * @param rst
     *            The {@link RangeSetType} to encode value list for
     * @param discreteCoverage
     *            The {@link DiscreteCoverage} with the value list
     * @throws EncodingException
     *             If an error occurs
     */
    protected void encodeValueList(RangeSetType rst, DiscreteCoverage<?> discreteCoverage) throws EncodingException {
        List<?> list = getList(discreteCoverage);
        Value<?> value = discreteCoverage.getRangeSet().iterator().next();
        if (value instanceof BooleanValue) {
            BooleanListDocument bld = BooleanListDocument.Factory.newInstance(getXmlOptions());
            bld.setBooleanList(list);
            rst.set(bld);
        } else if (value instanceof CategoryValue || value instanceof TextValue) {
            DataBlockType dbt = rst.addNewDataBlock();
            dbt.addNewRangeParameters().setHref(discreteCoverage.getRangeParameters());
            CoordinatesType ct = dbt.addNewTupleList();
            ct.setCs(",");
            ct.setStringValue(Joiner.on(",").join(list));
        } else if (value instanceof CountValue) {
            CountListDocument cld = CountListDocument.Factory.newInstance(getXmlOptions());
            cld.setCountList(list);
            rst.set(cld);
        } else if (value instanceof QuantityValue) {
            QuantityListDocument qld = QuantityListDocument.Factory.newInstance(getXmlOptions());
            MeasureOrNilReasonListType monrlt = qld.addNewQuantityList();
            if (discreteCoverage.isSetUnit()) {
                monrlt.setUom(discreteCoverage.getUnit());
            } else if (value.isSetUnit()) {
                monrlt.setUom(value.getUnit());
            }
            monrlt.setListValue(list);
            rst.set(qld);
        }
    }

    private List<?> getList(DiscreteCoverage<?> discreteCoverage) {
        return discreteCoverage.getRangeSet().stream().map(Value::getValue).collect(toList());
    }

}
