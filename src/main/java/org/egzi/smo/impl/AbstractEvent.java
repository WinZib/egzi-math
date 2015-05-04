package org.egzi.smo.impl;

import org.egzi.smo.api.Event;

public abstract class AbstractEvent implements Event, Comparable<AbstractEvent> {
    protected Integer priority = 0;

    @Override
    public Integer getPriority() {
        return priority;
    }

    @Override
    public int compareTo(AbstractEvent o) {

        if (o != null)
            return priority.compareTo(o.getPriority());
        else return 1;
    }
}
