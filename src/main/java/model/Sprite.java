package model;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Achmed Waly
 */
public class Sprite extends ImageView {

    private double prevX;
    private double prevY;
    private Property<Image> imageProperty = new SimpleObjectProperty<>();

    public Sprite() {
    }

    /**
     * Allocates a new Sprite object using the given image.
     *
     * @param image Image preferably a PNG format
     */
    public Sprite(Image image) { imageProperty.setValue(image); }

    /**
     * Sets the location of the Sprite
     *
     * @param x X-Coordinate
     * @param y Y-Coordinate
     */
    public void setLocation(double x, double y) {
        setPrevX();
        setPrevY();
        super.setX(x);
        super.setY(y);
    }

    public String getLocation(){
        return String.format("%s,%s", super.getX(), super.getY());
    }

    public double getPrevX() { return prevX; }

    private void setPrevX() {
        this.prevX = this.getX();
    }

    public double getPrevY() {
        return prevY;
    }

    private void setPrevY() {
        this.prevY = this.getY();
    }

    public Image getImageProperty() {
        return imageProperty.getValue();
    }

    public Property<Image> imagePropertyProperty() {
        return imageProperty;
    }

    public void setImageProperty(Image imageProperty) {
        this.imageProperty.setValue(imageProperty);
    }
}

