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
package org.n52.shetland.util;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.n52.shetland.util.CollectionHelper.unionOfListOfLists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 * @since 1.0.0
 *
 */
public class CollectionHelperTest {
    private final Set<String> EMPTY_COLLECTION = new HashSet<String>(0);

    @Test
    public void should_return_empty_list_when_union_receives_null() {
        assertThat(unionOfListOfLists((Set<Set<String>>) null), is(EMPTY_COLLECTION));
    }

    @Test
    public void should_return_empty_list_when_unionOfListOfLists_receives_empty_list() {
        final Collection<? extends Collection<String>> emptyList = new ArrayList<Set<String>>(0);
        assertThat(unionOfListOfLists(emptyList), is(EMPTY_COLLECTION));
    }

    @Test
    public void should_return_union_of_values_without_duplicates() {
        final Collection<String> listA = new ArrayList<String>(2);
        listA.add("A");
        listA.add("B");

        final Collection<String> listB = new ArrayList<String>(4);
        listB.add("B");
        listB.add("C");
        listB.add(null);

        final Collection<String> listC = new ArrayList<String>(2);
        listC.add("");

        final Collection<Collection<String>> col = new ArrayList<Collection<String>>(4);
        col.add(listA);
        col.add(listB);
        col.add(listC);
        col.add(null);
        col.add(new ArrayList<String>(0));

        final Collection<String> check = new HashSet<String>(4);
        check.add("A");
        check.add("B");
        check.add("C");
        check.add("");
        assertThat(unionOfListOfLists(col), is(check));
    }

    @Test
    public void isNotEmpty_should_return_true_if_map_is_not_empty() {
        final Map<String, String> map = new HashMap<String, String>(1);
        map.put("key", "value");
        assertThat(CollectionHelper.isNotEmpty(map), is(TRUE));
    }

    @Test
    public void isNotEmpty_should_return_false_if_map_is_empty() {
        final Map<String, String> map = new HashMap<String, String>(0);
        assertThat(CollectionHelper.isNotEmpty(map), is(FALSE));
    }

    @Test
    public void isEmpty_should_return_true_if_map_is_empty() {
        final Map<String, String> map = new HashMap<String, String>(0);
        assertThat(CollectionHelper.isEmpty(map), is(TRUE));
    }

    @Test
    public void isEmpty_should_return_false_if_map_is_not_empty() {
        final Map<String, String> map = new HashMap<String, String>(1);
        map.put("key", "value");
        assertThat(CollectionHelper.isEmpty(map), is(FALSE));
    }

    @Test
    public void should_return_String() {
        String empty = "()";
        String full = "(a,b,c)";
        Set<String> set = Sets.newLinkedHashSet();
        assertThat(CollectionHelper.collectionToString(set), is(empty));
        set.add("a");
        set.add("b");
        set.add("c");
        assertThat(CollectionHelper.collectionToString(set), is(full));
    }

    @Test
    public void should_return_set_sorted_by_value() {
        Map<String,Integer> unsorted = new HashMap<>();
        unsorted.put("A", 3);
        unsorted.put("B", 4);
        unsorted.put("C", 2);
        unsorted.put("D", 1);
        Map<String, Integer> sorted = CollectionHelper.sortByValue(unsorted);
        for (Entry<String, Integer> string : unsorted.entrySet()) {
            if (!sorted.containsKey(string.getKey()) || !sorted.containsValue(string.getValue())){
                fail("sorted set doesn't contain all values of unsorted");
            }
        }
        Iterator<Integer> iterator = sorted.values().iterator();
        assertThat(iterator.next(),is(1));
        assertThat(iterator.next(),is(2));
        assertThat(iterator.next(),is(3));
        assertThat(iterator.next(),is(4));
    }
}
