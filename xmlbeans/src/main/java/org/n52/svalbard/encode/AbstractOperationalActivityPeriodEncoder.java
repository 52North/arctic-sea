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

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.inspire.ef.OperationalActivityPeriod;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.XmlHelper;

import eu.europa.ec.inspire.schemas.ef.x40.OperationalActivityPeriodType;
import net.opengis.gml.x32.FeaturePropertyType;

public abstract class AbstractOperationalActivityPeriodEncoder
        extends AbstractEnvironmentalFaciltityEncoder<XmlObject, OperationalActivityPeriod> {

    @Override
    protected XmlObject createFeature(FeaturePropertyType featurePropertyType, AbstractFeature abstractFeature,
            EncodingContext context) throws EncodingException {
        OperationalActivityPeriodType encodedObject =
                createOperationalActivityPeriod((OperationalActivityPeriod) abstractFeature);
        featurePropertyType.addNewAbstractFeature().set(encodedObject);
        XmlHelper.substituteElement(featurePropertyType.getAbstractFeature(), encodedObject);
        return featurePropertyType;
    }

    protected OperationalActivityPeriodType createOperationalActivityPeriod(
            OperationalActivityPeriod operationalActivityPeriod) throws EncodingException {
        OperationalActivityPeriodType oapt = OperationalActivityPeriodType.Factory.newInstance();
        oapt.addNewActivityTime().addNewAbstractTimeObject()
                .set(encodeGML32(operationalActivityPeriod.getActivityTime()));
        // TODO check for substitution
        return oapt;
    }
}
