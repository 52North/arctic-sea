/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.janmayen;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface CharacterClasses {

    CharacterClass ASCII_LOWER_CASE = CharacterClass.forRange(97, 122);
    CharacterClass ASCII_UPPER_CASE = CharacterClass.forRange(65, 90);
    CharacterClass ASCII_LETTER = CharacterClass.forClasses(ASCII_LOWER_CASE, ASCII_UPPER_CASE);
    CharacterClass DIGIT_ASCII = CharacterClass.forRange(48, 57);
    CharacterClass DIGIT_TELUGU = CharacterClass.forRange(3174, 3183);
    CharacterClass DIGIT_LAO = CharacterClass.forRange(3792, 3801);
    CharacterClass DIGIT_KANNADA = CharacterClass.forRange(3302, 3311);
    CharacterClass DIGIT_GUJARATI = CharacterClass.forRange(2790, 2799);
    CharacterClass DIGIT_BENGALI = CharacterClass.forRange(2534, 2543);
    CharacterClass DIGIT_MALAYALAM = CharacterClass.forRange(3430, 3439);
    CharacterClass DIGIT_ORIYA = CharacterClass.forRange(2918, 2927);
    CharacterClass DIGIT_ARABIC_INDIC = CharacterClass.forRange(1632, 1641);
    CharacterClass DIGIT_TIBETAN = CharacterClass.forRange(3872, 3881);
    CharacterClass DIGIT_ARABIC_INDIC_EXTENDED = CharacterClass.forRange(1776, 1785);
    CharacterClass DIGIT_GURMUKHI = CharacterClass.forRange(2662, 2671);
    CharacterClass DIGIT_TAMIL = CharacterClass.forRange(3047, 3055);
    CharacterClass DIGIT_THAI = CharacterClass.forRange(3664, 3673);
    CharacterClass DIGIT_DEVANAGARI = CharacterClass.forRange(2406, 2415);
    CharacterClass DIGIT_NON_ASCII = CharacterClass.forClasses(DIGIT_ARABIC_INDIC,
                                                               DIGIT_ARABIC_INDIC_EXTENDED,
                                                               DIGIT_DEVANAGARI,
                                                               DIGIT_BENGALI,
                                                               DIGIT_GURMUKHI,
                                                               DIGIT_GUJARATI,
                                                               DIGIT_ORIYA,
                                                               DIGIT_TAMIL,
                                                               DIGIT_TELUGU,
                                                               DIGIT_KANNADA,
                                                               DIGIT_MALAYALAM,
                                                               DIGIT_THAI,
                                                               DIGIT_LAO,
                                                               DIGIT_TIBETAN);

}
