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

import java.util.Map;
import java.util.Set;

import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.inspire.base.InspireBaseConstants;
import org.n52.shetland.w3c.SchemaLocation;

import com.google.common.collect.Sets;

import eu.europa.ec.inspire.schemas.base.x33.IdentifierType;
import eu.europa.ec.inspire.schemas.base.x33.IdentifierType.VersionId;

public abstract class AbstractIdentifierEncoder<T>
        extends AbstractXmlEncoder<T, Identifier> {

    @Override
    public void addNamespacePrefixToMap(final Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(InspireBaseConstants.NS_BASE, InspireBaseConstants.NS_BASE_PREFIX);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(InspireBaseConstants.BASE_33_SCHEMA_LOCATION);
    }

    protected IdentifierType createIdentifierType(Identifier identifier) {
        IdentifierType it = IdentifierType.Factory.newInstance();
        return encodeIdentifierType(it, identifier);
    }

    protected IdentifierType encodeIdentifierType(IdentifierType it, Identifier identifier) {
        it.setLocalId(identifier.getLocalId());
        it.setNamespace(identifier.getNamespace());
        if (identifier.getVersionId().isPresent()) {
            it.addNewVersionId().setStringValue(identifier.getVersionId().get());
        } else if (identifier.getVersionId().isNil()) {
            VersionId vid = it.addNewVersionId();
            vid.setNil();
            if (identifier.getVersionId().hasReason()) {
                vid.setNilReason(identifier.getVersionId().getNilReason().get());
            }
        }
        return it;
    }

}
