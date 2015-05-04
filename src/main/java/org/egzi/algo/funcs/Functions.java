package org.egzi.algo.funcs;

/**
 * Created by WinZib on 4/20/2015.
 */
public enum Functions {
    NO("",""),
    ONE("u^2","org.egzi.algo.funcs.SqrFunc"),
    TWO("sin(u)","org.egzi.algo.funcs.CustFunc");

    String clazz;
    String name;

    Functions(String name, String clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public Func func() {
        try {
            return (Func)Class.forName(clazz).newInstance();
        } catch (Exception e) {
            return new SqrFunc();
        }
    }

    public String toString() {
        return name;
    }
}
