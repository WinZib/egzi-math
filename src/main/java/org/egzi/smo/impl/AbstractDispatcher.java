package org.egzi.smo.impl;

import org.egzi.smo.api.Dispatcher;
import org.egzi.smo.api.Worker;

import java.util.concurrent.*;

public abstract class AbstractDispatcher implements Dispatcher {
    public ExecutorService service;

    public void submit(Worker worker) {
        service.execute(worker);
    }

    public void onTimeout() {
    }

    public void shutdown() {
        service.shutdown();
    }

    public abstract int getQueueSize();
}
