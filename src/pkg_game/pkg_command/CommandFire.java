package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.Item;
import pkg_game.ItemBeamer;
import pkg_game.Player;
import pkg_game.pkg_room.Room;

public class CommandFire extends Command {
    protected CommandFire(final GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(final Player player) {
        if (!hasSecondWord()) {
            gui.println("Fire what?");
            return;
        }

        Item item = player.getItems().getItem(getSecondWord());
        if (item != null) {

            if (item instanceof ItemBeamer) {
                ItemBeamer beamer = (ItemBeamer)item;

                Room target = beamer.getRoom();
                if (target != null) {
                    player.setCurrentRoom(beamer.getRoom());
                    beamer.setRoom(null);
                    gui.println("You just teleported to another room!");
                    engine.printLocationInfo();
                } else {
                    gui.println("You beamer isn't charged yet");
                }
            } else {
                gui.println("You can't fire " + item.getName());
            }
        } else {
            gui.println("You don't have " + getSecondWord());
        }
    }
}
