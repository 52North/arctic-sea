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

import static org.n52.iceland.util.repository.ConfiguringSingletonServiceLoader.loadAndConfigure;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.cache.ContentCache;
import org.n52.iceland.cache.ContentCacheController;
import org.n52.iceland.ds.CacheFeederHandlerRepository;
import org.n52.iceland.ds.ConnectionProvider;
import org.n52.iceland.ds.ConnectionProviderIdentificator;
import org.n52.iceland.ds.DataConnectionProvider;
import org.n52.iceland.ds.Datasource;
import org.n52.iceland.ds.DatasourceDaoIdentifier;
import org.n52.iceland.ds.FeatureQueryHandler;
import org.n52.iceland.ds.HibernateDatasourceConstants;
import org.n52.iceland.ds.IFeatureConnectionProvider;
import org.n52.iceland.ds.OperationHandlerRepository;
import org.n52.iceland.event.ServiceEventBus;
import org.n52.iceland.event.events.ConfiguratorInitializedEvent;
import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.ogc.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsServiceIdentification;
import org.n52.iceland.ogc.ows.OwsServiceProvider;
import org.n52.iceland.ogc.ows.ServiceIdentificationFactory;
import org.n52.iceland.ogc.ows.ServiceProviderFactory;
import org.n52.iceland.util.repository.ConfiguringSingletonServiceLoader;
import org.n52.iceland.util.Producer;
import org.n52.iceland.util.lifecycle.Constructable;
import org.n52.iceland.util.lifecycle.Destroyable;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

/**
 * Singleton class reads the configFile and builds the RequestOperator and DAO;
 * configures the logger.
 *
 * @since 4.0.0
 */
