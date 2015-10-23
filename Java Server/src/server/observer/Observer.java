package server.observer;

import server.Message;

/**
 * Created by Saji on 10/22/2015.
 */
public abstract class Observer extends Thread {

    protected Subject subject;

    abstract void update(Message message);
}
