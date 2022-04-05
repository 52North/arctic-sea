/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.svalbard.encode;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.n52.janmayen.Producer;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.filter.FilterConstants.SpatialOperator;
import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;

import com.google.common.collect.Maps;

import net.opengis.fes.x20.BBOXType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class FesEncoderv20Test {

    private static FesEncoderv20 instance;

    @BeforeAll
    public static void initInstance() {
        instance = new FesEncoderv20();
        Producer<XmlOptions> options = () -> new XmlOptions();
        instance.setXmlOptions(options);

        EncoderRepository encoderRepository = new EncoderRepository();
        encoderRepository.setEncoders(Arrays.asList(instance));
        encoderRepository.init();

        SchemaRepository schemaRepository = new SchemaRepository();
        schemaRepository.setEncoderRepository(encoderRepository);
        schemaRepository.init();

        instance.setEncoderRepository(encoderRepository);
        instance.setXmlOptions(options);

    }

    @Test
    public final void should_return_correct_encoder_keys() {
        final Set<EncoderKey> expectedKeySet =
                CodingHelper.encoderKeysForElements(FilterConstants.NS_FES_2, TemporalFilter.class,
                        org.n52.shetland.ogc.filter.FilterCapabilities.class, SpatialFilter.class);
        final Set<EncoderKey> returnedKeySet = instance.getKeys();

        assertThat(returnedKeySet.size(), is(3));
        assertThat(returnedKeySet, is(expectedKeySet));
    }

    @Test
    public final void should_return_emptyMap_for_supportedTypes() {
        assertThat(instance.getSupportedTypes(), is(not(nullValue())));
        assertThat(instance.getSupportedTypes().isEmpty(), is(TRUE));
    }

    @Test
    public final void should_return_emptySet_for_conformanceClasses() {
        assertThat(instance.getConformanceClasses(SosConstants.SOS, Sos2Constants.SERVICEVERSION), is(not(nullValue())));
        assertThat(instance.getConformanceClasses(SosConstants.SOS, Sos2Constants.SERVICEVERSION).isEmpty(), is(TRUE));
    }

    @Test
    public final void should_add_own_prefix_to_prefixMap() {
        final Map<String, String> prefixMap = Maps.newHashMap();
        instance.addNamespacePrefixToMap(prefixMap);
        assertThat(prefixMap.isEmpty(), is(FALSE));
        assertThat(prefixMap.containsKey(FilterConstants.NS_FES_2), is(TRUE));
        assertThat(prefixMap.containsValue(FilterConstants.NS_FES_2_PREFIX), is(TRUE));
    }

    @Test
    public final void should_not_fail_if_prefixMap_is_null() {
        instance.addNamespacePrefixToMap(null);
    }

    @Test
    public final void should_return_contentType_xml() {
        assertThat(instance.getContentType(), is(MediaTypes.TEXT_XML));
    }

    @Test
    public final void should_return_correct_schema_location() {
        assertThat(instance.getSchemaLocations().size(), is(1));
        final SchemaLocation schemLoc = instance.getSchemaLocations().iterator().next();
        assertThat(schemLoc.getNamespace(), is("http://www.opengis.net/fes/2.0"));
        assertThat(schemLoc.getSchemaFileUrl(), is("http://schemas.opengis.net/filter/2.0/filterAll.xsd"));
    }

    @Test
    public final void should_return_exception_if_received_null() throws OwsExceptionReport, EncodingException {
        assertThrows(UnsupportedEncoderInputException.class, () -> {
            instance.encode(null);
            instance.encode(null, null);
            instance.encode(null, EncodingContext.empty());
        });
        assertThrows(UnsupportedEncoderInputException.class, () -> {
            instance.encode(null, null);
        });
        assertThrows(UnsupportedEncoderInputException.class, () -> {
            instance.encode(null, EncodingContext.empty());
        });

    }

    // @Test
    // deactivated until test fails on build server.
    public final void should_return_BBoxType_for_spatialFilter() throws EncodingException {
        final SpatialFilter filter = new SpatialFilter();
        filter.setOperator(SpatialOperator.BBOX);
        filter.setGeometry(new GeometryFactory().toGeometry(new Envelope(1, 2, 3, 4)));
        filter.setValueReference("valueReference");
        final XmlObject encode = instance.encode(filter);

        assertThat(encode, is(instanceOf(BBOXType.class)));
        final BBOXType xbBBox = (BBOXType) encode;
        assertThat(xbBBox.isSetExpression(), is(TRUE));
    }

}
