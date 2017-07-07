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
package org.n52.svalbard.decode;

import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.sensorML.AbstractSensorML;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifier;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

/**
 * Abstract {@link Decoder} class to decode OGC SensorML
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class AbstractSensorMLDecoder extends AbstractXmlDecoder<XmlObject, AbstractSensorML>
        implements ProcedureDecoder<AbstractSensorML, XmlObject> {

    private static final Set<String> DEFINITION_VALUES =
            Sets.newHashSet(OGCConstants.URN_UNIQUE_IDENTIFIER, OGCConstants.URN_IDENTIFIER_IDENTIFICATION);

    /**
     * Determine if an SosSMLIdentifier is the unique identifier for a procedure
     *
     * @param identifier
     *            SosSMLIdentifier to example for unique identifier
     *
     * @return whether the SosSMLIdentifier contains the unique identifier
     */
    protected boolean isIdentificationProcedureIdentifier(SmlIdentifier identifier) {
        return checkIdentificationNameForProcedureIdentifier(identifier.getName())
                || checkIdentificationDefinitionForProcedureIdentifier(identifier.getDefinition());
    }

    private boolean checkIdentificationNameForProcedureIdentifier(final String name) {
        return !Strings.isNullOrEmpty(name) && name.equals(OGCConstants.URN_UNIQUE_IDENTIFIER_END);
    }

    private boolean checkIdentificationDefinitionForProcedureIdentifier(final String definition) {
        if (Strings.isNullOrEmpty(definition)) {
            return false;
        }
        return DEFINITION_VALUES.contains(definition) || checkDefinitionStartsWithAndContains(definition);
    }

    private boolean checkDefinitionStartsWithAndContains(final String definition) {
        return definition.startsWith(OGCConstants.URN_UNIQUE_IDENTIFIER_START)
                && definition.contains(OGCConstants.URN_UNIQUE_IDENTIFIER_END);
    }

}
