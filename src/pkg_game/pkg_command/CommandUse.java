package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.Item;
import pkg_game.ItemBeamer;
import pkg_game.Player;

public class CommandUse extends Command {
    protected CommandUse(final GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(final Player player) {
        if (!hasSecondWord()) {
            engine.getGui().println("Use what?");
            return;
        }

        Item item = player.getItems().getItem(getSecondWord());
        if (item != null) {
            if(item instanceof ItemBeamer){
                ItemBeamer beamer = (ItemBeamer)item;
                beamer.setRoom(player.getCurrentRoom());
                gui.println("Your beamer is charged");
            }else{
                gui.println("You can't use " + item.getName());
            }
        } else {
            engine.getGui().println("You don't have " + getSecondWord());
        }

    }
}
