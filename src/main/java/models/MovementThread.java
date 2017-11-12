package models;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class MovementThread extends AnimationTimer {

    private final double VIEW_STEP_V = 0.012305;
    private final double VIEW_STEP_H = 0.01391;

    private boolean goNorth, goSouth, goWest, goEast;
    private Character player1;
    private AnimationThread animationThread;
    private double STEP;
    private DoubleProperty trueHeight;
    private DoubleProperty trueWidth;
    private GridPane gamePane;
    private ScrollPane gameScrollPane;

    public MovementThread(Character player1, AnimationThread animationThread, double STEP
            , DoubleProperty trueHeight, DoubleProperty trueWidth, GridPane gamePane, ScrollPane gameScrollPane) {
        this.player1 = player1;
        this.animationThread = animationThread;
        this.STEP = STEP;
        this.trueHeight = trueHeight;
        this.trueWidth = trueWidth;
        this.gamePane = gamePane;
        this.gameScrollPane = gameScrollPane;
    }

    @Override
    public synchronized void handle(long now) {
        if (goNorth) {
            if (player1.getCurrentLocationRow() - 1 > 0) {
                if (player1.getCurrentLocationRow() > 8 && player1.getCurrentLocationRow() < 91){
                    gameScrollPane.setVvalue(gameScrollPane.getVvalue() - VIEW_STEP_V);
                }
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
                player1.setCurrentLocationRow(99);
                gamePane.getChildren().remove(player1);
                gameScrollPane.setVvalue(gameScrollPane.getVmax());
                gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());
            }
            try {
                wait(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (goWest) {
            if (player1.getCurrentLocationColumn() - 1 > 0) {
                if (player1.getCurrentLocationColumn() > 12 && player1.getCurrentLocationColumn() < 87){
                    gameScrollPane.setHvalue(gameScrollPane.getHvalue() - VIEW_STEP_H);
                }
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
                player1.setCurrentLocationColumn(99);
                gamePane.getChildren().remove(player1);
                gameScrollPane.setHvalue(1);
                gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());

            }
            try {
                wait(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (goSouth) {
            if (player1.getCurrentLocationRow() + 1 < 100) {
                if (player1.getCurrentLocationRow() > 8){
                    gameScrollPane.setVvalue(gameScrollPane.getVvalue() + VIEW_STEP_V);
                }
                if (animationThread.getDirection().equals(animationThread.SOUTH_WALKING)) {
                    player1.setCurrentLocationRow(player1.getCurrentLocationRow() + 1);
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
                gameScrollPane.setVvalue(gameScrollPane.getVmin());
                gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());

            }
            try {
                wait(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (goEast) {
            if (player1.getCurrentLocationColumn() + 1 < 100) {
                if (player1.getCurrentLocationColumn() > 12) {
                    gameScrollPane.setHvalue(gameScrollPane.getHvalue() + VIEW_STEP_H);
                }
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
                gameScrollPane.setHvalue(0);
                gamePane.add(player1, player1.getCurrentLocationColumn(), player1.getCurrentLocationRow());
            }
            try {
                wait(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
