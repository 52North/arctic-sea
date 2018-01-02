/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.inspire;

import java.util.Locale;

/**
 * Enum for European ISO6392B three character languages
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public enum InspireEuLanguageISO6392B {
    BUL("bul"),
    CZE("cze"),
    DAN("dan"),
    DUT("dut"),
    ENG("eng"),
    EST("est"),
    FIN("fin"),
    FRE("fre"),
    GER("ger"),
    GRE("gre"),
    HUN("hun"),
    GLE("gle"),
    ITA("ita"),
    LAV("lav"),
    LIT("lit"),
    MLT("mlt"),
    POL("pol"),
    POR("por"),
    RUM("rum"),
    SLO("slo"),
    SLV("slv"),
    SPA("spa"),
    SWE("swe");

    private final String value;

    /**
     * constructor
     *
     * @param v the three character language string
     */
    InspireEuLanguageISO6392B(String v) {
        value = v;
    }

    /**
     * Get the value, three character language string
     *
     * @return the value
     */
    public String value() {
        return value;
    }

    /**
     * Get {@link InspireEuLanguageISO6392B} for string value
     *
     * @param v
     *            the string value to get {@link InspireEuLanguageISO6392B} for
     * @return {@link InspireEuLanguageISO6392B} of string value
     * @throws IllegalArgumentException
     *             if the string value is invalid
     */
    public static InspireEuLanguageISO6392B fromValue(String v) {
        for (InspireEuLanguageISO6392B c : InspireEuLanguageISO6392B.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    /**
     * Get {@link InspireEuLanguageISO6392B} for {@link InspireLanguageISO6392B}
     *
     * @param v
     *            {@link InspireLanguageISO6392B} to get
     *            {@link InspireEuLanguageISO6392B} for
     * @return {@link InspireEuLanguageISO6392B} of
     *         {@link InspireLanguageISO6392B}
     * @throws IllegalArgumentException
     *             if the {@link InspireLanguageISO6392B} is invalid
     */
    public static InspireEuLanguageISO6392B fromValue(InspireLanguageISO6392B v) {
        for (InspireEuLanguageISO6392B c : InspireEuLanguageISO6392B.values()) {
            if (c.value.equals(v.value())) {
                return c;
            }
        }
        throw new IllegalArgumentException(v.value());
    }

    /**
     * Get {@link InspireEuLanguageISO6392B} for {@link Locale}
     *
     * @param v
     *            {@link Locale} to get {@link InspireEuLanguageISO6392B} for
     * @return {@link InspireEuLanguageISO6392B} of {@link Locale}
     * @throws IllegalArgumentException
     *             if the {@link Locale} is invalid
     */
    public static InspireEuLanguageISO6392B fromValue(Locale v) {
        for (InspireEuLanguageISO6392B c : InspireEuLanguageISO6392B.values()) {
            if (c.value.equals(v.getISO3Country())) {
                return c;
            }
        }
        throw new IllegalArgumentException(v.getISO3Country());
    }
}
