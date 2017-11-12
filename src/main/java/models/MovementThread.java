package models;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.GridPane;

public class MovementThread extends AnimationTimer {

    private boolean goNorth, goSouth, goWest, goEast;
    private Character player1;
    private AnimationThread animationThread;
    private double STEP;
    private DoubleProperty trueHeight;
    private DoubleProperty trueWidth;
    private GridPane gamePane;

    public MovementThread(Character player1, AnimationThread animationThread, double STEP
            , DoubleProperty trueHeight, DoubleProperty trueWidth, GridPane gamePane) {
        this.player1 = player1;
        this.animationThread = animationThread;
        this.STEP = STEP;
        this.trueHeight = trueHeight;
        this.trueWidth = trueWidth;
        this.gamePane = gamePane;
    }

    @Override
    public synchronized void handle(long now) {
        if (goNorth) {
            if (player1.getCurrentLocationRow() - 1 > 0) {
                if (animationThread.getDirection().equals(animationThread.NORTH_WALKING)) {
                    player1.setCurrentLocationRow(player1.getCurrentLocationRow() - 1);
                    gamePane.getChildren().remove(player1);
                    gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());
                } else {
                    animationThread.interrupt();
                    animationThread.setDirection(animationThread.NORTH_WALKING);
                    animationThread.start();
                    player1.setCurrentLocationRow(player1.getCurrentLocationRow() - 1);
                    gamePane.getChildren().remove(player1);
                    gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());
                }
            } else {
                player1.setCurrentLocationRow(100);
                gamePane.getChildren().remove(player1);
                gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());
            }
        } else if (goWest) {
            if (player1.getCurrentLocationColumn() - 1 > 0) {
                if (animationThread.getDirection().equals(animationThread.WEST_WALKING)) {
                    player1.setCurrentLocationColumn(player1.getCurrentLocationColumn() - 1);
                    gamePane.getChildren().remove(player1);
                    gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());

                } else {
                    animationThread.interrupt();
                    animationThread.setDirection(animationThread.WEST_WALKING);
                    animationThread.start();
                    player1.setCurrentLocationColumn(player1.getCurrentLocationColumn() - 1);
                    gamePane.getChildren().remove(player1);
                    gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());

                }
            } else {
                player1.setCurrentLocationColumn(100);
                gamePane.getChildren().remove(player1);
                gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());

            }
        } else if (goSouth) {
            if (player1.getCurrentLocationRow() + 1 < 100) {
                if (animationThread.getDirection().equals(animationThread.SOUTH_WALKING)) {
                    player1.setCurrentLocationRow(player1.getCurrentLocationColumn() + 1);
                    gamePane.getChildren().remove(player1);
                    gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());

                } else {
                    animationThread.interrupt();
                    animationThread.setDirection(animationThread.SOUTH_WALKING);
                    animationThread.start();
                    player1.setCurrentLocationRow(player1.getCurrentLocationRow() + 1);
                    gamePane.getChildren().remove(player1);
                    gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());

                }
            } else {
                player1.setCurrentLocationRow(0);
                gamePane.getChildren().remove(player1);
                gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());

            }
        } else if (goEast) {
            if (player1.getCurrentLocationColumn() + 1 < 100) {
                if (animationThread.getDirection().equals(animationThread.EAST_WALKING)) {
                    player1.setCurrentLocationColumn(player1.getCurrentLocationColumn() + 1);
                    gamePane.getChildren().remove(player1);
                    gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());

                } else {
                    animationThread.interrupt();
                    animationThread.setDirection(animationThread.EAST_WALKING);
                    animationThread.start();
                    player1.setCurrentLocationColumn(player1.getCurrentLocationColumn() + 1);
                    gamePane.getChildren().remove(player1);
                    gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());
                }
            } else {
                player1.setCurrentLocationColumn(0);
                gamePane.getChildren().remove(player1);
                gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());
            }
        }
    }

    public void setGoNorth(boolean goNorth) {
        this.goNorth = goNorth;
        if (!goNorth&&!goSouth&&!goWest&&!goEast){
            animationThread.setDirection(animationThread.NORTH_ANIMATED);
        }
    }

    public void setGoSouth(boolean goSouth) {
        this.goSouth = goSouth;
        if (!goNorth&&!goSouth&&!goWest&&!goEast){
            animationThread.setDirection(animationThread.SOUTH_ANIMATED);
        }
    }

    public void setGoWest(boolean goWest) {
        this.goWest = goWest;
        if (!goNorth&&!goSouth&&!goWest&&!goEast){
            animationThread.setDirection(animationThread.WEST_ANIMATED);
        }
    }

    public void setGoEast(boolean goEast) {
        this.goEast = goEast;
        if (!goNorth&&!goSouth&&!goWest&&!goEast){
            animationThread.setDirection(animationThread.EAST_ANIMATED);
        }
    }
}
