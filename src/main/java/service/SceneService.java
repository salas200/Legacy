package service;

import controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneService {

    /**
     * Switches a scene to the specified screen
     *
     * @param parent   a component of the current scene
     * @param newScene the string name of the FXML file to load with /fxml/ or .fxml
     */
    public static void switchScene(Parent parent, String newScene) {
        switchScene(parent, loadFXML(newScene));
    }

    /**
     * Switches a scene to the specified screen
     *
     * @param parent a component of the current scene
     * @param root   the new component to display
     */
    private static void switchScene(Parent parent, Parent root) {
        Stage stage = (Stage) parent.getScene().getWindow();
        //use existing dimensions
        double width = stage.getScene().getWidth();
        double height = stage.getScene().getHeight();
        Scene scene = new Scene(root, width, height);

        stage.setScene(scene);
    }

    private static Parent loadFXML(String name) {
        Parent root = null;
        try {
            root = FXMLLoader.load(MainController.class.getResource("/" + name + ".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

}
