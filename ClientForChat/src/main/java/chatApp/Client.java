package chatApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;



public class Client extends JFrame implements ActionListener,ConnectionListener {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client();
            }
        });
    }

    String nickName = "";
    private static String IP_ADDR;
    private static final int PORT = 8890;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    static {
        try {
            IP_ADDR = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("Something wrong with ip address");
        }
    }


    private final JTextArea log;
    private final JTextField nickNameField;
    private final JTextField ipAddrField;
    private final JTextField inputField;
    private  Connection connection;

    private Client() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        log = new JTextArea();
        log.setEditable(false);
        log.setLineWrap(true);

        nickNameField = new JTextField("Enter nickname");
        ipAddrField = new JTextField("local");
        inputField = new JTextField();
        ipAddrField.setEditable(false);

        JPanel panel = new JPanel(new GridLayout(1, 0));
        panel.add(nickNameField);
        panel.add(ipAddrField);

        add(panel,BorderLayout.NORTH);
        add(log, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(this);
        nickNameField.addActionListener(this);
        ipAddrField.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(nickNameField.getText().equals("Enter nickname")
                || nickNameField.getText().isEmpty()){
            printMessage("Enter correct nickname");
            inputField.setText(null);
            return;
        }
        if(connection == null) {
            getConnection();
        }

        nickName = nickNameField.getText();

        String message = inputField.getText();
        if(message.isEmpty()) return;
        inputField.setText(null);
        connection.sendString(nickName + ": " + message);

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


    private synchronized  void printMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(message + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    private void getConnection() {
        try {
            connection = new Connection(this,IP_ADDR,PORT);
        } catch (IOException exception) {
            printMessage("Connection go wrong " + exception);
        }
    }
}