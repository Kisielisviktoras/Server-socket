package server;

/**
 * Created by Saji on 10/21/2015.
 */
public class Message {
    private String message;
    private int source;

    public Message(int source, String message) {
        this.message = message;
        this.source = source;
    }

    public String getMessage() {
        return "Client "+source+ ": "+message;
    }
}
