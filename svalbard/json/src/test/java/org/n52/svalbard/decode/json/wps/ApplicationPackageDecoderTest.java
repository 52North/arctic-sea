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
package org.n52.svalbard.decode.json.wps;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.janmayen.Json;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.DataTransmissionMode;
import org.n52.shetland.ogc.wps.JobControlOption;
import org.n52.shetland.ogc.wps.ProcessOffering;
import org.n52.shetland.ogc.wps.ap.ApplicationPackage;
import org.n52.shetland.ogc.wps.ap.DockerExecutionUnit;
import org.n52.shetland.ogc.wps.ap.ExecutionUnit;
import org.n52.svalbard.decode.AbstractDelegatingDecoder;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.DecoderRepository;
import org.n52.svalbard.decode.JsonDecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class ApplicationPackageDecoderTest {

    private Decoder<ApplicationPackage, JsonNode> decoder;

    private <T> Decoder<T, JsonNode> getDecoder(DecoderRepository repository, Class<T> type) {
        return repository.<T, JsonNode>tryGetDecoder(new JsonDecoderKey(type))
                       .orElseThrow(IllegalStateException::new);
    }

    @BeforeEach
    void setUp() {
        DecoderRepository repository = new DecoderRepository();
        List<Decoder<?, ?>> decoders = Arrays.asList(new ApplicationPackageDecoder(),
                                                     new DockerExecutionUnitDecoder(),
                                                     new ExecutionUnitDecoder(),
                                                     new ProcessDescriptionDecoder(),
                                                     new ProcessOfferingDecoder());
        repository.setDecoders(decoders);
        repository.init();
        for (Decoder<?, ?> decoder : decoders) {
            if (decoder instanceof AbstractDelegatingDecoder) {
                ((AbstractDelegatingDecoder<?, ?>) decoder).setDecoderRepository(repository);
            }
        }

        decoder = getDecoder(repository, ApplicationPackage.class);
    }

    @Test
    void test() throws IOException, DecodingException {
        JsonNode node = Json.loadURL(getClass().getResource("ndvi.json"));

        ApplicationPackage ap = decoder.decode(node);

        assertThat(ap, is(not(nullValue())));
        assertThat(ap.getExecutionUnits(), is(not(nullValue())));
        assertThat(ap.getExecutionUnits().size(), is(1));
        ExecutionUnit eu = ap.getExecutionUnits().iterator().next();
        assertThat(eu, is(instanceOf(DockerExecutionUnit.class)));
        DockerExecutionUnit deu = (DockerExecutionUnit) eu;
        assertThat(deu.getImage(), is("docker.52north.org/eopad/snap-sentinel-2-ndvi:latest"));
        assertThat(ap.getImmediateDeployment(), is(true));
        ProcessOffering po = ap.getProcessDescription();
        assertThat(po, is(not(nullValue())));
        assertThat(po.getJobControlOptions(), contains(is(JobControlOption.async())));
        assertThat(po.getOutputTransmissionModes(), contains(is(DataTransmissionMode.REFERENCE)));
        assertThat(po.getProcessVersion().isPresent(), is(true));
        assertThat(po.getProcessVersion().get(), is("1.0.0"));
        assertThat(po.getProcessDescription(), is(not(nullValue())));
        assertThat(po.getProcessDescription().getId(), is(new OwsCode("org.n52.project.tb15.eopad.NdviCalculation")));

    }
}