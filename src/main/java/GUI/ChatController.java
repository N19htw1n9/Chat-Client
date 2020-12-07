package GUI;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import Chat.ConnectionSocket;
import Chat.ChatData;

public class ChatController extends Controller implements Initializable {
    @FXML
    private Button quitButton;
    @FXML
    private ListView<String> openChatList;
    @FXML
    private ListView<Text> clientsList;
    @FXML
    private TextField chatInput;
    @FXML
    private ImageView sendButton;
    private ChatData.ChatUser toUser = null;

    public void quitButtonAction(Event e) {
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.close();
    }

    public void sendButtonClick(Event e) {
        try {
            connection.send(chatInput.getText(), toUser.id);
            chatInput.setText("");
        } catch (Exception e1) {
            openChatList.getItems().add("Please select a client");
        }
    }

    private void setListViewItemClick(ChatData.ChatUser c) {
        Text t = new Text("Client: " + Integer.toString(c.id));
        t.setOnMouseClicked(e -> {
            toUser = c;
        });
        clientsList.getItems().add(t);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = new ConnectionSocket(chatData -> {
            Platform.runLater(() -> {
                openChatList.getItems().add((String) chatData);
            });
        }, clientsListData -> {
            Platform.runLater(() -> {
                {
                    clientsList.getItems().clear();
                }

                ArrayList<ChatData.ChatUser> clients = (ArrayList<ChatData.ChatUser>) clientsListData;
                for (ChatData.ChatUser client : clients) {
                    setListViewItemClick(client);
                }
            });
        }, "localhost", 5555);
        connection.start();
    }
}
