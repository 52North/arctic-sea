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
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.iso.GcoConstants;
import org.n52.shetland.iso.gmd.GmdConstants;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.om.ObservationStream;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;

import com.google.common.collect.Sets;

/**
 * XML stream writer implementation for AQD eResporting
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class AqdGetObservationResponseXmlStreamWriter extends XmlStreamWriter<FeatureCollection> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AqdGetObservationResponseXmlStreamWriter.class);
    private static final long TIMER_PERIOD = 250;
    private Timer timer = new Timer(String.format("empty-string-write-task-for-%s", getClass().getSimpleName()), true);
    private TimerTask timerTask;

    public AqdGetObservationResponseXmlStreamWriter(EncodingContext context, OutputStream outputStream,
                                                    FeatureCollection element) throws XMLStreamException {
        super(context, outputStream, element);
    }

    @Override
    public void write() throws XMLStreamException, EncodingException {
        try {
            start();
            writeFeatureCollectionDoc();
            end();
            finish();
        } finally {
            cleanup();
        }
    }

    private void writeFeatureCollectionDoc()
            throws XMLStreamException, EncodingException {
        start(GmlConstants.QN_FEATURE_COLLECTION_32);
        addNamespaces();
        addSchemaLocations();
        FeatureCollection featureCollection = getElement();
        addGmlId(featureCollection.getGmlId());
        TimeInstant resultTime = new TimeInstant(new DateTime(DateTimeZone.UTC));
        for (AbstractFeature abstractFeature : featureCollection.getMembers().values()) {
            long start = System.currentTimeMillis();
            Encoder<XmlObject, AbstractFeature> encoder = getEncoder(abstractFeature);
            if (abstractFeature instanceof OmObservation) {
                OmObservation observation = (OmObservation) abstractFeature;
                if (observation.getValue() instanceof ObservationStream) {
                    try {
                        // start the timer task to write blank strings to avoid
                        // connection closing
                        startTimer();
                        ObservationStream mergeObservation = ((ObservationStream) observation.getValue()).merge();
                        LOGGER.debug("Observation processing requires {} ms", System.currentTimeMillis() - start);
                        int count = 0;
                        while (mergeObservation.hasNext()) {
                            OmObservation omObservation = mergeObservation.next();
                            if (abstractFeature.isSetGmlID()) {
                                if (count == 0) {
                                    omObservation.setGmlId(abstractFeature.getGmlId());
                                } else {
                                    omObservation.setGmlId(abstractFeature.getGmlId() + "_" + count);
                                }
                                count++;
                            }
                            omObservation.setResultTime(resultTime);
                            String xmlTextObservation = prepareObservation(omObservation, encoder);
                            // stop the timer task
                            stopTimer();
                            writeMember(xmlTextObservation);
                        }
                    } catch (OwsExceptionReport ex) {
                        throw new EncodingException(ex);
                    }
                } else {
                    writeMember(abstractFeature, encoder);
                }
            } else {
                writeMember(abstractFeature, encoder);
            }
            LOGGER.debug("Writing member requires {} ms", System.currentTimeMillis() - start);
        }
        end(GmlConstants.QN_FEATURE_COLLECTION_32);
    }

    private void addNamespaces() throws XMLStreamException {
        // W3C
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        // xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        namespace("xsd", "http://www.w3.org/2001/XMLSchema");

        // OGC
        namespace(OmConstants.NS_OM_PREFIX, OmConstants.NS_OM_2);
        namespace(GmlConstants.NS_GML_PREFIX, GmlConstants.NS_GML_32);
        namespace(SweConstants.NS_SWE_PREFIX, SweConstants.NS_SWE_20);
        namespace(SfConstants.NS_SAMS_PREFIX, SfConstants.NS_SAMS);
        namespace("sam", SfConstants.NS_SF);

        // ISO
        namespace(GmdConstants.NS_GMD_PREFIX, GmdConstants.NS_GMD);
        namespace(GcoConstants.NS_GCO_PREFIX, GcoConstants.NS_GCO);

        // AQD e-Reporting
        namespace(AqdConstants.NS_AQD_PREFIX, AqdConstants.NS_AQD);
        namespace(AqdConstants.NS_AM_PREFIX, AqdConstants.NS_AM);
        namespace(AqdConstants.NS_BASE_PREFIX, AqdConstants.NS_BASE);
        namespace(AqdConstants.NS_BASE2_PREFIX, AqdConstants.NS_BASE2);
        namespace(AqdConstants.NS_OMPR_PREFIX, AqdConstants.NS_OMPR);
        namespace(AqdConstants.NS_EF_PREFIX, AqdConstants.NS_EF);
        namespace(AqdConstants.NS_GN_PREFIX, AqdConstants.NS_GN);
        namespace(AqdConstants.NS_AD_PREFIX, AqdConstants.NS_AD);
    }

    private void addSchemaLocations() throws XMLStreamException {
        Set<SchemaLocation> schemaLocations = Sets.newHashSet();
        schemaLocations.add(AqdConstants.NS_AQD_SCHEMA_LOCATION);
        schemaLocations.add(GmlConstants.GML_32_SCHEMAL_LOCATION);
        schemaLocations.add(OmConstants.OM_20_SCHEMA_LOCATION);
        schemaLocations.add(SweConstants.SWE_20_SCHEMA_LOCATION);
        schemaLocation(schemaLocations);

    }

    private String addGmlId(String gmlId) throws XMLStreamException {
        attr(GmlConstants.QN_ID_32, gmlId);
        return gmlId;
    }

    private String prepareObservation(OmObservation omObservation, Encoder<XmlObject, AbstractFeature> encoder)
            throws EncodingException, XMLStreamException {

        String xmlText = encoder.encode(omObservation, getContext()).xmlText(getXmlOptions());
        // TODO check for better solutions
        xmlText = xmlText.replace("ns:ReferenceType", "gml:ReferenceType");
        xmlText = xmlText.replace(":ns=\"http://www.opengis.net/gml/3.2\"", ":gml=\"http://www.opengis.net/gml/3.2\"");
        xmlText = xmlText.replace("ns:DataArrayPropertyType", "swe:DataArrayPropertyType");
        xmlText = xmlText.replace(":ns=\"http://www.opengis.net/swe/2.0\"", ":swe=\"http://www.opengis.net/swe/2.0\"");
        xmlText = xmlText.replace("<ns:", "<swe:");
        xmlText = xmlText.replace("</ns:", "</swe:");
        return xmlText;
    }

    private void writeMember(AbstractFeature abstractFeature, Encoder<XmlObject, AbstractFeature> encoder)
            throws XMLStreamException, EncodingException {
        writeXmlObject(encoder.encode(abstractFeature, getContext()));
    }

    private void writeMember(String memberContent) throws XMLStreamException, EncodingException {
        start(GmlConstants.QN_FEATURE_MEMBER_32);
        rawText(memberContent);
        end(GmlConstants.QN_FEATURE_MEMBER_32);
    }

    private Encoder<XmlObject, AbstractFeature> getEncoder(AbstractFeature feature)
            throws EncodingException {
        if (feature.isSetDefaultElementEncoding()) {
            return getEncoder(CodingHelper.getEncoderKey(feature.getDefaultElementEncoding(), feature));
        } else if (getEncodeNamespace().isPresent()) {
            return getEncoder(CodingHelper.getEncoderKey(getEncodeNamespace().get(), feature));
        }
        return null;
    }

    /**
     * Initializ a new {@link WriteTimerTask}
     */
    private void initTimer() {
        timerTask = new WriteTimerTask();
    }

    /**
     * Schedule the {@link WriteTimerTask}
     */
    private void startTimer() {
        if (timerTask == null) {
            initTimer();
        }
        timer.schedule(timerTask, TIMER_PERIOD, TIMER_PERIOD);
        LOGGER.debug("Timer started!");
    }

    /**
     * Cancel the {@link WriteTimerTask} and set to <code>null</code>
     */
    private void stopTimer() {
        if (this.timerTask != null) {
            this.timerTask.cancel();
            this.timerTask = null;
            LOGGER.debug("Timer task {} canceled", WriteTimerTask.class.getSimpleName());
        }
    }

    /**
     * Cleanup the {@link Timer} and {@link TimerTask} to avoid conncetion timeout after 1000 ms Stops the
     * {@link WriteTimerTask}, Cancel {@link Timer} and set to <code>null</code>.
     */
    private void cleanup() {
        stopTimer();
        timerTask = null;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * {@link TimerTask} to write blank strings to the {@link OutputStream} to avoid conncetion timeout after 1000 ms
     *
     * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
     * @since 1.0.0
     *
     */
    private class WriteTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                chars("");
                flush();
            } catch (XMLStreamException xmlse) {
                cleanup();
                LOGGER.error("Error while writing empty string by timer task!", xmlse);
            }
        }
    }

}
