/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.event.events;

/**
 * Event is fired when the statistics counting outputstream size is enabled
 *
 */
public class CountingOutputStreamEvent extends AbstractFlowEvent {

    private Long bytesWritten;

    public CountingOutputStreamEvent(Long bytesWritten) {
        super(Thread.currentThread().getId());
        this.bytesWritten = bytesWritten;
    }

    public CountingOutputStreamEvent() {
        super(Thread.currentThread().getId());
    }

    public Long getBytesWritten() {
        return bytesWritten;
    }

    public void setBytesWritten(Long bytesWritten) {
        this.bytesWritten = bytesWritten;
    }

}
