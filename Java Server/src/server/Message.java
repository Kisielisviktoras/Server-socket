package server;

import server.observer.Observer;

/**
 * Created by Saji on 10/21/2015.
 */
public class Message {
    private String message;
    private int source;
    private Observer owner;

    public Message(int source, String message, Observer owner) {
        this.message = message;
        this.source = source;
        this.owner = owner;
    }

    public String getMessage() {
        return "Client "+source+ ": "+message;
    }

    public Observer getOwner() {
        return owner;
    }
}
