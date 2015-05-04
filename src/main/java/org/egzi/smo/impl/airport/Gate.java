package org.egzi.smo.impl.airport;

/**
 * Instance of gate which have one main parameter
 * capacity - count of passenger that can be passed through gate per time unit
 */
public class Gate {
    /**
     * count of passenger that can be passed through gate per time unit
     */
    private int maxCapacity;
    /**
     * Simple Gate name - "Gate - <NUMBER>"
     */
    private String name;


    private static int val = 0;
    private static int nextVal() {return ++val;}

    /**
     * Simple constructor which set maxCapacity value
     * @param maxCapacity count of passenger that can be passed through gate per time unit
     */
    public Gate(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.name = "Gate - " + nextVal();
    }

    /**
     * @return count of passenger that can be passed through gate per time unit
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Set maxCapacity value
     * @param maxCapacity new count of passenger that can be passed through gate per time unit
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * return generated name
     * @return gate name
     */
    public String getName() {
        return name;
    }

    /**
     * set new gate name
     * @param name new gate name
     */
    public void setName(String name) {
        this.name = name;
    }
}
