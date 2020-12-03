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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ChatController extends Controller {
    @FXML
    private Button quitButton;

    public void quitButtonAction(Event e)
    {
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        window.close();
    }
}
