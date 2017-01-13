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
package org.n52.svalbard;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.AbstractGML;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.AbstractXmlDecoder;
import org.n52.svalbard.decode.exception.DecodingException;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import net.opengis.gml.x32.AbstractFeatureType;
import net.opengis.gml.x32.AbstractGMLType;
import net.opengis.gml.x32.CodeType;
import net.opengis.gml.x32.CodeWithAuthorityType;
import net.opengis.gml.x32.ReferenceType;

public abstract class AbstractGmlDecoderv321<T, S> extends AbstractXmlDecoder<T, S> implements ConformanceClass {

    protected AbstractGML parseAbstractGMLType(AbstractGMLType agmlt, AbstractGML abstractGML)
            throws DecodingException {
        parseIdentifier(agmlt, abstractGML);
        parseNames(agmlt, abstractGML);
        paresDescription(agmlt, abstractGML);
        parseMetaDataProperty(agmlt, abstractGML);
        return null;
    }

    protected AbstractFeature parseAbstractFeatureType(AbstractFeatureType aft, AbstractFeature abstractFeature)
            throws DecodingException {
        parseAbstractGMLType(aft, abstractFeature);
        parseBoundedBy(aft, abstractFeature);
        parseLocation(aft, abstractFeature);
        return abstractFeature;
    }

    protected AbstractGML parseIdentifier(AbstractGMLType agmlt, AbstractGML abstractGML) throws DecodingException {
        if (agmlt.isSetIdentifier()) {
            abstractGML.setIdentifier(parseCodeWithAuthorityTye(agmlt.getIdentifier()));
        }
        return abstractGML;
    }

    protected AbstractGML parseNames(AbstractGMLType agmlt, AbstractGML abstractGML) throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(agmlt.getNameArray())) {
            for (CodeType ct : agmlt.getNameArray()) {
                abstractGML.addName(parseCodeType(ct));
            }
        }
        return abstractGML;
    }

    protected AbstractGML paresDescription(AbstractGMLType agmlt, AbstractGML abstractGML) {
        if (agmlt.isSetDescription()) {
            if (agmlt.getDescription().isSetHref()) {
                abstractGML.setDescription(agmlt.getDescription().getHref());
            } else {
                abstractGML.setDescription(agmlt.getDescription().getStringValue());
            }
        } else if (agmlt.isSetDescriptionReference()) {
            // TODO
        }
        return abstractGML;

    }

    protected AbstractGML parseMetaDataProperty(AbstractGMLType agmlt, AbstractGML abstractGML) {
        if (CollectionHelper.isNotNullOrEmpty(agmlt.getMetaDataPropertyArray())) {
            // TODO
        }
        return abstractGML;
    }

    protected AbstractFeature parseBoundedBy(AbstractFeatureType aft, AbstractFeature abstractFeature) {
        if (aft.isSetBoundedBy()) {
            // TODO
        }
        return abstractFeature;
    }

    protected AbstractFeature parseLocation(AbstractFeatureType aft, AbstractFeature abstractFeature) {
        if (aft.isSetLocation()) {
            // TODO
        }
        return abstractFeature;
    }

    protected CodeWithAuthority parseCodeWithAuthorityTye(CodeWithAuthorityType xbCodeWithAuthority) {
        if (xbCodeWithAuthority.getStringValue() != null && !xbCodeWithAuthority.getStringValue().isEmpty()) {
            CodeWithAuthority sosCodeWithAuthority = new CodeWithAuthority(xbCodeWithAuthority.getStringValue());
            sosCodeWithAuthority.setCodeSpace(xbCodeWithAuthority.getCodeSpace());
            return sosCodeWithAuthority;
        }
        return null;
    }

    protected org.n52.shetland.ogc.gml.CodeType parseCodeType(CodeType element) throws DecodingException {
        org.n52.shetland.ogc.gml.CodeType codeType = new org.n52.shetland.ogc.gml.CodeType(element.getStringValue());
        if (element.isSetCodeSpace()) {
            try {
                codeType.setCodeSpace(new URI(element.getCodeSpace()));
            } catch (URISyntaxException e) {
               throw new DecodingException(e, "Error while creating URI from '{}'", element.getCodeSpace());
            }
        }
        return codeType;
    }

    protected org.n52.shetland.ogc.gml.ReferenceType parseReferenceType(ReferenceType rt) {
        org.n52.shetland.ogc.gml.ReferenceType referenceType = new org.n52.shetland.ogc.gml.ReferenceType("UNKNOWN");
        if (rt.isSetTitle() && !Strings.isNullOrEmpty(rt.getTitle())) {
            referenceType.setTitle(rt.getTitle());
        }
        if (rt.isSetHref() && !Strings.isNullOrEmpty(rt.getHref())) {
            referenceType.setHref(rt.getHref());
        }
        if (rt.isSetRole() && !Strings.isNullOrEmpty(rt.getRole())) {
            referenceType.setRole(rt.getRole());
        }
        return referenceType;
    }


    protected List<org.n52.shetland.ogc.gml.ReferenceType> parseReferenceType(ReferenceType[] referenceTypes) {
        List<org.n52.shetland.ogc.gml.ReferenceType> list = Lists.newArrayList();
        if (CollectionHelper.isNotNullOrEmpty(referenceTypes)) {
            for (ReferenceType referenceType : referenceTypes) {
                list.add(parseReferenceType(referenceType));
            }
        }
        return list;
    }
}
