package GUI;

import BaccaratGame.BaccaratInfo;
import javafx.scene.control.ToggleGroup;
import BaccaratGame.ConnectionSocket;

import java.util.ArrayList;

public class Controller {
    protected ToggleGroup group;
    protected static ConnectionSocket connection;
    protected ArrayList<BaccaratInfo> roundStatsList = new ArrayList<>();
}
