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
package org.n52.svalbard.encode;

import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.CodingSettings;
import org.n52.svalbard.ConformanceClass;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.N52XmlHelper;
import org.n52.svalbard.util.XmlHelper;


/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 5.0.0
 *
 *
 * @param <T> the response type
 */
@Configurable
public abstract class AbstractXmlResponseEncoder<T> extends AbstractXmlEncoder<XmlObject, T>
        implements StreamingEncoder<XmlObject, T>, ConformanceClass {

    private final String namespace;
    private final String prefix;
    private final String version;
    private final Class<T> responseType;
    private boolean validate;
    private SchemaRepository schemaRepository;

    /**
     * constructor
     *
     * @param service      Service
     * @param version      Service version
     * @param operation    Service operation name
     * @param namespace    Service XML schema namespace
     * @param prefix       Service XML schema prefix
     * @param responseType Response type
     * @param validate     Indicator if the created/encoded object should be validated
     */
    public AbstractXmlResponseEncoder(String service, String version, String operation, String namespace,
                                      String prefix, Class<T> responseType, boolean validate) {
        this.namespace = namespace;
        this.prefix = prefix;
        this.version = version;
        this.responseType = responseType;
        this.validate = validate;
    }

    /**
     * constructor
     *
     * @param service      Service
     * @param version      Service version
     * @param operation    Service operation name
     * @param namespace    Service XML schema namespace
     * @param prefix       Service XML schema prefix
     * @param responseType Response type
     */
    public AbstractXmlResponseEncoder(String service, String version, String operation, String namespace,
                                      String prefix, Class<T> responseType) {
        this(service, version, operation, namespace, prefix, responseType, false);
    }

    @Inject
    public void setSchemaRepository(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    public SchemaRepository getSchemaRepository() {
        return schemaRepository;
    }

    @Setting(CodingSettings.VALIDATE_RESPONSE)
    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        if (nameSpacePrefixMap != null) {
            nameSpacePrefixMap.put(this.namespace, this.prefix);
        }
    }

    @Override
    public XmlObject encode(T response) throws EncodingException {
        if (response == null) {
            throw new UnsupportedEncoderInputException(this, (String) null);
        }
        return encode(response, EncodingContext.empty());
    }

    @Override
    public XmlObject encode(T response, EncodingContext additionalValues) throws EncodingException {
        if (response == null) {
            throw new UnsupportedEncoderInputException(this, (String) null);
        }
        XmlObject xml = create(response);
        setSchemaLocations(xml);
        if (validate) {
            XmlHelper.validateDocument(xml, EncodingException::new);
        }
        return xml;
    }

    private void setSchemaLocations(XmlObject document) {
        Map<String, SchemaLocation> schemaLocations = getSchemaLocations(document)
                .collect(toMap(SchemaLocation::getNamespace, Function.identity()));
        schemaLocations.putAll(getSchemaLocations().stream()
                .collect(toMap(SchemaLocation::getNamespace, Function.identity())));
        schemaLocations.putAll(getConcreteSchemaLocations(XmlHelper.getNamespace(document)).stream()
                .collect(toMap(SchemaLocation::getNamespace, Function.identity())));
        N52XmlHelper.setSchemaLocationsToDocument(document, schemaLocations.values());
    }

    private Stream<SchemaLocation> getSchemaLocations(XmlObject document) {
        return N52XmlHelper.getNamespaces(document).stream().map(this.schemaRepository::getSchemaLocation)
                .filter(Objects::nonNull).flatMap(Set::stream);
    }

    /**
     * Get the concrete schema locations for this {@link OwsServiceResponse} encoder
     *
     * @return the concrete schema locations
     */
    protected abstract Set<SchemaLocation> getConcreteSchemaLocations();

    protected Set<SchemaLocation> getConcreteSchemaLocations(String namespace) {
        return getConcreteSchemaLocations();
    }

    /**
     * Create an {@link XmlObject} from the {@link OwsServiceResponse} object
     *
     * @param response {@link OwsServiceResponse} to encode
     *
     * @return XML encoded {@link OwsServiceResponse}
     *
     * @throws EncodingException If an error occurs during the encoding
     */
    protected abstract XmlObject create(T response) throws EncodingException;

    /**
     * Override this method in concrete response encoder if streaming is supported for this operations.
     *
     * @param response       Implementation of {@link OwsServiceResponse}
     * @param outputStream   {@link OutputStream} to write
     * @param encodingValues {@link EncodingValues} with additional indicators for encoding
     *
     * @throws EncodingException If an error occurs during encoding/writing to stream
     */
    protected void create(T response, OutputStream outputStream, EncodingContext encodingValues)
            throws EncodingException {
        try {
            XmlOptions xmlOptions;
            if (encodingValues.has(StreamingEncoderFlags.EMBEDDED)) {
                xmlOptions = new XmlOptions(getXmlOptions());
                xmlOptions.setSaveNoXmlDecl();
            } else {
                xmlOptions = getXmlOptions();
            }
            XmlObject xmlObject = create(response);
            setSchemaLocations(xmlObject);
            xmlObject.save(outputStream, xmlOptions);
        } catch (IOException ioe) {
            throw new EncodingException("Error while writing element to stream!", ioe);
        }
    }

    protected Class<T> getResponseType() {
        return responseType;
    }

}
