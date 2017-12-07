package services;

import controllers.MainController;
import javafx.scene.image.Image;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceManager {

    public static Image loadImage(String path) {
        Image image = null;
        try {
             image = new Image(MainController.class.getResource(path).toString());
        } catch (IllegalArgumentException e) {
            Logger.getLogger(ResourceManager.class.getName()).log(Level.WARNING,"Incorrect path!");
        } catch (NullPointerException e) {
            Logger.getLogger(ResourceManager.class.getName()).log(Level.WARNING,"No resource found!");
        }


        return image;
    }
}
