/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sensorML.v20;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.n52.iceland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Class that represents SensorML 2.0 FeatrureOfInterest
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.2.0
 *
 */
public class SmlFeatureOfInterest extends SweAbstractDataComponent {

    private final Set<String> featuresOfInterest = Sets.newLinkedHashSet();

    private final Map<String, AbstractFeature> featuresOfInterestMap = Maps.newHashMap();

    @Override
    public SweDataComponentType getDataComponentType() {
        return null;
    }


    public SmlFeatureOfInterest addFeaturesOfInterest(Collection<String> features) {
        getFeaturesOfInterest().addAll(features);
        return this;
    }

    public SmlFeatureOfInterest addFeatureOfInterest(String featureIdentifier) {
        getFeaturesOfInterest().add(featureIdentifier);
        return this;
    }

    public Set<String> getFeaturesOfInterest() {
        return featuresOfInterest;
    }

    public boolean isSetFeaturesOfInterest() {
        return CollectionHelper.isNotEmpty(getFeaturesOfInterest()) || CollectionHelper.isNotEmpty(getFeaturesOfInterestMap());
    }

    public SmlFeatureOfInterest addFeaturesOfInterest(Map<String, AbstractFeature> featuresOfInterestMap) {
        getFeaturesOfInterestMap().putAll(featuresOfInterestMap);
        return this;
    }

    public SmlFeatureOfInterest addFeatureOfInterest(AbstractFeature feature) {
        getFeaturesOfInterestMap().put(feature.getIdentifier(), feature);
        return this;
    }

    public Map<String, AbstractFeature> getFeaturesOfInterestMap() {
        return featuresOfInterestMap;
    }

    public boolean isSetFeaturesOfInterestMap() {
        return CollectionHelper.isNotEmpty(getFeaturesOfInterestMap());
    }

    public boolean hasAbstractFeatureFor(String identifier) {
        return isSetFeaturesOfInterestMap() && getFeaturesOfInterestMap().containsKey(identifier);
    }

    public AbstractFeature getAbstractFeatureFor(String identifier) {
        return getFeaturesOfInterestMap().get(identifier);
    }

    public boolean isSetFeatures() {
        return isSetFeaturesOfInterest() || isSetFeaturesOfInterestMap();
    }

    @Override
    public <T> T accept(SweDataComponentVisitor<T> visitor) throws OwsExceptionReport {
        return visitor.visit(this);
    }

    @Override
    public void accept(VoidSweDataComponentVisitor visitor) throws OwsExceptionReport {
        visitor.visit(this);
    }


    @Override
    public SmlFeatureOfInterest clone() throws CloneNotSupportedException {
        SmlFeatureOfInterest clone = new SmlFeatureOfInterest();
        copyValueTo(clone);
        if (isSetFeaturesOfInterest()) {
            clone.addFeaturesOfInterest(Sets.newHashSet(getFeaturesOfInterest()));
        }
        if (isSetFeaturesOfInterestMap()) {
            clone.addFeaturesOfInterest(Maps.newHashMap(getFeaturesOfInterestMap()));
        }
        return clone;
    }
}
