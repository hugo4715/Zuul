package pkg_game.pkg_command;

import pkg_game.pkg_room.Door;
import pkg_game.GameEngine;
import pkg_game.Player;
import pkg_game.pkg_room.Room;

public class CommandGo extends Command {

    protected CommandGo(GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(Player player) {
        if (!hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            gui.println("Go where?");
            return;
        }

        String direction = getSecondWord();

        Room currentRoom = player.getCurrentRoom();
        // Try to leave current room.
        Door door = currentRoom.getExit(direction);

        if (door == null)
            gui.println("There is no door!");
        else {

            if (door.isLocked()) {
                if (door.getKey() == null) {
                    gui.println("This door is locked.");
                } else {
                    if (player.getItems().contains(door.getKey())) {
                        door.setLocked(false);
                        gui.println("You unlocked the door using " + door.getKey().getName());
                    } else {
                        gui.println("This door is locked, you need '" + door.getKey().getName() + "' to open it.");
                    }
                }
                return;
            }

            //get the room at the other side of this door
            Room nextRoom = door.getOtherSide(player.getCurrentRoom());

            if (nextRoom == null) {
                gui.println("This door cannot be openned from this side!");
                return;
            }

            player.goRoom(nextRoom);
            engine.printLocationInfo();

            if (nextRoom.getImageName() != null) {
                gui.showImage(nextRoom.getImageName());
            }
        }
    }
}
