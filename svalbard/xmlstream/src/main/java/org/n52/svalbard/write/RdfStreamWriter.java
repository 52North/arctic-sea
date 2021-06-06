/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import javax.xml.stream.XMLStreamException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFWriter;
import org.n52.shetland.rdf.RDF;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.exception.EncodingException;

public class RdfStreamWriter extends XmlStreamWriter<RDF> {

    public RdfStreamWriter(EncodingContext context, OutputStream outputStream, RDF element) throws XMLStreamException {
        super(context, outputStream, element);
    }

    @Override
    public void write() throws XMLStreamException, EncodingException {
        Model model = ModelFactory.createDefaultModel();
        getElement().addToModel(model);
        RDFWriter w = model.getWriter("RDF/XML-ABBREV");
        w.setProperty("showXMLDeclaration", "true");
        w.setProperty("tab", "4");
        w.write(model, getOutputStream(), null);
    }

}
