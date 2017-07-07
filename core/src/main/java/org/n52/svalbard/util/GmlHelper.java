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
package org.n52.svalbard.util;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Helper class for OGC GML. Contains methods to get QName for geometry or time
 * objects.
 *
 * @since 1.0.0
 *
 */
public final class GmlHelper {
    private GmlHelper() {
    }

    public static QName getGml321QnameForGeometry(Geometry geom) {
        if (geom instanceof Point) {
            return new QName(GmlConstants.NS_GML_32, GmlConstants.EN_POINT, GmlConstants.NS_GML);
        } else if (geom instanceof LineString) {
            return new QName(GmlConstants.NS_GML_32, GmlConstants.EN_LINE_STRING, GmlConstants.NS_GML);
        } else if (geom instanceof Polygon) {
            return new QName(GmlConstants.NS_GML_32, GmlConstants.EN_POLYGON, GmlConstants.NS_GML);
        }
        return new QName(GmlConstants.NS_GML_32, GmlConstants.EN_ABSTRACT_GEOMETRY, GmlConstants.NS_GML);
    }

    public static QName getGml321QnameForITime(Time iTime) {
        if (iTime instanceof TimeInstant) {
            return GmlConstants.QN_TIME_INSTANT_32;
        } else if (iTime instanceof TimePeriod) {
            return GmlConstants.QN_TIME_PERIOD_32;
        }
        return GmlConstants.QN_ABSTRACT_TIME_32;
    }

    public static QName getGml311QnameForITime(Time iTime) {
        if (iTime instanceof TimeInstant) {
            return GmlConstants.QN_TIME_INSTANT;
        } else if (iTime instanceof TimePeriod) {
            return GmlConstants.QN_TIME_PERIOD;
        }
        return GmlConstants.QN_ABSTRACT_TIME_32;
    }

    /**
     * Create {@link Time} from {@link DateTime}s
     *
     * @param start
     *            Start {@link DateTime}
     * @param end
     *            End {@link DateTime}
     * @return Resulting {@link Time}
     */
    public static Time createTime(DateTime start, DateTime end) {
        if (start.equals(end)) {
            return new TimeInstant(start);
        } else {
            return new TimePeriod(start, end);
        }
    }
}
