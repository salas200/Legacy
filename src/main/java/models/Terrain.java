package models;

import javafx.scene.image.Image;

public class Terrain extends Sprite {

    /**
     * Allocates a new Sprite object using the given image.
     *
     * @param image Image preferably a PNG format
     */
    public Terrain(Image image) {
        super(image);
        setImage(image);
    }
}
