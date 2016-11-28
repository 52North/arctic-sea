/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.util.http;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.binding.EncodingExceptionHandler;
import org.n52.iceland.coding.encode.ResponseProxy;
import org.n52.iceland.coding.encode.ResponseWriter;
import org.n52.iceland.coding.encode.ResponseWriterRepository;
import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.event.ServiceEventBus;
import org.n52.iceland.event.events.CountingOutputStreamEvent;
import org.n52.iceland.exception.HTTPException;
import org.n52.iceland.request.ResponseFormat;
import org.n52.iceland.response.ServiceResponse;
import org.n52.iceland.service.MiscSettings;
import org.n52.janmayen.http.HTTPConstants;
import org.n52.janmayen.http.HTTPHeaders;
import org.n52.janmayen.http.HTTPStatus;
import org.n52.janmayen.http.MediaType;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.io.CountingOutputStream;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
@Configurable
public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private Boolean isCountingOutputStream = false;

    private ServiceEventBus eventBus;

    public ServiceEventBus getEventBus() {
        return eventBus;
    }

    @Inject
    public void setEventBus(ServiceEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public Boolean getIsCountingOutputStream() {
        return isCountingOutputStream;
    }

    @Setting(MiscSettings.STATISTICS_COUNTING_OUTPUTSTREAM)
    public void setIsCountingOutputStream(Boolean isCountingOutputStream) {
        this.isCountingOutputStream = isCountingOutputStream;
    }

    @Deprecated
    public static boolean supportsGzipEncoding(HttpServletRequest req) {
        return HTTPHeaders.supportsGzipEncoding(req);
    }

    @Deprecated
    public static boolean isGzipEncoded(HttpServletRequest req) {
        return HTTPHeaders.isGzipEncoded(req);
    }

    @Deprecated
    public static List<MediaType> getAcceptHeader(HttpServletRequest req) {
        return HTTPHeaders.getAcceptHeader(req);
    }

    public static InputStream getInputStream(HttpServletRequest req) throws IOException {
        if (HTTPHeaders.isGzipEncoded(req)) {
            return new GZIPInputStream(req.getInputStream());
        } else {
            return req.getInputStream();
        }
    }

    public void writeObject(HttpServletRequest request, HttpServletResponse response, MediaType contentType,
            Object object, EncodingExceptionHandler owserHandler) throws IOException, HTTPException {
        writeObject(request, response, contentType, new GenericWritable(object, contentType), owserHandler);
    }

    public void writeObject(HttpServletRequest request, HttpServletResponse response, ServiceResponse sr,
            EncodingExceptionHandler owserHandler) throws IOException, HTTPException {
        response.setStatus(sr.getStatus().getCode());

        sr.getHeaderMap().forEach(response::addHeader);

        if (!sr.isContentLess()) {
            writeObject(request, response, sr.getContentType(), new ServiceResponseWritable(sr), owserHandler);
        }
    }

    public void writeObject(HttpServletRequest request, HttpServletResponse response, MediaType contentType,
            Writable writable, EncodingExceptionHandler owserHandler) throws IOException, HTTPException {
        OutputStream out = null;
        response.setContentType(writable.getEncodedContentType().toString());

        try {
            out = response.getOutputStream();
            if (HTTPHeaders.supportsGzipEncoding(request) && writable.supportsGZip()) {
                out = new GZIPOutputStream(out);
                response.setHeader(HTTPHeaders.CONTENT_ENCODING, HTTPConstants.GZIP_ENCODING);
            }
            if (isCountingOutputStream) {
                out = new CountingOutputStream(out);
            }

            if (writable.hasForcedHttpStatus()) {
                response.setStatus(writable.getForcedHttpStatus().getCode());
            }

            writable.write(out, new ResponseProxy(response));
            out.flush();
        } catch (EncodingException e) {
            Object writeOwsExceptionReport = owserHandler.handleEncodingException(request, response, e);
            if (writeOwsExceptionReport != null) {
                Writable owserWritable = getWritable(writeOwsExceptionReport, contentType);
                try {
                    owserWritable.write(out, new ResponseProxy(response));
                    if (out != null) {
                        out.flush();
                    }
                } catch (EncodingException ex) {
                    throw new HTTPException(HTTPStatus.INTERNAL_SERVER_ERROR, ex);
                }
            }
        } finally {
            if (out instanceof CountingOutputStream) {
                Long bytesWritten = ((CountingOutputStream) out).getCount();
                eventBus.submit(new CountingOutputStreamEvent(bytesWritten));
            }
            if (out != null) {
                LOGGER.debug("Response status = "+response.getStatus());
                out.close();
            }

        }
    }

    private static Writable getWritable(Object writeOwsExceptionReport, MediaType contentType) {
        if (writeOwsExceptionReport instanceof ServiceResponse) {
            return new ServiceResponseWritable((ServiceResponse) writeOwsExceptionReport);
        }
        return new GenericWritable(writeOwsExceptionReport, contentType);
    }

    private static class GenericWritable implements Writable {

        private final Object o;

        private final ResponseWriter<Object> writer;

        /**
         * constructor
         *
         * @param o
         *            {@link Object} to write
         * @param ct
         *            contentType to encode to
         */
        GenericWritable(Object o, MediaType ct) {
            this.o = o;
            writer = ResponseWriterRepository.getInstance().getWriter(o.getClass());
            if (writer == null) {
                throw new RuntimeException("no writer for " + o.getClass() + " found!");
            }
            writer.setContentType(ct);
        }

        @Override
        public boolean hasForcedHttpStatus() {
            return writer.hasForcedHttpStatus(this.o);
        }

        @Override
        public HTTPStatus getForcedHttpStatus() {
            return writer.getForcedHttpStatus(this.o);
        }

        @Override
        public boolean supportsGZip() {
            return writer.supportsGZip(o);
        }

        @Override
        public void write(OutputStream out, ResponseProxy responseProxy) throws IOException, EncodingException {
            writer.write(o, out, responseProxy);
        }

        @Override
        public MediaType getEncodedContentType() {
            if (o instanceof ResponseFormat) {
                return writer.getEncodedContentType((ResponseFormat) o);
            }
            return writer.getContentType();
        }
    }

    private static class ServiceResponseWritable implements Writable {

        private final ServiceResponse response;

        ServiceResponseWritable(ServiceResponse response) {
            this.response = response;
        }

        @Override
        public void write(OutputStream out, ResponseProxy responseProxy) throws IOException {
            // set content length if not gzipped
            if (!(out instanceof GZIPOutputStream) && response.getContentLength() > -1) {
                responseProxy.setContentLength(response.getContentLength());
            }
            response.writeToOutputStream(out);
        }

        @Override
        public boolean supportsGZip() {
            return response.supportsGZip();
        }

        @Override
        public MediaType getEncodedContentType() {
            return response.getContentType();
        }
    }

    public interface Writable {

        void write(OutputStream out, ResponseProxy responseProxy) throws IOException, EncodingException;

        boolean supportsGZip();

        MediaType getEncodedContentType();

        default boolean hasForcedHttpStatus() {
            return false;
        };

        default HTTPStatus getForcedHttpStatus() {
            return HTTPStatus.OK;
        };
    }

}
