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
package org.n52.iceland.statistics.api.interfaces.geolocation;

import java.util.Map;

import org.n52.shetland.ogc.ows.service.OwsServiceRequestContext;
import org.n52.janmayen.net.IPAddress;

public interface IStatisticsLocationUtil {
    public Map<String, Object> ip2SpatialData(IPAddress ip);

    /**
     *
     * @param host
     *            String of the IPv4 address e.g.: <code>123.123.123.123</code>
     * @return geo loation data in Elasticsearch's accept format
     */
    public Map<String, Object> ip2SpatialData(String host);

    public IPAddress resolveOriginalIpAddress(OwsServiceRequestContext ctx);
}
