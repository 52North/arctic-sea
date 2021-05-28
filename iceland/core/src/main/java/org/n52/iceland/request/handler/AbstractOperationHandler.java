/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.iceland.request.handler;

import static java.util.stream.Collectors.toSet;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.n52.faroe.ConfigurationError;
import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.binding.Binding;
import org.n52.iceland.binding.BindingRepository;
import org.n52.iceland.binding.MediaTypeBindingKey;
import org.n52.iceland.binding.PathBindingKey;
import org.n52.iceland.exception.HTTPException;
import org.n52.iceland.i18n.I18NSettings;
import org.n52.iceland.service.ServiceSettings;
import org.n52.janmayen.function.Functions;
import org.n52.janmayen.http.HTTPHeaders;
import org.n52.janmayen.http.HTTPMethods;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.i18n.LocaleHelper;
import org.n52.shetland.ogc.ows.OwsAllowedValues;
import org.n52.shetland.ogc.ows.OwsDCP;
import org.n52.shetland.ogc.ows.OwsDomain;
import org.n52.shetland.ogc.ows.OwsHttp;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.ows.OwsOperation;
import org.n52.shetland.ogc.ows.OwsRequestMethod;
import org.n52.shetland.ogc.ows.OwsValue;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@Configurable
public abstract class AbstractOperationHandler implements OperationHandler {
    private URI serviceURL;
    private BindingRepository bindingRepository;
    private Locale defaultLanguage = Locale.ENGLISH;
    private boolean showAllLanguages;

    @Inject
    public void setBindingRepository(BindingRepository bindingRepository) {
        this.bindingRepository = Objects.requireNonNull(bindingRepository);
    }

    public BindingRepository getBindingRepository() {
        return bindingRepository;
    }

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(final URI serviceURL)
            throws ConfigurationError {
        setURL(serviceURL);
    }

//    @Setting(value = ServiceSettings.URL, required = false)
    public void setURL(final URI serviceURL)
            throws ConfigurationError {
        this.serviceURL = Validation.notNull("Service URL", serviceURL);
    }

    protected URI getServiceURL() {
        return this.serviceURL;
    }

    @Setting(value = I18NSettings.I18N_DEFAULT_LANGUAGE, required = false)
    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = LocaleHelper.decode(defaultLanguage);
    }

    protected Locale getDefaultLanguage() {
        return defaultLanguage;
    }

    @Setting(value = I18NSettings.I18N_SHOW_ALL_LANGUAGE_VALUES, required = false)
    public void setShowAllLanguages(boolean showAllLanguages) {
        this.showAllLanguages = showAllLanguages;
    }

    protected boolean isShowAllLanguages() {
        return this.showAllLanguages;
    }

    protected Locale getRequestedLocale(OwsServiceRequest request) {
        return LocaleHelper.decode(request.getRequestedLanguage(), defaultLanguage);
    }

    private Set<OwsDCP> getDCP(String service, String version) {
        return Collections.singleton(getDCP(new OwsOperationKey(service, version, getOperationName())));
    }

    private OwsDCP getDCP(OwsOperationKey operation) {
        Set<OwsRequestMethod> methods = Stream
                .concat(getRequestMethodsForServiceURL(operation),
                        getRequestMethodsForBindingURL(operation))
                .collect(toSet());
        return new OwsHttp(methods);
    }

    private Stream<OwsRequestMethod> getRequestMethodsForBindingURL(OwsOperationKey operation) {
        return this.bindingRepository.getBindings().values().stream()
                .flatMap(Functions.currySecond(this::getRequestMethods, operation));
    }

    private Stream<OwsRequestMethod> getRequestMethodsForServiceURL(OwsOperationKey operation) {
        Map<String, Set<OwsValue>> mediaTypesByMethod = new HashMap<>(HTTPMethods.METHODS.size());
        this.bindingRepository.getBindings().values().stream()
                .forEach(binding -> HTTPMethods.METHODS.stream()
                    .filter(isMethodSupported(binding, operation))
                    .forEach(method ->
                            mediaTypesByMethod.computeIfAbsent(method, Functions.forSupplier(HashSet::new))
                                    .addAll(getMediaTypes(binding))));
        return mediaTypesByMethod.entrySet().stream()
                .map(e -> new OwsRequestMethod(this.serviceURL, e.getKey(), createContentTypeDomains(e.getValue())));
    }

    private Set<OwsValue> getMediaTypes(Binding binding) {
        return binding.getKeys().stream()
                .map(Functions.castIfInstanceOf(MediaTypeBindingKey.class))
                .filter(Optional::isPresent).map(Optional::get)
                .map(MediaTypeBindingKey::getMediaType)
                .map(MediaType::toString)
                .map(OwsValue::new)
                .collect(toSet());
    }

    private Set<OwsDomain> createContentTypeDomains(Set<OwsValue> value) {
        return Collections.singleton(createContentTypeDomain(value));
    }

    private OwsDomain createContentTypeDomain(Set<OwsValue> value) {
        return new OwsDomain(HTTPHeaders.CONTENT_TYPE, new OwsAllowedValues(value));
    }

    private Stream<OwsRequestMethod> getRequestMethods(Binding binding, OwsOperationKey operation) {
        Set<OwsDomain> constraints = createContentTypeDomains(getMediaTypes(binding));
        return binding.getKeys().stream()
                .map(Functions.castIfInstanceOf(PathBindingKey.class))
                .filter(Optional::isPresent).map(Optional::get)
                .map(PathBindingKey::getPath)
                .map(path -> URI.create(this.serviceURL + path))
                .flatMap(uri -> HTTPMethods.METHODS.stream()
                .filter(isMethodSupported(binding, operation))
                .map(method -> new OwsRequestMethod(uri, method, constraints)));
    }

    private Predicate<String> isMethodSupported(Binding binding, OwsOperationKey operation) {
        return method -> isMethodSupported(binding, method, operation);
    }

    private boolean isMethodSupported(Binding binding, String method, OwsOperationKey operation) {
        try {
            switch (method) {
                case HTTPMethods.GET:
                    return binding.checkOperationHttpGetSupported(operation);
                case HTTPMethods.POST:
                    return binding.checkOperationHttpPostSupported(operation);
                case HTTPMethods.PUT:
                    return binding.checkOperationHttpPutSupported(operation);
                case HTTPMethods.DELETE:
                    return binding.checkOperationHttpDeleteSupported(operation);
                default:
                    return false;
            }
        } catch (HTTPException ignored) {
            return false;
        }
    }

    @Override
    public OwsOperation getOperationsMetadata(String service, String version) throws OwsExceptionReport {
        String name = getOperationName();
        Set<OwsDomain> parameters = getOperationParameters(service, version);
        Set<OwsDomain> constraints = getOperationConstraints(service, version);
        Set<OwsMetadata> metadata = getOperationMetadata(service, version);
        Set<OwsDCP> dcp = getDCP(service, version);
        return new OwsOperation(name, parameters, constraints, metadata, dcp);
    }

    protected Set<OwsDomain> getOperationParameters(String service, String version) throws OwsExceptionReport {
        return null;
    }

    protected Set<OwsDomain> getOperationConstraints(String service, String version) throws OwsExceptionReport {
        return null;
    }

    protected Set<OwsMetadata> getOperationMetadata(String service, String version) throws OwsExceptionReport {
        return null;
    }

}
