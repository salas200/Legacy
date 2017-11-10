package models;

import javafx.scene.image.Image;

public class Item extends Sprite{

    private String name;

    /**
     * Allocates a new Item object using the given image.
     *
     * @param image Image preferably a PNG format
     */
    public Item(Image image, String name) {
        super(image);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
