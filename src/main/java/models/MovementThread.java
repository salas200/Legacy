package models;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;

public class MovementThread extends AnimationTimer {

    private boolean goNorth, goSouth, goWest, goEast;
    private Character player1;
    private AnimationThread animationThread;
    private double STEP;
    private DoubleProperty trueHeight;
    private DoubleProperty trueWidth;

    public void setSTEP(double STEP) {
        this.STEP = STEP;
    }

    public MovementThread(Character player1, AnimationThread animationThread, double STEP
            , DoubleProperty trueHeight, DoubleProperty trueWidth) {
        this.player1 = player1;
        this.animationThread = animationThread;
        this.STEP = STEP;
        this.trueHeight = trueHeight;

        this.trueWidth = trueWidth;
    }

    @Override
    public synchronized void handle(long now) {
        if (goNorth) {
            if (player1.getY() - STEP > 0) {
                if (animationThread.getDirection().equals(animationThread.NORTH_WALKING)) {
                    player1.setY(player1.getY() - STEP);
                } else {
                    animationThread.interrupt();
                    animationThread.setDirection(animationThread.NORTH_WALKING);
                    animationThread.start();
                    player1.setY(player1.getY() - STEP);

                }
            } else {
                player1.setLocation(player1.getX(), trueHeight.doubleValue());

            }
        } else if (goWest) {

            if (player1.getX() - STEP > 0) {
                if (animationThread.getDirection().equals(animationThread.WEST_WALKING)) {
                    player1.setX(player1.getX() - STEP);

                } else {
                    animationThread.interrupt();
                    animationThread.setDirection(animationThread.WEST_WALKING);
                    animationThread.start();
                    player1.setX(player1.getX() - STEP);

                }
            } else {
                player1.setLocation(trueWidth.doubleValue(), player1.getY());

            }
        } else if (goSouth) {
            if (player1.getY() + STEP < trueHeight.doubleValue()) {
                if (animationThread.getDirection().equals(animationThread.SOUTH_WALKING)) {
                    player1.setY(player1.getY() + STEP);

                } else {
                    animationThread.interrupt();
                    animationThread.setDirection(animationThread.SOUTH_WALKING);
                    animationThread.start();
                    player1.setY(player1.getY() + STEP);

                }
            } else {
                player1.setLocation(player1.getX(), 0);

            }
        } else if (goEast) {
            if (player1.getX() + STEP < trueWidth.doubleValue()) {
                if (animationThread.getDirection().equals(animationThread.EAST_WALKING)) {
                    player1.setX(player1.getX() + STEP);

                } else {
                    animationThread.interrupt();
                    animationThread.setDirection(animationThread.EAST_WALKING);
                    animationThread.start();
                    player1.setX(player1.getX() + STEP);
                }
            } else {
                player1.setLocation(0, player1.getY());
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
