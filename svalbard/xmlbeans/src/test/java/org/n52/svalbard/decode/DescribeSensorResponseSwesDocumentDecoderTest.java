package org.n52.svalbard.decode;

import java.io.IOException;
import java.util.Arrays;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.sos.response.DescribeSensorResponse;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.DescribeSensorResponseSwesDocumentDecoder;
import org.n52.svalbard.encode.DescribeSensorResponseEncoder;
import org.n52.svalbard.util.CodingHelper;

public class DescribeSensorResponseSwesDocumentDecoderTest {

    private DescribeSensorResponseSwesDocumentDecoder decoder;
    private DecoderRepository decoderRepository;

    @BeforeEach
    public void setup() {
        decoderRepository = new DecoderRepository();

        decoder = new DescribeSensorResponseSwesDocumentDecoder();
        decoder.setDecoderRepository(decoderRepository);
        decoder.setXmlOptions(XmlOptions::new);

        decoderRepository.setDecoders(Arrays.asList(decoder));
        decoderRepository.init();
    }

    @Test
    public void should_parse_DWithin_Filter() throws DecodingException, XmlException, IOException {
        XmlObject xml = decode("/DescribeSensorResponseSML101.xml");
        DecoderKey decoderKey = CodingHelper.getDecoderKey(xml);
        Decoder<DescribeSensorResponse, XmlObject> decoder = decoderRepository.getDecoder(decoderKey);
        DescribeSensorResponse doc = decoder.decode(xml);
    }

    private XmlObject decode(String fileName) throws DecodingException, XmlException, IOException {
        return XmlObject.Factory.parse(getClass().getResourceAsStream(fileName));
    }
}
