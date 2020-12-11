package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.HashSet;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import Chat.ConnectionSocket;
import Chat.ChatData;

public class ChatController extends Controller implements Initializable {
    @FXML
    private Button quitButton;
    @FXML
    private ListView<String> openChatList;
    @FXML
    private ListView<HBox> clientsList;
    @FXML
    private TextField chatInput;
    @FXML
    private ImageView sendButton;
    private HashSet<ChatData.ChatUser> toUsersSet = new HashSet<>();

    public void quitButtonAction(Event e) {
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.close();
        System.exit(1);
    }

    public void sendButtonClick(Event e) {
        if (toUsersSet.isEmpty()) {
            openChatList.getItems().add("Please select a client");
        } else {
            try {
                connection.send(chatInput.getText(), (HashSet<ChatData.ChatUser>) toUsersSet.clone());
            } catch (Exception e1) {
                openChatList.getItems().add("Error, couldn't send user data");
            }
            chatInput.setText("");
        }
    }

    private void setListViewItemClick(ChatData.ChatUser c) {
        CheckBox cb = new CheckBox("Client: " + Integer.toString(c.id));
        cb.setOnAction(e -> {
            if (cb.isSelected()) {
                toUsersSet.add(c);
            } else {
                toUsersSet.remove(c);
            }
        });

        HBox listItemContainer = new HBox(10, cb);
        clientsList.getItems().add(listItemContainer);
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

                clients.stream().forEach(client -> {
                    if (client == null)
                        return;
                    setListViewItemClick(client);
                });
            });
        }, "localhost", 5555, username);
        connection.start();
    }
}
