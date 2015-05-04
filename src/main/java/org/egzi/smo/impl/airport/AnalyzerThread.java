package org.egzi.smo.impl.airport;

import org.egzi.ui.MainWindow;
import org.egzi.ui.PlotContainer;
import org.egzi.ui.PlotContainerImpl;
import static org.egzi.smo.GlobalParameters.*;

/**
 * Thread which provide an analise of getting data
 */
public class AnalyzerThread implements Runnable {
    /**
     * Plane Dispatcher for current task
     */
    private PlaneDispatcher dispatcher;
    /**
     * Count of passed clocks
     */
    private long countOfClocks;

    private int timeFromPrevMiss;
    private int prevMissCount;

    //plots initialication
    protected static PlotContainer QueueToGuardSize = new PlotContainerImpl("Size of Average Queue to Quard", "Time", "Average Queue Size");
    protected static PlotContainer PlaneQueue = new PlotContainerImpl("Size of Average Queue of Plane", "Time", "Average Queue Size");
    protected static PlotContainer CountOfMissedPlane  = new PlotContainerImpl("Count of Missed Planes", "Time", "Missed planes");

    private int prevAveragePlaneQueueSize = 0;
    private int prevAveragePassengerQueueSize = 0;

    /**
     * Initialization of thread
     * @param dispatcher current {@link PlaneDispatcher} which provide management of plane
     */
    public AnalyzerThread(PlaneDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public static void init(MainWindow window) {
        window.addPlot(QueueToGuardSize);
        window.addPlot(PlaneQueue);
        window.addPlot(CountOfMissedPlane);
    }

    @Override
    public void run() {
        while (true) {

            try {
                //wait for next TimeUnit
                Thread.sleep(TIME_UNIT.toMillis(1L));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            //update plots
            countOfClocks++;

            if (prevMissCount != dispatcher.getMissedPlaneCount()) {
                prevMissCount = dispatcher.getMissedPlaneCount();
                timeFromPrevMiss = 0;
            } else {
                timeFromPrevMiss++;
            }
            CountOfMissedPlane.add((double)countOfClocks, (double)timeFromPrevMiss);


            prevAveragePassengerQueueSize = (prevAveragePassengerQueueSize +  GuardDispatcher.getInstance().getQueueSize()) / 2;
            prevAveragePlaneQueueSize = (prevAveragePlaneQueueSize + dispatcher.getQueueSize()) / 2;

            QueueToGuardSize.add((double) countOfClocks, (double)prevAveragePassengerQueueSize);
            PlaneQueue.add((double)countOfClocks, (double)prevAveragePlaneQueueSize);
            }
    }
}
