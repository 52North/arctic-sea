/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.statistics.api.parameters;

public class Description {

    private final InformationOrigin informationOrigin;
    private final Operation operation;
    private String desc;

    public Description(InformationOrigin informationOrigin, Operation operation) {
        this.informationOrigin = informationOrigin;
        this.operation = operation;
    }

    public Description(InformationOrigin informationOrigin, Operation operation, String desc) {
        this.informationOrigin = informationOrigin;
        this.operation = operation;
        this.desc = desc;
    }

    public InformationOrigin getInformationOrigin() {
        return informationOrigin;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Description [informationOrigin=" + informationOrigin + ", operation=" + operation + ", desc=" + desc +
               "]";
    }

    public enum InformationOrigin {
        RequestEvent,
        ResponseEvent,
        CountingStreamEvent,
        OutgoingResponseEvent,
        ExceptionEvent,
        Computed,
        None;
    }

    public enum Operation {
        None,
        Default,
        Metadata,
        GetCapabilities,
        GetObservation,
        GetObservationById,
        DescribeSensor,
        InsertObservation,
        GetResult,
        GetFeatureOfInterest,
        DeleteSensor,
        GetDataAvailability,
        GetResultTemplate,
        InsertResult,
        InsertResultTemplate,
        InsertSensor,
        UpdateSensor;
    }

}
