package service;

import controller.MainController;
import javafx.scene.image.Image;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceService {

    public static Image loadImage(String path) {
        Image image = null;
        try {
             image = new Image(MainController.class.getResource(path).toString());
        } catch (IllegalArgumentException e) {
            Logger.getLogger(ResourceService.class.getName()).log(Level.WARNING,"Incorrect path!");
        } catch (NullPointerException e) {
            Logger.getLogger(ResourceService.class.getName()).log(Level.WARNING,"No resource found!");
        }


        return image;
    }
}
