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
package org.n52.svalbard.encode;


import com.google.common.base.Strings;

/**
 * @deprecated move everything to {@link EncodingContext}.
 */
@Deprecated
public class EncodingValues {

    private EncodingContext additionalValues = EncodingContext.empty();
    private String gmlId;
    private boolean existFoiInDoc;
    private String version;
    private boolean encode;
    private String encodingNamespace;
    private boolean encodeOwsExceptionOnly;
    private boolean addSchemaLocation;
    private int indent;
    private boolean embedded;
    private Encoder<?, ?> encoder;

    public EncodingValues() {

    }

    public EncodingValues(EncodingContext additionalValues) {
        setAdditionalValues(additionalValues);
    }

    /**
     * @return the additionalValues
     */
    public EncodingContext getAdditionalValues() {
        return additionalValues;
    }

    /**
     * @param additionalValues the additionalValues to set
     */
    public EncodingValues setAdditionalValues(EncodingContext additionalValues) {
        this.additionalValues = additionalValues;
        return this;
    }

    public boolean hasAddtitionalValues() {
        return this.additionalValues.isEmpty();
    }

    /**
     * @return the gmlId
     */
    public String getGmlId() {
        return gmlId;
    }

    /**
     * @param gmlId the gmlId to set
     */
    public EncodingValues setGmlId(String gmlId) {
        this.gmlId = gmlId;
        return this;
    }

    public boolean isSetGmlId() {
        return !Strings.isNullOrEmpty(getGmlId());
    }

    /**
     * @return the existFoiInDoc
     */
    public boolean isExistFoiInDoc() {
        return existFoiInDoc;
    }

    /**
     * @param existFoiInDoc the existFoiInDoc to set
     */
    public EncodingValues setExistFoiInDoc(boolean existFoiInDoc) {
        this.existFoiInDoc = existFoiInDoc;
        return this;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public EncodingValues setVersion(String version) {
        this.version = version;
        return this;
    }

    public boolean isSetVersion() {
        return !Strings.isNullOrEmpty(getVersion());
    }

    /**
     * @return the asDocument
     */
    public boolean isAsDocument() {
        return this.additionalValues.has(XmlBeansEncodingFlags.DOCUMENT);
    }

    /**
     * @param asDocument the asDocument to set
     */
    public EncodingValues setAsDocument(boolean asDocument) {
        if (asDocument) {
            this.additionalValues = this.additionalValues.with(XmlBeansEncodingFlags.DOCUMENT)
                    .without(XmlBeansEncodingFlags.PROPERTY_TYPE)
                    .without(XmlBeansEncodingFlags.TYPE);
        } else {
            this.additionalValues = this.additionalValues.without(XmlBeansEncodingFlags.DOCUMENT);
        }
        return this;
    }

    /**
     * @return the asPropertyType
     */
    public boolean isAsPropertyType() {
        return this.additionalValues.has(XmlBeansEncodingFlags.PROPERTY_TYPE);
    }

    /**
     * @param asPropertyType the asPropertyType to set
     */
    public EncodingValues setAsPropertyType(boolean asPropertyType) {
        if (asPropertyType) {
            this.additionalValues = this.additionalValues
                    .with(XmlBeansEncodingFlags.PROPERTY_TYPE)
                    .without(XmlBeansEncodingFlags.DOCUMENT)
                    .without(XmlBeansEncodingFlags.TYPE);
        } else {
            this.additionalValues = this.additionalValues.without(XmlBeansEncodingFlags.PROPERTY_TYPE);
        }
        return this;
    }

    /**
     * @return the encode
     */
    public boolean isEncode() {
        return encode;
    }

    /**
     * @param encode the encode to set
     */
    public EncodingValues setEncode(boolean encode) {
        this.encode = encode;
        return this;
    }

    /**
     * @return the encodingNamespace
     */
    public String getEncodingNamespace() {
        if (encodingNamespace == null && getAdditionalValues().has(XmlEncoderFlags.ENCODE_NAMESPACE)) {
            encodingNamespace = getAdditionalValues().require(XmlEncoderFlags.ENCODE_NAMESPACE);
        }
        return encodingNamespace;
    }

    public boolean isSetEncodingNamespace() {
        return !Strings.isNullOrEmpty(getEncodingNamespace());
    }

    /**
     * @param encodingNamespace the encodingNamespace to set
     */
    public EncodingValues setEncodingNamespace(String encodingNamespace) {
        this.encodingNamespace = encodingNamespace;
        return this;
    }

    /**
     * @return the encodeOwsExceptionOnly
     */
    public boolean isEncodeOwsExceptionOnly() {
        return encodeOwsExceptionOnly;
    }

    /**
     * @param encodeOwsExceptionOnly the encodeOwsExceptionOnly to set
     */
    public EncodingValues setEncodeOwsExceptionOnly(boolean encodeOwsExceptionOnly) {
        this.encodeOwsExceptionOnly = encodeOwsExceptionOnly;
        return this;
    }

    /**
     * @return the addSchemaLocation
     */
    public boolean isAddSchemaLocation() {
        return addSchemaLocation;
    }

    /**
     * @param addSchemaLocation the addSchemaLocation to set
     */
    public void setAddSchemaLocation(boolean addSchemaLocation) {
        this.addSchemaLocation = addSchemaLocation;
    }

    /**
     * @return the indent
     * @deprecated do not use it... Svalbard's {@code IndentingXMLStreamWriter} should handle it
     */
    @Deprecated
    public int getIndent() {
        return indent;
    }

    /**
     * @param indent the indent to set
     */
    public EncodingValues setIndent(int indent) {
        if (indent >= 0) {
            this.indent = indent;
        }
        return this;
    }

    /**
     * @return the embedded
     */
    public boolean isEmbedded() {
        return embedded;
    }

    /**
     * @param embedded the embedded to set
     */
    public EncodingValues setEmbedded(boolean embedded) {
        this.embedded = embedded;
        return this;
    }

    /**
     * @return the encoder
     */
    public Encoder<?, ?> getEncoder() {
        return encoder;
    }

    /**
     * @param encoder the encoder to set
     */
    public void setEncoder(Encoder<?, ?> encoder) {
        this.encoder = encoder;
    }

    public boolean isSetEncoder() {
        return getEncoder() != null;
    }

}
