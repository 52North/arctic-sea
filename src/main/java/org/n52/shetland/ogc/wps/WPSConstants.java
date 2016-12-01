/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.wps;


import java.util.Set;

import org.n52.janmayen.http.MediaTypes;

import com.google.common.collect.ImmutableSet;

/**
 * {@code WPSConstants} holds all important and often used constants of OGC WPS (e.g.
 * name of the getCapabilities operation) that are global between all supported
 * versions
 *
 * @since 1.0.0
 */
public interface WPSConstants {

    @Deprecated
    String NS_WPS_PREFIX = "wps";

    /**
     * Constant for the content types of the accept formats
     */
    Set<String> ACCEPT_FORMATS = ImmutableSet.of(MediaTypes.APPLICATION_XML.toString());

    /**
     * Constant for the service name of the WPS.
     */
    String SERVICE = "WPS";

    /**
     * Constant 'inline' for response mode, which means that results are
     * contained inline the ProcessOutputs element of an execute response
     * document.
     * @deprecated see {@link ResponseMode}
     */
    @Deprecated
    String RESPONSE_MODE_INLINE = "inline";

    /**
     * Constant 'raw' for response mode, which means that result values of
     * an execute response are returned directly, i.e. not wrapped in an xml response
     * @deprecated see {@link ResponseMode}
     */
    @Deprecated
    String RESPONSE_MODE_RAW = "raw";

    /**
     * Array of constants for response mode.
     * @deprecated see {@link ResponseMode}
     */
    @Deprecated
    Set<String> RESPONSE_MODES = ImmutableSet.of(RESPONSE_MODE_INLINE,
                                                 RESPONSE_MODE_RAW);

    String VERSION_200 = "2.0.0";

    /**
     * The names of the operations supported by all versions of the WPS
     * specification.
     */
    enum Operations {
        GetCapabilities,
        DescribeProcess,
        Execute,
        GetResult,
        GetStatus,
        Dismiss;
    }
}
