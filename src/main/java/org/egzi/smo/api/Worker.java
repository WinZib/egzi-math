package org.egzi.smo.api;

import java.util.Queue;

public interface Worker extends Runnable {
    public  void onStart();
    public  void onFinish();
    public long getTimeOfProcessing();
    public void setTimeOfProcessing(long timeOfProcessing);
}
