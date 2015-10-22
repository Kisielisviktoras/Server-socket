package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by Saji on 10/10/2015.
 */
public class TestServer {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        OutputStream console = System.out;
        boolean alive = true;
        int clientCount = 0;

        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }
        ConnectionQueHandler queHandler = null;
        Thread queThread = null;
        try {
            queHandler = new ConnectionQueHandler(serverSocket);
            queThread = new Thread(queHandler);
            queThread.join();
            queThread.start();

            while (alive) {
                List<ChatClientHandler> chatClients = queHandler.getConnectedClients();
                String message = null;
                for (ChatClientHandler chatClientHandler : chatClients) {
                    message = chatClientHandler.getMessage();
                    if (message != null && message.length() != 0) {
                        if ("quit!".equals(message)) {
                            alive = false;
                            queThread.interrupt();
                        }
                        for (ChatClientHandler clientHandler : chatClients) {
                            if (!chatClientHandler.equals(clientHandler)) {
                                clientHandler.receive(new Message(chatClientHandler.getClientNumber(), message));
                            }
                        }
                        System.out.println("Char client "+chatClientHandler.getClientNumber()+" sent a message: "+message);
                    }
                }
                queHandler.connectPendingClients();
            }
        } catch (Exception ex) {
            System.out.println("Program end");
            ex.printStackTrace();
        } finally {
            queHandler.destroy();
            serverSocket.close();
        }
    }
}
