package Chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.lang.Thread;
import java.util.HashSet;
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

    private String buildToUsersString(HashSet<ChatData.ChatUser> toUsersSet) {
        StringBuilder res = new StringBuilder();
        toUsersSet.stream().forEach(user -> {
            res.append(user.id + ", ");
        });
        return res.toString();
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
                    String toUsersString = buildToUsersString(res.to);
                    chatCallback.accept(
                            String.format("From: %d;\t To: %s\nMessage: %s", res.from.id, toUsersString, res.message));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            chatCallback.accept(String.format("Error, could not connect to server %s port %d", this.ip, this.port));
        }
    }

    public void send(String message, HashSet<ChatData.ChatUser> toUsersSet) throws IOException, ClassNotFoundException {
        toUsersSet.stream().forEach(user -> {
            System.out.print(user.id + " ");
        });
        System.out.println("");

        ChatData req = new ChatData();
        req.message = message;
        req.to = toUsersSet;
        out.writeObject(req);
    }
}
