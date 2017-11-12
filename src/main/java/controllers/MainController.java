package controllers;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.*;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import models.AnimationThread;
import models.Character;
import models.MovementThread;
import models.Verb;

/**
 * @author Achmed Waly
 */
public class MainController implements Initializable {

    @FXML
    private TextFlow optionsTabContent;

    @FXML
    private Tab itemsTab;

    @FXML
    private Tab statsTab;

    @FXML
    private Tab skillsTab;

    @FXML
    private ScrollPane gameScrollPane;

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

    private GridPane gamePane = new GridPane();

    private final double STEP = 1;

    private final Image GRASS = new Image(getClass().getResource("/icons/grass.png").toString());
    private final Image STATIC_BASE = new Image(getClass().getResource("/icons/base/south_animated/0.png").toString());

    private DoubleProperty trueHeight = new SimpleDoubleProperty(600);
    private DoubleProperty trueWidth = new SimpleDoubleProperty(900);

    private Character player1;
    private Character dummy1;

    private ChangeListener<Number> playerXListener;
    private ChangeListener<Number> playerYListener;

    private Text introLocal = new Text();
    private Text introGlobal = new Text();
    private Text examine = new Text();

    private AnimationThread animationThread;
    private MovementThread movementThread;

    private IntegerProperty currentLocationRow = new SimpleIntegerProperty();
    private IntegerProperty currentLocationColumn = new SimpleIntegerProperty();

    private boolean goNorth, goSouth, goWest, goEast;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameScrollPane.setMinSize(900,600);
        gameScrollPane.setMaxSize(900, 600);

        //gameScrollPane.prefWidthProperty().addListener((observable, oldValue, newValue) -> trueWidth.set(newValue.doubleValue()));
        //gameScrollPane.prefHeightProperty().addListener((observable, oldValue, newValue) -> trueHeight.set(newValue.doubleValue()));

        createMap(100, gamePane);
        //gamePane.setBackground(new Background(new BackgroundImage(GRASS, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));

        dummy1 = new Character(STATIC_BASE,"dummy");

        player1 = new Character(STATIC_BASE, "shyzus", "");

        //player1.setLocation(50, 50);
        //dummy1.setLocation(200,200);

        // Give each property in the propertyList of the player a changeListener (NOTE ONLY PROPERTIES VISIBLE TO THE PLAYER)
        for (Property property : player1.getPropertyList()) {
            property.addListener((observable, oldValue, newValue) -> statsTabContent.getChildren().setAll(player1.getStats()));
        }

        // Resize scrollpane length as their chatbox height increases.
        oocScrollPane.vvalueProperty().bind(globalChatBox.heightProperty());
        chatScrollPane.vvalueProperty().bind(localChatBox.heightProperty());

        player1.currentLocationColumnProperty().bindBidirectional(currentLocationColumn);
        player1.currentLocationRowProperty().bindBidirectional(currentLocationRow);

        // Add listener for collisions
        playerXListener = player1.createPosChangeListener(player1, dummy1, STEP, trueHeight.getValue(), trueWidth.getValue());
        playerYListener = player1.createPosChangeListener(player1, dummy1, STEP, trueHeight.getValue(), trueWidth.getValue());

        player1.currentLocationColumnProperty().addListener((observable, oldValue, newValue) -> {
            Text statLocation = new Text("Location: (" + newValue.intValue() + ", " + player1.getCurrentLocationRow() + ")\n");
            statLocation.setFill(Color.WHITE);
            statsTabContent.getChildren().set(1, statLocation);
        });

        player1.currentLocationRowProperty().addListener((observable, oldValue, newValue) -> {
            Text statLocation = new Text("Location: (" + player1.getCurrentLocationColumn() + ", " + newValue.intValue() + ")\n");
            statLocation.setFill(Color.WHITE);
            statsTabContent.getChildren().set(1, statLocation);
        });

        player1.currentLocationColumnProperty().addListener(playerXListener);
        player1.currentLocationRowProperty().addListener(playerYListener);

        //Intro message
        introLocal = new Text("Welcome to the Legacy Demo!\n");
        introLocal.getStyleClass().add("intro");
        localChatBox.getChildren().add(introLocal);

        introGlobal = new Text("Welcome to the Legacy Demo!\nUse the help verb if you have questions!\n");
        introGlobal.getStyleClass().add("intro");
        globalChatBox.getChildren().add(introGlobal);

        //sayVerb implementation
        Verb sayVerb = new Verb("Say", "verb");
        sayVerb.setOnMouseClicked(event -> player1.say(localChatBox));

        //roleplayVerb implementation
        Verb roleplayVerb = new Verb("Roleplay", "verb");
        roleplayVerb.setOnMouseClicked(event -> player1.roleplay(localChatBox));

        //oocVerb implementation
        Verb oocVerb = new Verb("OOC", "verb");
        oocVerb.setOnMouseClicked(event -> player1.ooc(globalChatBox));

        //helpVerb implementation
        Verb helpVerb = new Verb("Help", "verb");
        helpVerb.setOnMouseClicked(event -> getHelp());

        List<TextFlow> tabList = Arrays.asList(statsTabContent, skillsTabContent, optionsTabContent, itemsTabContent
                , localChatBox, globalChatBox);

        for (TextFlow tab : tabList) {
            tab.getStyleClass().add("background");
        }

        optionsTabContent.getChildren().addAll(sayVerb, roleplayVerb, oocVerb, helpVerb);
        statsTabContent.getChildren().setAll(player1.getStats());

