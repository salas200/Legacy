package models;

import javafx.scene.control.Label;

public class Verb extends Label {

    public Verb(String text, String styleClass) {
        super(text);
        super.getStyleClass().add(styleClass);
    }
}
