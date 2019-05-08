package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.Item;
import pkg_game.Player;

public class CommandDrop extends Command {
    protected CommandDrop(final GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(final Player player) {
        if (hasSecondWord()) {
            Item chosen = player.getItems().getItem(getSecondWord());
            if (chosen != null) {
                player.drop(chosen);
                gui.println("You dropped " + chosen.getName());
            } else {
                gui.println("You don't have " + getSecondWord());
            }
        } else {
            if (player.getItems().isEmpty()) {
                gui.println("You don't have anything to drop.");
            } else {
                gui.println("Drop what? " + player.getItems().getMessage());
            }

        }
    }
}
