package controller;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import model.*;
import model.Character;
import service.MapService;
import service.ResourceService;
import service.SceneService;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

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

    @FXML
    private ScrollPane gameScrollPane;

    private Character character = null;

    private AnimationThread animationThread = null;

    private MovementThread movementThread = null;

    private DoubleProperty height = new SimpleDoubleProperty();
    private DoubleProperty width = new SimpleDoubleProperty();

    private Text introLocal = new Text();
    private Text introGlobal = new Text();
    private Text examine = new Text();

    private double canvasHeight = 1600;
    private double canvasWidth = 2400;

    private double step = 1;

    private LinkedList<Terrain> terrainLinkedList = new LinkedList<Terrain>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //  Set canvas size
        gameCanvas.setHeight(canvasHeight);
        gameCanvas.setWidth(canvasWidth);

        //  Set scroll pane size
        gameScrollPane.setMinHeight(600);
        gameScrollPane.setMaxHeight(600);
        gameScrollPane.setMinWidth(900);
        gameScrollPane.setMaxWidth(900);

        gameScrollPane.setOnKeyPressed(Event::consume);
        gameContainer.setOnScroll(Event::consume);

        gameScrollPane.setVmax(canvasHeight);
        gameScrollPane.setHmax(canvasWidth);

        //  Unsure as to why size for the movementthread is always a tile more
        height.set(canvasHeight - 32);
        width.set(canvasWidth - 32);

        //  Generate map
        MapService.generateBaseMap(gameCanvas, "/icons/grass.png");

        //  Generate terrain
        Terrain rock =  new Terrain("/icons/rock.png");
        MapService.spawnTerrain(gameContainer, rock, 64, 34);
        terrainLinkedList.add(rock);

        //  Generate character
        character = new Character(ResourceService.loadImage("/icons/base/south_animated/0.png"), "Shyzus", "1234");
        MapService.spawnCharacter(gameContainer, character, 0, 0);

        // Give each property in the propertyList of the player a changeListener (NOTE ONLY PROPERTIES VISIBLE TO THE PLAYER)
        for (Property property : character.getPropertyList()) {
            property.addListener((observable, oldValue, newValue) -> statsTabContent.getChildren().setAll(character.getStats()));
        }

        // Resize scrollpane length as their chatbox height increases.
        oocScrollPane.vvalueProperty().bind(globalChatBox.heightProperty());
        chatScrollPane.vvalueProperty().bind(localChatBox.heightProperty());

        character.xProperty().addListener((observable, oldValue, newValue) -> {
            Text statLocation = new Text("Location: (" + newValue.intValue() + ", " + (int) character.getY() + ")\n");
            statLocation.setFill(Color.WHITE);
            statsTabContent.getChildren().set(1, statLocation);

            if (newValue.intValue() + 32 >= (gameScrollPane.getMaxWidth() / 2) && newValue.intValue() < canvasWidth) {
                //  Set to center of screen
                gameScrollPane.setHvalue((newValue.intValue() - (gameScrollPane.getMaxWidth() / 2)) * 1.6);

            } else {
                gameScrollPane.setHvalue(0);
            }

        });

        character.yProperty().addListener((observable, oldValue, newValue) -> {
            Text statLocation = new Text("Location: (" + (int) character.getX() + ", " + newValue.intValue() + ")\n");
            statLocation.setFill(Color.WHITE);
            statsTabContent.getChildren().set(1, statLocation);

            if (newValue.intValue() + 32 >= (gameScrollPane.getMaxHeight() / 2) && newValue.intValue() < canvasHeight) {
                //  Set to center of screen
                gameScrollPane.setVvalue((newValue.intValue() - (gameScrollPane.getMaxHeight() / 2)) * 1.6);

            } else {
                gameScrollPane.setVvalue(0);
            }

        });


        //Intro message
        introLocal = new Text("Welcome to the Legacy Demo!\n");
        introLocal.getStyleClass().add("intro");
        localChatBox.getChildren().add(introLocal);

        introGlobal = new Text("Welcome to the Legacy Demo!\nUse the help verb if you have questions!\n");
        introGlobal.getStyleClass().add("intro");
        globalChatBox.getChildren().add(introGlobal);

        //sayVerb implementation
        Verb sayVerb = new Verb("Say", "verb");
        sayVerb.setOnMouseClicked(event -> character.say(localChatBox));

        //roleplayVerb implementation
        Verb roleplayVerb = new Verb("Roleplay", "verb");
        roleplayVerb.setOnMouseClicked(event -> character.roleplay(localChatBox));

        //oocVerb implementation
        Verb oocVerb = new Verb("OOC", "verb");
        oocVerb.setOnMouseClicked(event -> character.ooc(globalChatBox));
        
        //speedVerb implementation
        Verb speedVerb = new Verb("Set Speed", "verb");
        speedVerb.setOnMouseClicked(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setGraphic(null);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setHeaderText(null);
            dialog.setTitle("Set Speed");
            // The Java 8 way to get the response value (with lambda expression).
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(output -> {
                Boolean notWhitespace = false;
                for (char section : output.toCharArray()) {
                    notWhitespace = section != ' ';
                }

                if (!output.isEmpty() && notWhitespace) {
                    movementThread.setStep(Double.parseDouble(output));
                }
            });
        });

        Verb mapBuilder = new Verb("Open Map Builder", "verb");
        mapBuilder.setOnMouseClicked(event -> {
            SceneService.switchScene(tabPane.getParent(),"MapBuilder");
        });

        //helpVerb implementation
        Verb helpVerb = new Verb("Help", "verb");
        helpVerb.setOnMouseClicked(event -> getHelp());

        List<TextFlow> tabList = Arrays.asList(statsTabContent, skillsTabContent, optionsTabContent, itemsTabContent
                , localChatBox, globalChatBox);

        for (TextFlow tab : tabList) {
            tab.getStyleClass().add("background");
        }

        optionsTabContent.getChildren().addAll(sayVerb, roleplayVerb, oocVerb, helpVerb, speedVerb, mapBuilder);
        statsTabContent.getChildren().setAll(character.getStats());

        animationThread = new AnimationThread("character", character, null);
        animationThread.start();

        movementThread = new MovementThread(character, terrainLinkedList ,animationThread, step, height, width);

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
    void reconnect() {

    }

    @FXML
    void setMacros() {

    }

    @FXML
    void exit() {

    }

    @FXML
    void resizegameCanvas() {

    }

    @FXML
    void getAbout() {

    }

    @FXML
    void getHelp() {

    }
}
