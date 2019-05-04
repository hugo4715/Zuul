package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.Item;
import pkg_game.Player;
import pkg_game.pkg_room.Room;

public class CommandFire extends Command {
    protected CommandFire(GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(Player player) {
        if(!hasSecondWord()){
            gui.println("Fire what?");
            return;
        }

        Item item = player.getItems().getItem(getSecondWord());
        if(item != null){
            //it's a switch so we can add more firable stuff later
            switch (item.getName()){
                case "Beamer":
                    Room target = player.getBeamerTarget();
                    if(target != null){
                        player.setCurrentRoom(player.getBeamerTarget());
                        player.setBeamerTarget(null);
                        gui.println("You just teleported to another room!");
                        engine.printLocationInfo();
                    }else{
                        gui.println("You beamer isn't charged yet");
                    }

                    break;
                default:
                    gui.println("You can't fire " + item.getName());
            }
        }else{
            gui.println("You don't have " + getSecondWord());
        }
    }
}
