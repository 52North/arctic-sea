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

import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.inspire.InspireID;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.w3c.Nillable;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class InpireIDReader extends XmlReader<InspireID> {

    private InspireID inspireID;

    @Override
    protected void read(QName name)
            throws XMLStreamException, OwsExceptionReport {
        if (name.equals(AqdConstants.QN_BASE_IDENTIFIER)) {
        } else if (name.equals(AqdConstants.QN_BASE_LOCAL_ID)) {
            this.inspireID.setLocalId(chars());
        } else if (name.equals(AqdConstants.QN_BASE_NAMESPACE)) {
            this.inspireID.setNamespace(chars());
        } else if (name.equals(AqdConstants.QN_BASE_VERSION_ID)) {
            this.inspireID.setVersionId(delegate(new NillableStringReader()));
        } else {
            ignore();
        }
    }

    @Override
    protected void begin() {
        this.inspireID = new InspireID().setVersionId(Nillable.<String>absent());
    }

    @Override
    protected InspireID finish()
            throws OwsExceptionReport {
        return this.inspireID;
    }

}
