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
package org.n52.shetland.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.joda.time.DateTime;

/**
 * Class to generate an id by using MessageDigest SHA-256 and a random double
 *
 * @since 7.3.0
 *
 */
public class IdGenerator {

    /**
     * Message digest for generating single identifier
     */
    private static final MessageDigest MESSAGE_DIGEST;
    private static final Random RANDOM;

    /**
     * Instantiation of the message digest
     */
    static {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance("SHA-256");
            RANDOM = new Random();
        } catch (final NoSuchAlgorithmException nsae) {
            throw new Error("Error while getting SHA-256 messagedigest!", nsae);
        }
    }

    /**
     * Generates a sensor id from description and current time as long.
     *
     * @param message
     *                sensor description
     *
     * @return generated sensor id as hex SHA-256.
     */
    public static String generate(String message) {
        final long autoGeneratredID = new DateTime().getMillis();
        final String concate = RANDOM.nextDouble() + message + Long.toString(autoGeneratredID);
        return bytesToHex(MESSAGE_DIGEST.digest(concate.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Transforms byte to hex representation
     *
     * @param bytes
     *          bytes
     *
     * @return hex
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder buffer = new StringBuilder(2 * bytes.length);
        for (int i = 0; i < bytes.length; ++i) {
            buffer.append(Character.forDigit((bytes[i] >> 4) & 0xF, 16));
            buffer.append(Character.forDigit(bytes[i] & 0xF, 16));
        }
        return buffer.toString();
    }

}
