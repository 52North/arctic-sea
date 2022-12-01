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
package org.n52.iceland.ogc.filter;

import org.n52.shetland.ogc.filter.ComparisonFilter;
import org.n52.shetland.ogc.filter.FilterConstants;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public class ComparisonFilterTest {

    @Test
    public void testCopyOf_like() throws OwsExceptionReport {
        ComparisonFilter original = new ComparisonFilter(FilterConstants.ComparisonOperator.PropertyIsLike, "vr", "val", "up", "escape");
        original.setMatchCase(false);
        original.setSingleChar("single");
        original.setWildCard("wild");
        ComparisonFilter copy = original.copy();

        assertThat(copy.getEscapeString(), is(original.getEscapeString()));
        assertThat(copy.getSingleChar(), is(original.getSingleChar()));
        assertThat(copy.getValue(), is(original.getValue()));
        assertThat(copy.getValueReference(), is(original.getValueReference()));
        assertThat(copy.getValueUpper(), is(original.getValueUpper()));
        assertThat(copy.getWildCard(), is(original.getWildCard()));

        assertThat(copy, is(not(original)));
        assertThat("copy is not equal to original, equal to is not implemented", not(copy.equals(original)));
    }

}
