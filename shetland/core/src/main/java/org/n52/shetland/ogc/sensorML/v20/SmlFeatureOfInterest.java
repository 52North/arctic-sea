/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Maps;

/**
 * Class that represents SensorML 2.0 FeatrureOfInterest
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class SmlFeatureOfInterest extends SweAbstractDataComponent {

    private final Map<String, AbstractFeature> featuresOfInterestMap = Maps.newHashMap();

    @Override
    public SweDataComponentType getDataComponentType() {
        return null;
    }

    public SmlFeatureOfInterest addFeaturesOfInterest(Collection<String> features) {
        features.forEach(this::addFeatureOfInterest);
        return this;
    }

    public SmlFeatureOfInterest addFeaturesOfInterest(Map<String, AbstractFeature> featuresOfInterestMap) {
        getFeaturesOfInterestMap().putAll(featuresOfInterestMap);
        return this;
    }

    public SmlFeatureOfInterest addFeatureOfInterest(String featureIdentifier) {
        getFeaturesOfInterestMap().put(featureIdentifier,
                new SamplingFeature(new CodeWithAuthority(featureIdentifier)));
        return this;
    }

    public SmlFeatureOfInterest addFeatureOfInterest(AbstractFeature feature) {
        getFeaturesOfInterestMap().put(feature.getIdentifier(), feature);
        return this;
    }

    public Set<String> getFeaturesOfInterest() {
        return getFeaturesOfInterestMap().keySet();
    }

    public boolean isSetFeaturesOfInterest() {
        return CollectionHelper.isNotEmpty(getFeaturesOfInterest())
                || CollectionHelper.isNotEmpty(getFeaturesOfInterestMap());
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
    public <T, X extends Throwable> T accept(SweDataComponentVisitor<T, X> visitor) throws X {
        return visitor.visit(this);
    }

    @Override
    public <X extends Throwable> void accept(VoidSweDataComponentVisitor<X> visitor) throws X {
        visitor.visit(this);
    }

    @Override
    public SmlFeatureOfInterest copy() {
        SmlFeatureOfInterest clone = new SmlFeatureOfInterest();
        copyValueTo(clone);
        if (isSetFeaturesOfInterestMap()) {
            clone.addFeaturesOfInterest(new HashMap<>(getFeaturesOfInterestMap()));
        }
        return clone;
    }
}
