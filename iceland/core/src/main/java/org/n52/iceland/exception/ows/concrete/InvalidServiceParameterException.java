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
package org.n52.iceland.exception.ows.concrete;

import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.exception.InvalidParameterValueException;

/**
 * @since 1.0.0
 *
 */
public class InvalidServiceParameterException extends InvalidParameterValueException {
    private static final long serialVersionUID = 4979630437608155123L;

    public InvalidServiceParameterException(String value) {
        at(OWSConstants.GetCapabilitiesParams.service);
        withMessage("The value of the mandatory parameter '%s' is invalid. Delivered value was: %s",
                OWSConstants.GetCapabilitiesParams.service, value);
    }
}
