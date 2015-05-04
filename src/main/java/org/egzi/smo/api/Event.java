package org.egzi.smo.api;

public interface Event {
    /**
     * Each event should be prioritized
     * 0 - default priority
     * 1,2,3... in other case
     * @return integer value - priority of current event
     */
    Integer getPriority();

}
