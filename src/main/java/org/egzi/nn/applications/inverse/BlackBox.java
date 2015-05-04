package org.egzi.nn.applications.inverse;

import java.util.ArrayList;
import java.util.List;

// y''(t) + w^2 y(t) = f(t) ->
// [y(t-dt) - 2y(t) + y(t+dt)]/dt^2 + w^2 y(t) = f(t)
public class BlackBox {
    List<Double> ys = new ArrayList<Double>();
    List<Double> forces = new ArrayList<Double>();
    Double w, y0, y1, dt;

    public BlackBox(Double w, Double y0, Double y1, Double dt) {
            ys = new ArrayList<Double>();
            forces = new ArrayList<Double>();
            ys.add(y1);
            ys.add(y0);
            this.y0 = y0;
            this.y1 = y1;
            this.dt = dt;
            this.w = w;
    }

    public void reset() {
        ys = new ArrayList<Double>();
        forces = new ArrayList<Double>();
        ys.add(y1);
        ys.add(y0);
    }

    public Double yNext(Double f) {
        //substractional schema with saving
        Double y = (f - w*w* yCurrent())*dt*dt + 2 * yCurrent() - yPrevious();
        ys.add(y);
        forces.add(f);
        return y;
    }

    public Double tryY(Double f) {
        //substractional schema without saving
        return f * dt * dt- w*w* yCurrent()*dt*dt + 2* yCurrent() - yPrevious();
    }

    /**
     * Calculate force by substraction schema
     * @param y current input value
     * @return correctness force
     */
    public Double bestF(Double y) {
        return (yPrevious() - 2* yCurrent() + y)/dt/dt + w*w* yCurrent();
    }

    public int ny() {
        return ys.size();
    }
    public Double yPrevious() {
        return ys.get(ny()-2);
    }

    public Double yCurrent(){
        return ys.get(ys.size() - 1);
    }

    public int nf() {
        return forces.size();
    }

}
