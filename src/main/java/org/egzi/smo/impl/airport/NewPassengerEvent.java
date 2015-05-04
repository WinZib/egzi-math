package org.egzi.smo.impl.airport;

import org.egzi.math.RandomUtil;
import org.egzi.smo.impl.AbstractEvent;
import static org.egzi.smo.GlobalParameters.*;

public class NewPassengerEvent extends AbstractEvent {
    protected boolean isTransit;
    protected boolean isCitizen;

    public static int countOfForeigner = 0;
    public static int countOfTransit = 0;

    public NewPassengerEvent() {
        isTransit = RandomUtil.getUniformDistributed(0, 100) <= PROBABILITY_OF_TRANSIT_PASSENGER;
        isCitizen = RandomUtil.getUniformDistributed(0, 100) <= PROBABILITY_OF_CITIZEN_PASSENGER;
        if (!isCitizen) countOfForeigner++;
        if (isTransit) countOfTransit++;

    }

    public NewPassengerEvent(boolean isTransit,
                             boolean isCitizen) {
        this.isCitizen = isCitizen;
        this.isTransit = isTransit;
    }

    public boolean isTransit() {
        return isTransit;
    }

    public void setTransit(boolean transit) {
        isTransit = transit;
    }

    public boolean isCitizen() {
        return isCitizen;
    }

    public void setCitizen(boolean citizen) {
        isCitizen = citizen;
    }
}
