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
package org.n52.shetland.ogc.gml;

import java.util.List;

import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Referenceable;

import com.google.common.collect.Lists;

/**
 * Internal representation of the OGC GML VerticlaCRS.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class VerticalCRS
        extends AbstractCRS {

    private Referenceable<VerticalCS> verticalCS = Referenceable.of(Nillable.<VerticalCS> missing());

    private Referenceable<VerticalDatum> verticalDatum = Referenceable.of(Nillable.<VerticalDatum> missing());

    public VerticalCRS(
            CodeWithAuthority identifier, String scope, Referenceable<VerticalCS> verticalCS,
            Referenceable<VerticalDatum> verticalDatum) {
        this(identifier, Lists.newArrayList(scope), verticalCS, verticalDatum);
    }

    public VerticalCRS(
            CodeWithAuthority identifier, List<String> scope, Referenceable<VerticalCS> verticalCS,
            Referenceable<VerticalDatum> verticalDatum) {
        super(identifier, scope);
        setVerticalCS(verticalCS);
        setVerticalDatum(verticalDatum);
    }

    /**
     * @return the verticalCS
     */
    public Referenceable<VerticalCS> getVerticalCS() {
        return verticalCS;
    }

    /**
     * @param verticalCS
     *            the verticalCS to set
     */
    public void setVerticalCS(Referenceable<VerticalCS> verticalCS) {
        this.verticalCS = verticalCS;
    }

    /**
     * @return the verticalDatum
     */
    public Referenceable<VerticalDatum> getVerticalDatum() {
        return verticalDatum;
    }

    /**
     * @param verticalDatum
     *            the verticalDatum to set
     */
    public void setVerticalDatum(Referenceable<VerticalDatum> verticalDatum) {
        this.verticalDatum = verticalDatum;
    }

}
