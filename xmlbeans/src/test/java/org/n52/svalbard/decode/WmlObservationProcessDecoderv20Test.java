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
package org.n52.svalbard.decode;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.values.ReferenceValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.sensorML.SensorMLConstants;
import org.n52.shetland.ogc.om.series.wml.ObservationProcess;
import org.n52.svalbard.decode.WmlObservationProcessDecoderv20;

/**
 * Test class for {@link WmlObservationProcessDecoderv20}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class WmlObservationProcessDecoderv20Test {

    WmlObservationProcessDecoderv20 decoder = new WmlObservationProcessDecoderv20();

    final String REF_OFFERING = "refOffering";

    final String TEXT_OFFERING = "textOffering";

    private ObservationProcess getObservationProcess() {
        ObservationProcess op = new ObservationProcess();
        op.addParameter(getReferencedNamedValue());
        op.addParameter(getTextNamedValue());
        return op;
    }

    private NamedValue<ReferenceType> getReferencedNamedValue() {
        NamedValue<ReferenceType> nv = new NamedValue<ReferenceType>();
        nv.setName(getNameValueOfferingName());
        nv.setValue(new ReferenceValue(new ReferenceType(REF_OFFERING)));
        return nv;
    }

    private NamedValue<String> getTextNamedValue() {
        NamedValue<String> nv = new NamedValue<String>();
        nv.setName(getNameValueOfferingName());
        nv.setValue(new TextValue(TEXT_OFFERING));
        return nv;
    }

    private ReferenceType getNameValueOfferingName() {
        return new ReferenceType(SensorMLConstants.ELEMENT_NAME_OFFERINGS);
    }
}
