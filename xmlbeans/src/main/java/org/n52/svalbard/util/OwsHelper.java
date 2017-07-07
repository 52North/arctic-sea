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
package org.n52.svalbard.util;

import java.util.Locale;

import javax.xml.namespace.QName;

import org.n52.shetland.ogc.ows.OWSConstants;

/**
 * Helper class for OGC OWS
 *
 * @since 1.0.0
 *
 */
public final class OwsHelper {
    private OwsHelper() {
    }

    /**
     * Sets the first character to UpperCase.
     *
     * @param name
     *            String to be modified.
     * @return Modified string.
     */
    public static String refactorOpsName(String name) {
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);

    }

    /**
     * Get OWS QName for localName
     *
     * @param localName
     *            Local name
     * @return QName for localName
     */
    public static QName getQNameForLocalName(String localName) {
        return new QName(OWSConstants.NS_OWS, localName, OWSConstants.NS_OWS_PREFIX);
    }

}
