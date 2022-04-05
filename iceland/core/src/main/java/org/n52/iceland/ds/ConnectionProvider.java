/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.iceland.ds;

/**
 * Interface for a connection provider that handles the connection to the underlying data source (e.g. database, web
 * service). Implementation can contain a ConnectionPool.
 *
 * @since 1.0.0
 */
public interface ConnectionProvider {

    /**
     * Get a data source connection.
     *
     * @return Connection to the data source
     *
     * @throws ConnectionProviderException if no connection could be acquired
     */
    Object getConnection() throws ConnectionProviderException;

    /**
     * Return the connection to the provider.
     *
     * @param connection the connection
     */
    void returnConnection(Object connection);

    /**
     * Get the max connections number
     *
     * @return The max connection number
     */
    int getMaxConnections();

}
