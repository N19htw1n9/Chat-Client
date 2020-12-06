package Chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.lang.Thread;
import java.util.List;
import java.util.function.Consumer;

public class ConnectionSocket extends Thread {
    private Socket socket;
    private String ip;
    private int port;
    private Consumer<Serializable> chatCallback, clientsListCallback;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ConnectionSocket(Consumer<Serializable> chatCallback, Consumer<Serializable> clientsListCallback, String ip,
            int port) {
        this.ip = ip;
        this.port = port;
        this.chatCallback = chatCallback;
        this.clientsListCallback = clientsListCallback;
    }

    @Override
    public void run() {
        try {
            this.socket = new Socket(this.ip, this.port);
            out = new ObjectOutputStream(this.socket.getOutputStream());
            in = new ObjectInputStream(this.socket.getInputStream());
            this.socket.setTcpNoDelay(true);

            chatCallback.accept("Connected to server...");

            while (true) {
                ChatData res = (ChatData) in.readObject();

                if (res.clients != null) {
                    clientsListCallback.accept(res.clients);
                }
                if (res.message != null) {
                    chatCallback.accept(
                            String.format("From: %d;\t To: %d\nMessage: %s", res.from.id, res.to.id, res.message));
                }
            }
        } catch (Exception e) {
            chatCallback.accept(String.format("Error, could not connect to server %s port %d", this.ip, this.port));
        }
    }

    private ChatData.ChatUser buildUser(int id, String name) {
        ChatData.ChatUser user = new ChatData.ChatUser();
        user.id = id;
        user.name = name;
        return user;
    }

    public void send(String message, int to) throws IOException, ClassNotFoundException {
        ChatData req = new ChatData();
        req.message = message;
        req.to = buildUser(to, null);
        out.writeObject(req);
    }
}
