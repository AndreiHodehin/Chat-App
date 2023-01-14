package chatApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;


public class Server implements ConnectionListener {

    public static void main(String[] args) {
        new Server();
    }

    private final Set<Connection> connections = new HashSet<>();
    private Server() {

        try(ServerSocket serverSocket = new ServerSocket(8890)) {

            while (true) {
                try {
                    new Connection(serverSocket.accept(), this);
                } catch (IOException exception) {
                    System.out.println("Connection exception");
                }

            }
        } catch (Exception e){
            System.err.println("Something wrong with server");
        }
    }

    @Override
    public synchronized void onConnectionReady(Connection connection) {
        connections.add(connection);
    }

    @Override
    public synchronized void onReceiveString(Connection connection, String message) {
        sendToAll(message);
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        connections.remove(connection);
        sendToAll("Client disconnected");
    }

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        System.err.println("Something wrong with connection" + e);
    }

    private void sendToAll(String msg) {
        System.out.println(msg);
        for (Connection conn: connections) {
            conn.sendString(msg);
        }
    }
}
