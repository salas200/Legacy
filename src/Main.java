import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Achmed Waly
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("resources/Main.fxml"));
            primaryStage.setTitle("Legacy");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("resources/Styles.css").toString());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException | NullPointerException e) {
            Logger.getGlobal().log(Level.SEVERE,e.getMessage());
            Platform.exit();
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
