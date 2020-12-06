package GUI;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import Chat.ConnectionSocket;
import Chat.ChatData;

public class ChatController extends Controller implements Initializable {
    @FXML
    private Button quitButton;
    @FXML
    private ListView<String> openChatList;
    @FXML
    private ListView<String> clientsList;
    @FXML
    private TextField chatInput;
    @FXML
    private ImageView sendButton;

    public void quitButtonAction(Event e) {
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.close();
    }

    public void sendButtonClick(Event e) {
        try {
            connection.send(chatInput.getText(), 2);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = new ConnectionSocket(chatData -> {
            Platform.runLater(() -> {
                openChatList.getItems().add((String) chatData);
            });
        }, clientsListData -> {
            Platform.runLater(() -> {
                List<ChatData.ChatUser> clients = (List<ChatData.ChatUser>) clientsListData;
                clientsList.getItems().clear();
                for (ChatData.ChatUser client : clients) {
                    clientsList.getItems().add("Client: " + Integer.toString(client.id));
                }
            });
        }, "localhost", 5555);
        connection.start();
    }
}
