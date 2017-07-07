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

import java.util.Map;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.values.RectifiedGridCoverage;
import org.n52.svalbard.encode.exception.EncodingException;

import net.opengis.gml.x32.DiscreteCoverageType;
import net.opengis.gml.x33.ce.SimpleMultiPointDocument;
import net.opengis.gml.x33.ce.SimpleMultiPointType;

/**
 * Abstract {@link Encoder} implementation for {@link RectifiedGridCoverage}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 * @param <T> Target
 */
public abstract class AbstractRectifiedGridCoverageTypeEncoder<T>
        extends AbstractCoverageEncoder<T, RectifiedGridCoverage> {

    /**
     * Encodes the {@link RectifiedGridCoverage} to {@link DiscreteCoverageType}
     *
     * @param rectifiedGridCoverage
     *            The {@link RectifiedGridCoverage}
     * @param additionalValues
     *            Helper values
     * @return Encoded {@link RectifiedGridCoverage}
     * @throws EncodingException
     *             If an error occurs
     */
    protected DiscreteCoverageType encodeRectifiedGridCoverage(RectifiedGridCoverage rectifiedGridCoverage,
            EncodingContext ec) throws EncodingException {
        DiscreteCoverageType dct = DiscreteCoverageType.Factory.newInstance();
        dct.setId(rectifiedGridCoverage.getGmlId());
        XmlObject encodedGeometry = encodeDomainSet(rectifiedGridCoverage);
        dct.addNewDomainSet().set(encodedGeometry);
        dct.setRangeSet(encodeRangeSet(dct, rectifiedGridCoverage));
        return dct;
    }

    private XmlObject encodeDomainSet(RectifiedGridCoverage rectifiedGridCoverage) {
        SimpleMultiPointDocument smpd = SimpleMultiPointDocument.Factory.newInstance();
        SimpleMultiPointType smpt = smpd.addNewSimpleMultiPoint();
        smpt.setId("smp_" + rectifiedGridCoverage.getGmlId());
        smpt.addNewPosList().setListValue(rectifiedGridCoverage.getDomainSet());
        return smpd;
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        super.addNamespacePrefixToMap(nameSpacePrefixMap);
        nameSpacePrefixMap.put(GmlConstants.NS_GML_32, GmlConstants.NS_GML_PREFIX);
    }

}
