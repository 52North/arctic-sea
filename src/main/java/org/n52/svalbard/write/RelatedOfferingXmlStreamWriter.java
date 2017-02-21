/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.write;

import java.io.OutputStream;

import javax.xml.stream.XMLStreamException;

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.sos.ro.OfferingContext;
import org.n52.shetland.ogc.sos.ro.RelatedOfferingConstants;
import org.n52.shetland.ogc.sos.ro.RelatedOfferings;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.EncodingValues;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class RelatedOfferingXmlStreamWriter extends XmlStreamWriter<RelatedOfferings> {

    private RelatedOfferings relatedOfferings;

    public RelatedOfferingXmlStreamWriter(RelatedOfferings relatedOfferings) {
        setRelatedOfferings(relatedOfferings);
    }

    public RelatedOfferingXmlStreamWriter() {
    }

    @Override
    public void write(OutputStream out) throws XMLStreamException, EncodingException {
        write(getRelatedOfferings(), out);
    }

    @Override
    public void write(OutputStream out, EncodingValues encodingValues) throws XMLStreamException, EncodingException {
        write(getRelatedOfferings(), out, encodingValues);
    }

    @Override
    public void write(RelatedOfferings response, OutputStream out) throws XMLStreamException, EncodingException {
        write(response, out, new EncodingValues());
    }

    @Override
    public void write(RelatedOfferings relatedOfferings, OutputStream out, EncodingValues encodingValues)
            throws XMLStreamException, EncodingException {
        try {
            setRelatedOfferings(relatedOfferings);
            init(out, encodingValues);
            start(encodingValues.isEmbedded());
            writeRelatedOfferingsDoc(encodingValues);
            end();
            finish();
        } catch (XMLStreamException xmlse) {
            throw new EncodingException(xmlse);
        }
    }

    private void writeRelatedOfferingsDoc(EncodingValues encodingValues) throws XMLStreamException {
        start(RelatedOfferingConstants.QN_RO_RELATED_OFFERINGS);
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        namespace(RelatedOfferingConstants.NS_RO_PREFIX, RelatedOfferingConstants.NS_RO);
        namespace(GmlConstants.NS_GML_PREFIX, GmlConstants.NS_GML_32);
        addXlinkHrefAttr(RelatedOfferingConstants.RELATED_OFFERINGS);
        addXlinkTitleAttr(RelatedOfferingConstants.RELATED_OFFERINGS);
        for (OfferingContext offeringContext : getRelatedOfferings().getValue()) {
            start(RelatedOfferingConstants.QN_RO_RELATED_OFFERING);
            writeOfferingContext(offeringContext);
            end(RelatedOfferingConstants.QN_RO_RELATED_OFFERING);
        }
        end(RelatedOfferingConstants.QN_RO_RELATED_OFFERINGS);

    }

    private void writeOfferingContext(OfferingContext offeringContext) throws XMLStreamException {
        start(RelatedOfferingConstants.QN_RO_OFFERING_CONTEXT);
        writeRole(offeringContext.getRole());
        writeRelatedOffering(offeringContext.getRelatedOffering());
        end(RelatedOfferingConstants.QN_RO_OFFERING_CONTEXT);
    }

    private void writeRole(ReferenceType role) throws XMLStreamException {
        empty(RelatedOfferingConstants.QN_RO_ROLE);
        addXlinkHrefAttr(role.getHref());
    }

    private void writeRelatedOffering(ReferenceType relatedOffering) throws XMLStreamException {
        empty(RelatedOfferingConstants.QN_RO_RELATED_OFFERING);
        addXlinkHrefAttr(relatedOffering.getHref());
        if (relatedOffering.isSetTitle()) {
            addXlinkTitleAttr(relatedOffering.getTitle());
        } else {
            addXlinkTitleAttr(getTitleFromHref(relatedOffering.getHref()));
        }
    }

    private String getTitleFromHref(String href) {
        String title = href;
        if (title.startsWith("http")) {
            title = title.substring(title.lastIndexOf('/') + 1, title.length());
        } else if (title.startsWith("urn")) {
            title = title.substring(title.lastIndexOf(':') + 1, title.length());
        }
        if (title.contains("#")) {
            title = title.substring(title.lastIndexOf('#') + 1, title.length());
        }
        return title;
    }

    /**
     * @return the relatedOfferings
     */
    protected RelatedOfferings getRelatedOfferings() {
        return relatedOfferings;
    }

    /**
     * @param relatedOfferings the relatedOfferings to set
     */
    protected void setRelatedOfferings(RelatedOfferings relatedOfferings) {
        this.relatedOfferings = relatedOfferings;
    }

}
