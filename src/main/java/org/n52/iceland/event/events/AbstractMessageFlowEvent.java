package org.n52.iceland.event.events;

import org.n52.iceland.event.ServiceEvent;

public class AbstractMessageFlowEvent implements ServiceEvent {
    private final Long messageGroupId;

    public AbstractMessageFlowEvent(Long messageGroupId) {
        this.messageGroupId = messageGroupId;
    }

    public Long getMessageGroupId() {
        return messageGroupId;
    }

}
