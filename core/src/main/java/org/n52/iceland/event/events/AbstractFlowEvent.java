/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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

import org.n52.iceland.event.ServiceEvent;

public class AbstractFlowEvent implements ServiceEvent {
    private final Long eventsGroupId;

    public AbstractFlowEvent(Long eventsGroupId) {
        this.eventsGroupId = eventsGroupId;
    }

    public Long getMessageGroupId() {
        return eventsGroupId;
    }

}
