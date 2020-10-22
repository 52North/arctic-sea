/*
 * Copyright (C) 2018-2020 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */

package org.n52.shetland.ogc.sensorML.elements;

import com.google.common.collect.Lists;
import org.n52.shetland.util.CollectionHelper;

import java.util.List;

/**
 * Implementation of sml:ClassifierList
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class SmlIdentifierList extends SmlIdentifier {

    private List<SmlIdentifier> identification;

    public boolean isSetClassification() {
        return !CollectionHelper.nullEmptyOrContainsOnlyNulls(identification);
    }

    public List<SmlIdentifier> getIdentification() {
        return identification;
    }

    public void setIdentification(List<SmlIdentifier> classifiers) {
        if (isSetClassification()) {
            this.identification.addAll(classifiers);
        } else {
            this.identification = classifiers;
        }
    }

    public void addIdentifier(SmlIdentifier classifier) {
        if (!isSetClassification()) {
            this.identification = Lists.newArrayList();
        }
        this.identification.add(classifier);
    }
}
