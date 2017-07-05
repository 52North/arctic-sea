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

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.AbstractGML;
import org.n52.shetland.ogc.gml.FeatureWith.FeatureWithEncode;
import org.n52.shetland.ogc.gml.FeatureWith.FeatureWithGeometry;
import org.n52.shetland.ogc.gml.FeatureWith.FeatureWithUrl;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.encode.exception.EncodingException;

import net.opengis.gml.x32.AbstractGMLType;
import net.opengis.gml.x32.FeaturePropertyType;

public abstract class AbstractGmlEncoderv321<T, S> extends AbstractXmlEncoder<T, S> {

    protected XmlObject createFeaturePropertyTypeFrom(final AbstractFeature abstractFeature,
            final EncodingContext context) throws EncodingException {
        final FeaturePropertyType featurePropertyType = createFeaturePropertyType();
        if (context.has(XmlBeansEncodingFlags.REFERENCED) && context.getBoolean(XmlBeansEncodingFlags.REFERENCED)) {
            featurePropertyType.setHref(abstractFeature.getIdentifierCodeWithAuthority().getValue());
            return featurePropertyType;
        } else {
            if (abstractFeature.isSetGmlID()) {
                featurePropertyType.setHref("#" + abstractFeature.getGmlId());
                return featurePropertyType;
            } else {
                if (context.has(XmlBeansEncodingFlags.ENCODE)
                        && !context.getBoolean(XmlBeansEncodingFlags.ENCODE)
                        || (abstractFeature instanceof FeatureWithEncode
                                && !((FeatureWithEncode) abstractFeature).isEncode())) {
                    featurePropertyType.setHref(abstractFeature.getIdentifierCodeWithAuthority().getValue());
                    if (abstractFeature.isSetName()) {
                        featurePropertyType.setTitle(abstractFeature.getFirstName().getValue());
                    }
                    return featurePropertyType;
                }
                if (abstractFeature instanceof FeatureWithGeometry
                        && !((FeatureWithGeometry) abstractFeature).isSetGeometry()) {
                    featurePropertyType.setHref(abstractFeature.getIdentifierCodeWithAuthority().getValue());
                    if (abstractFeature.isSetName()) {
                        featurePropertyType.setTitle(abstractFeature.getFirstName().getValue());
                    }
                    return featurePropertyType;
                }
                if (abstractFeature instanceof FeatureWithUrl && ((FeatureWithUrl) abstractFeature).isSetUrl()) {
                    featurePropertyType.setHref(((FeatureWithUrl) abstractFeature).getUrl());
                    if (abstractFeature.isSetIdentifier()) {
                        featurePropertyType.setTitle(abstractFeature.getIdentifierCodeWithAuthority().getValue());
                    } else {
                        if (abstractFeature.isSetName()) {
                            featurePropertyType.setTitle(abstractFeature.getFirstName().getValue());
                        }
                    }
                    return featurePropertyType;
                } else {
                    return createFeature(featurePropertyType, abstractFeature, context);
                }
            }
        }

    }

    protected void addId(AbstractGMLType aft, AbstractFeature abstractFeature) {
        aft.setId(abstractFeature.getGmlId());
    }

    protected boolean addIdentifier(AbstractGMLType aft, AbstractGML abstractFeature) throws EncodingException {
        if (aft != null && abstractFeature != null) {
            if (abstractFeature.isSetIdentifier()) {
                aft.addNewIdentifier().set(encodeObjectToXml(GmlConstants.NS_GML_32,
                        abstractFeature.getIdentifierCodeWithAuthority()));
                return true;
            }
        }
        return false;
    }

    protected void addName(AbstractGMLType aft, AbstractGML abstractFeature) throws EncodingException {
        if (aft != null && abstractFeature != null) {
            if (abstractFeature.isSetName()) {
                removeExitingNames(aft);
                for (org.n52.shetland.ogc.gml.CodeType codeType : abstractFeature.getName()) {
                    aft.addNewName().set(encodeObjectToXml(GmlConstants.NS_GML_32, codeType));
                }
            }
        }
    }

    protected void addDescription(AbstractGMLType aft, AbstractGML abstractFeature) {
        if (aft != null && abstractFeature != null) {
            if (abstractFeature.isSetDescription()) {
                if (!aft.isSetDescription()) {
                    aft.addNewDescription();
                }
                aft.getDescription().setStringValue(abstractFeature.getDescription());
            }
        }
    }

    protected void removeExitingNames(AbstractGMLType aft) {
        if (CollectionHelper.isNotNullOrEmpty(aft.getNameArray())) {
            for (int i = 0; i < aft.getNameArray().length; i++) {
                aft.removeName(i);
            }
        }
    }

    protected FeaturePropertyType createFeaturePropertyType() {
        return FeaturePropertyType.Factory.newInstance(getXmlOptions());
    }

    protected abstract XmlObject createFeature(FeaturePropertyType featurePropertyType, AbstractFeature abstractFeature,
            EncodingContext context) throws EncodingException;

}
