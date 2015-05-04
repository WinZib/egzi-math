package org.egzi.smo.impl.airport;


import org.egzi.smo.impl.AbstractDispatcher;

import java.util.concurrent.*;

import static org.egzi.smo.GlobalParameters.*;

public class GuardDispatcher extends AbstractDispatcher{

    protected BlockingQueue<Runnable> workerQueue = new LinkedBlockingDeque<Runnable>();

    //singleton part
    private static GuardDispatcher instance = new GuardDispatcher();

    public static GuardDispatcher getInstance() {
        return instance;
    }

    private ArrayBlockingQueue<Guard> availableGuards = new ArrayBlockingQueue<Guard>(COUNT_OF_GUARDS);

    private GuardDispatcher() {
    }

    public Guard getAvailableGuard() {
        return availableGuards.poll();
    }

    public void returnGuard(Guard guard) {
        availableGuards.add(guard);
    }


    public void init() {
        workerQueue = new LinkedBlockingDeque<Runnable>();
        service = new ThreadPoolExecutor(COUNT_OF_GUARDS, COUNT_OF_GUARDS, 1, TimeUnit.DAYS, workerQueue);
        availableGuards = new ArrayBlockingQueue<Guard>(COUNT_OF_GUARDS);

        System.out.println(COUNT_OF_GUARDS);

        for (int i = 0; i < COUNT_OF_GUARDS; i++) {
            availableGuards.add(new Guard());
        }
    }

    public void shutdown() {
        service.shutdown();
        workerQueue = null;
        availableGuards = null;
    }

    @Override
    public int getQueueSize() {
        return workerQueue.size();
    }
}
