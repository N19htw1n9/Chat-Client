package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import Chat.ConnectionSocket;

public class ChatController extends Controller implements Initializable {
    @FXML
    private Button quitButton;
    @FXML
    private ListView openChatList;

    public void quitButtonAction(Event e) {
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = new ConnectionSocket(data -> {
            Platform.runLater(() -> {
                openChatList.getItems().add(data);
            });
        }, "localhost", 5555);
        connection.start();
    }
}
