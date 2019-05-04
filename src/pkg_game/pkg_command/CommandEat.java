package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.Item;
import pkg_game.Player;
import pkg_game.pkg_command.Command;

public class CommandEat extends Command {
    protected CommandEat(GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(Player player) {
        if (hasSecondWord()) {
            Item chosen = player.getItems().getItem(getSecondWord());

            if (chosen != null) {

                //i'm using a switch here because we will need to add other eatable items
                switch (chosen.getName()) {
                    case "MagicCookie":
                        player.increaseMaxWeight(10);
                        player.getItems().removeItem(chosen);
                        gui.println("You ate a Magic Cookie! You are feeling a lot stronger now.");
                        break;
                    default:
                        gui.println("you can't eat " + chosen.getName());
                }
            } else {
                gui.println("You don't have " + getSecondWord());
            }
        } else {
            gui.println("What do you want to eat?");
        }
    }
}
