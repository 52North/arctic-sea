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
package org.n52.svalbard.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;

import org.apache.xmlbeans.XmlOptions;

import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.janmayen.Producer;
import org.n52.janmayen.function.Consumers;
import org.n52.janmayen.function.Functions;
import org.n52.janmayen.function.Predicates;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.janmayen.lifecycle.Destroyable;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.CodingSettings;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.SchemaAwareEncoder;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * XML utility class
 *
 * @since 1.0.0
 *
 */
@Configurable
public final class XmlOptionsHelper implements Constructable, Destroyable, Producer<XmlOptions> {
    @Deprecated
    private static XmlOptionsHelper instance;

    private EncoderRepository encoderRepository;

    private final ReentrantLock lock = new ReentrantLock();

    private XmlOptions xmlOptions;

    private String characterEncoding = "UTF-8";

    private boolean prettyPrint = true;

    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    @Override
    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    public void init() {
        XmlOptionsHelper.instance = this;
    }

    // TODO: To be used by other encoders to have common prefixes
    private Map<String, String> getPrefixMap() {
        Map<String, String> prefixMap = new HashMap<>();
        prefixMap.put(OGCConstants.NS_OGC, OGCConstants.NS_OGC_PREFIX);
        // prefixMap.put(OmConstants.NS_OM, OmConstants.NS_OM_PREFIX);
        // prefixMap.put(SfConstants.NS_SA, SfConstants.NS_SA_PREFIX);
        // prefixMap.put(Sos1Constants.NS_SOS, SosConstants.NS_SOS_PREFIX);
        prefixMap.put(W3CConstants.NS_XLINK, W3CConstants.NS_XLINK_PREFIX);
        prefixMap.put(W3CConstants.NS_XSI, W3CConstants.NS_XSI_PREFIX);
        prefixMap.put(W3CConstants.NS_XS, W3CConstants.NS_XS_PREFIX);
        encoderRepository.getEncoders().stream()
                .filter(Predicates.instanceOf(SchemaAwareEncoder.class))
                .map(Functions.cast(SchemaAwareEncoder.class))
                .forEach(Consumers.currySecond(SchemaAwareEncoder<?, ?>::addNamespacePrefixToMap, prefixMap));
        return prefixMap;
    }

    /**
     * Get the XML options for SOS 1.0.0
     *
     * @return SOS 1.0.0 XML options
     */
    public XmlOptions getXmlOptions() {
        if (xmlOptions == null) {
            lock.lock();
            try {
                if (xmlOptions == null) {
                    xmlOptions = new XmlOptions();
                    Map<String, String> prefixes = getPrefixMap();
                    xmlOptions.setSaveSuggestedPrefixes(prefixes);
                    xmlOptions.setSaveImplicitNamespaces(prefixes);
                    xmlOptions.setSaveAggressiveNamespaces();
                    if (prettyPrint) {
                        xmlOptions.setSavePrettyPrint();
                    }
                    xmlOptions.setSaveNamespacesFirst();
                    xmlOptions.setCharacterEncoding(characterEncoding);
                }
            } finally {
                lock.unlock();
            }
        }
        return xmlOptions;
    }

    /**
     * Cleanup, set XML options to null
     */
    @Override
    public void destroy() {
        xmlOptions = null;
    }

    public void setPrettyPrint(boolean prettyPrint) {
        lock.lock();
        try {
            if (this.prettyPrint != prettyPrint) {
                setReload();
            }
            this.prettyPrint = prettyPrint;
        } finally {
            lock.unlock();
        }
    }

    @Setting(CodingSettings.CHARACTER_ENCODING)
    public void setCharacterEncoding(String characterEncoding) {
        lock.lock();
        try {
            Validation.notNullOrEmpty("Character Encoding", characterEncoding);
            if (!this.characterEncoding.equals(characterEncoding)) {
                setReload();
            }
            this.characterEncoding = characterEncoding;
        } finally {
            lock.unlock();
        }
    }

    private void setReload() {
        lock.lock();
        try {
            xmlOptions = null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public XmlOptions get() {
        return getXmlOptions();
    }

    /**
     * Get INSTANCE from class with default character encoding UTF-8
     *
     * @return INSTANCE
     *
     * @deprecated Use injection:
     *
     * <pre>
     * &#064;Inject
     * private Provider&lt;XmlOptioon&gt; xmloptions;
     * ...
     * XmlOptions options = this.xmlOptions.get();
     * </pre>
     */
    @Deprecated
    public static XmlOptionsHelper getInstance() {
        return instance;
    }
}
