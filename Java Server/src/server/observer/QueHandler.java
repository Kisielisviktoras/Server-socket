package server.observer;

import server.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Saji on 10/21/2015.
 */
public class QueHandler extends Subject {

    private final ServerSocket serverSocket;

    private int clientNumber;
    private boolean alive = true;

    public QueHandler(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        System.out.println("Waiting for new clients");
        String inputLine = new String("Client: %s has joined");
        while (alive) {
            try {
                if (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    ClientConnectionHolder observerableSubject = new ClientConnectionHolder(clientSocket, ++clientNumber, this);
                    observerableSubject.start();

                    String joinNotification = String.format(inputLine, clientNumber);
                    registerObserver(observerableSubject, new Message(0, joinNotification, null));
                } else {
                    terminate();
                }
            } catch (SocketException ex) {
                ex.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void terminate() {
        notifyObservers(new Message(0, "Connection terminated", null));
        System.out.println("Que handler is being destroyed");
        this.alive = false;
    }
}
