package org.n52.svalbard.decode.exception;

import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.sos.response.DescribeSensorResponse;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.AbstractSwesDecoderv20;
import org.n52.svalbard.decode.DecoderKey;
import org.n52.svalbard.util.CodingHelper;

import net.opengis.swes.x20.DescribeSensorResponseDocument;

public class DescribeSensorResponseSwesDocumentDecoder extends AbstractSwesDecoderv20<DescribeSensorResponse> {

    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
            CodingHelper.decoderKeysForElements(SwesConstants.NS_SWES_20,
                                                DescribeSensorResponseDocument.class));

    @Override
    public DescribeSensorResponse decode(XmlObject objectToDecode) throws DecodingException {
       if (objectToDecode instanceof DescribeSensorResponseDocument) {
           DescribeSensorResponseDocument doc = (DescribeSensorResponseDocument) objectToDecode;
           doc.getDescribeSensorResponse().getDescriptionArray()[0].getSensorDescription();
           return new DescribeSensorResponse();
       }
       throw new UnsupportedDecoderInputException(this, objectToDecode);
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return DECODER_KEYS;
    }


}
