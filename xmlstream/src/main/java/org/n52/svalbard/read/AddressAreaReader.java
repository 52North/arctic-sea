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
package org.n52.svalbard.read;

import javax.xml.namespace.QName;

import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.inspire.GeographicalName;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class AddressAreaReader extends NillableSubtagReader<GeographicalName> {

    @Override
    protected QName getSubtagName() {
        return AqdConstants.QN_GN_GEOGRAPHICAL_NAME;
    }

    @Override
    protected XmlReader<GeographicalName> getSubtagDelegate() {
        return new GeographicalNameReader();
    }

}
