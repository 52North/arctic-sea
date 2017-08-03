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
package org.n52.svalbard.encode.json;

import org.n52.janmayen.Json;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.exception.CodedException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionCode;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.ExceptionEncoderKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class OwsExceptionReportEncoder
        extends JSONEncoder<OwsExceptionReport> {

    public static final char LF = '\n';
    public static final String LINE = "line";
    public static final String CLASS = "class";
    public static final String FILE = "file";
    public static final String METHOD = "method";
    public static final String STACK_TRACE = "stackTrace";
    private static final Logger log = LoggerFactory.getLogger(OwsExceptionReportEncoder.class);

    public OwsExceptionReportEncoder() {
        super(OwsExceptionReport.class, new ExceptionEncoderKey(MediaTypes.APPLICATION_JSON));
    }

    @Override
    public JsonNode encodeJSON(OwsExceptionReport t)
            throws JSONEncodingException {
        final ObjectNode exceptionReport = Json.nodeFactory().objectNode();
        exceptionReport.put(JSONConstants.VERSION, t.getVersion());
        final ArrayNode exceptions = exceptionReport.putArray(JSONConstants.EXCEPTIONS);
        for (CodedException ce : t.getExceptions()) {
            final ObjectNode exception = exceptions.addObject();
            exception.put(JSONConstants.CODE,
                    ce.getCode() != null ? ce.getCode().toString() : OwsExceptionCode.NoApplicableCode.toString());
            if (ce.getLocator() != null && !ce.getLocator().isEmpty()) {
                exception.put(JSONConstants.LOCATOR, ce.getLocator());
            }
            final String message = getExceptionText(ce);
            if (message != null && !message.isEmpty()) {
                exception.put(JSONConstants.TEXT, message);
            }
            if (log.isDebugEnabled()) {
                exception.set(STACK_TRACE, encodeStackTrace(ce));
            }
        }
        return exceptionReport;
    }

    protected String getExceptionText(CodedException ce) {
        final StringBuilder exceptionText = new StringBuilder();
        if (ce.getMessage() != null) {
            exceptionText.append(ce.getMessage());
        }
        if (ce.getCause() != null) {
            if (exceptionText.length() > 0) {
                exceptionText.append(LF);
            }
            exceptionText.append("[EXCEPTION]: ").append(LF);
            String localizedMessage = ce.getCause().getLocalizedMessage();
            String message = ce.getCause().getMessage();
            if (localizedMessage != null && message != null) {
                if (!message.equals(localizedMessage)) {
                    exceptionText.append(message).append(LF);
                }
                exceptionText.append(localizedMessage).append(LF);
            } else {
                exceptionText.append(localizedMessage).append(LF);
                exceptionText.append(message).append(LF);
            }
        }
        return exceptionText.toString();
    }

    private JsonNode encodeStackTrace(Throwable t) {
        ArrayNode json = nodeFactory().arrayNode();
        for (StackTraceElement e : t.getStackTrace()) {
            json.add(String.format("%s.%s(:%d)", e.getClassName(), e.getMethodName(), e.getLineNumber()));
        }
        return json;
    }
}
