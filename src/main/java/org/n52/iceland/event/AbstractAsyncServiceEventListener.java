package org.n52.iceland.event;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.n52.iceland.util.GroupedAndNamedThreadFactory;

public abstract class AbstractAsyncServiceEventListener implements
        ServiceEventListener {
    private final Executor executor;

    public AbstractAsyncServiceEventListener(int threadPoolSize) {
        this.executor = Executors
                .newFixedThreadPool(threadPoolSize,
                                    new GroupedAndNamedThreadFactory(getClass()
                                            .getName() + "-worker"));
    }

    public AbstractAsyncServiceEventListener() {
        this(3);
    }

    @Override
    public void handle(ServiceEvent event) {
        this.executor.execute(create(event));
    }

    protected abstract Runnable create(ServiceEvent event);
}
