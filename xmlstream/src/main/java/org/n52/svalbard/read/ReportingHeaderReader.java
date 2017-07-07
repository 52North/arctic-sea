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
import org.n52.shetland.aqd.EReportingChange;
import org.n52.shetland.aqd.EReportingHeader;
import org.n52.svalbard.decode.exception.DecodingException;

public class ReportingHeaderReader extends XmlReader<EReportingHeader> {

    private EReportingHeader header;
    private boolean change;
    private String changeDescription;

    @Override
    protected void begin() {
        this.header = new EReportingHeader();
    }

    @Override
    protected void read(QName name)
            throws XMLStreamException, DecodingException {
        if (name.equals(AqdConstants.QN_AQD_INSPIRE_ID)) {
            this.header.setInspireID(delegate(new InpireIDReader()));
        } else if (name.equals(AqdConstants.QN_AQD_REPORTING_AUTHORITY)) {
            this.header
                    .setReportingAuthority(delegate(new ReportingAuthorityReader()));
        } else if (name.equals(AqdConstants.QN_AQD_REPORTING_PERIOD)) {
            this.header
                    .setReportingPeriod(delegate(new ReportingPeriodReader()));
        } else if (name.equals(AqdConstants.QN_AQD_CONTENT)) {
            this.header.addContent(delegate(new ReferenceableFeatureReader()));
        } else if (name.equals(AqdConstants.QN_AQD_DELETE)) {
            this.header.addDelete(delegate(new ReferenceableFeatureReader()));
        } else if (name.equals(AqdConstants.QN_AQD_CHANGE)) {
            this.change = Boolean.valueOf(chars());
        } else if (name.equals(AqdConstants.QN_AQD_CHANGE_DESCRIPTION)) {
            this.changeDescription = chars();
        } else {
            ignore();
        }
    }

    @Override
    protected EReportingHeader finish()
            throws DecodingException {
        return this.header
                .setChange(new EReportingChange(change, changeDescription));
    }

}