        gamePane.add(player1,0,0);

        animationThread = new AnimationThread("player1",player1, null);
        animationThread.start();

        movementThread = new MovementThread(player1, animationThread, STEP, trueHeight, trueWidth, gamePane);

        gamePane.setOnKeyPressed(event -> {
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

        gamePane.setOnScroll(Event::consume);
        gameScrollPane.setOnKeyPressed(Event::consume);


        gamePane.setOnKeyReleased(event -> {
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

        gamePane.setOnMouseClicked(event -> Platform.runLater(gamePane::requestFocus));

        statsTab.setOnSelectionChanged(event -> Platform.runLater(gamePane::requestFocus));
        statsTabContent.setOnMouseClicked(event -> Platform.runLater(gamePane::requestFocus));
        skillsTab.setOnSelectionChanged(event -> Platform.runLater(gamePane::requestFocus));
        skillsTabContent.setOnMouseClicked(event -> Platform.runLater(gamePane::requestFocus));
        itemsTab.setOnSelectionChanged(event -> Platform.runLater(gamePane::requestFocus));
        itemsTabContent.setOnMouseClicked(event -> Platform.runLater(gamePane::requestFocus));
        optionsTab.setOnSelectionChanged(event -> Platform.runLater(gamePane::requestFocus));
        optionsTabContent.setOnMouseClicked(event -> Platform.runLater(gamePane::requestFocus));

        localChatBox.setOnMouseClicked(event -> Platform.runLater(gamePane::requestFocus));
        globalChatBox.setOnMouseClicked(event -> Platform.runLater(gamePane::requestFocus));

        Platform.runLater(gamePane::requestFocus);

        gameScrollPane.setContent(gamePane);
    }

    /**
     * Display help page in a popup.
     */
    @FXML
    private void getHelp() {
        try {
            Stage stage = new Stage();
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.load(getClass().getResource("help/Help.html").toString());
            Scene scene = new Scene(webView);
            stage.setScene(scene);
            stage.setTitle("Help");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(gamePane.getScene().getWindow());
            stage.show();
            stage.setOnCloseRequest(event -> webEngine.load(null));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exit the application.
     */
    @FXML
    private void exit() {
        Platform.exit();
    }

    /**
     * TODO: JAVADOC
     */
    @FXML
    private void reconnect() {
        Text level = new Text("SYSTEM: RECONNECT FUNCTION NOT YET IMPLEMENTED!\n");
        level.getStyleClass().add("error");
        globalChatBox.getChildren().add(level);
    }

    /**
     * TODO: JAVADOC
     */
    @FXML
    private void setMacros() {
        Text level = new Text("SYSTEM: SETMACROS FUNCTION NOT YET IMPLEMENTED!\n");
        level.getStyleClass().add("error");
        globalChatBox.getChildren().add(level);
    }

    /**
     * TODO: JAVADOC
     */
    @FXML
    private void getAbout() {
        Text level = new Text("SYSTEM: GETABOUT FUNCTION NOT YET IMPLEMENTED!\n");
        level.getStyleClass().add("error");
        globalChatBox.getChildren().add(level);
    }

    /**
     * Resize the gamePane based on input.
     */
    @FXML
    private void resizeGamePane() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Resize");
        dialog.setHeaderText(null);

        // Set the button types.
        ButtonType confirmButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField height = new TextField();
        height.setPromptText(String.valueOf(gamePane.getHeight()));
        TextField width = new TextField();
        width.setPromptText(String.valueOf(gamePane.getWidth()));

        gridPane.add(new Label("Height:"), 0, 0);
        gridPane.add(height, 1, 0);
        gridPane.add(new Label("Width:"), 2, 0);
        gridPane.add(width, 3, 0);

        dialog.getDialogPane().setContent(gridPane);

        // Convert the result to a username-width-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType && !height.getText().isEmpty() && !width.getText().isEmpty()) {
                return new Pair<>(height.getText(), width.getText());
            } else {
                return null;
            }
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        if (result.isPresent()) {
            double heightValue = Double.parseDouble(result.get().getKey());
            double widthValue = Double.parseDouble(result.get().getValue());

            gamePane.setMinSize(widthValue, heightValue);
            gamePane.setMaxSize(widthValue, heightValue);
            //updateListeners();
        } else {
            dialog.close();
        }
    }

    private void createMap(int size, GridPane gamePane){
        for (int i = 0; i < size; i++) {
            gamePane.getRowConstraints().add(new RowConstraints(32));
            gamePane.getColumnConstraints().add(new ColumnConstraints(32));
        }

        for (int i = 0; i < size ; i++) {
            for (int j = 0; j < size; j++) {
                Pane tile = new Pane();
                tile.setBackground(new Background(new BackgroundImage(GRASS, BackgroundRepeat.REPEAT,
                        BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));
                gamePane.add(tile,i, j);
            }
        }
    }

    /**
     * Updates the listeners for player and dummy properties.
     */
    private void updateListeners() {
        //player1.xProperty().removeListener(playerXListener);
        //player1.yProperty().removeListener(playerYListener);


        //playerXListener = player1.createPosChangeListener(player1, player1, STEP, trueHeight.getValue(), trueWidth.getValue());
        //playerYListener = player1.createPosChangeListener(player1, player1, STEP, trueHeight.getValue(), trueWidth.getValue());

        //player1.xProperty().addListener(playerXListener);
        //player1.yProperty().addListener(playerYListener);

    }
}
