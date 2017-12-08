package controllers;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextFlow;
import models.AnimationThread;
import models.Character;
import models.MovementThread;
import services.MapService;
import services.ResourceService;

import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable{

    @FXML
    private TextFlow optionsTabContent;

    @FXML
    private Canvas gameCanvas;

    @FXML
    private Tab itemsTab;

    @FXML
    private Tab statsTab;

    @FXML
    private Tab skillsTab;

    @FXML
    private ScrollPane oocScrollPane;

    @FXML
    private TextFlow globalChatBox;

    @FXML
    private TextFlow skillsTabContent;

    @FXML
    private TextFlow statsTabContent;

    @FXML
    private Tab optionsTab;

    @FXML
    private BorderPane mainPane;

    @FXML
    private TextFlow itemsTabContent;

    @FXML
    private TextFlow localChatBox;

    @FXML
    private TabPane tabPane;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private Group gameContainer;

    private Character character = null;

    private AnimationThread animationThread = null;

    private MovementThread movementThread = null;

    private DoubleProperty height = new SimpleDoubleProperty();

    private DoubleProperty width = new SimpleDoubleProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //  Set canvas size
        gameCanvas.setHeight(600);
        gameCanvas.setWidth(900);
        height.set(600);
        width.set(900);

        //  Add change listener for the size
        gameCanvas.heightProperty().addListener((observable, oldValue, newValue) ->
                gameContainer.minHeight(newValue.doubleValue()));
        gameCanvas.widthProperty().addListener((observable, oldValue, newValue) ->
                gameContainer.minWidth(newValue.doubleValue()));

        //  Generate map
        MapService.generateBaseMap(gameCanvas, "/icons/grass.png");

        character = new Character(ResourceService.loadImage("/icons/base/south_animated/0.png"),"Shyzus", "1234");
        MapService.spawnCharacter(gameContainer, character, 3, 3);

        animationThread = new AnimationThread("player1",character, null);
        animationThread.start();

        movementThread = new MovementThread(character, animationThread, 1, height, width);

        gameCanvas.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    movementThread.setGoNorth(true);
                    break;
                case LEFT:
                    movementThread.setGoWest(true);
                    break;
                case DOWN:
                    movementThread.setGoSouth(true);
                    break;
                case RIGHT:
                    movementThread.setGoEast(true);
                    break;
            }
        });

        gameCanvas.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:
                    movementThread.setGoNorth(false);
                    break;
                case LEFT:
                    movementThread.setGoWest(false);
                    break;
                case DOWN:
                    movementThread.setGoSouth(false);
                    break;
                case RIGHT:
                    movementThread.setGoEast(false);
                    break;
            }
        });

        movementThread.start();

        gameCanvas.setOnMouseClicked(event -> Platform.runLater(gameCanvas::requestFocus));

        statsTab.setOnSelectionChanged(event -> Platform.runLater(gameCanvas::requestFocus));
        statsTabContent.setOnMouseClicked(event -> Platform.runLater(gameCanvas::requestFocus));
        skillsTab.setOnSelectionChanged(event -> Platform.runLater(gameCanvas::requestFocus));
        skillsTabContent.setOnMouseClicked(event -> Platform.runLater(gameCanvas::requestFocus));
        itemsTab.setOnSelectionChanged(event -> Platform.runLater(gameCanvas::requestFocus));
        itemsTabContent.setOnMouseClicked(event -> Platform.runLater(gameCanvas::requestFocus));
        optionsTab.setOnSelectionChanged(event -> Platform.runLater(gameCanvas::requestFocus));
        optionsTabContent.setOnMouseClicked(event -> Platform.runLater(gameCanvas::requestFocus));

        localChatBox.setOnMouseClicked(event -> Platform.runLater(gameCanvas::requestFocus));
        globalChatBox.setOnMouseClicked(event -> Platform.runLater(gameCanvas::requestFocus));

        Platform.runLater(gameCanvas::requestFocus);
    }


    @FXML
    void reconnect(ActionEvent event) {

    }

    @FXML
    void setMacros(ActionEvent event) {

    }

    @FXML
    void exit(ActionEvent event) {

    }

    @FXML
    void resizegameCanvas(ActionEvent event) {

    }

    @FXML
    void getAbout(ActionEvent event) {

    }

    @FXML
    void getHelp(ActionEvent event) {

    }
}
