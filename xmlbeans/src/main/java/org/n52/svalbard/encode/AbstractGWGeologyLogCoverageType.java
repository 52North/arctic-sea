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

import org.n52.shetland.ogc.om.values.ProfileLevel;
import org.n52.shetland.ogc.om.values.ProfileValue;
import org.n52.shetland.util.JavaHelper;
import org.n52.svalbard.encode.exception.EncodingException;

import net.opengis.gwmlWell.x22.GWGeologyLogCoverageType;

public abstract class AbstractGWGeologyLogCoverageType<T>
        extends AbstractGroundWaterMLEncoder<T, ProfileValue> {

    protected GWGeologyLogCoverageType encodeGWGeologyLogCoverage(ProfileValue gwGeologyLogCoverage)
            throws EncodingException {
        GWGeologyLogCoverageType gwglct = GWGeologyLogCoverageType.Factory.newInstance();
        setGmlId(gwglct, gwGeologyLogCoverage);
        addIdentifier(gwglct, gwGeologyLogCoverage);
        addDescription(gwglct, gwGeologyLogCoverage);
        addName(gwglct, gwGeologyLogCoverage);
        setElements(gwglct, gwGeologyLogCoverage);
        return gwglct;
    }

    private void setGmlId(GWGeologyLogCoverageType gwglct, ProfileValue gwGeologyLogCoverage) {
        if (!gwGeologyLogCoverage.isSetGmlID()) {
            gwGeologyLogCoverage
                    .setGmlId(JavaHelper.generateID(Double.toString(System.currentTimeMillis() * Math.random())));
        }
        gwGeologyLogCoverage.setGmlId("gwglc_" + gwGeologyLogCoverage.getGmlId());
        gwglct.setId(gwGeologyLogCoverage.getGmlId());
    }

    private void setElements(GWGeologyLogCoverageType gwglct, ProfileValue gwGeologyLogCoverage)
            throws EncodingException {
        if (gwGeologyLogCoverage.isSetValue()) {
            for (ProfileLevel logValue : gwGeologyLogCoverage.getValue()) {
                gwglct.addNewElement().set(encodeGWMLProperty(logValue));
            }
        }
    }

}
