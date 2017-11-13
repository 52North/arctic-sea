package org.n52.svalbard.encode;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.sos.SosResultStructure;
import org.n52.shetland.ogc.sos.request.InsertResultTemplateRequest;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

public class InsertResultTemplateRequestEncoderTest {

    private InsertResultTemplateRequestEncoder encoder;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        encoder = new InsertResultTemplateRequestEncoder();
    }

    @Test
    public void shouldThrowExceptionOnNullInput() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertResultTemplateRequestEncoder.class.getSimpleName() +
                " can not encode 'null'"));

        encoder.create(null);
    }

    @Test
    public void shouldThrowExceptionWhenObservationTemplateIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertResultTemplateRequestEncoder.class.getSimpleName() +
                " can not encode 'missing ObservationTemplate'"));

        encoder.create(new InsertResultTemplateRequest());
    }

    @Test
    public void shouldThrowExceptionWhenResultStructureIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertResultTemplateRequestEncoder.class.getSimpleName() +
                " can not encode 'missing resultStructure'"));

        InsertResultTemplateRequest request = new InsertResultTemplateRequest();
        request.setObservationTemplate(new OmObservationConstellation());
        encoder.create(request);
    }

    @Test
    public void shouldThrowExceptionWhenResultEncodingIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertResultTemplateRequestEncoder.class.getSimpleName() +
                " can not encode 'missing resultEncoding'"));

        InsertResultTemplateRequest request = new InsertResultTemplateRequest();
        request.setObservationTemplate(new OmObservationConstellation());
        request.setResultStructure(new SosResultStructure(new SweDataRecord()));
        encoder.create(request);
    }

}
