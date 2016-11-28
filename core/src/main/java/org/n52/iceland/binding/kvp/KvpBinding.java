/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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

import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.binding.BindingConstants;
import org.n52.iceland.binding.BindingKey;
import org.n52.iceland.binding.MediaTypeBindingKey;
import org.n52.iceland.binding.PathBindingKey;
import org.n52.iceland.binding.SimpleBinding;
import org.n52.iceland.coding.OperationKey;
import org.n52.iceland.coding.decode.OwsDecodingException;
import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.exception.HTTPException;
import org.n52.iceland.exception.ows.concrete.InvalidServiceParameterException;
import org.n52.iceland.exception.ows.concrete.MissingRequestParameterException;
import org.n52.shetland.ogc.ows.exception.MissingServiceParameterException;
import org.n52.iceland.exception.ows.concrete.VersionNotSupportedException;
import org.n52.iceland.ogc.sos.ConformanceClasses;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.iceland.service.MiscSettings;
import org.n52.janmayen.Streams;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.OWSConstants.RequestParams;
import org.n52.shetland.ogc.ows.exception.InvalidParameterValueException;
import org.n52.shetland.ogc.ows.exception.OperationNotSupportedException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.OperationDecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;

/**
 * OWS binding for Key-Value-Pair (HTTP-Get) requests
 *
 * @since 1.0.0
 */
@Configurable
public class KvpBinding extends SimpleBinding {

    private static final Logger LOGGER = LoggerFactory.getLogger(KvpBinding.class);

    @Deprecated // SOS-specific
    private static final Set<String> CONFORMANCE_CLASSES = Collections
            .singleton(ConformanceClasses.SOS_V2_KVP_CORE_BINDING);

    private static final ImmutableSet<BindingKey> KEYS = ImmutableSet.<BindingKey>builder()
            .add(new PathBindingKey(BindingConstants.KVP_BINDING_ENDPOINT))
            .add(new MediaTypeBindingKey(MediaTypes.APPLICATION_KVP))
            .build();

    private boolean useHttpResponseCodes;

    private boolean includeOriginal = false;

    @Setting(MiscSettings.HTTP_STATUS_CODE_USE_IN_KVP_POX_BINDING)
    public void setUseHttpResponseCodes(boolean useHttpResponseCodes) {
        this.useHttpResponseCodes = useHttpResponseCodes;
    }

    @Override
    protected boolean isUseHttpResponseCodes() {
        return this.useHttpResponseCodes;
    }

    @Setting(MiscSettings.INCLUDE_ORIGINAL_REQUEST)
    public void setIncludeOriginalRequest(boolean includeOriginal) {
        this.includeOriginal = includeOriginal;
    }

    @Override
    public Set<BindingKey> getKeys() {
        return Collections.unmodifiableSet(KEYS);
    }

    @Override
    public Set<MediaType> getSupportedEncodings() {
        return Collections.singleton(MediaTypes.APPLICATION_KVP);
    }

    @Override
    public String getUrlPattern() {
        return BindingConstants.KVP_BINDING_ENDPOINT;
    }


    @Override
    public Set<String> getConformanceClasses(String service, String version) {
        if (SosConstants.SOS.equals(service) && Sos2Constants.SERVICEVERSION.equals(version)) {
            return Collections.unmodifiableSet(CONFORMANCE_CLASSES);
        }
        return Collections.emptySet();
    }

    @Override
    protected MediaType getDefaultContentType() {
        return MediaTypes.APPLICATION_XML;
    }

    @Override
    public void doGetOperation(HttpServletRequest req, HttpServletResponse res) throws HTTPException, IOException {
        LOGGER.debug("KVP-REQUEST: {}", req.getQueryString());
        OwsServiceRequest serviceRequest = null;
        try {
            serviceRequest = parseRequest(req);
            // add request context information
            serviceRequest.setRequestContext(getRequestContext(req));
            OwsServiceResponse response = getServiceOperator(serviceRequest).receiveRequest(serviceRequest);
            writeResponse(req, res, response);
        } catch (OwsExceptionReport oer) {
            oer.setVersion(serviceRequest != null ? serviceRequest.getVersion() : null);
            writeOwsExceptionReport(req, res, oer);
        }
    }

    @Override
    public boolean checkOperationHttpGetSupported(OperationKey k) {
        return hasDecoder(k, MediaTypes.APPLICATION_KVP);
    }

    private String normalizeParameterName(String name) {
        return name.replace("amp;", "").toLowerCase(Locale.ROOT);
    }

    protected OwsServiceRequest parseRequest(HttpServletRequest req) throws OwsExceptionReport {

        if (req.getParameterMap() == null || req.getParameterMap().isEmpty()) {
            throw new MissingRequestParameterException();
        }

        Map<String, String> parameters = Streams.stream(req.getParameterNames())
                .collect(toMap(this::normalizeParameterName, req::getParameter));

        String service = parameters.get(RequestParams.service.name());
        String version = parameters.get(RequestParams.version.name());
        String operation = parameters.get(RequestParams.request.name());

        if (Strings.isNullOrEmpty(service)) {
            throw new MissingServiceParameterException();
        }

        if (!isServiceSupported(service)) {
            throw new InvalidServiceParameterException(service);
        }

        if (Strings.isNullOrEmpty(operation)) {
            throw new MissingRequestParameterException();
        }

        if (version != null && !isVersionSupported(service, version)) {
            throw new VersionNotSupportedException();
        }

        Decoder<OwsServiceRequest, Map<String, String>> decoder
                = getDecoder(new OperationDecoderKey(service, version, operation, MediaTypes.APPLICATION_KVP));

        if (decoder == null) {
            throw new OperationNotSupportedException(operation);
        }

        OwsServiceRequest request;
        try {
            request = decoder.decode(parameters);
        } catch (OwsDecodingException ex) {
            throw ex.getCause();
        } catch (DecodingException ex) {
            throw new InvalidParameterValueException().withMessage(ex.getMessage())
                    .causedBy(ex).at(ex.getLocation().orElse(null));
        }
        if (this.includeOriginal) {
            request.setOriginalRequest(String.join("?", req.getRequestURL(), req.getQueryString()));
        }

        return request;
    }
}
