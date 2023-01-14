package com.webClient.client;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;

@Service
public class Client implements  ConnectionListener {
    private String conversation = "";
    String nickName = "";
    private static String IP_ADDR;
    private static final int PORT = 8890;
    private  Connection connection;

    static {
        try {
            IP_ADDR = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("Something wrong with ip address");
        }
    }

    public Client() {
    }

    @Override
    public void onConnectionReady(Connection connection) {
        connection.sendString("Connected: " + nickName);

    }

    @Override
    public void onReceiveString(Connection connection, String message) {
        printMessage(message);
    }

    @Override
    public void onDisconnect(Connection connection) {
        printMessage("Connection close...");
    }

    @Override
    public void onException(Connection connection, Exception e) {
        printMessage("Connection go wrong " + e);
    }

    public synchronized void sendMessage(String message) {
        connection.sendString(nickName + ": " + message);
    }
    private synchronized  String printMessage(String message) {
        conversation = conversation.concat(message).concat("\r\n");
        return conversation;
    }

    public Connection getConnection() {
        try {
             connection = new Connection(this, IP_ADDR, PORT);
             return connection;
        } catch (IOException exception) {
            printMessage("Connection go wrong " + exception);
            return null;
        }
    }

    public boolean isConnected() {
        return connection != null;
    }
    @Override
    public String toString() {
        return conversation;
    }
    public void setNickName(String nick) {
        this.nickName = nick;
    }
    public String getNickName() {
        return nickName;
    }
}