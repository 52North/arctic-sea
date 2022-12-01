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
package org.n52.svalbard.encode;

import java.io.OutputStream;

import javax.xml.stream.XMLStreamException;

import org.n52.shetland.ogc.om.OmObservation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.write.XmlStreamWriter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings({"EI_EXPOSE_REP2"})
public class InspireOmObservationXmlStreamWriter extends XmlStreamWriter<OmObservation> {


    private InspireOmObservationEncoder encoder;

    public InspireOmObservationXmlStreamWriter(EncodingContext context, OutputStream outputStream,
            OmObservation element, InspireOmObservationEncoder encoder) throws XMLStreamException {
        super(context, outputStream, element);
        this.encoder = encoder;
    }

    @Override
    public void write() throws XMLStreamException, EncodingException {
        rawText(encoder.encode(getElement(), getContext()).xmlText(getXmlOptions().setSaveNoXmlDecl()));
    }

}
