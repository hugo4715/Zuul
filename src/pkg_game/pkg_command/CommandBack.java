package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.Player;
import pkg_game.pkg_room.Room;

public class CommandBack extends Command {
    protected CommandBack(final GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(final Player player) {
        if (!player.goBack()) {
            gui.println("You can't go back");
        } else {
            Room currentRoom = player.getCurrentRoom();
            engine.tickEntities();
            gui.println(currentRoom.getLongDescription());
            if (currentRoom.getImageName() != null)
                gui.showImage(currentRoom.getImageName());
        }
    }
}
