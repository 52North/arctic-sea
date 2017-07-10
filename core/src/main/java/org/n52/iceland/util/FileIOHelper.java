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
package org.n52.iceland.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.n52.iceland.exception.ows.concrete.GenericThrowableWrapperException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

/**
 * Utility class for file handling
 *
 * @since 1.0.0
 *
 */
public final class FileIOHelper {

    private static final byte LINE_FEED = (byte) '\n';

    private static final byte CARRIAGE_RETURN = (byte) '\r';

    private static final String READ_MODE = "r";

    private FileIOHelper() {
    }

    /**
     * Loads a file and returns an InputStream
     *
     *
     *
     * @param file File to load
     *
     * @return InputStream of the file
     *
     * @throws OwsExceptionReport If and error occurs;
     * @deprecated use {@link FileInputStream}
     */
    @Deprecated
    public static InputStream loadInputStreamFromFile(File file) throws OwsExceptionReport {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException fnfe) {
            throw new GenericThrowableWrapperException(fnfe);
        }
    }

    /* TODO refactor this */
    public static List<String> tail(File file, int lines) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, READ_MODE)) {
            final long length = file.length() - 1;
            ArrayList<String> out = new ArrayList<String>(lines);
            StringBuilder sb = new StringBuilder();
            byte prev = -1;
            for (long pos = length; pos != -1; pos--) {
                raf.seek(pos);
                byte b = raf.readByte();
                try {
                    if (b == CARRIAGE_RETURN) {
                        continue;
                    } else if (b == LINE_FEED) {
                        if (pos == length || pos == length - 1 || prev == CARRIAGE_RETURN) {
                            continue;
                        }
                        out.add(sb.reverse().toString());
                        sb = null;
                        if (out.size() == lines) {
                            break;
                        } else {
                            sb = new StringBuilder();
                        }
                    } else {
                        sb.append((char) b);
                    }
                } finally {
                    prev = b;
                }
            }
            if (sb != null) {
                out.add(sb.reverse().toString());
            }
            Collections.reverse(out);
            return out;
        }
    }

}
