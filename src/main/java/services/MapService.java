package services;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import models.Character;

public class MapService {

    public static void generateBaseMap(Canvas gameCanvas, String path) {
        int rows = (int) Math.round(gameCanvas.getHeight() / 32);
        int columns = (int) Math.round(gameCanvas.getWidth() / 32);
        int currentRow = 0;
        int currentColumn = 0;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                gameCanvas.getGraphicsContext2D().drawImage(ResourceService.loadImage(path), currentColumn, currentRow);
                currentRow += 32;
                if (j == rows - 1) currentRow = 0;
            }
            gameCanvas.getGraphicsContext2D().drawImage(ResourceService.loadImage(path), currentColumn, currentRow);
            currentColumn += 32;
        }
    }

    public static void spawnCharacter(Group gameContainer, Character character, int x, int y) {
        gameContainer.getChildren().add(character);
        character.setX(x);
        character.setY(y);
    }

}
