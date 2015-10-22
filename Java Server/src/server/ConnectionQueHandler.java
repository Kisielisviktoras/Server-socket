package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Saji on 10/21/2015.
 */
public class ConnectionQueHandler implements Runnable {

    private static List<ChatClientHandler> pendingClientList = new ArrayList<ChatClientHandler>();
    private static List<ChatClientHandler> connectedClients = new ArrayList<ChatClientHandler>();
    private final ServerSocket serverSocket;

    private int clientNumber;
    private boolean alive = true;

    public ConnectionQueHandler(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        System.out.println("Waiting for new clients");

        while (alive) {
            try {
                if (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    ChatClientHandler handler = new ChatClientHandler(clientSocket, ++clientNumber);
                    Thread handlerThread = new Thread(handler);
                    handlerThread.join();
                    handlerThread.start();
                    pendingClientList.add(handler);
                } else {
                    destroy();
                }
            } catch (SocketException ex) {
                ex.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                destroy();
            }
        }

    }



    public void destroy() {
        for (ChatClientHandler chatClientHandler : pendingClientList) {
            chatClientHandler.receive(new Message(chatClientHandler.getClientNumber(), "Que handler is being destroyed. Disconnected"));
            chatClientHandler.destroy();
        }
        System.out.println("Que handler is being destroyed");
        this.alive = false;
    }

    public void connectPendingClients() {
        Iterator<ChatClientHandler> iterator = pendingClientList.iterator();
        while(iterator.hasNext()) {
            ChatClientHandler client = iterator.next();
            connectedClients.add(client);
            iterator.remove();
        }
    }

    public List<ChatClientHandler> getConnectedClients() {
        return connectedClients;
    }
}
