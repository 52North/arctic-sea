package org.n52.shetland.ogc.sos.request;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;
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
