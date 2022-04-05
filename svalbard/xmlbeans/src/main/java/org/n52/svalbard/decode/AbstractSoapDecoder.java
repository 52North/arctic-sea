/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.svalbard.decode;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.n52.janmayen.exception.CompositeException;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.shetland.w3c.soap.AbstractSoap;
import org.n52.shetland.w3c.soap.SoapHeader;
import org.n52.shetland.w3c.wsa.WsaActionHeader;
import org.n52.shetland.w3c.wsa.WsaConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.SchemaRepository;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public abstract class AbstractSoapDecoder extends AbstractXmlDecoder<XmlObject, AbstractSoap<?>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSoapDecoder.class);

    private final Set<DecoderKey> decoderKeys;

    private SchemaRepository schemaRepository;

    public AbstractSoapDecoder(String namespace) {
        this.decoderKeys = Collections.<DecoderKey> singleton(new XmlNamespaceDecoderKey(namespace, XmlObject.class));
    }

    @Inject
    public void setSchemaRepository(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(decoderKeys);
    }

    @Override
    public AbstractSoap<?> decode(XmlObject xmlObject) throws DecodingException {
        try {
            return createEnvelope(xmlObject);
        } catch (DecodingException de) {
            return createFault(de);
        }
    }

    protected abstract AbstractSoap<?> createEnvelope(XmlObject xml) throws DecodingException;

    protected abstract AbstractSoap<?> createFault(DecodingException xml);

    protected String checkSoapAction(String soapAction, List<SoapHeader> soapHeaders) {
        if (soapAction != null && !soapAction.isEmpty()) {
            return soapAction;
        } else if (CollectionHelper.isNotEmpty(soapHeaders)) {
            for (SoapHeader soapHeader : soapHeaders) {
                if (WsaConstants.NS_WSA.equals(soapHeader.getNamespace()) && soapHeader instanceof WsaActionHeader) {
                    return ((WsaActionHeader) soapHeader).getValue();
                }
            }
        }
        return null;
    }

    protected String getFaultReasons(DecodingException de) {
        if (de.getCause() instanceof CompositeException) {
            return Joiner.on("\n")
                    .join(((CompositeException) de.getCause()).getExceptions()
                            .stream()
                            .map(e -> e.getMessage())
                            .collect(Collectors.toList()));
        }
        return de.getMessage();
    }

    protected void fixNamespaceForXsiType(XmlObject content, Map<?, ?> namespaces) {
        final XmlCursor cursor = content.newCursor();
        while (cursor.hasNextToken()) {
            if (cursor.toNextToken().isStart()) {
                final String xsiType = cursor.getAttributeText(W3CConstants.QN_XSI_TYPE);
                if (xsiType != null) {
                    final String[] toks = xsiType.split(":");
                    if (toks.length > 1) {
                        String prefix = toks[0];
                        String localName = toks[1];
                        String namespace = (String) namespaces.get(prefix);
                        if (Strings.isNullOrEmpty(namespace)) {
                            namespace = schemaRepository.getNamespaceFor(prefix);
                        }
                        if (!Strings.isNullOrEmpty(namespace)) {
                            cursor.setAttributeText(W3CConstants.QN_XSI_TYPE,
                                    Joiner.on(":").join(
                                            XmlHelper.getPrefixForNamespace(content, (String) namespaces.get(prefix)),
                                            localName));
                        }
                    }

                }
            }
        }
        cursor.dispose();
    }
}
