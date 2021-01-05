/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.encode.exception;

import com.google.common.base.Joiner;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class NotYetSupportedEncodingException extends EncodingException {
    private static final long serialVersionUID = 8214490617892996058L;

    public NotYetSupportedEncodingException(String feature) {
        super("%s is not yet supported", feature);
    }

    public NotYetSupportedEncodingException(String type, Object feature) {
        super("The %s %s is not yet supported", type, feature);
    }

    public NotYetSupportedEncodingException(String type, Object feature, Object... supportedFeatures) {
        super("The %s %s is not yet supported. Currently supported: %s", type, feature,
                Joiner.on(", ").join(supportedFeatures));
    }
}
