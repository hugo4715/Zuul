package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.Player;
import pkg_game.pkg_command.Command;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CommandTest extends Command {
    protected CommandTest(GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(Player player) {
        if (hasSecondWord()) {

            //search for the specified file
            File file = new File(getSecondWord() + ".txt");

            if (file.exists()) {
                try {
                    engine.setTesting(true);
                    //read all lines and execute commands
                    Files.readAllLines(file.toPath())
                            .stream().filter(line -> !line.startsWith("#"))
                            .forEach(engine::interpretCommand);
                } catch (IOException e) {
                    e.printStackTrace();
                    gui.println("Could not read " + file.getName() + "!");
                }
                engine.setTesting(false);
            } else {
                gui.println("Unknown file " + file.getName());
            }

        } else {
            gui.println("Please provide a file name!");
        }
    }
}
