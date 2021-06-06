/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.om.values.GeometryValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.shetland.util.CollectionHelper;

import org.locationtech.jts.geom.Geometry;

public class ParameterHolder {

    private final SortedSet<NamedValue<?>> parameter = new TreeSet<>();

    public SortedSet<NamedValue<?>> getParameter() {
        return parameter;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<Value<T>> getParameter(String name) {
        if (name == null || !name.isEmpty()) {
            return Optional.empty();
        }
        return this.parameter.stream()
                .filter(nv -> nv.getName().getHref().equals(name))
                .map(nv -> (Value<T>) nv.getValue())
                .findAny();
    }

    public ParameterHolder setParameter(Collection<NamedValue<?>> parameter) {
        this.parameter.clear();
        if (parameter != null) {
            this.parameter.addAll(parameter);
        }
        return this;
    }

    public ParameterHolder addParameter(Collection<NamedValue<?>> parameter) {
        if (parameter != null) {
            this.parameter.addAll(parameter);
        }
        return this;
    }

    public ParameterHolder addParameter(NamedValue<?> parameter) {
        if (parameter != null) {
            this.parameter.add(parameter);
        }
        return this;
    }

    public boolean removeParameter(NamedValue<?> parameter) {
        if (parameter != null) {
            return this.parameter.remove(parameter);
        }
        return false;
    }

    public boolean isSetParameter() {
        return CollectionHelper.isNotEmpty(getParameter());
    }

    /**
     * Check whether height parameter is set
     *
     * @return <code>true</code>, if height parameter is set
     */
    public boolean isSetHeightParameter() {
        return isSetParameter() ? getParameter().stream().anyMatch(this::isHeightParameter) : false;
    }

    /**
     * Get height parameter
     *
     * @return Height parameter
     */
    @SuppressWarnings("unchecked")
    public NamedValue<BigDecimal> getHeightParameter() {
        if (isSetParameter()) {
            for (NamedValue<?> namedValue : getParameter()) {
                if (isHeightParameter(namedValue)) {
                    return (NamedValue<BigDecimal>) namedValue;
                }
            }
        }
        return null;
    }

    private boolean isHeightParameter(NamedValue<?> namedValue) {
        return namedValue.isSetName() && namedValue.getName().isSetHref() &&
               (namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_HEIGHT_URL) ||
                namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_HEIGHT) ||
                namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_ELEVATION) ||
                namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_FROM_HEIGHT) ||
               namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_TO_HEIGHT)) &&
               namedValue.getValue() instanceof QuantityValue;
    }

    /**
     * Check whether depth parameter is set
     *
     * @return <code>true</code>, if depth parameter is set
     */
    public boolean isSetDepthParameter() {
        return getParameter().stream().anyMatch(this::isDepthParameter);
    }

    /**
     * Get depth parameter
     *
     * @return Depth parameter
     */
    @SuppressWarnings("unchecked")
    public NamedValue<BigDecimal> getDepthParameter() {
        if (isSetParameter()) {
            for (NamedValue<?> namedValue : getParameter()) {
                if (isHeightDepthParameter(namedValue)) {
                    return (NamedValue<BigDecimal>) namedValue;
                }
            }
        }
        return null;
    }

    private boolean isDepthParameter(NamedValue<?> namedValue) {
        return namedValue.isSetName() && namedValue.getName().isSetHref() &&
                (namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_DEPTH_URL) ||
                        namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_DEPTH) ||
                        namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_FROM_DEPTH) ||
                       namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_TO_DEPTH)) &&
               namedValue.getValue() instanceof QuantityValue;
    }

    public boolean isSetHeightDepthParameter() {
        return getParameter().stream().anyMatch(this::isHeightDepthParameter);
    }

    public NamedValue<BigDecimal> getHeightDepthParameter() {
        return isSetDepthParameter() ? getDepthParameter() : getHeightParameter();
    }

    private boolean isHeightDepthParameter(NamedValue<?> namedValue) {
        return isHeightParameter(namedValue) || isDepthParameter(namedValue);
    }

    public boolean isSetFromToParameter() {
        return isSetFromParameter() || isSetToParameter();
    }

    private boolean isSetToParameter() {
        return getParameter().stream().anyMatch(this::isToParameter);
    }

    private boolean isSetFromParameter() {
        return getParameter().stream().anyMatch(this::isFromParameter);
    }

    private boolean isToParameter(NamedValue<?> namedValue) {
        return namedValue.isSetName() && namedValue.getName().isSetHref() &&
               (namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_TO_DEPTH) ||
                namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_TO_HEIGHT) ||
                namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_TO)) &&
               namedValue.getValue() instanceof QuantityValue;
    }

    private boolean isFromParameter(NamedValue<?> namedValue) {
        return namedValue.isSetName() && namedValue.getName().isSetHref() &&
               (namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_FROM_DEPTH) ||
                namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_FROM_HEIGHT) ||
                namedValue.getName().getHref().equals(OmConstants.PARAMETER_NAME_FROM)) &&
               namedValue.getValue() instanceof QuantityValue;
    }

    @SuppressWarnings("unchecked")
    public NamedValue<BigDecimal> getToParameter() {
        return getParameter().stream().filter(this::isToParameter)
                .findFirst().map(nv -> (NamedValue<BigDecimal>) nv).orElse(null);
    }

    @SuppressWarnings("unchecked")
    public NamedValue<BigDecimal> getFromParameter() {
        return getParameter().stream().filter(this::isFromParameter)
                .findFirst().map(nv -> (NamedValue<BigDecimal>) nv).orElse(null);
    }

    /**
     * Check whether spatial filtering profile parameter is set
     *
     * @return <code>true</code>, if spatial filtering profile parameter is set
     */
    public boolean isSetSpatialFilteringProfileParameter() {
        return getParameter().stream().anyMatch(this::isSamplingGeometryParameter);
    }

    /**
     * Add sampling geometry to observation
     *
     * @param samplingGeometry The sampling geometry to set
     *
     * @return this
     */
    public ParameterHolder addSpatialFilteringProfileParameter(Geometry samplingGeometry) {
        final NamedValue<Geometry> namedValue = new NamedValue<>();
        namedValue.setName(new ReferenceType(OmConstants.PARAM_NAME_SAMPLING_GEOMETRY));
        namedValue.setValue(new GeometryValue(samplingGeometry));
        addParameter(namedValue);
        return this;
    }

    /**
     * Get spatial filtering profile parameter
     *
     * @return Spatial filtering profile parameter
     */
    @SuppressWarnings("unchecked")
    public NamedValue<Geometry> getSpatialFilteringProfileParameter() {
        return getParameter().stream().filter(this::isSamplingGeometryParameter)
                .findFirst().map(nv -> (NamedValue<Geometry>) nv).orElse(null);
    }

    /**
     * Check whether sampling geometry for spatial filtering profile is set
     *
     * @return <code>true</code>, if sampling geometry for spatial filtering profile is set
     */
    private boolean isSamplingGeometryParameter(NamedValue<?> namedValue) {
        return namedValue.isSetName() && namedValue.getName().isSetHref() &&
               namedValue.getName().getHref().equals(OmConstants.PARAM_NAME_SAMPLING_GEOMETRY) &&
               namedValue.getValue() instanceof GeometryValue;
    }

}
