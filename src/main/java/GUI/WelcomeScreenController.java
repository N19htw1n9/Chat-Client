package GUI;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class WelcomeScreenController extends Controller {
    @FXML
    private Button joinServerButton;
    @FXML
    private TextField usernameTextField;

    public void joinServerButtonClick(Event e) throws IOException {
        String usernameValue = usernameTextField.getText();
        username = usernameValue;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatScreen.fxml"));
        Parent root = (Parent) loader.load();

        Scene newScene = new Scene(root);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setTitle(String.format("%s's Chat Room", usernameValue));
        window.setScene(newScene);
        window.show();
    }
}
