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
package org.n52.shetland.ogc.sensorML;

import org.n52.shetland.ogc.sensorML.elements.SmlLocation;
import org.n52.shetland.ogc.sensorML.elements.SmlPosition;

/**
 * @since 1.0.0
 *
 */
public class AbstractComponent extends AbstractProcess {
    private SmlPosition position;
    private SmlLocation location;

    public SmlPosition getPosition() {
        return position;
    }

    public AbstractComponent setPosition(final SmlPosition position) {
        this.position = position;
        return this;
    }

    public boolean isSetPosition() {
        return position != null;
    }

    public SmlLocation getLocation() {
        return location;
    }

    public AbstractComponent setLocation(final SmlLocation location) {
        this.location = location;
        return this;
    }

    public boolean isSetLocation() {
        return location != null;
    }

    @Override
    public String getDefaultElementEncoding() {
        return SensorMLConstants.NS_SML;
    }
}
