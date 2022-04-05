/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import com.google.common.base.Strings;
import org.locationtech.jts.geom.Geometry;

public interface FeatureWith {

    interface FeatureWithGeometry {
        /**
         * Get feature geometry
         *
         * @return Feature geometry
         */
        Geometry getGeometry();

        /**
         * Set feature geometry, checks whether srid is valid
         *
         * @param geometry
         *            Geometry to set
         */
        void setGeometry(Geometry geometry);

        /**
         * Check whether geometry is set
         *
         * @return <code>true</code>, if geometry is set
         */
        default boolean isSetGeometry() {
            return getGeometry() != null && !getGeometry().isEmpty();
        }
    }

    interface FeatureWithFeatureType {
        /**
         * Get feature type
         *
         * @return Type of this feature
         */
        String getFeatureType();

        /**
         * Set feature type
         *
         * @param featureType
         *            Type of this feature
         */
        void setFeatureType(String featureType);

        /**
         * Check whether feature type is set
         *
         * @return <code>true</code>, if feature type is set
         */
        default boolean isSetFeatureType() {
            return !Strings.isNullOrEmpty(getFeatureType());
        }
    }

    interface FeatureWithUrl {
        /**
         * Get URL
         *
         * @return URL
         */
        String getUrl();

        /**
         * Set URL
         *
         * @param url
         *            URL to set
         */
        void setUrl(String url);

        /**
         * Check whether URL is set
         *
         * @return <code>true</code>, if URL is set
         */
        default boolean isSetUrl() {
            return !Strings.isNullOrEmpty(getUrl());
        }
    }

    interface FeatureWithEncode {

        /**
         * Check whether parameters are set
         *
         * @return <code>true</code>, if parameters are set
         */
        boolean isSetParameter();

        /**
         * Check whether feature should be encoded
         *
         * @return <code>true</code>, if feature should be encoded
         */
        boolean isEncode();
    }

}
