package org.egzi.smo.impl.airport;

import org.egzi.smo.impl.AbstractEvent;


public class NewPlaneEvent extends AbstractEvent {

    static int id = 0;

    public int curId = id++;

    private long countOfPassengers;

    public NewPlaneEvent(long countOfPassengers) {
        this.countOfPassengers = countOfPassengers;
    }

    public long getCountOfPassengers() {
        return countOfPassengers;
    }

    public String toString() {
        return "{plane:{count:"+countOfPassengers+",type=" + curId + "}}";
    }
}
