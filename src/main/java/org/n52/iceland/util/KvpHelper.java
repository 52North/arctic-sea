/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.exception.ows.MissingParameterValueException;
import org.n52.iceland.ogc.ows.OWSConstants.RequestParams;
import org.n52.iceland.request.operator.RequestOperatorKey;
import org.n52.iceland.request.operator.RequestOperatorRepository;

import com.google.common.base.Strings;
import java.net.URI;
import java.net.URISyntaxException;
import org.n52.iceland.exception.CodedException;

/**
 * Utility class for Key-Value-Pair (KVP) requests
 *
 * @since 4.0.0
 *
 */
public final class KvpHelper {
    private KvpHelper() {
    }

    public static Map<String, String> getKvpParameterValueMap(HttpServletRequest req) {
        Map<String, String> kvp = new HashMap<>();
        Enumeration<?> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            // all key names to lower case
            String key = (String) parameterNames.nextElement();
            kvp.put(key.replace("amp;", "").toLowerCase(Locale.ROOT), req.getParameter(key));
        }
        return kvp;
    }

    public static String checkParameterSingleValue(String value, String name) throws CodedException {
        if (checkParameterMultipleValues(value, name).size() == 1) {
            return value;
        } else {
            throw new InvalidParameterValueException(name, value);
        }
    }

    public static String checkParameterSingleValue(String value, Enum<?> name) throws CodedException {
        return checkParameterSingleValue(value, name.name());
    }

    public static URI checkParameterSingleURI(String values, String name) throws CodedException {
        String value = checkParameterSingleValue(values, name);
        try {
            URI uri = new URI(value);
            return uri;
        } catch (URISyntaxException e) {
            throw new InvalidParameterValueException(name, values).withMessage("Cannot parse provided value '%s' to URI", value).causedBy(e);
        }
    }
    
    public static boolean checkParameterBooleanValue(String value, String name) throws CodedException {
        checkParameterValue(value, name);
        return Boolean.valueOf(value);
    }

    public static List<String> checkParameterMultipleValues(String values, String name)
            throws MissingParameterValueException {
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
            throws MissingParameterValueException {
        return checkParameterMultipleValues(values, name.name());
    }

    public static void checkParameterMultipleValues(List<String> values, String name)
            throws MissingParameterValueException {
        if (CollectionHelper.isEmpty(values)) {
            throw new MissingParameterValueException(name);
        }
        for (String parameterValue : values) {
            if (Strings.isNullOrEmpty(parameterValue)) {
                throw new MissingParameterValueException(name);
            }
        }
    }

    public static void checkParameterValue(String value, String name) throws CodedException {
        if (Strings.isNullOrEmpty(value)) {
            throw new MissingParameterValueException(name);
        }
    }

    public static void checkParameterValue(String value, Enum<?> name) throws CodedException {
        checkParameterValue(value, name.name());
    }

    private static String getParameterValue(String name, Map<String, String> map) {
        return map.computeIfAbsent(name, key
                -> map.entrySet().stream()
                .filter(e -> e.getKey().equalsIgnoreCase(key))
                .findFirst().map(Entry::getValue).orElse(null)
        );
    }

    public static String getParameterValue(Enum<?> name, Map<String, String> map) {
        return getParameterValue(name.name(), map);
    }

    /**
     * Perform a sanity check on the request parameter without considering version.
     *
     * @param value
     * @throws InvalidParameterValueException
     */
    @Deprecated
    public static void checkRequestParameter(String value) throws InvalidParameterValueException {
        for (RequestOperatorKey rok : RequestOperatorRepository.getInstance().getAllRequestOperatorKeys()) {
            if (value.equals(rok.getOperationName())) {
                return;
            }
        }
        throw new InvalidParameterValueException(RequestParams.request, value);
    }
}
