package org.egzi.smo.impl.airport;

import org.egzi.math.RandomUtil;
import org.egzi.smo.api.Worker;
import org.egzi.smo.impl.AbstractGenerator;

import static org.egzi.smo.GlobalParameters.HIGH_COUNT_OF_PASSENGERS;
import static org.egzi.smo.GlobalParameters.LOW_COUNT_OF_PASSENGERS;

public class UnscheduledPlaneGenerator extends AbstractGenerator {

    /**
     * Simple constructor. Link new Generator instance with PlaneDispatcher
     * @param dispatcher {@link PlaneDispatcher} instance which will consume generated objects
     */
    public UnscheduledPlaneGenerator(PlaneDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public NewPlaneEvent generate() {
        return new NewPlaneEvent(
                RandomUtil.getUniformDistributed(LOW_COUNT_OF_PASSENGERS,
                        HIGH_COUNT_OF_PASSENGERS));
    }

    @Override
    public Worker createWorker() {
        return new GateWorker((PlaneDispatcher)dispatcher, generate());
    }

    @Override
    public long timeOfNextEvent() {
        return RandomUtil.getNormalDistributed(20, 2);
    }

    @Override
    protected AbstractGenerator newInstance() {
        return new UnscheduledPlaneGenerator((PlaneDispatcher)dispatcher);
    }
}
