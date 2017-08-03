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
package org.n52.shetland.w3c;

import javax.xml.namespace.QName;


/**
 * Constants class for W3C
 *
 * @since 1.0.0
 *
 */
public interface W3CConstants {

    // namespaces and schema locations
    String NS_XLINK = "http://www.w3.org/1999/xlink";

    String NS_XLINK_PREFIX = "xlink";

    String NS_XSI = "http://www.w3.org/2001/XMLSchema-instance";

    String NS_XSI_PREFIX = "xsi";

    String SCHEMA_LOCATION_XLINK = "http://www.w3.org/1999/xlink.xsd";

    SchemaLocation XLINK_SCHEMA_LOCATION = new SchemaLocation(NS_XLINK, SCHEMA_LOCATION_XLINK);

    String SCHEMA_LOCATION = "schemaLocation";

    String NS_XS = "http://www.w3.org/2001/XMLSchema";

    String NS_XS_PREFIX = "xs";

    // attribute names
    String AN_HREF = "href";

    String AN_TITLE = "title";

    String AN_TYPE = "type";

    String AN_NIL = "nil";

    String AN_SHOW = "show";

    String AN_ARCROLE = "arcrole";

    String AN_ACTUATE = "actuate";

    String AN_ROLE = "role";

    String AN_SCHEMA_LOCATION = SCHEMA_LOCATION;

    String AN_XMLNS = "xmlns";

    String PREFIX_XMLNS = "xmlns:";

    // qnames
    QName QN_SCHEMA_LOCATION = new QName(NS_XSI, AN_SCHEMA_LOCATION);

    QName QN_SCHEMA_LOCATION_PREFIXED = new QName(NS_XSI, AN_SCHEMA_LOCATION, NS_XSI_PREFIX);

    QName QN_XSI_TYPE = new QName(NS_XSI, AN_TYPE, NS_XSI_PREFIX);

    QName QN_XLINK_HREF = new QName(NS_XLINK, AN_HREF, NS_XLINK_PREFIX);

    QName QN_XSI_NIL = new QName(NS_XSI, AN_NIL, NS_XSI_PREFIX);

    QName QN_XLINK_TITLE = new QName(NS_XLINK, AN_TITLE, NS_XLINK_PREFIX);

    QName QN_XLINK_TYPE = new QName(NS_XLINK, AN_TYPE, NS_XLINK_PREFIX);

    QName QN_XLINK_ROLE = new QName(NS_XLINK, AN_ROLE, NS_XLINK_PREFIX);

    QName QN_XLINK_ARCROLE = new QName(NS_XLINK, AN_ARCROLE, NS_XLINK_PREFIX);

    QName QN_XLINK_SHOW = new QName(NS_XLINK, AN_SHOW, NS_XLINK_PREFIX);

    QName QN_XLINK_ACTUATE = new QName(NS_XLINK, AN_ACTUATE, NS_XLINK_PREFIX);

}
