package model;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;

import java.util.LinkedList;

public class MovementThread extends AnimationTimer {

    private boolean goNorth, goSouth, goWest, goEast;
    private Character player1;
    private AnimationThread animationThread;
    private double step;
    private DoubleProperty trueHeight;
    private DoubleProperty trueWidth;
    private LinkedList<Terrain> terrainLinkedList;

    public void setStep(double step) {
        if (step <= 0) {
            step = 1;
        }
        this.step = step;
    }

    public MovementThread(Character player1, LinkedList<Terrain> terrainLinkedList, AnimationThread animationThread, double step
            , DoubleProperty trueHeight, DoubleProperty trueWidth) {
        this.player1 = player1;
        this.terrainLinkedList = terrainLinkedList;
        this.animationThread = animationThread;
        this.step = step;
        this.trueHeight = trueHeight;
        this.trueWidth = trueWidth;
    }

    @Override
    public synchronized void handle(long now) {
        boundCheck();
        if (goNorth) {
            if (player1.getY() - step > 0) {

                //  Check for terrain
                for (Terrain terrain : terrainLinkedList) {
                    while (player1.intersects(terrain.getLayoutBounds())) {
                        if (player1.getY() < terrain.getY()) {
                            break;
                        } else {
                            player1.setLocation(player1.getX(), player1.getY() + 1);
                            boundCheck();
                        }
                    }
                    break;
                }

                boundCheck();
                if (animationThread.getDirection().equals(animationThread.NORTH_WALKING)) {
                    player1.setLocation(player1.getX(), player1.getY() - step);
                } else {
                    animationThread.interrupt();
                    animationThread.setDirection(animationThread.NORTH_WALKING);
                    animationThread.start();
                    player1.setLocation(player1.getX(), player1.getY() - step);

                }
            } else {
                player1.setLocation(player1.getX(), trueHeight.doubleValue());

            }
        } else if (goWest) {

            if (player1.getX() - step > 0) {

                //  Check for terrain
                for (Terrain terrain : terrainLinkedList) {
                    while (player1.intersects(terrain.getLayoutBounds())) {
                        if (player1.getX() < terrain.getX()) {
                            break;
                        } else {
                            player1.setLocation(player1.getX() + 1, player1.getY());
                            boundCheck();
                        }

                    }
                    break;
                }

                boundCheck();

                if (animationThread.getDirection().equals(animationThread.WEST_WALKING)) {
                    player1.setLocation(player1.getX() - step, player1.getY());

                } else {
                    animationThread.interrupt();
                    animationThread.setDirection(animationThread.WEST_WALKING);
                    animationThread.start();
                    player1.setLocation(player1.getX() - step, player1.getY());

                }
            } else {
                player1.setLocation(trueWidth.doubleValue(), player1.getY());

            }
        } else if (goSouth) {

            if (player1.getY() + step < trueHeight.doubleValue()) {

                //  Check for terrain
                for (Terrain terrain : terrainLinkedList) {
                    while (player1.intersects(terrain.getLayoutBounds())) {
                        if (player1.getY() > terrain.getY()) {
                            break;
                        } else {
                            player1.setLocation(player1.getX(), player1.getY() - 1);
                            boundCheck();
                        }
                    }
                    break;
                }

                boundCheck();

                if (animationThread.getDirection().equals(animationThread.SOUTH_WALKING)) {
                    player1.setLocation(player1.getX(), player1.getY() + step);

                } else {
                    animationThread.interrupt();
                    animationThread.setDirection(animationThread.SOUTH_WALKING);
                    animationThread.start();
                    player1.setLocation(player1.getX(), player1.getY() + step);

                }
            } else {
                player1.setLocation(player1.getX(), 0);

            }
        } else if (goEast) {

            if (player1.getX() + step < trueWidth.doubleValue()) {

                //  Check for terrain
                for (Terrain terrain : terrainLinkedList) {
                    while (player1.intersects(terrain.getLayoutBounds())) {
                        if (player1.getX() > terrain.getX()) {
                            break;
                        } else {
                            
                            player1.setLocation(player1.getX() - 1, player1.getY());
                            boundCheck();
                        }
                    }
                    break;
                }

                boundCheck();

                if (animationThread.getDirection().equals(animationThread.EAST_WALKING)) {
                    player1.setLocation(player1.getX() + step, player1.getY());
                    boundCheck();

                } else {
                    animationThread.interrupt();
                    animationThread.setDirection(animationThread.EAST_WALKING);
                    animationThread.start();
                    player1.setLocation(player1.getX() + step, player1.getY());
                }
            } else {
                player1.setLocation(0, player1.getY());
            }
        }
    }

    public void boundCheck() {
        if (player1.getY() < 0) {
            player1.setLocation(player1.getX(), player1.getY() + 32);
        }

        if (player1.getX() < 0) {
            player1.setLocation(player1.getX() + 32, player1.getY());
        }

        if (player1.getY() > trueHeight.get()) {
            player1.setLocation(player1.getX(), player1.getY() - 32);
        }

        if (player1.getX() > trueWidth.get()) {
            player1.setLocation(player1.getX() - 32, player1.getY());
        }
    }


    public void setGoNorth(boolean goNorth) {
        this.goNorth = goNorth;
        if (!goNorth && !goSouth && !goWest && !goEast) {
            animationThread.setDirection(animationThread.NORTH_ANIMATED);
        }
    }

    public void setGoSouth(boolean goSouth) {
        this.goSouth = goSouth;
        if (!goNorth && !goSouth && !goWest && !goEast) {
            animationThread.setDirection(animationThread.SOUTH_ANIMATED);
        }
    }

    public void setGoWest(boolean goWest) {
        this.goWest = goWest;
        if (!goNorth && !goSouth && !goWest && !goEast) {
            animationThread.setDirection(animationThread.WEST_ANIMATED);
        }
    }

    public void setGoEast(boolean goEast) {
        this.goEast = goEast;
        if (!goNorth && !goSouth && !goWest && !goEast) {
            animationThread.setDirection(animationThread.EAST_ANIMATED);
        }
    }
}
