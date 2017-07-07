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
import java.util.Optional;

import javax.xml.stream.XMLStreamException;

import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.svalbard.encode.EncodingContext;

/**
 * Implementation of {@link AbstractOmV20XmlStreamWriter} to write O&M 2.0
 * encoded {@link OmObservation}s to stream
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class OmV20XmlStreamWriter extends AbstractOmV20XmlStreamWriter {
    public OmV20XmlStreamWriter(EncodingContext context, OutputStream outputStream, OmObservation element)
            throws XMLStreamException {
        super(context, outputStream, element);
    }

    @Override
    protected Optional<String> getDefaultFeatureEncodingNamespace() {
        return Optional.of(SfConstants.NS_SAMS);
    }

    @Override
    protected void writeAddtitionalNamespaces() throws XMLStreamException {
        // nothig to add
    }
}
