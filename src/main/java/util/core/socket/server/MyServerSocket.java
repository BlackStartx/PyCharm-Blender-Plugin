package util.core.socket.server;

import util.core.socket.MySocketConnection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class MyServerSocket {
    private final String ip;
    private final int port;
    private ServerSocket serverSocket;

    public MyServerSocket(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public boolean open() {
        if (this.serverSocket != null) return true;

        try {
            this.serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ip));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void asyncWaitClient(MySocketConnection.MySocketConnectionInterface socketConnectionInterface) {
        new Thread(() -> {
            try {
                new MySocketConnection(this.serverSocket.accept(), socketConnectionInterface);
            } catch (IOException ignored) {
            }
        }).start();
    }
}
