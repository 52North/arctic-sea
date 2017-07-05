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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.sos.encode.AbstractGmlEncoderv321;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.SosConstants.HelperValues;
import org.n52.sos.util.CodingHelper;
import org.n52.sos.w3c.SchemaLocation;
import org.n52.svalbard.inspire.base.InspireBaseConstants;
import org.n52.svalbard.inspire.base2.InspireBase2Constants;
import org.n52.svalbard.inspire.ef.InspireEfConstants;

import com.google.common.collect.Sets;

public abstract class AbstractEnvironmentalFaciltityEncoder<T> extends AbstractGmlEncoderv321<T> {
    
    @Override
    public Set<String> getConformanceClasses() {
        return Collections.emptySet();
    }

    @Override
    public void addNamespacePrefixToMap(final Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(InspireEfConstants.NS_EF, InspireEfConstants.NS_EF_PREFIX);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(InspireEfConstants.EF_40_SCHEMA_LOCATION);
    }

    protected static XmlObject encodeEF(Object o) throws OwsExceptionReport {
        return CodingHelper.encodeObjectToXml(InspireEfConstants.NS_EF, o);
    }
    
    protected static XmlObject encodeEFPropertyType(Object o) throws OwsExceptionReport {
        return CodingHelper.encodeObjectToXmlPropertyType(InspireEfConstants.NS_EF, o);
    }
    
    protected static XmlObject encodeEFDocument(Object o) throws OwsExceptionReport {
        return CodingHelper.encodeObjectToXmlDocument(InspireEfConstants.NS_EF, o);
    }

    protected static XmlObject encodeEF(Object o, Map<HelperValues, String> helperValues) throws OwsExceptionReport {
        return CodingHelper.encodeObjectToXml(InspireEfConstants.NS_EF, o, helperValues);
    }
    
    protected static XmlObject encodeBASE2(Object o) throws OwsExceptionReport {
        return CodingHelper.encodeObjectToXml(InspireBase2Constants.NS_BASE2, o);
    }
    
    protected static XmlObject encodeBASE2PropertyType(Object o) throws OwsExceptionReport {
        return CodingHelper.encodeObjectToXmlPropertyType(InspireBase2Constants.NS_BASE2, o);
    }
    
    protected static XmlObject encodeBASE2Document(Object o) throws OwsExceptionReport {
        return CodingHelper.encodeObjectToXmlDocument(InspireBase2Constants.NS_BASE2, o);
    }

    protected static XmlObject encodeBASE2(Object o, Map<HelperValues, String> helperValues) throws OwsExceptionReport {
        return CodingHelper.encodeObjectToXml(InspireBase2Constants.NS_BASE2, o, helperValues);
    }
    
    protected static XmlObject encodeBASE(Object o) throws OwsExceptionReport {
        return CodingHelper.encodeObjectToXml(InspireBaseConstants.NS_BASE, o);
    }
    
    protected static XmlObject encodeBASEPropertyType(Object o) throws OwsExceptionReport {
        return CodingHelper.encodeObjectToXmlPropertyType(InspireBaseConstants.NS_BASE, o);
    }
    
    protected static XmlObject encodeBASEDocument(Object o) throws OwsExceptionReport {
        return CodingHelper.encodeObjectToXmlDocument(InspireBaseConstants.NS_BASE, o);
    }

    protected static XmlObject encodeBASE(Object o, Map<HelperValues, String> helperValues) throws OwsExceptionReport {
        return CodingHelper.encodeObjectToXml(InspireBaseConstants.NS_BASE, o, helperValues);
    }
    
}
