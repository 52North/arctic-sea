/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.svalbard;

import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.svalbard.util.XmlHelper;

public interface XPathConstants {

    String XPATH_PREFIX_SOS_20 = XmlHelper.getXPathPrefix(SosConstants.NS_SOS_PREFIX, Sos2Constants.NS_SOS_20);

    String XPATH_PREFIXES_SWES = XmlHelper.getXPathPrefix(SwesConstants.NS_SWES_PREFIX, SwesConstants.NS_SWES_20);

}
