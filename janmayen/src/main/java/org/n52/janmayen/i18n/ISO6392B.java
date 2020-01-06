/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.janmayen.i18n;

import java.util.Locale;

/**
 * Enum for ISO 639 bibliographic codes.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 5.3.0
 *
 */
public enum ISO6392B {
    ALB("alb", "sqi"),
    ARM("arm", "hye"),
    BAS("baq", "eus"),
    BUR("bur", "mya"),
    CHI("chi", "zho"),
    CRO("scr", "hrv"),
    CZE("cze", "ces"),
    DUT("dut", "nld"),
    FRE("fre", "fra"),
    GEO("geo", "kat"),
    GER("ger", "deu"),
    GRE("gre", "ell"),
    ICE("ice", "isl"),
    MAC("mac", "mkd"),
    MAL("may", "msa"),
    MAO("mao", "mri"),
    PER("per", "fas"),
    SER("scc", "srp"),
    SLO("slo", "slk"),
    TIB("tib", "bod"),
    WEL("wel", "cym");

    private final String iso;

    private final String isoBib;

    ISO6392B(String isoBib, String iso) {
        this.iso = iso;
        this.isoBib = isoBib;
    }

    public String getIso() {
        return iso;
    }

    public String getIsoBib() {
        return isoBib;
    }

    public static ISO6392B fromValue(String v) {
        for (ISO6392B c : ISO6392B.values()) {
            if (c.getIso().equalsIgnoreCase(v) || c.getIsoBib().equalsIgnoreCase(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public static ISO6392B fromValue(ISO6392B v) {
        for (ISO6392B c : ISO6392B.values()) {
            if (c.getIso().equalsIgnoreCase(v.getIso()) || c.getIsoBib().equalsIgnoreCase(v.getIsoBib())) {
                return c;
            }
        }
        throw new IllegalArgumentException(v.getIso() + " or " + v.getIsoBib());
    }

    public static ISO6392B fromValue(Locale v) {
        for (ISO6392B c : ISO6392B.values()) {
            if (c.getIso().equalsIgnoreCase(v.getISO3Country())
                    || c.getIsoBib().equalsIgnoreCase(v.getISO3Country())) {
                return c;
            }
        }
        throw new IllegalArgumentException(v.getISO3Country());
    }
}
