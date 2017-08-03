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
package org.n52.shetland.inspire;

import org.joda.time.DateTime;
import org.n52.shetland.ogc.gml.time.TimeInstant;

/**
 * Service internal representation of INSPIRE date of publication
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class InspireDateOfPublication extends TimeInstant implements InspireDateOf {

    private static final long serialVersionUID = -8856434223315117287L;

    /**
     * constructor
     */
    public InspireDateOfPublication() {
        super();
    }

    /**
     * constructor
     *
     * @param dateTime
     *            the date of publication
     */
    public InspireDateOfPublication(DateTime dateTime) {
        super(dateTime);
    }

    @Override
    public String toString() {
        return String.format("%s %n[%n value=%s%n]", this.getClass().getSimpleName(), super.toString());
    }

}
