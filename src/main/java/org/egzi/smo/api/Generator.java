package org.egzi.smo.api;

/**
 * Created with IntelliJ IDEA.
 * User: WinZib
 * Date: 5/13/13
 * Time: 11:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Generator {
    Event generate();
    long timeOfNextEvent();
}
