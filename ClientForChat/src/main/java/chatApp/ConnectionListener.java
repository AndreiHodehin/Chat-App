package chatApp;

public interface ConnectionListener {
    void onConnectionReady(Connection connection);
    void onReceiveString(Connection connection,String message);
    void onDisconnect(Connection connection);
    void onException(Connection connection, Exception e);

}
