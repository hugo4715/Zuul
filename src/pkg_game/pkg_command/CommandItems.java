package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.Player;

public class CommandItems extends Command {
    protected CommandItems(GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(Player player) {
        if (!player.getItems().isEmpty()) {
            StringBuilder sb = new StringBuilder("Your items: ");
            sb.append(player.getItems().getMessage());
            sb.append("\nTotal weight: ");
            sb.append(player.countWeight());
            sb.append("/");
            sb.append(player.getMaxWeight());
            gui.println(sb.toString());
        } else {
            gui.println("You don't have any items.");
        }
    }
}
