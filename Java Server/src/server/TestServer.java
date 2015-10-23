package server;

import server.observer.QueHandler;

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

        QueHandler queHandler = null;
        try {
            queHandler = new QueHandler(serverSocket);
            queHandler.start();
            queHandler.join();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("Program end");
            queHandler.terminate();
            serverSocket.close();
        }
    }
}
