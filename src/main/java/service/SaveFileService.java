package service;

import model.Character;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveFileService {

    private static final String LOGGERNAME = SaveFileService.class.getName();
    private static final String SAVEPATHBASE = System.getProperty("user.home") + "/.legacy/";

    public static void createSaveFile(Character character) {
        File saveFile = new File(SAVEPATHBASE + character.getName());

        if (!saveFile.exists()){
            try {
                if (new File(SAVEPATHBASE).mkdir()){
                    saveFile.createNewFile();
                    Logger.getLogger(LOGGERNAME).log(Level.CONFIG, "Created new save directory.");
                } else {
                    Logger.getLogger(LOGGERNAME).log(Level.WARNING, "Failed to create save directory");
                }
            } catch (NullPointerException e){
                Logger.getLogger(LOGGERNAME).log(Level.WARNING, "Something went wrong while saving: " + e.getMessage());
            } catch (IOException e) {
                Logger.getLogger(LOGGERNAME).log(Level.WARNING, "Failed to create save file: " + e.getMessage());
            }
        }

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(SAVEPATHBASE + character.getName()))) {

            oos.writeObject(character);

            Logger.getLogger(LOGGERNAME).log(Level.INFO, "Save written for " + character.getName() + ".");

        } catch (IOException e) {
            Logger.getLogger(LOGGERNAME).log(Level.WARNING, "Failed to write save for " + character.getName() + ": " + e.getMessage());
        }
    }

    public static Character readSaveFile(Character character) {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(SAVEPATHBASE + character.getName()))) {

            character = (Character) ois.readObject();

            Logger.getLogger(LOGGERNAME).log(Level.INFO, "Save read for " + character.getName() + ".");

            return character;
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(LOGGERNAME).log(Level.WARNING, "Failed to read save for " + character.getName() + ": " + e.getMessage());
        }

        return character;
    }
}
