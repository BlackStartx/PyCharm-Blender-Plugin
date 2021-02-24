package util.core.socket;

import util.core.MyByteConversion;
import util.core.MyInterfaces;
import util.core.json_util.values.MyJsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MySocketConnection {

    public final Socket socket;

    private final MySocketConnectionInterface connectionInterface;
    private final OutputStream outputStream;
    private final MyInterfaces.Action1<MySocketConnection> onClose;

    private Thread listeningThread;

    public MySocketConnection(Socket socket, MySocketConnectionInterface connectionInterface, MyInterfaces.Action1<MySocketConnection> onClose) {
        this.socket = socket;
        this.connectionInterface = connectionInterface;
        this.onClose = onClose;
        this.outputStream = GetOutputStream();

        startListeningThread();
    }

    private OutputStream GetOutputStream() {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            return null;
        }
    }

    private static Data readData(InputStream inp) throws IOException {
        byte[] myId = new byte[1];
        int unused = inp.read(myId);
        byte[] myLen = new byte[4];
        int lenLen = inp.read(myLen);
        byte[] myPacket = new byte[MyByteConversion.byteToInt(myLen)];
        int packetLen = inp.read(myPacket);
        return new Data(myId[0], myPacket);
    }

    public void sendJsonData(MyJsonObject<?> myJsonDocument) {
        sendData((byte) 4, myJsonDocument.getJsonString().getBytes());
    }

    public void close(boolean sendData, boolean runOnCloseListener) {
        if (sendData) sendData((byte) 0, new byte[]{0});
        connectionInterface.onEnd(this);
        if(runOnCloseListener) onClose.run(this);
        listeningThread.interrupt();
    }

    public void sendData(byte id, byte[] array) {
        new Thread(() -> {
            byte[] newArray = MyByteConversion.intToByteArray(array.length);
            try {
                outputStream.write(id);
                outputStream.write(newArray);
                outputStream.write(array);
                outputStream.flush();
            } catch (IOException e) {
                // The connection has been shut-down.
            }
        }).start();
    }

    private void startListeningThread() {
        listeningThread = new Thread(() -> {
            try {
                connectionInterface.onConnectionStart(this);
                InputStream inputStream = socket.getInputStream();
                while (!socket.isClosed()) {
                    Data data = readData(inputStream);
                    if (data.getId() == 0) break;
                    connectionInterface.onMessage(this, data);
                }
                close(true, true);
            } catch (IOException e) {
                // The connection has been shut-down.
                close(false, true);
            }
        });
        listeningThread.start();
    }

    public interface MySocketConnectionInterface {
        void onConnectionStart(MySocketConnection socket);

        void onMessage(MySocketConnection socket, Data message);

        void onEnd(MySocketConnection socket);
    }

    public static class Data {
        private final byte id;
        private final byte[] data;

        Data(byte id, byte[] data) {
            this.id = id;
            this.data = data;
        }

        public byte getId() {
            return id;
        }

        public byte[] getData() {
            return data;
        }

        public String getStringData() {
            return MyByteConversion.byteToString(data);
        }
    }
}
