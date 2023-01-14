package com.webClient.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection {

    private final Socket socket;
    private final Thread thread;
    private final ConnectionListener listener;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public Connection(ConnectionListener listener, String ipAddress, int port) throws IOException {
        this(new Socket(ipAddress,port),listener);
    }

    public Connection(Socket socket, final ConnectionListener listener) throws IOException {

        this.socket = socket;
        this.listener = listener;

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8));

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    listener.onConnectionReady(Connection.this);
                    while (!thread.isInterrupted()) {
                        listener.onReceiveString(Connection.this, reader.readLine());
                    }
                } catch (IOException e) {
                    listener.onException(Connection.this, e);
                } finally {
                    listener.onDisconnect(Connection.this);
                }
            }
        });
        thread.start();

    }

    public synchronized void sendString(String msg) {
        try {
            writer.write(msg + "\n");
            writer.flush();
        } catch (IOException e) {
            listener.onException(Connection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        thread.interrupt();
        try {
        socket.close();
        } catch (IOException e) {
            listener.onException(Connection.this, e);
        }
    }

    @Override
    public String toString() {
     return socket.getInetAddress().getHostAddress() ;
    }
}
