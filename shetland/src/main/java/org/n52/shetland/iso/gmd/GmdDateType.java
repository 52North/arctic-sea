/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.iso.gmd;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GmdDateType {

    private static final String EN_PUBLICATION = "publication";
    private static final GmdDateType PUBLICATION
            = new GmdDateType("eng", EN_PUBLICATION, EN_PUBLICATION);
    private final String codeList;
    private final String codeListValue;
    private final String codeSpace;
    private final String value;

    public GmdDateType(String codeList, String codeListValue, String codeSpace,
                       String value) {
        this.codeList = codeList;
        this.codeListValue = codeListValue;
        this.codeSpace = codeSpace;
        this.value = value;
    }

    public GmdDateType(String codeList, String codeListValue, String value) {
        this(codeList, codeListValue, null, value);
    }

    public String getCodeList() {
        return codeList;
    }

    public String getCodeListValue() {
        return codeListValue;
    }

    public String getCodeSpace() {
        return codeSpace;
    }

    public String getValue() {
        return value;
    }

    public static GmdDateType publication() {
        return PUBLICATION;
    }

}
