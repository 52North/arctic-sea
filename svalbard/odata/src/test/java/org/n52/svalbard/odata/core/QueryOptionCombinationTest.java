/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.svalbard.odata.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class QueryOptionCombinationTest extends QueryOptionTests {

    private final String countFilter = "$count=true";
    private final String orderByFilter = "$orderby=id";
    private final String selectFilter = "$select=id";
    private final String skipFilter = "$skip=0";
    private final String topFilter = "$top=1";
    private final String filterFilter_literal = "$filter=id eq '52N'";
    private final String filterFilter_spatial = "$filter=st_equals(location, geography'POINT (30 10)')";
    private final String filterFilter_temporal = "$filter=phenomenonTime le 2020-04-21T04:50:00.000Z";
    private final String filterFilter_temporal_tz = "$filter=phenomenonTime le 2020-04-21T04:50:00.000+02:00";
    private final String expandFilter = "$expand=Datastreams";

    private final String[] allFilters_literal = {
            countFilter, orderByFilter, selectFilter, skipFilter, topFilter,
            filterFilter_literal, expandFilter
    };
    private final String[] allFilters_spatial = {
            countFilter, orderByFilter, selectFilter, skipFilter, topFilter,
            filterFilter_spatial, expandFilter
    };
    private final String[] allFilters_temporal = {
            countFilter, orderByFilter, selectFilter, skipFilter, topFilter,
            filterFilter_temporal, expandFilter
    };

    private final String[] allFilters_temporal_tz = {
            countFilter, orderByFilter, selectFilter, skipFilter, topFilter,
            filterFilter_temporal_tz, expandFilter
    };

    @Test
    public void testAllPermutationsDoNotThrowError() {
        testPermutations(allFilters_literal);
        testPermutations(allFilters_spatial);
        testPermutations(allFilters_temporal);
        testPermutations(allFilters_temporal_tz);
    }

    private void testPermutations(String[] allFilters) {
        testPermutations(Arrays.asList(0, 1, 2, 3, 4, 5, 6), 0, (list) -> {
            StringBuilder filters = new StringBuilder();
            for (Integer integer : list) {
                filters.append("&").append(allFilters[integer]);
            }
            init(filters.substring(1));
            Assertions.assertDoesNotThrow(
                    () -> parser.queryOptions().accept(new STAQueryOptionVisitor()),
                    "Threw error on:" + filters.substring(1)
            );
        });
    }

    /**
     * Code taken from https://stackoverflow.com/a/14444037
     * Licensed under MIT License as stated here: https://meta.stackexchange
     * .com/questions/271080/the-mit-license-clarity-on-using-code-on-stack-overflow-and-stack-exchange
     */
    private static void testPermutations(java.util.List<Integer> arr,
                                         int k,
                                         Consumer<List<Integer>> consumer) {
        for (int i = k; i < arr.size(); i++) {
            java.util.Collections.swap(arr, i, k);
            testPermutations(arr, k + 1, consumer);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() - 1) {
            consumer.accept(arr);
        }
    }
}
