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
package org.n52.svalbard.write;

import java.io.OutputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.shetland.inspire.omso.InspireOMSOConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.svalbard.encode.EncodingContext;

public class PointTimeSeriesObservationXmlStreamWriter extends WmlTVPEncoderv20XmlStreamWriter {

    public PointTimeSeriesObservationXmlStreamWriter(
            EncodingContext context,
            OutputStream outputStream,
            OmObservation element)
            throws XMLStreamException {
        super(context, outputStream, element);
    }

    @Override
    protected QName getDocumentName() {
        return InspireOMSOConstants.QN_POINT_TIMESERES_OBSERVATION;
    }

    protected void writeAddtitionalNamespaces() throws XMLStreamException {
        super.writeAddtitionalNamespaces();
        namespace(InspireOMSOConstants.NS_OMSO_PREFIX, InspireOMSOConstants.NS_OMSO_30);
    }
}