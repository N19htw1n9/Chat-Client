package GUI;

// READ THESE COMMENTS FOR CHAT APPLICATION
/*
    I finished the GUI components but here are a couple things to keep in mind:
    - the sendMessageButton has an ImageView in there that I used for stylistic
        purposes (to make it look cool), when you add actionElements into the
        button, make sure that it's added to the button and not the ImageView

    - For the client dropdown selection I did some research and decided to use
      a combobox object, because I found a way on StackOverflow to make multiple
      checkboxes within the combobox. See this link:
            https://stackoverflow.com/questions/26186572/selecting-multiple-items-from-combobox

    - Lastly, I recommend watching this video to get an idea on how to make the
        chat work, I incorporated similar design elements so it stays straightforward
        and simple based on this video. See this link:
            https://www.youtube.com/watch?v=VVUuo9VO2II
 */

import BaccaratGame.BaccaratInfo;
import GUI.Controller;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ChatController extends Controller {
    @FXML private TextField playerBidText;
    @FXML private ToggleButton playerToggleButton;
    @FXML private ToggleButton bankerToggleButton;
    @FXML private HBox BankerHBox;
    @FXML private HBox PlayerHBox;
    @FXML private Button quitButton;
    @FXML private Button roundStatsButton;
    @FXML private Button startButton;
    @FXML private ImageView bankerLeftCard;
    @FXML private ImageView bankerRightCard;
    @FXML private ImageView bankerExtraCard;
    @FXML private ImageView playerLeftCard;
    @FXML private ImageView playerRightCard;
    @FXML private ImageView playerExtraCard;
    @FXML private Pane bankerPane;
    @FXML private Pane playerPane;
    @FXML private Label bankerWinLabel;
    @FXML private Label playerWinLabel;
    @FXML private Label winningsLabel;

    private double winningsCount = 0;

    public void quitButtonAction(Event e)
    {
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        window.close();
    }

    public void sendButtonAction(Event e)
    {
        try
        {

        }
        catch (Exception err)
        {
            System.out.println("Bid value must be a number");
            return;
        }


        try
        {

        }
        catch (Exception err)
        {
            System.out.println("Something went wrong while trying to send the request to the server");
            return;
        }
    }
}
