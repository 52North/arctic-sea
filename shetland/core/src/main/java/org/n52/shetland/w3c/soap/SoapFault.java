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
package org.n52.shetland.w3c.soap;

import java.util.Locale;

import javax.xml.namespace.QName;

/**
 * SOAP Fault representation
 *
 * @since 1.0.0
 *
 */
public class SoapFault {

    private QName faultCode;

    private QName faultSubcode;

    private String faultReason;

    private Locale locale;

    private String detailText;

    public void setFaultCode(QName faultCode) {
        this.faultCode = faultCode;
    }

    public void setFaultReason(String faultReason) {
        this.faultReason = faultReason;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setDetailText(String detailText) {
        this.detailText = detailText;
    }

    public QName getFaultCode() {
        return faultCode;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getFaultReason() {
        return faultReason;
    }

    public String getDetailText() {
        return detailText;
    }

    public void setFaultSubcode(QName faultSubcode) {
        this.faultSubcode = faultSubcode;
    }

    /**
     * @return the faultSubcode
     */
    public QName getFaultSubcode() {
        return faultSubcode;
    }

}
