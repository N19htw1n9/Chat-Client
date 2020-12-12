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
    private ChatData.ChatUser me = null;
    private String username;

    public ConnectionSocket(Consumer<Serializable> chatCallback, Consumer<Serializable> clientsListCallback, String ip,
            int port, String username) {
        this.ip = ip;
        this.port = port;
        this.chatCallback = chatCallback;
        this.clientsListCallback = clientsListCallback;
        this.username = username;
    }

    private String buildToUsersString(HashSet<ChatData.ChatUser> toUsersSet) {
        StringBuilder res = new StringBuilder();
        toUsersSet.stream().forEach(user -> {
            res.append(user.id + ", ");
        });
        return res.toString();
    }

    private String getNameFromUserName(ChatData.ChatUser toUser) {
        return (toUser.name != null) ? String.format("%s (%d)", toUser.name, toUser.id)
                : String.format("Anonymous (%d)", toUser.id);
    }

    @Override
    public void run() {
        try {
            this.socket = new Socket(this.ip, this.port);
            out = new ObjectOutputStream(this.socket.getOutputStream());
            in = new ObjectInputStream(this.socket.getInputStream());
            this.socket.setTcpNoDelay(true);

            chatCallback.accept("Connected to server...");

            sendUserData();

            while (true) {
                ChatData res = (ChatData) in.readObject();

                if (res.me != null) {
                    me = res.me;
                    chatCallback.accept("Hello, " + me.name);
                }
                if (res.clients != null) {
                    clientsListCallback.accept(res.clients);
                }
                if (res.message != null) {
                    String toUsersString = buildToUsersString(res.to);
                    chatCallback.accept(
                            String.format("From: %s;\t To: %s\nMessage: %s", getNameFromUserName(res.from),
                                    toUsersString, res.message));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            chatCallback.accept(String.format("Error, could not connect to server %s port %d", this.ip, this.port));
        }
    }

    public void send(String message, HashSet<ChatData.ChatUser> toUsersSet) throws IOException, ClassNotFoundException {
        ChatData req = new ChatData();
        req.message = message;
        req.to = toUsersSet;
        out.writeObject(req);
    }

    public void sendUserData() throws IOException {
        ChatData req = new ChatData();

        ChatData.ChatUser user = new ChatData.ChatUser();
        user.name = username;

        req.me = user;
        try {
            out.writeObject(req);
        } catch (Exception e) {
            System.out.println("Lol. Got eem!!");
        }
    }
}
