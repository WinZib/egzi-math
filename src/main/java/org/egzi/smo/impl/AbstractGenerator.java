package org.egzi.smo.impl;

import org.egzi.smo.api.Dispatcher;
import org.egzi.smo.api.Generator;
import org.egzi.smo.api.Worker;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.egzi.smo.GlobalParameters.*;

public abstract class AbstractGenerator extends Thread implements Generator {
    protected Dispatcher dispatcher;


    public void run() {
        try{
            while (true) {
                dispatcher.submit(createWorker());
                Thread.sleep(TIME_UNIT.toMillis(timeOfNextEvent()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract Worker createWorker();

    protected abstract AbstractGenerator newInstance();
}
