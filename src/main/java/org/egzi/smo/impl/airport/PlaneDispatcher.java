package org.egzi.smo.impl.airport;

import org.egzi.smo.api.Worker;
import org.egzi.smo.impl.AbstractDispatcher;

import java.util.concurrent.*;

import static org.egzi.smo.GlobalParameters.*;

public class PlaneDispatcher extends AbstractDispatcher {
    BlockingQueue<Gate> availableGatesQueue;

    private int countOfMiss = 0;

    protected BlockingQueue<Runnable> workerQueue = new ArrayBlockingQueue<Runnable>(MAXIMUM_QUEUE_SIZE);
    protected ScheduledExecutorService timeoutService = Executors.newScheduledThreadPool(COUNT_OF_GATES);

    public PlaneDispatcher() {
        service = new ThreadPoolExecutor(COUNT_OF_GATES, COUNT_OF_GATES, MAXIMUM_TIME_OF_WAITING, TimeUnit.SECONDS, workerQueue,
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        onTimeout();
                    }
                });

        availableGatesQueue = new ArrayBlockingQueue<Gate>(COUNT_OF_GATES);
        //init gates
        for (int i = 0; i < COUNT_OF_GATES; i++) {
            Gate gate = new Gate(GATE_CAPACITY);
            availableGatesQueue.add(gate);
        }

    }

    public void submit(Worker worker) {
        //submit new worker task and schedule timeout checking
        TimeoutTask timeoutTask = new TimeoutTask(this, service.submit(worker));
        timeoutService.schedule(timeoutTask, MAXIMUM_TIME_OF_WAITING, TIME_UNIT);
    }

    public Gate getAvailableGate() {
        if (availableGatesQueue.size() == 0)
            throw new RuntimeException("There's no available gate");
        return availableGatesQueue.poll();
    }

    public void returnGate(Gate gate) {
        availableGatesQueue.add(gate);
    }

    public void onTimeout() {
        countOfMiss++;
    }

    public Integer getMissedPlaneCount() {
        return countOfMiss;
    }

    @Override
    public int getQueueSize() {
        return workerQueue.size();
    }

    /**
     * Task which will cancel plane task by timeout timeout
     */
    private static class TimeoutTask implements Runnable {
        FutureTask<?> future;
        PlaneDispatcher dispatcher;

        public TimeoutTask(PlaneDispatcher dispatcher, Future<?> future) {
            this.dispatcher = dispatcher;
            this.future = (FutureTask<?>)future;
        }

        @Override
        public void run() {
            //is task complete
            if (!future.isDone())
            {
                //cancel if not running
                if (future.cancel(false)) {
                    //delete from waiting queue
                    ((ThreadPoolExecutor)dispatcher.service).getQueue().remove(future);
                    //increment miss count
                    dispatcher.countOfMiss++;
                }
            }
        }
    }
}
