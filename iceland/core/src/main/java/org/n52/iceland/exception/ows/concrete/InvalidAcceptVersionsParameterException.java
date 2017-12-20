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
package org.n52.iceland.exception.ows.concrete;

import java.util.Arrays;
import java.util.List;

import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.exception.VersionNegotiationFailedException;

import com.google.common.base.Joiner;


/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class InvalidAcceptVersionsParameterException extends VersionNegotiationFailedException {

    private static final long serialVersionUID = -4208117985311582007L;

    @SuppressWarnings("ThrowableResultIgnored")
    public InvalidAcceptVersionsParameterException(String... acceptVersions) {
        this(Arrays.asList(acceptVersions));
    }

    @SuppressWarnings("ThrowableResultIgnored")
    public InvalidAcceptVersionsParameterException(List<String> acceptVersions) {
        withMessage("The requested %s values (%s) are not supported by this service!",
                OWSConstants.GetCapabilitiesParams.AcceptVersions, Joiner.on(", ").join(acceptVersions));
    }
}
