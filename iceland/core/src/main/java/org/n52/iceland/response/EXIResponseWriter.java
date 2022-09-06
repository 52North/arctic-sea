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
package org.n52.iceland.response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.n52.iceland.binding.exi.EXISettings;
import org.n52.iceland.coding.encode.AbstractResponseWriter;
import org.n52.iceland.coding.encode.ResponseProxy;
import org.n52.iceland.coding.encode.ResponseWriterKey;
import org.n52.janmayen.Producer;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.exi.EXIObject;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.exception.EncodingException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.common.base.Charsets;
import com.siemens.ct.exi.core.EXIFactory;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.main.api.sax.EXIResult;

/**
 * Writer class for {@link EXIObject}
 *
 * Converts XML documents via EXI encoding using {@link EXISettings}.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 2.0.0
 *
 */
public class EXIResponseWriter
        extends AbstractResponseWriter<EXIObject<XmlObject>> {

    public static final ResponseWriterKey KEY = new ResponseWriterKey(EXIObject.class);

    private final Producer<EXIFactory> exiFactory;
    private final Producer<XmlOptions> xmlOptions;

    // we can not use injection in this class as it is manually created by a
    // factory
    public EXIResponseWriter(
            EncoderRepository encoderRepository, Producer<EXIFactory> exiFactory, Producer<XmlOptions> xmlOptions) {
        super(encoderRepository);
        this.exiFactory = exiFactory;
        this.xmlOptions = xmlOptions;
    }

    @Override
    public Set<ResponseWriterKey> getKeys() {
        return Collections.singleton(KEY);
    }

    @Override
    public void write(EXIObject<XmlObject> exiObject, OutputStream out, ResponseProxy responseProxy)
            throws IOException, EncodingException {
        write(exiObject, out);
    }

    @Override
    public void write(EXIObject<XmlObject> exiObject, OutputStream out) throws IOException, EncodingException {
        byte[] bytes = getBytes(exiObject);
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            EXIResult result = new EXIResult(this.exiFactory.get());
            result.setOutputStream(out);
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(result.getHandler());
            xmlReader.parse(new InputSource(is));
        } catch (EXIException | SAXException e) {
            throw new EncodingException(e);
        }
    }

    private byte[] getBytes(EXIObject<XmlObject> exi) {
        XmlObject doc = exi.getDoc();
        String text = doc.xmlText(this.xmlOptions.get());
        return text.getBytes(Charsets.UTF_8);
    }

    @Override
    public MediaType getContentType() {
        return MediaTypes.APPLICATION_EXI;
    }

    @Override
    public void setContentType(MediaType contentType) {
        // ignore
    }

    @Override
    public boolean supportsGZip(EXIObject<XmlObject> t) {
        return false;
    }

}
