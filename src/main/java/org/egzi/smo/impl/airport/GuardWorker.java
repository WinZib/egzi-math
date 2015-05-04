package org.egzi.smo.impl.airport;

import org.egzi.smo.impl.AbstractWorker;
import static org.egzi.smo.GlobalParameters.*;

public class GuardWorker extends AbstractWorker {
    Guard guard;

    public GuardWorker(NewPassengerEvent event) {
        //if transit passenger
        if (event.isTransit()) setTimeOfProcessing(0);
        //if citizen
        if (event.isCitizen()) setTimeOfProcessing(TIME_OF_PROCESSING_CITIZENS);
        //if foreigner
        else setTimeOfProcessing(TIME_OF_PROCESSING_FOREIGNER);
    }

    @Override
    public void onStart() {
        guard = GuardDispatcher.getInstance().getAvailableGuard();
    }

    @Override
    public void onFinish() {
        GuardDispatcher.getInstance().returnGuard(guard);
    }
}
