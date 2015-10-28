/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.filter;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import org.junit.Test;
import org.n52.iceland.exception.ows.OwsExceptionReport;

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
        ComparisonFilter copy = ComparisonFilter.of(original);

        Assert.assertThat(copy.getEscapeString(), is(original.getEscapeString()));
        Assert.assertThat(copy.getSingleChar(), is(original.getSingleChar()));
        Assert.assertThat(copy.getValue(), is(original.getValue()));
        Assert.assertThat(copy.getValueReference(), is(original.getValueReference()));
        Assert.assertThat(copy.getValueUpper(), is(original.getValueUpper()));
        Assert.assertThat(copy.getWildCard(), is(original.getWildCard()));

        Assert.assertThat(copy, is(not(original)));
        Assert.assertThat("copy is not equal to original, equal to is not implemented", not(copy.equals(original)));
    }

}
