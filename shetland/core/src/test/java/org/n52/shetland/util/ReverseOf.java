/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.util;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 4.0.0
 */
public class ReverseOf extends BaseMatcher<Geometry> {

    private Geometry original;

    public ReverseOf(Geometry original) {
        this.original = original;
    }

    @Override
    public boolean matches(Object item) {
        if (item == null || item.getClass() != original.getClass()) {
            return false;
        }
        Geometry geom = (Geometry) item;
        Coordinate[] orig = original.getCoordinates();
        Coordinate[] switched = geom.getCoordinates();
        if (orig.length != switched.length) {
            return false;
        }
        for (int i = 0; i < orig.length; ++i) {
            if (!isSwitched(orig[i], switched[i])) {
                return false;
            }
        }
        return true;
    }

    protected boolean equal(double a, double b) {
        return Double.isNaN(a) ? Double.isNaN(b) : Double.compare(a, b) == 0;
    }

    protected boolean isSwitched(Coordinate a, Coordinate b) {
        return equal(a.getX(), b.getY()) && equal(a.getY(), b.getX()) && equal(a.getZ(), b.getZ());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("reverse of ").appendValue(original);
    }

    public static Matcher<Geometry> reverseOf(Geometry geom) {
        return new ReverseOf(geom);
    }
}
