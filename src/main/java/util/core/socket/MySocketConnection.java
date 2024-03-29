package util.core.socket;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class MySocketConnection {
    public final Socket socket;

    private final MySocketConnectionInterface connectionInterface;
    private final OutputStream outputStream;
    private Thread listeningThread;

    public MySocketConnection(Socket socket, MySocketConnectionInterface connectionInterface) {
        this.socket = socket;
        this.connectionInterface = connectionInterface;
        this.outputStream = getOutputStream();

        startListeningThread();
    }

    private static Data readData(InputStream inp) throws IOException {
        byte[] id = new byte[1];
        if (!read(inp, id)) return null;
        byte[] len = new byte[4];
        if (!read(inp, len)) return null;
        byte[] packet = new byte[ByteBuffer.wrap(len).getInt()];
        return read(inp, packet) ? new Data(id[0], packet) : null;
    }

    private static boolean read(InputStream inp, byte[] array) throws IOException {
        return array.length == inp.read(array);
    }

    private OutputStream getOutputStream() {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public void sendJsonData(JSONObject jsonObject) {
        sendData((byte) 4, jsonObject.toString().getBytes());
    }

    public void close(boolean sendData) {
        if (sendData) sendData((byte) 0, new byte[]{0});
        connectionInterface.onEnd(this);
        listeningThread.interrupt();
    }

    public void sendData(byte id, byte[] array) {
        new Thread(() -> {
            byte[] newArray = Data.intToByteArray(array.length);
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
                    if (data == null) continue;
                    if (data.getId() == 0) break;
                    connectionInterface.onMessage(this, data);
                }
                close(true);
            } catch (IOException e) {
                close(false);
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

        public static byte[] intToByteArray(int a) {
            return new byte[]{(byte) ((a >> 24) & 0xFF), (byte) ((a >> 16) & 0xFF), (byte) ((a >> 8) & 0xFF), (byte) (a & 0xFF)};
        }

        public byte getId() {
            return id;
        }

        public byte[] getData() {
            return data;
        }

        public String getStringData() {
            return new String(data);
        }
    }
}
