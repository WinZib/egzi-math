package org.egzi.smo.impl;

import static org.egzi.smo.GlobalParameters.*;
import org.egzi.smo.api.Worker;

import java.text.MessageFormat;

public abstract class AbstractWorker implements Worker, Runnable {
    protected long timeOfProcessing;

    public abstract void onStart();
    public abstract void onFinish();

    public long getTimeOfProcessing() {
        return timeOfProcessing;
    }

    public void setTimeOfProcessing(long timeOfProcessing) {
        this.timeOfProcessing = timeOfProcessing;
    }
    @Override
    public void run() {
        onStart();
        try{
            runImpl();
        } catch (InterruptedException e){
            System.out.println(MessageFormat.format("Worker(:1) was interrupted", toString()));
        } finally {
            onFinish();
        }
    }

    public void runImpl() throws InterruptedException{
        Thread.sleep(TIME_UNIT.toMillis(getTimeOfProcessing()));
    }
}
