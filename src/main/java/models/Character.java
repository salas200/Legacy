package models;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Achmed Waly
 */
public class Character extends Sprite {

    private StringProperty name = new SimpleStringProperty();
    private String password;
    private DoubleProperty health = new SimpleDoubleProperty();
    private DoubleProperty trueAge = new SimpleDoubleProperty();
    private DoubleProperty physAge = new SimpleDoubleProperty();
    private DoubleProperty lift = new SimpleDoubleProperty();
    private DoubleProperty powerLevel = new SimpleDoubleProperty();
    private ListProperty<Item> inventory = new SimpleListProperty();
    private List<Property<?>> propertyList = Arrays.asList(name, health, powerLevel, trueAge, physAge, lift);
    private double energyReserves;
    private double strength;
    private double endurance;
    private double force;
    private double speed;
    private double resistance;
    private double offence;
    private double defence;
    private double regeneration;
    private double recovery;

    private double energyMod;
    private double strengthMod;
    private double enduranceMod;
    private double powerLevelMod;
    private double forceMod;
    private double speedMod;
    private double resistanceMod;
    private double offenceMod;
    private double defenceMod;

    private double recovRatio;
    private double regenRatio;

    private Random numbGenerator = new Random();
    private Timer regenerationTimer = new Timer("regeneration", true);

    /**
     * Allocates a new Character object using the given image and data.
     * This constructor is meant to be used for player characters(PC).
     *
     * @param image    Image preferably a PNG format
     * @param name     Name of character
     * @param password Password of character
     */
    public Character(Image image, String name, String password) {
        super(image);
        this.setImage(image);
        this.name.set(name);
        this.password = hashPassword(password);
        health.set(100);
        trueAge.set(21);
        physAge.set(21);
        lift.set(4);
        powerLevelMod = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble();
        energyMod = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble();
        strengthMod = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble();
        enduranceMod = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble();
        forceMod = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble();
        speedMod = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble();
        resistanceMod = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble();
        offenceMod = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble();
        defenceMod = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble();
        recovRatio = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble();
        regenRatio = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble();
        energyReserves = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble() * getEnergyMod();
        powerLevel.set(Math.floor(numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble() * getPowerLevelMod()));
        strength = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble() * getStrengthMod();
        endurance = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble() * getEnduranceMod();
        force = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble() * getForceMod();
        speed = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble() * getSpeedMod();
        resistance = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble() * getResistanceMod();
        offence = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble() * getOffenceMod();
        defence = numbGenerator.doubles(1, 1, 10).findFirst().getAsDouble() * getDefenceMod();
    }

    /**
     * Allocates a new Character object using the given image.
     * This constructor is meant to be used for non-player characters(NPC).
     *
     * @param image Image preferably a PNG format
     */
    public Character(Image image, String name) {
        super(image);
        this.name.set(name);
        health.set(100);
        trueAge.set(21);
        physAge.set(21);
        lift.set(4);
        powerLevel.set(numbGenerator.doubles(1, 1, Double.MAX_VALUE).findFirst().getAsDouble());
    }


    public String getName() {
        return name.get();
    }


    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getHealth() {
        return health.get();
    }

    public DoubleProperty healthProperty() {
        return health;
    }

    public void setHealth(double health) {
        this.health.set(health);
    }

    public double getTrueAge() {
        return trueAge.get();
    }

    public DoubleProperty trueAgeProperty() {
        return trueAge;
    }

    public void setTrueAge(double trueAge) {
        this.trueAge.set(trueAge);
    }

    public double getPhysAge() {
        return physAge.get();
    }

    public DoubleProperty physAgeProperty() {
        return physAge;
    }

    public void setPhysAge(double physAge) {
        this.physAge.set(physAge);
    }

    public double getLift() {
        return lift.get();
    }

    public DoubleProperty liftProperty() {
        return lift;
    }

    public void setLift(double lift) {
        this.lift.set(lift);
    }

    public double getPowerLevel() {
        return powerLevel.get();
    }

    public DoubleProperty powerLevelProperty() {
        return powerLevel;
    }

    public void setPowerLevel(double powerLevel) {
        this.powerLevel.set(powerLevel);
    }

    public double getEnergyReserves() {
        return energyReserves;
    }

