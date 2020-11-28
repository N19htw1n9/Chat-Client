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

public class GameScreenController extends Controller {
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

    public void toggleButton() {
        group = new ToggleGroup();
        playerToggleButton.setToggleGroup(group);
        bankerToggleButton.setToggleGroup(group);

        if (playerToggleButton.isSelected()) {
            playerToggleButton.setStyle("-fx-background-color: #ed7117");
            bankerToggleButton.setStyle("-fx-background-color: orange");
        }

        else if (bankerToggleButton.isSelected()) {
            bankerToggleButton.setStyle("-fx-background-color: #ed7117");
            playerToggleButton.setStyle("-fx-background-color: orange");
        }

        else if (!(bankerToggleButton.isSelected() || playerToggleButton.isSelected())) {
            bankerToggleButton.setStyle("-fx-background-color: orange");
            playerToggleButton.setStyle("-fx-background-color: orange");
        }
    }

    public void quitButtonAction(Event e) throws IOException {
        Parent quitScreen = FXMLLoader.load(getClass().getResource("/QuitScreen.fxml"));
        Scene quitScene = new Scene(quitScreen);

        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        window.setScene(quitScene);
        window.setTitle("Quit game");
        window.show();
    }

    public void roundStatsButtonAction(Event e) throws IOException {
        Parent roundStatsScreen = FXMLLoader.load(getClass().getResource("/RoundStatsScreen.fxml"));
        Scene roundStatsScene = new Scene(roundStatsScreen);

        Scene currentScene = ((Node)e.getSource()).getScene();
        this.gameSceneState = currentScene;
        Stage window = (Stage) currentScene.getWindow();
        window.setScene(roundStatsScene);
        window.setTitle("Round stats");
        window.show();
    }

    private Image imageFromCard(String card) {
        String path = "/Cards/" + card + ".jpg";
        Image image = new Image(getClass().getResource(path).toExternalForm());
        return image;
    }

    public void startButtonAction(Event e) {
        // Hide extra cards
        this.bankerExtraCard.setStyle("visibility: hidden;");
        this.playerExtraCard.setStyle("visibility: hidden;");

        double bid;
        try {
            bid = Double.parseDouble(playerBidText.getText());
        } catch (Exception err) {
            System.out.println("Bid value must be a number");
            return;
        }

        String hand = "Player";
        if (bankerToggleButton.isSelected())
            hand = "Banker";

        try {
            connection.send(bid, hand);
            BaccaratInfo res = connection.recieve();

            this.roundStatsList.add(res);

            // Change banker cards
            this.bankerLeftCard.setImage(this.imageFromCard(res.bankerHand.get(0)));
            this.bankerRightCard.setImage(this.imageFromCard(res.bankerHand.get(1)));

            // Change player cards
            this.playerLeftCard.setImage(this.imageFromCard(res.playerHand.get(0)));
            this.playerRightCard.setImage(this.imageFromCard(res.playerHand.get(1)));

            // Show extra banker card
            if (res.bankerHand.size() == 3) {
                this.bankerExtraCard.setStyle("visibility: none;");
                this.bankerExtraCard.setImage(this.imageFromCard(res.bankerHand.get(2)));
            }

            // Show extra player card
            if (res.playerHand.size() == 3) {
                this.playerExtraCard.setStyle("visibility: none;");
                this.playerExtraCard.setImage(this.imageFromCard(res.playerHand.get(2)));
            }

            // Display total winnings
            winningsCount += res.winnings;
            winningsLabel.setText("Winnings: $" + String.format("%.2f", winningsCount));

            // Visual feedback showing winner
            if(res.winner.equals("Banker")) {
                bankerWinLabel.setText("Banker won!");
                bankerWinLabel.setStyle("-fx-color: #60b31d");
                playerWinLabel.setText("");
                bankerPane.setStyle("-fx-border-color: #60b31d; -fx-border-width: 7px");
                playerPane.setStyle("-fx-border-color: transparent");
            } else if(res.winner.equals("Player")) {
                playerWinLabel.setText("Player won!");
                playerWinLabel.setStyle("-fx-color: #60b31d");
                bankerWinLabel.setText("");
                playerPane.setStyle("-fx-border-color: #60b31d; -fx-border-width: 7px");
                bankerPane.setStyle("-fx-border-color: transparent");
            } else {
                playerWinLabel.setText("It's a Draw!");
                playerWinLabel.setStyle("-fx-text-fill: #daa520");
                bankerWinLabel.setText("It's a Draw!");
                bankerWinLabel.setStyle("-fx-text-fill: #daa520");
                bankerPane.setStyle("-fx-border-color: #daa520; -fx-border-width: 5px");
                playerPane.setStyle("-fx-border-color: #daa520; -fx-border-width: 5px");
            }

        } catch (Exception err) {
            System.out.println("Something went wrong while trying to send the request to the server");
            return;
        }
    }
}
