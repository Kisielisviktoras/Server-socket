package server.observer;

import server.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saji on 10/22/2015.
 */
public class ClientConnectionHolder extends Observer {

    private Socket clientSocket;
    private int clientNumber;
    private PrintWriter out;
    private BufferedReader in;
    private boolean alive = true;

    public ClientConnectionHolder(Socket client, int clientNumber, Subject subject) {
        this.subject = subject;
        this.clientSocket = client;
        this.clientNumber = clientNumber;
        try {
            this.out = new PrintWriter(client.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            System.out.println("ERROR While getting I/O buffers from a socket");
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            String newLine = "";
            while (alive) {
                newLine = in.readLine();
                if (newLine.contains("quit!")) {
                    terminate();
                }
                if (!newLine.equals("null")) {
                    subject.notifyObservers(new Message(clientNumber, newLine, this));
                }
            }
        } catch (SocketException exception) {
        } catch (IOException e) {
        } finally {
            terminate();
        }
    }

    public void terminate() {
        System.out.println("Chat client "+clientNumber+ " is being destroyed");
        this.alive = false;
    }

    public int getClientNumber() {
        return this.clientNumber;
    }

    @Override
    protected void update(Message message) {
        out.println(message.getMessage());
    }
}
