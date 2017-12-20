/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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

import java.util.Objects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class NcName {

    private static final CharacterClass IDEOGRAPHIC = CharacterClass.forClasses(
            CharacterClass.forRange(0x4E00, 0x9FA5), CharacterClass.forRange(0x3021, 0x3029),
            CharacterClass.forChars(0x3007));
    private static final CharacterClass COMBINING = CharacterClass.forClasses(
            CharacterClass.forRange(0x0300, 0x0345), CharacterClass.forRange(0x0360, 0x0361),
            CharacterClass.forRange(0x0483, 0x0486), CharacterClass.forRange(0x0591, 0x05A1),
            CharacterClass.forRange(0x05A3, 0x05B9), CharacterClass.forRange(0x05BB, 0x05BD),
            CharacterClass.forRange(0x05C1, 0x05C2), CharacterClass.forRange(0x064B, 0x0652),
            CharacterClass.forRange(0x06D6, 0x06DC), CharacterClass.forRange(0x06DD, 0x06DF),
            CharacterClass.forRange(0x06E0, 0x06E4), CharacterClass.forRange(0x06E7, 0x06E8),
            CharacterClass.forRange(0x06EA, 0x06ED), CharacterClass.forRange(0x0901, 0x0903),
            CharacterClass.forRange(0x093E, 0x094C), CharacterClass.forRange(0x0951, 0x0954),
            CharacterClass.forRange(0x0962, 0x0963), CharacterClass.forRange(0x0981, 0x0983),
            CharacterClass.forRange(0x09C0, 0x09C4), CharacterClass.forRange(0x09C7, 0x09C8),
            CharacterClass.forRange(0x09CB, 0x09CD), CharacterClass.forRange(0x09E2, 0x09E3),
            CharacterClass.forRange(0x0A40, 0x0A42), CharacterClass.forRange(0x0A47, 0x0A48),
            CharacterClass.forRange(0x0A4B, 0x0A4D), CharacterClass.forRange(0x0A70, 0x0A71),
            CharacterClass.forRange(0x0A81, 0x0A83), CharacterClass.forRange(0x0ABE, 0x0AC5),
            CharacterClass.forRange(0x0AC7, 0x0AC9), CharacterClass.forRange(0x0ACB, 0x0ACD),
            CharacterClass.forRange(0x0B01, 0x0B03), CharacterClass.forRange(0x0B3E, 0x0B43),
            CharacterClass.forRange(0x0B47, 0x0B48), CharacterClass.forRange(0x0B4B, 0x0B4D),
            CharacterClass.forRange(0x0B56, 0x0B57), CharacterClass.forRange(0x0B82, 0x0B83),
            CharacterClass.forRange(0x0BBE, 0x0BC2), CharacterClass.forRange(0x0BC6, 0x0BC8),
            CharacterClass.forRange(0x0BCA, 0x0BCD), CharacterClass.forRange(0x0C01, 0x0C03),
            CharacterClass.forRange(0x0C3E, 0x0C44), CharacterClass.forRange(0x0C46, 0x0C48),
            CharacterClass.forRange(0x0C4A, 0x0C4D), CharacterClass.forRange(0x0C55, 0x0C56),
            CharacterClass.forRange(0x0C82, 0x0C83), CharacterClass.forRange(0x0CBE, 0x0CC4),
            CharacterClass.forRange(0x0CC6, 0x0CC8), CharacterClass.forRange(0x0CCA, 0x0CCD),
            CharacterClass.forRange(0x0CD5, 0x0CD6), CharacterClass.forRange(0x0D02, 0x0D03),
            CharacterClass.forRange(0x0D3E, 0x0D43), CharacterClass.forRange(0x0D46, 0x0D48),
            CharacterClass.forRange(0x0D4A, 0x0D4D), CharacterClass.forRange(0x0E34, 0x0E3A),
            CharacterClass.forRange(0x0E47, 0x0E4E), CharacterClass.forRange(0x0EB4, 0x0EB9),
            CharacterClass.forRange(0x0EBB, 0x0EBC), CharacterClass.forRange(0x0EC8, 0x0ECD),
            CharacterClass.forRange(0x0F18, 0x0F19), CharacterClass.forRange(0x0F71, 0x0F84),
            CharacterClass.forRange(0x0F86, 0x0F8B), CharacterClass.forRange(0x0F90, 0x0F95),
            CharacterClass.forRange(0x0F99, 0x0FAD), CharacterClass.forRange(0x0FB1, 0x0FB7),
            CharacterClass.forRange(0x20D0, 0x20DC), CharacterClass.forRange(0x302A, 0x302F),
            CharacterClass.forChars(0x05BF, 0x05C4, 0x0670, 0x093C, 0x094D, 0x09BC, 0x09BE,
                                    0x09BF, 0x09D7, 0x0A02, 0x0A3C, 0x0A3E, 0x0A3F, 0x0ABC,
                                    0x0B3C, 0x0BD7, 0x0D57, 0x0E31, 0x0EB1, 0x0F35, 0x0F37,
                                    0x0F39, 0x0F3E, 0x0F3F, 0x0F97, 0x0FB9, 0x20E1, 0x3099,
                                    0x309A));
    private static final CharacterClass NON_ASCII_BASE = CharacterClass.forClasses(
            CharacterClass.forRange(0x00D8, 0x00F6), CharacterClass.forRange(0x00F8, 0x00FF),
            CharacterClass.forRange(0x0100, 0x0131), CharacterClass.forRange(0x0134, 0x013E),
            CharacterClass.forRange(0x0141, 0x0148), CharacterClass.forRange(0x014A, 0x017E),
            CharacterClass.forRange(0x0180, 0x01C3), CharacterClass.forRange(0x01CD, 0x01F0),
            CharacterClass.forRange(0x01F4, 0x01F5), CharacterClass.forRange(0x01FA, 0x0217),
            CharacterClass.forRange(0x0250, 0x02A8), CharacterClass.forRange(0x02BB, 0x02C1),
            CharacterClass.forRange(0x0388, 0x038A), CharacterClass.forRange(0x038E, 0x03A1),
            CharacterClass.forRange(0x03A3, 0x03CE), CharacterClass.forRange(0x03D0, 0x03D6),
            CharacterClass.forRange(0x03E2, 0x03F3), CharacterClass.forRange(0x0401, 0x040C),
            CharacterClass.forRange(0x040E, 0x044F), CharacterClass.forRange(0x0451, 0x045C),
            CharacterClass.forRange(0x045E, 0x0481), CharacterClass.forRange(0x0490, 0x04C4),
            CharacterClass.forRange(0x04C7, 0x04C8), CharacterClass.forRange(0x04CB, 0x04CC),
            CharacterClass.forRange(0x04D0, 0x04EB), CharacterClass.forRange(0x04EE, 0x04F5),
            CharacterClass.forRange(0x04F8, 0x04F9), CharacterClass.forRange(0x0531, 0x0556),
            CharacterClass.forRange(0x0561, 0x0586), CharacterClass.forRange(0x05D0, 0x05EA),
            CharacterClass.forRange(0x05F0, 0x05F2), CharacterClass.forRange(0x0621, 0x063A),
            CharacterClass.forRange(0x0641, 0x064A), CharacterClass.forRange(0x0671, 0x06B7),
            CharacterClass.forRange(0x06BA, 0x06BE), CharacterClass.forRange(0x06C0, 0x06CE),
            CharacterClass.forRange(0x06D0, 0x06D3), CharacterClass.forRange(0x06E5, 0x06E6),
            CharacterClass.forRange(0x0905, 0x0939), CharacterClass.forRange(0x0958, 0x0961),
            CharacterClass.forRange(0x0985, 0x098C), CharacterClass.forRange(0x098F, 0x0990),
            CharacterClass.forRange(0x0993, 0x09A8), CharacterClass.forRange(0x09AA, 0x09B0),
            CharacterClass.forRange(0x09B6, 0x09B9), CharacterClass.forRange(0x09DC, 0x09DD),
            CharacterClass.forRange(0x09DF, 0x09E1), CharacterClass.forRange(0x09F0, 0x09F1),
            CharacterClass.forRange(0x0A05, 0x0A0A), CharacterClass.forRange(0x0A0F, 0x0A10),
            CharacterClass.forRange(0x0A13, 0x0A28), CharacterClass.forRange(0x0A2A, 0x0A30),
            CharacterClass.forRange(0x0A32, 0x0A33), CharacterClass.forRange(0x0A35, 0x0A36),
            CharacterClass.forRange(0x0A38, 0x0A39), CharacterClass.forRange(0x0A59, 0x0A5C),
            CharacterClass.forRange(0x0A72, 0x0A74), CharacterClass.forRange(0x0A85, 0x0A8B),
            CharacterClass.forRange(0x0A8F, 0x0A91), CharacterClass.forRange(0x0A93, 0x0AA8),
            CharacterClass.forRange(0x0AAA, 0x0AB0), CharacterClass.forRange(0x0AB2, 0x0AB3),
            CharacterClass.forRange(0x0AB5, 0x0AB9), CharacterClass.forRange(0x0B05, 0x0B0C),
            CharacterClass.forRange(0x0B0F, 0x0B10), CharacterClass.forRange(0x0B13, 0x0B28),
            CharacterClass.forRange(0x0B2A, 0x0B30), CharacterClass.forRange(0x0B32, 0x0B33),
            CharacterClass.forRange(0x0B36, 0x0B39), CharacterClass.forRange(0x0B5C, 0x0B5D),
            CharacterClass.forRange(0x0B5F, 0x0B61), CharacterClass.forRange(0x0B85, 0x0B8A),
            CharacterClass.forRange(0x0B8E, 0x0B90), CharacterClass.forRange(0x0B92, 0x0B95),
            CharacterClass.forRange(0x0B99, 0x0B9A), CharacterClass.forRange(0x0B9E, 0x0B9F),
            CharacterClass.forRange(0x0BA3, 0x0BA4), CharacterClass.forRange(0x0BA8, 0x0BAA),
            CharacterClass.forRange(0x0BAE, 0x0BB5), CharacterClass.forRange(0x0BB7, 0x0BB9),
            CharacterClass.forRange(0x0C05, 0x0C0C), CharacterClass.forRange(0x0C0E, 0x0C10),
            CharacterClass.forRange(0x0C12, 0x0C28), CharacterClass.forRange(0x0C2A, 0x0C33),
            CharacterClass.forRange(0x0C35, 0x0C39), CharacterClass.forRange(0x0C60, 0x0C61),
            CharacterClass.forRange(0x0C85, 0x0C8C), CharacterClass.forRange(0x0C8E, 0x0C90),
            CharacterClass.forRange(0x0C92, 0x0CA8), CharacterClass.forRange(0x0CAA, 0x0CB3),
            CharacterClass.forRange(0x0CB5, 0x0CB9), CharacterClass.forRange(0x0CE0, 0x0CE1),
            CharacterClass.forRange(0x0D05, 0x0D0C), CharacterClass.forRange(0x0D0E, 0x0D10),
            CharacterClass.forRange(0x0D12, 0x0D28), CharacterClass.forRange(0x0D2A, 0x0D39),
            CharacterClass.forRange(0x0D60, 0x0D61), CharacterClass.forRange(0x0E01, 0x0E2E),
            CharacterClass.forRange(0x0E32, 0x0E33), CharacterClass.forRange(0x0E40, 0x0E45),
            CharacterClass.forRange(0x0E81, 0x0E82), CharacterClass.forRange(0x0E87, 0x0E88),
            CharacterClass.forRange(0x0E94, 0x0E97), CharacterClass.forRange(0x0E99, 0x0E9F),
            CharacterClass.forRange(0x0EA1, 0x0EA3), CharacterClass.forRange(0x0EAA, 0x0EAB),
            CharacterClass.forRange(0x0EAD, 0x0EAE), CharacterClass.forRange(0x0EB2, 0x0EB3),
            CharacterClass.forRange(0x0EC0, 0x0EC4), CharacterClass.forRange(0x0F40, 0x0F47),
            CharacterClass.forRange(0x0F49, 0x0F69), CharacterClass.forRange(0x10A0, 0x10C5),
            CharacterClass.forRange(0x10D0, 0x10F6), CharacterClass.forRange(0x1102, 0x1103),
            CharacterClass.forRange(0x1105, 0x1107), CharacterClass.forRange(0x110B, 0x110C),
            CharacterClass.forRange(0x110E, 0x1112), CharacterClass.forRange(0x1154, 0x1155),
            CharacterClass.forRange(0x115F, 0x1161), CharacterClass.forRange(0x116D, 0x116E),
            CharacterClass.forRange(0x1172, 0x1173), CharacterClass.forRange(0x11AE, 0x11AF),
            CharacterClass.forRange(0x11B7, 0x11B8), CharacterClass.forRange(0x11BC, 0x11C2),
            CharacterClass.forRange(0x1E00, 0x1E9B), CharacterClass.forRange(0x1EA0, 0x1EF9),
            CharacterClass.forRange(0x1F00, 0x1F15), CharacterClass.forRange(0x1F18, 0x1F1D),
            CharacterClass.forRange(0x1F20, 0x1F45), CharacterClass.forRange(0x1F48, 0x1F4D),
            CharacterClass.forRange(0x1F50, 0x1F57), CharacterClass.forRange(0x1F5F, 0x1F7D),
            CharacterClass.forRange(0x1F80, 0x1FB4), CharacterClass.forRange(0x1FB6, 0x1FBC),
            CharacterClass.forRange(0x1FC2, 0x1FC4), CharacterClass.forRange(0x1FC6, 0x1FCC),
            CharacterClass.forRange(0x1FD0, 0x1FD3), CharacterClass.forRange(0x1FD6, 0x1FDB),
            CharacterClass.forRange(0x1FE0, 0x1FEC), CharacterClass.forRange(0x1FF2, 0x1FF4),
            CharacterClass.forRange(0x1FF6, 0x1FFC), CharacterClass.forRange(0x212A, 0x212B),
            CharacterClass.forRange(0x2180, 0x2182), CharacterClass.forRange(0x3041, 0x3094),
            CharacterClass.forRange(0x30A1, 0x30FA), CharacterClass.forRange(0x3105, 0x312C),
            CharacterClass.forRange(0xAC00, 0xD7A3), CharacterClass.forRange(0x00C0, 0x00D6),
            CharacterClass.forChars(0x0386, 0x038C, 0x03DA, 0x03DC, 0x03DE, 0x03E0, 0x0559,
                                    0x06D5, 0x093D, 0x09B2, 0x0A5E, 0x0A8D, 0x0ABD, 0x0AE0,
                                    0x0B3D, 0x0B9C, 0x0CDE, 0x0E30, 0x0E84, 0x0E8A, 0x0E8D,
                                    0x0EA5, 0x0EA7, 0x0EB0, 0x0EBD, 0x1100, 0x1109, 0x113C,
                                    0x113E, 0x1140, 0x114C, 0x114E, 0x1150, 0x1159, 0x1163,
                                    0x1165, 0x1167, 0x1169, 0x1175, 0x119E, 0x11A8, 0x11AB,
                                    0x11BA, 0x11EB, 0x11F0, 0x11F9, 0x1F59, 0x1F5B, 0x1F5D,
                                    0x1FBE, 0x2126, 0x212E));
    private static final CharacterClass EXTENDER = CharacterClass.forClasses(
            CharacterClass.forRange(0x3031, 0x3035), CharacterClass.forRange(0x309D, 0x309E),
            CharacterClass.forRange(0x30FC, 0x30FE),
            CharacterClass.forChars(0x00B7, 0x02D0, 0x02D1, 0x0387, 0x0640, 0x0E46, 0x0EC6,
                                    0x3005));

    private static final CharacterClass LETTER = CharacterClass.forClasses(CharacterClasses.ASCII_LETTER,
                                                                           NON_ASCII_BASE,
                                                                           IDEOGRAPHIC);
    private static final CharacterClass DIGIT = CharacterClass.forClasses(CharacterClasses.DIGIT_ASCII,
                                                                          CharacterClasses.DIGIT_NON_ASCII);
    private static final CharacterClass NC_NAME = CharacterClass.forClasses(LETTER, DIGIT, COMBINING, EXTENDER,
                                                                            CharacterClass.forChars('.', '-', '_'));

    private String name;

    public NcName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public boolean isValid() {
        return isValid(this.name);
    }

    /**
     * Checks if {@code name} is a valid NCName.
     *
     * @param name the name
     *
     * @return if the name is valid
     */
    public static boolean isValid(String name) {
        if (name.isEmpty() || (name.charAt(0) != '_' && !LETTER.includes(name.charAt(0)))) {
            return false;
        }
        for (int i = 1; i < name.length(); ++i) {
            if (!NC_NAME.includes(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void makeValid(char replacement) {
        this.name = makeValid(name, replacement);
    }

    public void makeValid() {
        this.name = makeValid(name);
    }

    /**
     * Replaces all invalid characters in {@code name} with {@code replacement}.
     *
     * @param name        the name
     * @param replacement the replacement character
     *
     * @return the valid NCName
     */
    public static String makeValid(String name, char replacement) {
        if (replacement != '_' && !LETTER.includes(replacement)) {
            throw new IllegalArgumentException();
        }
        if (isValid(name)) {
            return name;
        }
        StringBuilder builder = new StringBuilder(name.length());
        char c = name.charAt(0);
        if (c == '_' || LETTER.includes(c)) {
            builder.append(c);
        } else {
            builder.append(replacement);
        }
        for (int i = 1; i < name.length(); ++i) {
            c = name.charAt(i);
            if (NC_NAME.includes(c)) {
                builder.append(c);
            } else {
                builder.append(replacement);
            }
        }
        return builder.toString();
    }

    /**
     * Replaces all invalid characters in {@code name} with an underscore.
     *
     * @param name the name
     *
     * @return the valid NCName
     */
    public static String makeValid(String name) {
        return makeValid(name, '_');
    }

}
