package util.core.socket.server;

import util.core.MyInterfaces;
import util.core.socket.MySocketConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.net.InetAddress;
import java.net.ServerSocket;

public class MyServerSocket {

    private final String ip;
    private final int port;

    private ServerSocket serverSocket;

    private ArrayList<MySocketConnection> socketConnections;

    public MyServerSocket(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public boolean open(){
        if(this.serverSocket != null) return true;

        try{
            this.serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ip));
            socketConnections = new ArrayList<>();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void iterateConnections(MyInterfaces.Action1<MySocketConnection> doThis){
        if(this.serverSocket == null) return;

        for (MySocketConnection connection : socketConnections) doThis.run(connection);
    }

    public boolean closeServer() {
        if(this.serverSocket == null) return true;

        for (MySocketConnection connection : socketConnections) connection.close(true, false);
        try {
            this.serverSocket.close();
            this.serverSocket = null;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void asyncWaitClient(MySocketConnection.MySocketConnectionInterface socketConnectionInterface) {
        new Thread(() -> {
            try {
                socketConnections.add(new MySocketConnection(this.serverSocket.accept(), socketConnectionInterface, connection -> socketConnections.remove(connection)));
            } catch (IOException ignored) {

            }
        }).start();
    }
}
