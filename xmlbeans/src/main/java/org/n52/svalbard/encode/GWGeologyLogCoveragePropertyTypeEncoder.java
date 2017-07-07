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

import java.util.Collections;
import java.util.Set;

import org.n52.shetland.ogc.gwml.GWMLConstants;
import org.n52.shetland.ogc.om.values.ProfileValue;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import net.opengis.gwmlWell.x22.GWGeologyLogCoveragePropertyType;

public class GWGeologyLogCoveragePropertyTypeEncoder
        extends AbstractGWGeologyLogCoverageType<GWGeologyLogCoveragePropertyType> {

    private static final Set<EncoderKey> ENCODER_KEYS =
            Sets.newHashSet(new ClassToClassEncoderKey(ProfileValue.class, GWGeologyLogCoveragePropertyType.class),
                    new XmlPropertyTypeEncoderKey(GWMLConstants.NS_GWML_22, ProfileValue.class));

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public GWGeologyLogCoveragePropertyType encode(ProfileValue gwGeologyLogCoverage) throws EncodingException {
        return encode(gwGeologyLogCoverage, EncodingContext.empty());
    }

    @Override
    public GWGeologyLogCoveragePropertyType encode(ProfileValue gwGeologyLogCoverage, EncodingContext ec)
            throws EncodingException {
        GWGeologyLogCoveragePropertyType gwglcpt = GWGeologyLogCoveragePropertyType.Factory.newInstance();
        gwglcpt.setGWGeologyLogCoverage(encodeGWGeologyLogCoverage(gwGeologyLogCoverage));
        return gwglcpt;
    }

}
