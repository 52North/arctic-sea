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
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileDownloader {

    private static final Logger LOG = LoggerFactory.getLogger(FileDownloader.class);

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

    public static void gunzipFile(String filePath) throws IOException {
        File file = new File(filePath);
        String outPath = null;
        final byte[] buff = new byte[1024];

        if (!file.getName().endsWith("gz")) {
            throw new IOException("File is not ends with .gz extension");
        } else {
            outPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 3);
        }

        FileOutputStream out = null;
        GzipCompressorInputStream gzFile = null;
        try {
            out = new FileOutputStream(outPath);
            gzFile = new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(file)));
            int n = 0;
            while (-1 != (n = gzFile.read(buff))) {
                out.write(buff, 0, n);
            }
            LOG.debug("Extracted file path {}", outPath);
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(gzFile);
        }
    }

    public static boolean isPathExists(String fullPath) {
        if (fullPath == null) {
            return false;
        }
        return new File(fullPath).exists();
    }
}
