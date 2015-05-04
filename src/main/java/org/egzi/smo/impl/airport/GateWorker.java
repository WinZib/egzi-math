package org.egzi.smo.impl.airport;

import org.egzi.smo.impl.AbstractWorker;

import static org.egzi.smo.GlobalParameters.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GateWorker extends AbstractWorker {
    PlaneDispatcher dispatcher;
    NewPlaneEvent event;
    public static AtomicInteger worker = new AtomicInteger(0);
    Gate gate;

    public GateWorker(PlaneDispatcher dispatcher, NewPlaneEvent event) {
        this.dispatcher = dispatcher;
        this.event = event;
    }

    @Override
    public void onStart() {
        worker.incrementAndGet();
        gate = dispatcher.getAvailableGate();
        setTimeOfProcessing(event.getCountOfPassengers() / gate.getMaxCapacity());
    }

    @Override
    public void onFinish() {
        worker.decrementAndGet();
        dispatcher.returnGate(gate);
    }

    public void runImpl() throws InterruptedException{
        System.out.println(event);

        long passengers = event.getCountOfPassengers();
        while (passengers > 0) {

            for (int i = 0; i < Math.min(gate.getMaxCapacity(), passengers); i++) {
                GuardDispatcher.getInstance().submit(new GuardWorker(new NewPassengerEvent()));
            }

            //sleep for a second
            Thread.sleep(TIME_UNIT.toMillis(1L));
        }
    }
}
