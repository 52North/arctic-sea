/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.decode;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.opengis.ows.x11.ExceptionReportDocument;
import net.opengis.ows.x11.ExceptionReportDocument.ExceptionReport;
import net.opengis.ows.x11.ExceptionType;

import org.n52.janmayen.stream.Streams;
import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.exception.CodedException;
import org.n52.shetland.ogc.ows.exception.CompositeOwsException;
import org.n52.shetland.ogc.ows.exception.ExceptionCode;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.svalbard.decode.exception.DecodingException;

/**
 * Decoder for OWS v1.1.0 Exception Reports.
 *
 * @author Christian Autermann
 */
public class OwsExceptionReportDecoder extends AbstractXmlDecoder<ExceptionReportDocument, OwsExceptionReport> {

    private static final DecoderKey KEY = new XmlNamespaceDecoderKey(OWSConstants.NS_OWS,
                                                                     ExceptionReportDocument.class);

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.singleton(KEY);
    }

    @Override
    public OwsExceptionReport decode(ExceptionReportDocument doc) throws DecodingException {
        return decode(doc.getExceptionReport());
    }

    private OwsExceptionReport decode(ExceptionReport report) {
        String version = report.getVersion();
        ExceptionType[] exceptionTypes = report.getExceptionArray();
        List<CodedException> exceptions = Streams.stream(exceptionTypes).map(this::decode).collect(toList());
        return new CompositeOwsException(exceptions).setVersion(version);
    }

    private CodedException decode(ExceptionType e) {
        String code = e.getExceptionCode();
        String locator = e.getLocator();
        String message = String.join("\n", e.getExceptionTextArray());
        return new GenericCodedException(code).at(locator).withMessage(message);
    }

    private static class GenericExceptionCode implements ExceptionCode {
        private final String code;

        GenericExceptionCode(String code) {
            this.code = code;
        }

        @Override
        public String getSoapFaultReason() {
            return this.code;
        }

        @Override
        public String toString() {
            return this.code;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.code);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final GenericExceptionCode other = (GenericExceptionCode) obj;
            return Objects.equals(getCode(), other.getCode());
        }

        public String getCode() {
            return code;
        }
    }

    private static class GenericCodedException extends CodedException {
        private static final long serialVersionUID = 8443662702566120820L;

        GenericCodedException(String code) {
            super(new GenericExceptionCode(code));
        }

    }

}
