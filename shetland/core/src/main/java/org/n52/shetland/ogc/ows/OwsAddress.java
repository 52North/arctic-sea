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
package org.n52.shetland.ogc.ows;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsAddress {

    private final List<String> deliveryPoint;
    private final Optional<String> city;
    private final Optional<String> administrativeArea;
    private final Optional<String> postalCode;
    private final Optional<String> country;
    private final List<String> electronicMailAddress;

    public OwsAddress(List<String> deliveryPoint, String city, String administrativeArea, String postalCode,
            String country, List<String> electronicMailAddress) {
        this.deliveryPoint = deliveryPoint == null ? Collections.emptyList() : deliveryPoint;
        this.city = Optional.ofNullable(Strings.emptyToNull(city));
        this.administrativeArea = Optional.ofNullable(Strings.emptyToNull(administrativeArea));
        this.postalCode = Optional.ofNullable(Strings.emptyToNull(postalCode));
        this.country = Optional.ofNullable(Strings.emptyToNull(country));
        this.electronicMailAddress = electronicMailAddress == null ? Collections.emptyList() : electronicMailAddress;
    }

    public OwsAddress(String deliveryPoint, String city, String administrativeArea, String postalCode, String country,
            String electronicMailAddress) {
        this(toList(deliveryPoint), city, administrativeArea, postalCode, country, toList(electronicMailAddress));
    }

    public List<String> getDeliveryPoint() {
        return Collections.unmodifiableList(deliveryPoint);
    }

    public Optional<String> getCity() {
        return city;
    }

    public Optional<String> getAdministrativeArea() {
        return administrativeArea;
    }

    public Optional<String> getPostalCode() {
        return postalCode;
    }

    public Optional<String> getCountry() {
        return country;
    }

    public List<String> getElectronicMailAddress() {
        return Collections.unmodifiableList(electronicMailAddress);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.deliveryPoint);
        hash = 53 * hash + Objects.hashCode(this.city);
        hash = 53 * hash + Objects.hashCode(this.administrativeArea);
        hash = 53 * hash + Objects.hashCode(this.postalCode);
        hash = 53 * hash + Objects.hashCode(this.country);
        hash = 53 * hash + Objects.hashCode(this.electronicMailAddress);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OwsAddress other = (OwsAddress) obj;
        if (!Objects.equals(this.deliveryPoint, other.deliveryPoint)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.administrativeArea, other.administrativeArea)) {
            return false;
        }
        if (!Objects.equals(this.postalCode, other.postalCode)) {
            return false;
        }
        if (!Objects.equals(this.country, other.country)) {
            return false;
        }
        if (!Objects.equals(this.electronicMailAddress, other.electronicMailAddress)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OwsAddress{" + "deliveryPoint=" + deliveryPoint + ", city=" + city + ", administrativeArea="
                + administrativeArea + ", postalCode=" + postalCode + ", country=" + country
                + ", electronicMailAddress=" + electronicMailAddress + '}';
    }

    private static <
            T> List<T> toList(T t) {
        return Optional.ofNullable(t).map(Collections::singletonList).orElseGet(Collections::emptyList);
    }

}
