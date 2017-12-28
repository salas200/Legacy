package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.NodeGestures;
import model.PannableCanvas;
import model.SceneGestures;
import model.Terrain;
import service.SceneService;

import java.net.URL;
import java.util.ResourceBundle;

public class MapBuilderController implements Initializable {
    @FXML
    private TabPane toolsTabPane;

    @FXML
    private Tab turfTab;

    @FXML
    private HBox buttonsHBox;

    @FXML
    private Tab terrainTab;

    @FXML
    private ScrollPane gameScrollPane;

    private String selected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Terrain rock = new Terrain("/icons/rock.png");

        VBox vBox = new VBox();
        vBox.setSpacing(10);

        Button backBtn = new Button("Back to Legacy");
        backBtn.setOnAction(event -> {
            SceneService.switchScene(gameScrollPane.getParent(), "Main", MainController.class.getResource("/Styles.css").toString());
        });

        vBox.getChildren().addAll(rock, backBtn);
        terrainTab.setContent(vBox);

        Group group = new Group();

        // create canvas
        PannableCanvas canvas = new PannableCanvas();

        // create sample nodes which can be dragged
        NodeGestures nodeGestures = new NodeGestures( canvas);

        rock.setOnMouseClicked(event -> {
            selected = "rock";
            Terrain newRock = new Terrain("/icons/rock.png");
            newRock.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
            newRock.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
            canvas.getChildren().add(newRock);
        });

        Label label1 = new Label("Draggable node 1");
        label1.setTranslateX(10);
        label1.setTranslateY(10);
        label1.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        label1.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

        Label label2 = new Label("Draggable node 2");
        label2.setTranslateX(100);
        label2.setTranslateY(100);
        label2.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        label2.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

        Label label3 = new Label("Draggable node 3");
        label3.setTranslateX(200);
        label3.setTranslateY(200);
        label3.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        label3.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

        Circle circle1 = new Circle( 300, 300, 50);
        circle1.setStroke(Color.ORANGE);
        circle1.setFill(Color.ORANGE.deriveColor(1, 1, 1, 0.5));
        circle1.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        circle1.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

        Rectangle rect1 = new Rectangle(100,100);
        rect1.setTranslateX(450);
        rect1.setTranslateY(450);
        rect1.setStroke(Color.BLUE);
        rect1.setFill(Color.BLUE.deriveColor(1, 1, 1, 0.5));
        rect1.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        rect1.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

        canvas.getChildren().addAll(label1, label2, label3, circle1, rect1);

        group.getChildren().add(canvas);

        gameScrollPane.setContent(group);
        gameScrollPane.setPrefWidth(1024);
        gameScrollPane.setPrefHeight(768);

        SceneGestures sceneGestures = new SceneGestures(canvas);
        gameScrollPane.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        gameScrollPane.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        gameScrollPane.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        canvas.addGrid();

    }
}