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
