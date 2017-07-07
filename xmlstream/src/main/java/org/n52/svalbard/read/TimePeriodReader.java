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
package org.n52.svalbard.read;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.svalbard.decode.exception.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class TimePeriodReader extends XmlReader<TimePeriod> {

    private TimePeriod time;

    @Override
    protected void begin() {
        this.time = new TimePeriod();
    }

    @Override
    protected void read(QName name)
            throws XMLStreamException, DecodingException {
        if (name.equals(GmlConstants.QN_END_POSITION_32)) {
            time.setEnd(DateTimeHelper.parseIsoString2DateTime(chars()));
        } else if (name.equals(GmlConstants.QN_BEGIN_POSITION_32)) {
            time.setStart(DateTimeHelper.parseIsoString2DateTime(chars()));
        } else {
            ignore();
        }
    }

    @Override
    protected TimePeriod finish() {
        return this.time;
    }

}