    public void setEnergyReserves(double energyReserves) {
        this.energyReserves = energyReserves;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getEndurance() {
        return endurance;
    }

    public void setEndurance(double endurance) {
        this.endurance = endurance;
    }

    public double getForce() {
        return force;
    }

    public void setForce(double force) {
        this.force = force;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

    public double getOffence() {
        return offence;
    }

    public void setOffence(double offence) {
        this.offence = offence;
    }

    public double getDefence() {
        return defence;
    }

    public void setDefence(double defence) {
        this.defence = defence;
    }

    public double getRegeneration() {
        return regeneration;
    }

    public void setRegeneration(double regeneration) {
        this.regeneration = regeneration;
    }

    public double getRecovery() {
        return recovery;
    }

    public void setRecovery(double recovery) {
        this.recovery = recovery;
    }

    public double getEnergyMod() {
        return energyMod;
    }

    public void setEnergyMod(double energyMod) {
        this.energyMod = energyMod;
    }

    public double getStrengthMod() {
        return strengthMod;
    }

    public void setStrengthMod(double strengthMod) {
        this.strengthMod = strengthMod;
    }

    public double getEnduranceMod() {
        return enduranceMod;
    }

    public void setEnduranceMod(double enduranceMod) {
        this.enduranceMod = enduranceMod;
    }

    public double getPowerLevelMod() {
        return powerLevelMod;
    }

    public void setPowerLevelMod(double powerLevelMod) {
        this.powerLevelMod = powerLevelMod;
    }

    public double getForceMod() {
        return forceMod;
    }

    public void setForceMod(double forceMod) {
        this.forceMod = forceMod;
    }

    public double getSpeedMod() {
        return speedMod;
    }

    public void setSpeedMod(double speedMod) {
        this.speedMod = speedMod;
    }

    public double getResistanceMod() {
        return resistanceMod;
    }

    public void setResistanceMod(double resistanceMod) {
        this.resistanceMod = resistanceMod;
    }

    public double getOffenceMod() {
        return offenceMod;
    }

    public void setOffenceMod(double offenceMod) {
        this.offenceMod = offenceMod;
    }

    public double getDefenceMod() {
        return defenceMod;
    }

    public void setDefenceMod(double defenceMod) {
        this.defenceMod = defenceMod;
    }

    public double getRecovRatio() {
        return recovRatio;
    }

    public void setRecovRatio(double recovRatio) {
        this.recovRatio = recovRatio;
    }

    public double getRegenRatio() {
        return regenRatio;
    }

    public void setRegenRatio(double regenRatio) {
        this.regenRatio = regenRatio;
    }

    public Timer getRegenerationTimer() {
        return regenerationTimer;
    }

    public void setRegenerationTimer(Timer regenerationTimer) {
        this.regenerationTimer = regenerationTimer;
    }

    public List<Property<?>> getPropertyList() {
        return propertyList;
    }

    /**
     * Prompts the user with a dialog what they want to say in localChat
     *
     * @param localChatBox The chat box that will contain the output
     */
    public void say(TextFlow localChatBox) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setGraphic(null);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setHeaderText(null);
        dialog.setTitle("Say");
        // The Java 8 way to get the response value (with lambda expression).
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(output -> {
            Boolean notWhitespace = false;
            for (char section : output.toCharArray()) {
                notWhitespace = section != ' ';
            }

            if (!output.isEmpty() && notWhitespace) {
                Text name = new Text("- " + nameProperty().get());
                name.getStyleClass().add("playerName");
                localChatBox.getChildren().add(name);
                Text trueOutput = new Text(": '" + output + "'\n");
                trueOutput.setFill(Color.WHITE);
                localChatBox.getChildren().add(trueOutput);
            }
        });
    }

    /**
     * Prompts the user with a dialog what they want to roleplay in localChat
     *
     * @param localChatBox The chat box that will contain the output
     */
    public void roleplay(TextFlow localChatBox) {
        TextInputDialog dialog = new TextInputDialog();
        Font roleplayFont = Font.font("System", FontWeight.BOLD, 14);
        dialog.setGraphic(null);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setHeaderText("Roleplay:");
        dialog.setTitle("Roleplay");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        TextArea content = new TextArea();
        content.setWrapText(true);
        grid.add(content, 0, 0);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(content::requestFocus);

        // The Java 8 way to get the response value (with lambda expression).
        dialog.showAndWait();
        Optional<String> result = Optional.ofNullable(content.getText());
        result.ifPresent(output -> {
            output = output.trim().replace("\n", " ");
            if (!output.isEmpty()) {
                Text start = new Text("* ");
                start.setFont(roleplayFont);
                start.setFill(Color.WHITE);
                Text name = new Text(nameProperty().get());
                name.setFont(roleplayFont);
                name.setFill(Color.YELLOW);
                localChatBox.getChildren().addAll(start, name);
                Text trueOutput = new Text(String.format(" %s *%n", output));
                trueOutput.setFont(roleplayFont);
                trueOutput.setFill(Color.WHITE);
                localChatBox.getChildren().add(trueOutput);
            }
        });
    }

    /**
     * Prompts the user with a dialog what they want to roleplay in localChat
     *
     * @param globalChatBox The chat box that will contain the output
     */
    public void ooc(TextFlow globalChatBox) {
        Font oocFont = Font.font("System", FontWeight.BOLD, 14);
        TextInputDialog dialog = new TextInputDialog();
        dialog.setGraphic(null);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setHeaderText(null);
        dialog.setTitle("OOC");
        // The Java 8 way to get the response value (with lambda expression).
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(output -> {
            Boolean notWhitespace = false;
            for (char section : output.toCharArray()) {
                notWhitespace = section != ' ';
            }

            if (!output.isEmpty() && notWhitespace) {
                Text name = new Text("(OOC) " + nameProperty().get());
                name.setFill(Color.YELLOW);
                name.setFont(oocFont);
                Text trueOutput = new Text(": " + output + "\n");
                trueOutput.setFont(oocFont);
                trueOutput.setFill(Color.WHITE);
                globalChatBox.getChildren().addAll(name, trueOutput);
            }
        });
    }

    /**
     * Get stats visible to the user
     *
     * @return List containing Text objects containing the visible user's stats
     */
    public List<Text> getStats() {
        List<Text> stats = Arrays.asList(
                new Text("Name: " + getName() + "\n"),
                new Text("Location: (" + String.valueOf((int) getX()) + ", " + String.valueOf((int) getY()) + ")\n"),
                new Text("Health: " + String.valueOf(getHealth()) + "\n"),
                new Text("Power Level: " + String.valueOf(getPowerLevel()) + "\n"),
                new Text("Physical Age: (" + String.valueOf(getPhysAge()) + ") True Age: (" + String.valueOf(getTrueAge()) + ")\n"),
                new Text("Lift: " + String.valueOf(getLift()) + "KG\n"));
        for (Text stat : stats) {
            stat.setFill(Color.WHITE);
        }

        return stats;
    }

    /**
     * Hash passwords
     *
     * @param password Character password
     * @return Hashed password
     */
    public String hashPassword(String password) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] sha512sum = new byte[0];
        if (messageDigest != null) {
            sha512sum = messageDigest.digest(password.getBytes());
        } else {
            Logger.getGlobal().log(Level.SEVERE, "Hashing of password returned null!");
        }
        return String.format("%032X", new BigInteger(1, sha512sum));
    }

    /**
     * ChangeListener for intersection of bounds by specified variables.
     *
     * @param source Source of call
     * @param object Possible collision target
     * @param step   Step size for movement
     * @return ChangeListener for X & Y-Axis displacement of source and target. Specified for collision handling.
     */
    public ChangeListener<Number> createPosChangeListener(Sprite source, Object object, double step, double trueHeight, double trueWidth) {
        Sprite target;
        if (object instanceof Sprite) {
            target = (Sprite) object;

            ChangeListener<Number> checkIntersection = (ob, n, n1) -> {
                if (source.getBoundsInParent().intersects(target.getBoundsInParent())) {
                    if (source.getX() == target.getX() && source.getY() > target.getY()) {
                        if (source.getY() + step > trueHeight) {
                            source.setLocation(source.getX(), 25);
                        } else {
                            source.setY(source.getY() + step);
                        }
                    } else if (source.getX() == target.getX() && source.getY() < target.getY()) {
                        if (source.getY() - step < 25) {
                            source.setLocation(source.getX(), trueHeight);
                        } else {
                            source.setY(source.getY() - step);
                        }
                    } else if (source.getY() == target.getY() && source.getX() > target.getX()) {
                        if (source.getX() + step > trueWidth) {
                            source.setLocation(25, source.getY());
                        } else {
                            source.setX(source.getX() + step);
                        }
                    } else if (source.getY() == target.getY() && source.getX() < target.getX()) {
                        if (source.getX() - step < 25) {
                            source.setLocation(trueWidth, source.getY());
                        } else {
                            source.setX(source.getX() - step);
                        }
                    }
                }
            };

            return checkIntersection;
        } else {
            throw new IllegalArgumentException("Object given is not of sprite instance.");
        }
    }

}

