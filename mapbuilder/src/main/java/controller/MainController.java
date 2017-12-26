package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import model.Terrain;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TabPane toolsTabPane;

    @FXML
    private Tab turfPane;

    @FXML
    private HBox buttonsHBox;

    @FXML
    private Tab terrainTab;

    @FXML
    private ScrollPane gameScrollPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Terrain rock = new Terrain("/icons/rock.png");


    }
}

