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
import org.n52.shetland.ogc.om.values.ProfileLevel;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import net.opengis.gwmlWell.x22.LogValuePropertyType;

public class LogValuePropertyTypeEncoder
        extends AbstractLogValueTypeEncoder<LogValuePropertyType> {

    private static final Set<EncoderKey> ENCODER_KEYS =
            Sets.newHashSet(new ClassToClassEncoderKey(ProfileLevel.class, LogValuePropertyType.class),
                    new XmlPropertyTypeEncoderKey(GWMLConstants.NS_GWML_22, ProfileLevel.class));

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public LogValuePropertyType encode(ProfileLevel logValue, EncodingContext additionalValues)
            throws EncodingException {
        LogValuePropertyType lvpt = LogValuePropertyType.Factory.newInstance();
        if (logValue.isSetValue()) {
            lvpt.setLogValue(encodeLogValue(logValue));
        } else {
            lvpt.setNil();
        }
        return lvpt;
    }

}
