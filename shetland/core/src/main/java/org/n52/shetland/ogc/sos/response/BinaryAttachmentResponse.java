/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.sos.response;

import org.n52.janmayen.http.MediaType;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Simple response class for binary data to be included as a response attachment (for download)
 *
 * @author <a href="mailto:shane@axiomalaska.com">Shane StClair</a>
 *
 * @since 1.0.0
 */
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class BinaryAttachmentResponse {
    private byte[] bytes;
    private MediaType contentType;
    private String filename;

    public BinaryAttachmentResponse(byte[] bytes, MediaType contentType, String filename) {
        this.bytes = bytes;
        this.contentType = contentType;
        this.filename = filename;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public MediaType getContentType() {
        return contentType;
    }

    public void setContentType(MediaType contentType) {
        this.contentType = contentType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getSize() {
        return bytes == null ? -1 : bytes.length;
    }

    @Override
    public String toString() {
        return "BinaryAttachmentResponse [size = " + getSize()
                + ", contentType=" + contentType
                + ", filename=" + filename + "]";
    }
}
