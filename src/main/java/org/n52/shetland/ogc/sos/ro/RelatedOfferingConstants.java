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
package org.n52.shetland.ogc.sos.ro;

import javax.xml.namespace.QName;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public interface RelatedOfferingConstants {
    String NS_RO = "http://www.opengis.net/sosro/1.0";

    String NS_RO_PREFIX = "ro";

    String ROLE = "sub-offering";

    String RELATED_OFFERINGS = "relatedOfferings";
    
    String EN_RO_RELATED_OFFERINGS = "RelatedOfferings";

    String EN_RO_OFFERING_CONTEXT = "OfferingContext";

    String EN_RO_ROLE = "role";

    String EN_RO_RELATED_OFFERING = "relatedOffering";

    QName QN_RO_RELATED_OFFERINGS = new QName(NS_RO, EN_RO_RELATED_OFFERINGS, NS_RO_PREFIX);

    QName QN_RO_OFFERING_CONTEXT = new QName(NS_RO, EN_RO_OFFERING_CONTEXT, NS_RO_PREFIX);

    QName QN_RO_ROLE = new QName(NS_RO, EN_RO_ROLE, NS_RO_PREFIX);

    QName QN_RO_RELATED_OFFERING = new QName(NS_RO, EN_RO_RELATED_OFFERING, NS_RO_PREFIX);
}
