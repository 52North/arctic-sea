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

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Point;

import net.opengis.gml.x32.PointPropertyType;
import net.opengis.gml.x32.PointType;

/**
 * {@link Encoder} implementation for {@link Point} to {@link PointPropertyType}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class PointPropertyTypeEncoder extends AbstractXmlEncoder<PointPropertyType, Point> {

    private static final Set<EncoderKey> ENCODER_KEYS =
            Sets.newHashSet(new ClassToClassEncoderKey(PointPropertyType.class, Point.class),
                    new XmlPropertyTypeEncoderKey(GmlConstants.NS_GML_32, Point.class));

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public PointPropertyType encode(Point point) throws EncodingException {
        return encode(point, EncodingContext.empty());
    }

    @Override
    public PointPropertyType encode(Point point, EncodingContext ec)
            throws EncodingException {
        PointPropertyType ppt = PointPropertyType.Factory.newInstance();
        ppt.setPoint(encodePointType(point, ec));
        return ppt;
    }

    private PointType encodePointType(Point point, EncodingContext ec)
            throws EncodingException {
        return (PointType) encodeGML(point, ec);
    }

    protected XmlObject encodeGML(Object o, EncodingContext ec)
            throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o, ec);
    }

}
