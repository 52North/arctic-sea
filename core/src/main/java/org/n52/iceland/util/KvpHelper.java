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
package org.n52.iceland.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.n52.iceland.binding.kvp.AbstractKvpDecoder;
import org.n52.iceland.request.operator.RequestOperatorKey;
import org.n52.iceland.request.operator.RequestOperatorRepository;
import org.n52.shetland.ogc.ows.OWSConstants.RequestParams;
import org.n52.shetland.ogc.ows.exception.InvalidParameterValueException;
import org.n52.shetland.ogc.ows.exception.MissingParameterValueException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;

/**
 * Utility class for Key-Value-Pair (KVP) requests
 *
 * @since 1.0.0
 * @deprecated use {@link AbstractKvpDecoder}
 *
 */
@Deprecated
public final class KvpHelper {
    private KvpHelper() {
    }

    public static String checkParameterSingleValue(String value, String name) throws OwsExceptionReport {
        if (checkParameterMultipleValues(value, name).size() == 1) {
            return value;
        } else {
            throw new InvalidParameterValueException(name, value);
        }
    }

    public static String checkParameterSingleValue(String value, Enum<?> name) throws OwsExceptionReport {
        return checkParameterSingleValue(value, name.name());
    }

    public static URI checkParameterSingleURI(String values, String name) throws OwsExceptionReport {
        String value = checkParameterSingleValue(values, name);
        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            throw new InvalidParameterValueException().at(name).causedBy(e)
                    .withMessage("Cannot parse provided value '%s' to URI", value);
        }
    }

    public static boolean checkParameterBooleanValue(String value, String name) throws OwsExceptionReport {
        checkParameterValue(value, name);
        return Boolean.valueOf(value);
    }

    public static List<String> checkParameterMultipleValues(String values, String name)
            throws OwsExceptionReport {
        if (values.isEmpty()) {
            throw new MissingParameterValueException(name);
        }
        List<String> splittedParameterValues = Arrays.asList(values.split(","));
        for (String parameterValue : splittedParameterValues) {
            if (Strings.isNullOrEmpty(parameterValue)) {
                throw new MissingParameterValueException(name);
            }
        }
        return splittedParameterValues;
    }

    public static List<String> checkParameterMultipleValues(String values, Enum<?> name)
            throws OwsExceptionReport {
        return checkParameterMultipleValues(values, name.name());
    }

    public static void checkParameterMultipleValues(List<String> values, String name)
            throws OwsExceptionReport {
        if (CollectionHelper.isEmpty(values)) {
            throw new MissingParameterValueException(name);
        }
        for (String parameterValue : values) {
            if (Strings.isNullOrEmpty(parameterValue)) {
                throw new MissingParameterValueException(name);
            }
        }
    }

    public static void checkParameterValue(String value, String name) throws OwsExceptionReport {
        if (Strings.isNullOrEmpty(value)) {
            throw new MissingParameterValueException(name);
        }
    }

    public static void checkParameterValue(String value, Enum<?> name) throws OwsExceptionReport {
        checkParameterValue(value, name.name());
    }

    private static String getParameterValue(String name, Map<String, String> map) {
        return map.computeIfAbsent(name,
                                   key -> map.entrySet().stream()
                                           .filter(e -> e.getKey().equalsIgnoreCase(key))
                                           .findFirst().map(Entry::getValue).orElse(null));
    }

    public static String getParameterValue(Enum<?> name, Map<String, String> map) {
        return getParameterValue(name.name(), map);
    }

    /**
     * Perform a sanity check on the request parameter without considering version.
     *
     * @param value the value
     *
     * @throws OwsExceptionReport if the operation could not be found
     * @deprecated use injection to get the {@link RequestOperatorRepository}.
     */
    @Deprecated
    public static void checkRequestParameter(String value) throws OwsExceptionReport {
        for (RequestOperatorKey rok : RequestOperatorRepository.getInstance().getAllRequestOperatorKeys()) {
            if (value.equals(rok.getOperationName())) {
                return;
            }
        }
        throw new InvalidParameterValueException(RequestParams.request, value);
    }
}
