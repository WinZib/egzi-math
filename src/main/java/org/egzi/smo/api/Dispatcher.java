package org.egzi.smo.api;

import java.util.Queue;
import java.util.concurrent.locks.Condition;

public interface Dispatcher {
    void submit(Worker worker);
    void onTimeout();
    int getQueueSize();
}
