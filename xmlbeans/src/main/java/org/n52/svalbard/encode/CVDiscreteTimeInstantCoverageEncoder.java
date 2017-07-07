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
import org.n52.svalbard.encode.exception.EncodingException;

import net.opengis.cv.x02.gml32.CVDiscreteTimeInstantCoveragePropertyType;

/**
 * Encoder for {@link CVDiscreteTimeInstantCoveragePropertyType}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class CVDiscreteTimeInstantCoverageEncoder
        extends AbstractXmlEncoder<XmlObject, Object> {

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.emptySet();
    }

    @Override
    public XmlObject encode(Object objectToEncode, EncodingContext ec) throws EncodingException {
        CVDiscreteTimeInstantCoveragePropertyType cvdticpt =
                CVDiscreteTimeInstantCoveragePropertyType.Factory.newInstance(getXmlOptions());
        // CVDiscreteTimeInstantCoverageType cvdtict =
        // cvdticpt.addNewCVDiscreteTimeInstantCoverage();
        // CVTimeInstantValuePairPropertyType cvtivppt =
        // cvdtict.addNewElement();
        // CVTimeInstantValuePairType cvtivpt =
        // cvtivppt.addNewCVTimeInstantValuePair();
        // TimeInstantPropertyType tipt = cvtivpt.addNewGeometry();
        // XmlObject addNewValue = cvtivpt.addNewValue();
        return cvdticpt;
    }

}
