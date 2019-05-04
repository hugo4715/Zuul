package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.Item;
import pkg_game.Player;

public class CommandTake extends Command {

    protected CommandTake(GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(Player player) {
        if (hasSecondWord()) {

            //search the specified item
            Item chosen = player.getCurrentRoom().getItems().getItem(getSecondWord());

            if (chosen != null) {
                if (player.canTake(chosen)) {
                    player.takeItem(chosen);
                    gui.println("You picked up a " + chosen.getName());
                } else {
                    gui.println("It's too heavy! You may need to drop some items.");
                }

            } else {
                gui.println("'" + getSecondWord() + "' is not an item name!");
            }
        } else {
            gui.println("Take what?");
        }
    }
}
