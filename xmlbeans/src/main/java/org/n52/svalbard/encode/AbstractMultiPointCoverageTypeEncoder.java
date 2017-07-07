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
import org.n52.shetland.ogc.om.values.MultiPointCoverage;
import org.n52.shetland.ogc.om.values.MultiPointCoverage.PointValueLists;
import org.n52.shetland.util.JavaHelper;
import org.n52.svalbard.encode.exception.EncodingException;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;

import net.opengis.gml.x32.DiscreteCoverageType;
import net.opengis.gml.x32.DomainSetType;
import net.opengis.gml.x32.MultiPointDomainDocument;

/**
 * Abstract {@link Encoder} implementation to encode {@link MultiPointCoverage}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 * @param <T> Target
 */
public abstract class AbstractMultiPointCoverageTypeEncoder<T>
        extends AbstractCoverageEncoder<T, MultiPointCoverage> {

    /**
     * Encode {@link MultiPointCoverage} to {@link DiscreteCoverageType}
     *
     * @param dct
     *            {@link DiscreteCoverageType} to add values to
     * @param multiPointCoverage
     *            {@link MultiPointCoverage} to encode
     * @return {@link DiscreteCoverageType}
     * @throws EncodingException
     *             If an error occurs
     */
    protected DiscreteCoverageType encodeMultiPointCoverageType(DiscreteCoverageType dct,
            MultiPointCoverage multiPointCoverage) throws EncodingException {
        dct.setId(multiPointCoverage.getGmlId());
        PointValueLists pointValues = multiPointCoverage.getPointValue();
        encodeMultiPointDomain(dct, pointValues);
        encodeRangeSet(dct, multiPointCoverage);
        return dct;
    }

    private void encodeMultiPointDomain(DiscreteCoverageType dct, PointValueLists pointValues)
            throws EncodingException {
        MultiPointDomainDocument mpdd = MultiPointDomainDocument.Factory.newInstance();
        DomainSetType mpdst = mpdd.addNewMultiPointDomain();
        GeometryFactory factory = pointValues.getPoints().get(0).getFactory();
        MultiPoint multiPoint = factory.createMultiPoint(pointValues.getPoints().toArray(new Point[0]));
        EncodingContext ec =
                EncodingContext.of(XmlBeansEncodingFlags.GMLID, JavaHelper.generateID(multiPoint.toString()))
                        .with(XmlBeansEncodingFlags.PROPERTY_TYPE, true);
        XmlObject encodedGeometry = encodeGML(multiPoint, ec);
        mpdst.addNewAbstractGeometry().set(encodedGeometry);
        substitute(mpdst.getAbstractGeometry(), encodedGeometry);
        dct.setDomainSet(mpdst);
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        super.addNamespacePrefixToMap(nameSpacePrefixMap);
        nameSpacePrefixMap.put(GmlConstants.NS_GML_32, GmlConstants.NS_GML_PREFIX);
    }

    protected XmlObject encodeGML(Object o, EncodingContext ec) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o, ec);
    }
}
