package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.Player;

import java.io.File;
import java.io.IOException;

public class CommandLoad extends Command {
    protected CommandLoad(final GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(final Player player) {
        if(!hasSecondWord()){
            gui.println("Please specify the name of the save.");
            return;
        }
        File file = new File("saves" + File.separator + getSecondWord() + ".save");
        try {
            engine.load(file);
            gui.println("Loaded game successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            gui.println("Could not load file: " + e.getMessage());
        }
    }
}
