package org.egzi.smo;

/**
 * Created with IntelliJ IDEA.
 * User: WinZib
 * Date: 5/19/13
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 */
public enum TimeUnit2 {
    SECONDS("Seconds"),
    MILIS("Miliseconds");

    String value;

    TimeUnit2(String value) {
        this.value = value;
    }

    public long toMillis(long t) {
        if (this == TimeUnit2.SECONDS)
            return t * 1000;
        if (this == TimeUnit2.MILIS)
            return t;
        return t;
    }

    public java.util.concurrent.TimeUnit toConcurrentTimeUnit() {
        if (this == TimeUnit2.SECONDS)
            return java.util.concurrent.TimeUnit.SECONDS;
        if (this == TimeUnit2.MILIS)
            return java.util.concurrent.TimeUnit.MILLISECONDS;
        return java.util.concurrent.TimeUnit.MILLISECONDS;
    }

}
