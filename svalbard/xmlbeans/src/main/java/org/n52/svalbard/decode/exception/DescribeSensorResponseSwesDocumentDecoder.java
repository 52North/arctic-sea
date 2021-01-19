package org.n52.svalbard.decode.exception;

import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.ogc.sos.response.DescribeSensorResponse;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.AbstractSwesDecoderv20;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.DecoderKey;
import org.n52.svalbard.decode.XmlNamespaceDecoderKey;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;

import net.opengis.swes.x20.DescribeSensorResponseDocument;
import net.opengis.swes.x20.DescribeSensorResponseType;
import net.opengis.swes.x20.DescribeSensorResponseType.Description;
import net.opengis.swes.x20.SensorDescriptionType;

public class DescribeSensorResponseSwesDocumentDecoder extends AbstractSwesDecoderv20<DescribeSensorResponse> {

    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
            CodingHelper.decoderKeysForElements(SwesConstants.NS_SWES_20,
                                                DescribeSensorResponseDocument.class));

    @Override
    public DescribeSensorResponse decode(XmlObject objectToDecode) throws DecodingException {
       if (objectToDecode instanceof DescribeSensorResponseDocument) {
           DescribeSensorResponse response = new DescribeSensorResponse();
           DescribeSensorResponseDocument dsrd = (DescribeSensorResponseDocument) objectToDecode;
           DescribeSensorResponseType dsrt = dsrd.getDescribeSensorResponse();
           response.setOutputFormat(dsrt.getProcedureDescriptionFormat());
           for (Description description : dsrt.getDescriptionArray()) {
            SensorDescriptionType sdt = description.getSensorDescription();
            if (sdt.isSetValidTime()) {
                // TODO
            }
            try {
                final XmlObject xbProcedureDescription = XmlObject.Factory
                        .parse(getNodeFromNodeList(sdt.getData().getDomNode().getChildNodes()));
                checkFormatWithNamespace(dsrt.getProcedureDescriptionFormat(),
                        XmlHelper.getNamespace(xbProcedureDescription));
                final Decoder<?, XmlObject> decoder =
                        getDecoder(new XmlNamespaceDecoderKey(dsrt.getProcedureDescriptionFormat(),
                                xbProcedureDescription.getClass()));
                if (decoder != null) {
                    final Object decodedProcedureDescription = decoder.decode(xbProcedureDescription);
                    if (decodedProcedureDescription instanceof SosProcedureDescription) {
                        response.addSensorDescription((SosProcedureDescription) decodedProcedureDescription);
                    } else if (decodedProcedureDescription instanceof AbstractFeature) {
                        response.addSensorDescription(new SosProcedureDescription<>(
                                (AbstractFeature) decodedProcedureDescription));
                    }
                }
            } catch (final XmlException xmle) {
                throw new DecodingException("Error while parsing procedure description of InsertSensor request!", xmle);
            }
        }
           return response;
       }
       throw new UnsupportedDecoderInputException(this, objectToDecode);
    }
    
    @Override
    public Set<DecoderKey> getKeys() {
        return DECODER_KEYS;
    }


}
