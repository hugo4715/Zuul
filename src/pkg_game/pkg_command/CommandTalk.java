package pkg_game.pkg_command;

import pkg_game.pkg_entity.Entity;
import pkg_game.pkg_entity.EntityTalkable;
import pkg_game.GameEngine;
import pkg_game.Player;

import java.util.Optional;

public class CommandTalk extends Command {
    protected CommandTalk(final GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(final Player player) {
        if(!hasSecondWord()){
            gui.println("Talk to who?");
            return;
        }

        Optional<Entity> entityOptional = engine.getEntities().stream()
                .filter(entity -> entity instanceof EntityTalkable)
                .filter(entity -> entity.getRoom().equals(player.getCurrentRoom()))
                .filter(entity -> entity.getName().equalsIgnoreCase(getSecondWord()))
                .findAny();

        if(entityOptional.isPresent()){
            Entity entity = entityOptional.get();
            gui.println(entity.getName() + ": " + ((EntityTalkable)entity).getText());
        }else{
            gui.println(getSecondWord() + " isn't in this room.");
        }
    }
}
