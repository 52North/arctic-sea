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
package org.n52.iceland.statistics.api.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public final class FileDownloader {

    private static final Logger LOG = LoggerFactory.getLogger(FileDownloader.class);

    private FileDownloader() {
    }

    /**
     * Download the url to the specified location
     *
     * @param url url to download
     * @param outfilePath outputfile location
     *
     * @throws FileNotFoundException if an error occurs
     * @throws IOException           if an error occurs
     */
    public static void downloadFile(String url, String outfilePath) throws FileNotFoundException, IOException {
        Objects.requireNonNull(url);
        Objects.requireNonNull(outfilePath);

        URL fileUrl = new URL(url);
        File out = new File(outfilePath);
        LOG.info("Downloading file {} to {}", fileUrl, out);
        FileUtils.copyURLToFile(fileUrl, out);
    }

    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION")
    public static void gunzipFile(String filePath) throws IOException {
        File file = new File(filePath);
        String outPath = null;
        final byte[] buff = new byte[1024];

        if (!file.getName().endsWith("gz")) {
            throw new IOException("File is not ends with .gz extension");
        } else {
            outPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 3);
        }

        try (FileOutputStream fout = new FileOutputStream(outPath);
             FileInputStream fin = new FileInputStream(file);
             BufferedInputStream bin = new BufferedInputStream(fin);
             GzipCompressorInputStream gzipin = new GzipCompressorInputStream(bin);) {
            int n = 0;
            while (-1 != (n = gzipin.read(buff))) {
                fout.write(buff, 0, n);
            }
            LOG.debug("Extracted file path {}", outPath);
        } catch (IOException e) {
            throw e;
        }
    }

    public static boolean isPathExists(String fullPath) {
        if (fullPath == null) {
            return false;
        }
        return new File(fullPath).exists();
    }
}
