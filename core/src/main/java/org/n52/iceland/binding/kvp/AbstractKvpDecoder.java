/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.binding.kvp;

import java.util.Map;

import org.n52.iceland.coding.decode.Decoder;
import org.n52.iceland.coding.decode.DecoderKey;
import org.n52.iceland.coding.decode.DecodingException;
import org.n52.iceland.coding.decode.OperationDecoderKey;
import org.n52.iceland.coding.decode.OwsDecodingException;
import org.n52.iceland.exception.ows.CompositeOwsException;
import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.util.http.MediaTypes;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public abstract class AbstractKvpDecoder<T> implements Decoder<T, Map<String, String>> {

    @Override
    public T decode(Map<String, String> parameters)
            throws DecodingException {
        CompositeOwsException exceptions = new CompositeOwsException();
        T t = createRequest();
        parameters.forEach((name, value) -> {
            try {
                decodeParameter(t, name, value);
            } catch (OwsExceptionReport e) {
                exceptions.add(e);
            }
        });
        try {
            exceptions.throwIfNotEmpty();
        } catch (CompositeOwsException ex) {
            throw new OwsDecodingException(ex);
        }
        return t;
    }

    protected abstract T createRequest();

    protected abstract void decodeParameter(T request, String name, String values)
            throws OwsExceptionReport;

    protected static DecoderKey createKey(String service, String version, String operation) {
        return new OperationDecoderKey(service, version, operation, MediaTypes.APPLICATION_KVP);
    }

    protected static DecoderKey createKey(String service, String version, Enum<?> operation) {
        return createKey(service, version, operation.name());
    }

    protected static OwsExceptionReport unsupportedParameter(String name, String value) {
        return new InvalidParameterValueException(name, value)
                .withMessage("The parameter '%s' is not supported.", name);
    }
}
