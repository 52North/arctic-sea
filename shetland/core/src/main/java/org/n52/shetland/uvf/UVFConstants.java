/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.uvf;

import java.util.Collections;
import java.util.List;

import org.n52.janmayen.http.MediaType;
import org.n52.shetland.util.CollectionHelper;

public interface UVFConstants {

    String LINE_ENDING = "lineending";

    MediaType CONTENT_TYPE_UVF = new MediaType("application", "uvf");

    MediaType CONTENT_TYPE_UVF_WINDOWS = CONTENT_TYPE_UVF.withParameter(LINE_ENDING, "Windows");

    MediaType CONTENT_TYPE_UVF_UNIX = CONTENT_TYPE_UVF.withParameter(LINE_ENDING, "Unix");

    MediaType CONTENT_TYPE_UVF_MAC = CONTENT_TYPE_UVF.withParameter(LINE_ENDING, "Mac");

    /**
     * Time format to be used in UVF encoded data: <code>yyMMddHHmm</code>, e.g. <code>7001011230</code> is
     * 01.01.1970 12:30 UTC
     */
    String TIME_FORMAT = "yyMMddHHmm";

    /**
     * The identifiers length is limited to 15 characters following UVF spec for lines 2, 3
     */
    int MAX_IDENTIFIER_LENGTH = 15;

    /**
     * The maximum length of a value string is limited to 10 characters. Hence, the values are shortened, e.g.
     * <code>52.1234567890</code> will be cut to <code>52.1234567</code>
     */
    int MAX_VALUE_LENGTH = 10;

    /**
     * No data values MUST be encoded with <code>-777</code> in the UVF format.
     */
    String NO_DATA_STRING = "-777";

    /**
     * The list of allowed CRS EPSG codes. Here, the German GK bands:
     * <ul>
     * <li>31466</li>
     * <li>31467</li>
     * <li>31468</li>
     * <li>31469</li>
     * </ul>
     */
    List<String> ALLOWED_CRS = Collections.unmodifiableList(CollectionHelper.list("31466", "31467", "31468", "31469"));

    int MINIMUM_EPSG_CODE = 31466;

    int MAXIMUM_EPSG_CODE = 31469;

    String LINE_ENDING_UNIX = "\n";

    String LINE_ENDING_WINDOWS = "\r\n";

    String LINE_ENDING_MAC = "\r";

    enum LineEnding {
        Windows,
        Unix,
        Mac;
    }

    enum FunktionInterpretation {
        Linie,
        Blockanfang,
        Blockende,
        Summenlinie;
    }
}
