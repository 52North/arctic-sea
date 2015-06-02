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
package org.n52.iceland.service;


import java.util.Locale;
import java.util.Properties;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;

import org.n52.iceland.cache.ContentCache;
import org.n52.iceland.cache.ContentCacheController;
import org.n52.iceland.ds.ConnectionProvider;
import org.n52.iceland.ds.DataConnectionProvider;
import org.n52.iceland.ds.FeatureConnectionProvider;
import org.n52.iceland.ds.FeatureQueryHandler;
import org.n52.iceland.event.ServiceEventBus;
import org.n52.iceland.event.events.ConfiguratorInitializedEvent;
import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.ogc.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsServiceIdentification;
import org.n52.iceland.ogc.ows.OwsServiceProvider;
import org.n52.iceland.ogc.ows.ServiceIdentificationFactory;
import org.n52.iceland.ogc.ows.ServiceProviderFactory;
import org.n52.iceland.util.LocalizedProducer;
import org.n52.iceland.util.Producer;


/**
 * Singleton class reads the configFile and builds the RequestOperator and DAO;
 * configures the logger.
 *
 * @since 4.0.0
 */
@Deprecated
public class Configurator implements Constructable {
    private static Configurator instance = null;
    private ServletContext servletContext;
    private String basepath;
    private FeatureQueryHandler featureQueryHandler;
    private ConnectionProvider dataConnectionProvider;
    private ConnectionProvider featureConnectionProvider;
    private ContentCacheController contentCacheController;
    private ServiceIdentificationFactory serviceIdentificationFactory;
    private ServiceProviderFactory serviceProviderFactory;
    private String connectionProviderIdentificator;
    private String datasourceDaoIdentificator;
    private ServiceEventBus eventBus;

    public Configurator() {
        // ugly hack for singleton access
    }

    @Inject
    public void setEventBus(ServiceEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Autowired(required = false)
    public void setFeatureConnectionProvider(FeatureConnectionProvider featureConnectionProvider) {
        this.featureConnectionProvider = featureConnectionProvider;
    }

    /**
     * @return the implemented feature connection provider
     */
    public ConnectionProvider getFeatureConnectionProvider() {
        return featureConnectionProvider;
    }

    @Inject
    public void setContentCacheController(ContentCacheController contentCacheController) {
        this.contentCacheController = contentCacheController;
    }

    @Inject
    public void setServiceIdentificationFactory(ServiceIdentificationFactory serviceIdentificationFactory) {
        this.serviceIdentificationFactory = serviceIdentificationFactory;
    }

    public ServiceIdentificationFactory getServiceIdentificationFactory() throws OwsExceptionReport {
        return serviceIdentificationFactory;
    }

    @Inject
    public void setServiceProviderFactory(ServiceProviderFactory serviceProviderFactory) {
        this.serviceProviderFactory = serviceProviderFactory;
    }

    @Inject
    public void setFeatureQueryHandler(FeatureQueryHandler featureQueryHandler) {
        this.featureQueryHandler = featureQueryHandler;
    }



    /**
     * @return the implemented feature query handler
     */
    public FeatureQueryHandler getFeatureQueryHandler() {
        return featureQueryHandler;
    }

    @Inject
    public void setDataConnectionProvider(DataConnectionProvider dataConnectionProvider) {
        this.dataConnectionProvider = dataConnectionProvider;
    }

    /**
     * @return the implemented data connection provider
     */
    public ConnectionProvider getDataConnectionProvider() {
        return dataConnectionProvider;
    }

    @Inject
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void init() throws ConfigurationException {
        Configurator.instance = this;
        this.basepath = this.servletContext.getRealPath("/");
        if (featureConnectionProvider == null) {
            featureConnectionProvider = dataConnectionProvider;
        }
        this.eventBus.submit(new ConfiguratorInitializedEvent());
    }

    /**
     * @return Returns the service identification
     *         <p/>
     * @throws OwsExceptionReport
     */
    public OwsServiceIdentification getServiceIdentification() throws OwsExceptionReport {
        return get(serviceIdentificationFactory);
    }

    /**
     * @return Returns the service identification for the specific language
     *         <p/>
     * @throws OwsExceptionReport
     */
    public OwsServiceIdentification getServiceIdentification(Locale lanugage) throws OwsExceptionReport {
        return get(serviceIdentificationFactory, lanugage);
    }

    /**
     * @return Returns the service provider
     *         <p/>
     * @throws OwsExceptionReport
     */
    public OwsServiceProvider getServiceProvider() throws OwsExceptionReport {
        return get(serviceProviderFactory);
    }

    /**
     * @return the base path for configuration files
     */
    public String getBasePath() {
        return basepath;
    }

    /**
     * @return the current contentCacheController
     */
    public ContentCache getCache() {
        return this.contentCacheController.getCache();
    }

    /**
     * @return the current contentCacheController
     */
    public ContentCacheController getCacheController() {
        return contentCacheController;
    }

    /**
     * @return the connectionProviderIdentificator
     */
    public String getConnectionProviderIdentificator() {
        return connectionProviderIdentificator;
    }

    /**
     * @return the datasourceDaoIdentificator
     */
    public String getDatasourceDaoIdentificator() {
        return datasourceDaoIdentificator;
    }

    /**
     * @return Returns the instance of the Configurator. <tt>null</tt> will be
     *         returned if the parameterized
     *         {@link #createInstance(Properties, String)} method was not
     *         invoked before. Usually this will be done in the SOS.
     *         <p/>
     * @see Configurator#createInstance(Properties, String)
     */
    public static Configurator getInstance() {
        return instance;
    }

    /**
     * @param connectionProviderConfig
     * @param basepath
     * @return Returns an instance of the SosConfigurator. This method is used
     *         to implement the singelton pattern
     *
     * @throws ConfigurationException
     *             if the initialization failed
     */
    public static Configurator createInstance(Properties connectionProviderConfig, String basepath) {
        return getInstance();
    }

    private static <T> T get(Producer<T> factory) throws OwsExceptionReport {
        try {
            return factory.get();
        } catch (Exception e) {
            if (e instanceof OwsExceptionReport) {
                throw (OwsExceptionReport) e;
            } else if (e.getCause() instanceof OwsExceptionReport) {
                throw (OwsExceptionReport) e.getCause();
            } else {
                throw new NoApplicableCodeException()
                        .causedBy(e).withMessage("Could not request object from %s", factory);
            }
        }
    }

    private static <T> T get(LocalizedProducer<T> factory, Locale language)
            throws OwsExceptionReport {
        try {
            return factory.get(language);
        } catch (Exception e) {
            if (e instanceof OwsExceptionReport) {
                throw (OwsExceptionReport) e;
            } else if (e.getCause() instanceof OwsExceptionReport) {
                throw (OwsExceptionReport) e.getCause();
            } else {
                throw new NoApplicableCodeException()
                        .causedBy(e).withMessage("Could not request object from %s", factory);
            }
        }
    }
}
