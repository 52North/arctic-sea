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
package org.n52.shetland.ogc.sos.request;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.sos.ExtendedIndeterminateTime;

public class GetObservationRequestTest {

    @Test
    public void getNotFirstLatestTemporalFilter() {
        GetObservationRequest request = getRequest();
        request.addTemporalFilter(new TemporalFilter(FilterConstants.TimeOperator.TM_Equals,
                new TimeInstant(ExtendedIndeterminateTime.FIRST), "phenomenonTime"));
        assertThat(request.getNotFirstLatestTemporalFilter().isEmpty(), is(true));
        assertThat(request.getFirstLatestTemporalFilter().size(), is(1));
    }

    private GetObservationRequest getRequest() {
        return new GetObservationRequest();
    }

}
