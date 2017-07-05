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
package org.n52.svalbard.encode.inspire.ef;

import java.util.Map;

import org.apache.xmlbeans.XmlObject;
import org.n52.sos.ogc.gml.AbstractFeature;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.SosConstants.HelperValues;
import org.n52.sos.util.XmlHelper;
import org.n52.svalbard.inspire.ef.OperationalActivityPeriod;

import eu.europa.ec.inspire.schemas.ef.x40.OperationalActivityPeriodType;
import net.opengis.gml.x32.FeaturePropertyType;

public abstract class AbstractOperationalActivityPeriodEncoder extends AbstractEnvironmentalFaciltityEncoder<OperationalActivityPeriod>{

    @Override
    protected XmlObject createFeature(FeaturePropertyType featurePropertyType, AbstractFeature abstractFeature,
            Map<HelperValues, String> additionalValues) throws OwsExceptionReport {
        OperationalActivityPeriodType encodedObject = createOperationalActivityPeriod((OperationalActivityPeriod)abstractFeature);
        featurePropertyType.addNewAbstractFeature().set(encodedObject);
        XmlHelper.substituteElement(featurePropertyType.getAbstractFeature(), encodedObject);
        return featurePropertyType;
    }
    
    protected OperationalActivityPeriodType createOperationalActivityPeriod(OperationalActivityPeriod operationalActivityPeriod) throws OwsExceptionReport {
        OperationalActivityPeriodType oapt = OperationalActivityPeriodType.Factory.newInstance();
        oapt.addNewActivityTime().addNewAbstractTimeObject().set(encodeGML32(operationalActivityPeriod.getActivityTime()));
        // TODO check for substitution
        return oapt;
    }
}
