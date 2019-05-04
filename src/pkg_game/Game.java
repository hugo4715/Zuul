package pkg_game;

import java.io.*;
import java.util.Arrays;

/**
 * A class with all the logic to make our game run
 */
public class Game{
    private static final byte[] MAGIC = new byte[]{0xA,0x3,0x8,0xB,0x8,0xC};
    private static Game game;

    private UserInterface userInterface;
    private GameEngine gameEngine;

    /**
     * Create and initialize a new game
     */
    public Game() {
        this.gameEngine = new GameEngine();
        this.userInterface = new UserInterface(gameEngine);
        gameEngine.setGUI(userInterface);
    }

    /**
     * Load the given save file
     * @throws IOException If the save cannot be load (eg: file does not exists, invalid or corrupt file, etc)
     */
    public void load(File file) throws IOException{
        try(FileInputStream fis = new FileInputStream(file);ObjectInputStream ois = new ObjectInputStream(fis)){
            byte[] fileMagic = new byte[MAGIC.length];
            ois.read(fileMagic);
            if(!Arrays.equals(MAGIC,fileMagic)){
                throw new IOException("This file is not a game file!");
            }
            userInterface.clearText();
            gameEngine = (GameEngine) ois.readObject();
            userInterface.setEngine(gameEngine);
            gameEngine.setGUI(userInterface);
        } catch (ClassNotFoundException e) {
            throw new IOException("Invalid game save (it may come from a previous game version and thus cannot be loaded.");
        }
    }

    /**
     * Save the game to the specified file
     * @param file The file to save the game to
     * @throws IOException If the game can't be saved, reason may include an invalid or unwritable file
     */
    public void save(File file) throws IOException {
        file.getParentFile().mkdirs();//create necessary directories

        //serialize the game to a file
        try(FileOutputStream fis = new FileOutputStream(file);ObjectOutputStream oos = new ObjectOutputStream(fis)){
            oos.write(MAGIC);//write the magic bytes, so we know this file is indeed a save file
            oos.writeObject(gameEngine);
        }
    }

    public static void main(String[] args) {
        game = new Game();
    }

    public static Game getGame() {
        return game;
    }
}
