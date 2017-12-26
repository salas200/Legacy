package model;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;
import sun.misc.Launcher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnimationThread extends Thread {

    private final String BASE_RSC = "icons/base/";

    public final String NORTH_ANIMATED = BASE_RSC.concat("north_animated");
    public final String NORTH_WALKING = BASE_RSC.concat("north_walking");

    public final String EAST_ANIMATED = BASE_RSC.concat("east_animated");
    public final String EAST_WALKING = BASE_RSC.concat("east_walking");

    public final String WEST_ANIMATED = BASE_RSC.concat("west_animated");
    public final String WEST_WALKING = BASE_RSC.concat("west_walking");

    public final String SOUTH_ANIMATED = BASE_RSC.concat("south_animated");
    public final String SOUTH_WALKING = BASE_RSC.concat("south_walking");

    private List<Image> imageList = new ArrayList<>();
    private String direction;
    private Sprite target;
    private Timeline timeline = new Timeline(60);
    private File jarFile = new File("./Legacy.jar");

    /**
     * Allocates a new {@code Thread} object.
     *
     * @param name the name of the new thread
     */
    public AnimationThread(String name, Sprite target, String direction) {
        super(name);
        super.setDaemon(true);
        this.target = target;
        if (direction == null) {
            this.direction = SOUTH_ANIMATED;
        }
    }

    @Override
    public synchronized void start() {
        timeline.stop();
        for (int i = 0; i < imageList.size() - 1; i++) {

            int finalI = i;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 125), event -> target.setImage(imageList.get(finalI))));

        }

        timeline.setAutoReverse(false);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    /**
     * Loads in the files for character animation.
     */
    private List<Image> animation(String direction) {

        switch (direction) {
            case "NORTH_ANIMATED":
                direction = NORTH_ANIMATED;
                break;
            case "NORTH_WALKING":
                direction = NORTH_WALKING;
                break;
            case "EAST_ANIMATED":
                direction = EAST_ANIMATED;
                break;
            case "EAST_WALKING":
                direction = EAST_WALKING;
                break;
            case "WEST_ANIMATED":
                direction = WEST_ANIMATED;
                break;
            case "WEST_WALKING":
                direction = WEST_WALKING;
                break;
            case "SOUTH_ANIMATED":
                direction = SOUTH_ANIMATED;
                break;
            case "SOUTH_WALKING":
                direction = SOUTH_WALKING;
                break;
        }

        imageList.clear();

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        int imageCount = 0;

        try {
            jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            if (jarFile.isFile()) {  // Run with JAR file
                final JarFile jar = new JarFile(jarFile);
                final JarEntry entry = jar.getJarEntry(direction);
                final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    if (name.contains(direction)) {
                        imageCount++;
                    }
                }
                imageCount--;
                jar.close();
            } else { // Run with IDE
                final URL url = Launcher.getLauncher().getClassLoader().getResource(direction);
                if (url != null) {
                    try {
                        final File apps = new File(url.toURI());
                        for (File app : apps.listFiles()) {
                            imageCount++;
                        }
                    } catch (URISyntaxException ex) {
                        Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            Logger.getGlobal().log(Level.WARNING, "IOException: Read/Write failure!");
        } catch (NullPointerException ex) {
            Logger.getGlobal().log(Level.WARNING, "NullPointerException: Could not find images!");
        } catch (URISyntaxException e1) {
            Logger.getGlobal().log(Level.WARNING, "URISyntaxException: direction to jarfile is incorrect!");
        }

        try {
            for (int i = 0; i < imageCount; i++) {
                InputStream imageInputStream = contextClassLoader.getResourceAsStream(direction + "/" + i + ".png");
                imageList.add(i, new Image(imageInputStream));
            }

        } catch (NullPointerException ex) {
            Logger.getGlobal().log(Level.WARNING, "NullPointerException: Streaming images returned a null value!");
        }

        return imageList;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
        imageList = animation(direction);
    }
}