public class Configurator implements Constructable, Destroyable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Configurator.class);

    /**
     * instance attribute, due to the singleton pattern.
     */
    private static Configurator instance = null;
    @Inject
    private ServletContext servletContext;

    /**
     * base path for configuration files.
     */
    @Deprecated
    private String basepath;
    private Properties dataConnectionProviderProperties;
    private Properties featureConnectionProviderProperties;
    private FeatureQueryHandler featureQueryHandler;
    private ConnectionProvider dataConnectionProvider;
    private ConnectionProvider featureConnectionProvider;
    private ContentCacheController contentCacheController;
    @Deprecated
    @Inject
    private ServiceIdentificationFactory serviceIdentificationFactory;
    @Deprecated
    @Inject
    private ServiceProviderFactory serviceProviderFactory;
    private Set<String> providedJdbcDrivers = Sets.newHashSet();
    private String connectionProviderIdentificator;
    private String datasourceDaoIdentificator;


    public Configurator() {
        // ugly hack for singleton access
        Configurator.instance = this;
    }

    @Override
    public void init() throws ConfigurationException {
        Properties connectionProviderConfig = DatabaseSettingsHandler.getInstance(this.servletContext).getAll();
        this.basepath = this.servletContext.getRealPath("/");
        dataConnectionProviderProperties = connectionProviderConfig;
        getIdentificators(dataConnectionProviderProperties);
        if (Strings.isNullOrEmpty(connectionProviderIdentificator)) {
            logAndThrowConfigurationException("No connection provider identificator available!");
        }
        if (Strings.isNullOrEmpty(datasourceDaoIdentificator)) {
            logAndThrowConfigurationException("No datasource DAO identificator available!");
        }
        LOGGER.info("Configurator initialized: [basepath={}]", this.basepath, dataConnectionProviderProperties);
        this.initialize();
    }

    /**
     * Get the {@link ConnectionProviderIdentificator} and
     * {@link DatasourceDaoIdentifier} values from {@link Datasource}
     * implementation
     *
     * @param dataConnectionProviderProperties
     *            Datasource properties
     */
    private void getIdentificators(Properties dataConnectionProviderProperties) {
        String className = dataConnectionProviderProperties.getProperty(Datasource.class.getCanonicalName());
        if (className == null) {
            LOGGER.error("Can not find datasource class in datasource.properties!");
            throw new ConfigurationException("Missing Datasource Property!");
        }
        try {
            Datasource datasource = (Datasource) Class.forName(className).newInstance();
            connectionProviderIdentificator = datasource.getConnectionProviderIdentifier();
            datasourceDaoIdentificator = datasource.getDatasourceDaoIdentifier();
        } catch (ClassNotFoundException ex) {
            LOGGER.error("Can not instantiate Datasource!", ex);
            throw new ConfigurationException(ex);
        } catch (InstantiationException ex) {
            LOGGER.error("Can not instantiate Datasource!", ex);
            throw new ConfigurationException(ex);
        } catch (IllegalAccessException ex) {
            LOGGER.error("Can not instantiate Datasource!", ex);
            throw new ConfigurationException(ex);
        }

    }

    private void logAndThrowConfigurationException(String message) {
        LOGGER.info(message);
        throw new ConfigurationException(message);
    }

    /**
     * Initialize this class. Since this initialization is not done in the
     * constructor, dependent classes can use the SosConfigurator already when
     * called from here.
     */
    private void initialize() throws ConfigurationException {
        LOGGER.info("\n******\n Configurator initialization started\n******\n");

        ServiceConfiguration.getInstance();

        initializeConnectionProviders();
        CacheFeederHandlerRepository.createInstance(getDatasourceDaoIdentificator());

        serviceIdentificationFactory = new ServiceIdentificationFactory();
        serviceProviderFactory = new ServiceProviderFactory();
        OperationHandlerRepository.createInstance(getDatasourceDaoIdentificator());
        featureQueryHandler = loadAndConfigure(FeatureQueryHandler.class, false, getDatasourceDaoIdentificator());
        contentCacheController = loadAndConfigure(ContentCacheController.class, false);

        ServiceEventBus.fire(new ConfiguratorInitializedEvent());
        LOGGER.info("\n******\n Configurator initialization finished\n******\n");
    }

    /**
     * @return Returns the service identification
     *         <p/>
     * @throws OwsExceptionReport
     */
    @Deprecated
    public OwsServiceIdentification getServiceIdentification() throws OwsExceptionReport {
        return get(serviceIdentificationFactory);
    }

    /**
     * @return Returns the service identification for the specific language
     *         <p/>
     * @throws OwsExceptionReport
     */
    @Deprecated
    public OwsServiceIdentification getServiceIdentification(Locale lanugage) throws OwsExceptionReport {
        return get(serviceIdentificationFactory, lanugage);
    }

    @Deprecated
    public ServiceIdentificationFactory getServiceIdentificationFactory() throws OwsExceptionReport {
        return (ServiceIdentificationFactory) serviceIdentificationFactory;
    }

    /**
     * @return Returns the service provider
     *         <p/>
     * @throws OwsExceptionReport
     */
    @Deprecated
    public OwsServiceProvider getServiceProvider() throws OwsExceptionReport {
        return get(serviceProviderFactory);
    }

    /**
     * @return the base path for configuration files
     */
    @Deprecated
    public String getBasePath() {
        return basepath;
    }

    /**
     * @return the current contentCacheController
     */
    @Deprecated
    public ContentCache getCache() {
        return getCacheController().getCache();
    }

    /**
     * @return the current contentCacheController
     */
    @Deprecated
    public ContentCacheController getCacheController() {
        return contentCacheController;
    }

    /**
     * @return the implemented data connection provider
     */
    @Deprecated
    public ConnectionProvider getDataConnectionProvider() {
        return dataConnectionProvider;
    }

    /**
     * @return the implemented feature connection provider
     */
    @Deprecated
    public ConnectionProvider getFeatureConnectionProvider() {
        return featureConnectionProvider;
    }

    /**
     * @return the implemented feature query handler
     */
    @Deprecated
    public FeatureQueryHandler getFeatureQueryHandler() {
        return featureQueryHandler;
    }

    public void addProvidedJdbcDriver(String providedJdbcDriver) {
        this.providedJdbcDrivers.add(providedJdbcDriver);
    }

    public Set<String> getProvidedJdbcDriver() {
        return this.providedJdbcDrivers;
    }

    protected void initializeConnectionProviders() throws ConfigurationException {
        checkForProvidedJdbc();
        dataConnectionProvider =
                ConfiguringSingletonServiceLoader.<ConnectionProvider> loadAndConfigure(DataConnectionProvider.class,
                        true, getConnectionProviderIdentificator());
        featureConnectionProvider =
                ConfiguringSingletonServiceLoader.<ConnectionProvider> loadAndConfigure(
                        IFeatureConnectionProvider.class, false, getConnectionProviderIdentificator());
        dataConnectionProvider.initialize(dataConnectionProviderProperties);
        if (featureConnectionProvider != null) {
            featureConnectionProvider.initialize(featureConnectionProviderProperties != null
                                                 ? featureConnectionProviderProperties
                                                 : dataConnectionProviderProperties);
        } else {
            featureConnectionProvider = dataConnectionProvider;
        }
    }

    /**
     * Check method if JDBC driver is provided.
     */
    private void checkForProvidedJdbc() {
        if (!dataConnectionProviderProperties.containsKey(HibernateDatasourceConstants.PROVIDED_JDBC)
                || (dataConnectionProviderProperties.containsKey(HibernateDatasourceConstants.PROVIDED_JDBC) &&
                    dataConnectionProviderProperties.getProperty(HibernateDatasourceConstants.PROVIDED_JDBC).equals("true"))) {
            addProvidedJdbcDriver(dataConnectionProviderProperties.getProperty(HibernateDatasourceConstants.HIBERNATE_DRIVER_CLASS));
        }
    }

    /**
     * Eventually cleanup everything created by the constructor
     */
    @Override
    public synchronized void destroy() {
        cleanup(dataConnectionProvider);
        cleanup(featureConnectionProvider);
        cleanupDrivers(getProvidedJdbcDriver());
        instance = null;
    }


    protected void cleanupDrivers(Set<String> providedJdbcDriver) {
        if (ServiceConfiguration.getInstance().isDeregisterJdbcDriver()) {
            LOGGER.debug("Deregistering JDBC driver is enabled!");
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                if (!providedJdbcDriver.contains(driver.getClass().getName())) {
                    try {
                        DriverManager.deregisterDriver(driver);
                        LOGGER.info("Deregistering JDBC driver: {}", driver);
                    } catch (SQLException e) {
                        LOGGER.error("Error deregistering driver " + driver, e);
                    }
                } else {
                    LOGGER.debug("JDBC driver {} is marked to do not deregister", driver);
                }
            }
        } else {
            LOGGER.debug("Deregistering of JDBC driver(s) is disabled!");
        }
    }

    /**
     * @return the connectionProviderIdentificator
     */
    @Deprecated
    public String getConnectionProviderIdentificator() {
        return connectionProviderIdentificator;
    }

    /**
     * @return the datasourceDaoIdentificator
     */
    @Deprecated
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
    @Deprecated
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
    @Deprecated
    public static Configurator createInstance(
            final Properties connectionProviderConfig,
                                              final String basepath)
            throws ConfigurationException {
        return instance;
    }

    private static void cleanup(final Destroyable c) {
        if (c != null) {
            c.destroy();
        }
    }

    protected static <T> T get(final Producer<T> factory)
            throws OwsExceptionReport {
        try {
            return factory.get();
        } catch (final Exception e) {
            if (e.getCause() != null && e.getCause() instanceof OwsExceptionReport) {
                throw (OwsExceptionReport) e.getCause();
            } else {
                throw new NoApplicableCodeException().withMessage("Could not request object from %s", factory).causedBy(e);
            }
        }
    }

    protected static <T> T get(final Producer<T> factory,
                               Locale language)
            throws OwsExceptionReport {
        try {
            return factory.get(language);
        } catch (final Exception e) {
            if (e.getCause() != null && e.getCause() instanceof OwsExceptionReport) {
                throw (OwsExceptionReport) e.getCause();
            } else {
                throw new NoApplicableCodeException().withMessage("Could not request object from %s", factory);
            }
        }
    }
}
