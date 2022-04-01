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

import java.util.Random;

import org.locationtech.jts.geom.Coordinate;


public class JTSHelperForTesting {

    private static final Random RANDOM = new Random();

    public static Coordinate randomCoordinate() {
        return new Coordinate(RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble());
    }

    public static Coordinate[] randomCoordinates(int size) {
        Coordinate[] coordinates = new Coordinate[size];
        for (int i = 0; i < size; ++i) {
            coordinates[i] = randomCoordinate();
        }
        return coordinates;
    }

    public static Coordinate[] randomCoordinateRing(int size) {
        Coordinate[] coordinates = randomCoordinates(size);
        if (size > 0) {
            coordinates[size - 1] = coordinates[0];
        }
        return coordinates;
    }
}
