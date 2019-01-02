/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.binding.Binding;
import org.n52.iceland.binding.BindingRepository;
import org.n52.iceland.event.events.ExceptionEvent;
import org.n52.iceland.event.events.IncomingRequestEvent;
import org.n52.iceland.event.events.OutgoingResponseEvent;
import org.n52.iceland.exception.HTTPException;
import org.n52.janmayen.event.EventBus;
import org.n52.janmayen.http.HTTPHeaders;
import org.n52.janmayen.http.HTTPMethods;
import org.n52.janmayen.http.HTTPStatus;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.google.common.util.concurrent.UncheckedTimeoutException;

/**
 * The servlet of the Service which receives the incoming HttpPost and HttpGet requests and sends the operation result
 * documents to the client TODO review exception handling
 *
 * @since 1.0.0
 */
@Configurable
@Controller
@RequestMapping(value = "/service", consumes = "*/*", produces = "*/*")
public class Service extends HttpServlet {
    public static final String REQUEST_TIMEOUT = "service.request.timeout";
    private static final long serialVersionUID = -2103692310137045855L;
    private static final String BINDING_DELETE_METHOD = "doDeleteOperation";
    private static final String BINDING_PUT_METHOD = "doPutOperation";
    private static final String BINDING_POST_METHOD = "doPostOperation";
    private static final String BINDING_GET_METHOD = "doGetOperation";
    private static final AtomicLong COUNTER = new AtomicLong(0);
    private static final TimeLimiter TIME_LIMITER = SimpleTimeLimiter.create(Executors.newCachedThreadPool());
    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);
    private Integer requestTimeout = 0;

    @Inject
    private transient BindingRepository bindingRepository;

    @Inject
    private transient EventBus serviceEventBus;


    private long logRequest(HttpServletRequest request) {
        long count = COUNTER.incrementAndGet();
        this.serviceEventBus.submit(new IncomingRequestEvent(request, count));

        if (LOGGER.isDebugEnabled()) {
            Enumeration<?> headerNames = request.getHeaderNames();
            StringBuilder headers = new StringBuilder();
            while (headerNames.hasMoreElements()) {
                String name = (String) headerNames.nextElement();
                headers.append("> ").append(name).append(": ").append(request.getHeader(name)).append("\n");
            }
            LOGGER.debug("Incoming request No. {}:\n> [{} {} {}] from {} {}\n{}",
                         count, request.getMethod(), request.getRequestURI(), request.getProtocol(),
                         request.getRemoteAddr(), request.getRemoteHost(), headers);
        }
        return count;
    }

    private void logResponse(HttpServletRequest request, HttpServletResponse response,
                             long count, Stopwatch stopwatch) {
        long elapsed = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
        this.serviceEventBus.submit(new OutgoingResponseEvent(request, response, count, elapsed));
        LOGGER.debug("Outgoing response for request No. {} is committed = {} (took {} ms)",
                     count, response.isCommitted(), elapsed);
    }

    @Override
    @Deprecated
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        delete(request, response);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        long currentCount = logRequest(request);
        try {
            getBinding(request).doDeleteOperation(request, response);
        } catch (HTTPException exception) {
            onHttpException(request, response, exception);
        } finally {
            logResponse(request, response, currentCount, stopwatch);
        }
    }

    @Deprecated
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        get(request, response);
    }

    @RequestMapping(method = RequestMethod.GET)
    public void get(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        long currentCount = logRequest(request);
        try {
            getBinding(request).doGetOperation(request, response);
        } catch (HTTPException exception) {
            onHttpException(request, response, exception);
        } finally {
            logResponse(request, response, currentCount, stopwatch);
        }
    }

    @Deprecated
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        post(request, response);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void post(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        long currentCount = logRequest(request);
        try {
            getBinding(request).doPostOperation(request, response);
        } catch (HTTPException exception) {
            onHttpException(request, response, exception);
        } finally {
            logResponse(request, response, currentCount, stopwatch);
        }
    }

    @Deprecated
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        put(request, response);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void put(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        long currentCount = logRequest(request);
        try {
            getBinding(request).doPutOperation(request, response);
        } catch (HTTPException exception) {
            onHttpException(request, response, exception);
        } finally {
            logResponse(request, response, currentCount, stopwatch);
        }
    }

    @Deprecated
    @Override
    public void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        options(request, response);
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    private void options(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException, ServletException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        long currentCount = logRequest(request);
        Binding binding = null;
        try {
            binding = getBinding(request);
            binding.doOptionsOperation(request, response);
        } catch (HTTPException exception) {
            if (exception.getStatus() == HTTPStatus.METHOD_NOT_ALLOWED && binding != null) {
                doDefaultOptions(binding, request, response);
            } else {
                onHttpException(request, response, exception);
            }
        } finally {
            logResponse(request, response, currentCount, stopwatch);
        }
    }

    /**
     * Get the implementation of {@link Binding} that is registered for the given <code>request</code>.
     *
     * @param request URL pattern from request URL
     *
     * @return The implementation of {@link Binding} that is registered for the given <code>urlPattern</code>.
     *
     *
     * @throws HTTPException If the URL pattern or ContentType is not supported by this service.
     */
    private Binding getBinding(HttpServletRequest request) throws HTTPException {
        final String requestURI = request.getPathInfo();
        if (requestURI == null || requestURI.isEmpty() || requestURI.equals("/")) {
            MediaType contentType = getContentType(request);
            // strip of the parameters to get rid of things like encoding
            Binding binding = this.bindingRepository.getBinding(contentType.withoutParameters());
            if (binding == null) {
                if (contentType.equals(MediaTypes.APPLICATION_KVP)) {
                    throw new HTTPException(HTTPStatus.METHOD_NOT_ALLOWED);
                } else {
                    throw new HTTPException(HTTPStatus.UNSUPPORTED_MEDIA_TYPE);
                }
            } else {
                if (requestTimeout > 0) {
                    try {
                        return TIME_LIMITER.newProxy(binding, Binding.class, requestTimeout, TimeUnit.SECONDS);
                    } catch (UncheckedTimeoutException ute) {
                        HTTPException httpException = new HTTPException(HTTPStatus.GATEWAY_TIME_OUT);
                        httpException.addSuppressed(ute);
                        throw httpException;
                    }
                }
                return binding;
            }
        }
        throw new HTTPException(HTTPStatus.NOT_FOUND);
    }

    private MediaType getContentType(HttpServletRequest request)
            throws HTTPException {
        if (request.getContentType() == null) {
            // default to KVP for GET requests
            if (request.getMethod().equals(HTTPMethods.GET)) {
                return MediaTypes.APPLICATION_KVP;
            } else {
                throw new HTTPException(HTTPStatus.BAD_REQUEST);
            }
        } else {
            try {
                return MediaType.parse(request.getContentType());
            } catch (IllegalArgumentException e) {
                throw new HTTPException(HTTPStatus.BAD_REQUEST, e);
            }
        }
    }

    protected void onHttpException(HttpServletRequest request, HttpServletResponse response, HTTPException exception)
            throws IOException {
        this.serviceEventBus.submit(new ExceptionEvent(exception));
        response.sendError(exception.getStatus().getCode(), exception.getMessage());
    }

    protected void doDefaultOptions(Binding binding, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Set<String> methods = getDeclaredBindingMethods(binding.getClass());
        StringBuilder allow = new StringBuilder();
        if (methods.contains(BINDING_GET_METHOD)) {
            allow.append(HTTPMethods.GET);
            allow.append(", ");
            allow.append(HTTPMethods.HEAD);
        }
        if (methods.contains(BINDING_POST_METHOD)) {
            if (allow.length() != 0) {
                allow.append(", ");
            }
            allow.append(HTTPMethods.POST);
        }
        if (methods.contains(BINDING_PUT_METHOD)) {
            if (allow.length() != 0) {
                allow.append(", ");
            }
            allow.append(HTTPMethods.PUT);
        }
        if (methods.contains(BINDING_DELETE_METHOD)) {
            if (allow.length() != 0) {
                allow.append(", ");
            }
            allow.append(HTTPMethods.DELETE);
        }

        if (allow.length() != 0) {
            allow.append(", ");
        }
        allow.append(HTTPMethods.TRACE);
        allow.append(", ");
        allow.append(HTTPMethods.OPTIONS);
        response.setHeader(HTTPHeaders.ALLOW, allow.toString());
    }

    private Set<String> getDeclaredBindingMethods(Class<?> c) {
        if (c.equals(Binding.class)) {
            return Collections.emptySet();
        } else {
            Set<String> parent = getDeclaredBindingMethods(c.getSuperclass());
            for (Method m : c.getDeclaredMethods()) {
                parent.add(m.getName());
            }
            return parent;
        }
    }

    @Setting(REQUEST_TIMEOUT)
    public void setRequestTimeout(Integer requestTimeout) {
        if (requestTimeout != null) {
            this.requestTimeout = requestTimeout;
        }
    }

}
