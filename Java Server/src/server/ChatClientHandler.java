package server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saji on 10/21/2015.
 */
public class ChatClientHandler implements Runnable, Receiver {

    private Socket clientSocket;
    private int clientNumber;
    private PrintWriter out;
    private BufferedReader in;
    private List<String> messages = new ArrayList<String>();
    private boolean alive = true;
    private boolean isRunning = false;

    public ChatClientHandler(Socket client, int clientNumber) {
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
        String inputLine = new String("Client: "+clientNumber+" has joined");
        messages.add(inputLine);
        isRunning = true;
        try {
            String newLine = "";
            while (alive) {
                newLine = in.readLine();
                if (newLine.contains("quit!")) {
                    destroy();
                }
                if (!newLine.equals("null")) {
                    messages.add(newLine);
                }
            }
        } catch (SocketException exception) {
            destroy();
        } catch (IOException e) {
            destroy();
            e.printStackTrace();
        }
    }

    @Override
    public void receive(Message message) {
        out.println(message.getMessage());
    }

    public String getMessage() {
        if (isRunning && !messages.isEmpty()) {
            return messages.remove(0);
        } else {
            return null;
        }
    }

    public void destroy() {
        System.out.println("Chat client "+clientNumber+ " is being destroyed");
        this.alive = false;
    }

    public int getClientNumber() {
        return this.clientNumber;
    }
}
