/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.iceland.coding.encode;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Proxy class for HttpServletResponse to give ResponseWriters access to selected methods, including addHeader and
 * setContentLength.
 *
 * @author <a href="mailto:shane@axiomalaska.com">Shane StClair</a>
 *
 * @since 1.0.0
 */
public class ResponseProxy {
    private final HttpServletResponse response;

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public ResponseProxy(HttpServletResponse response) throws IOException {
        if (response == null) {
            throw new NullPointerException("Response cannot be null");
        }
        this.response = response;
    }

    public void addHeader(String headerIdentifier, String headerValue) {
        response.addHeader(headerIdentifier, headerValue);
    }

    public void setContentLength(int contentLength) {
        response.setContentLength(contentLength);
    }
}
