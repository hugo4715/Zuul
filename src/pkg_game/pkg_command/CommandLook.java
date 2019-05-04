package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.Player;
import pkg_game.pkg_command.Command;

public class CommandLook extends Command {
    protected CommandLook(GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(Player player) {
        engine.printLocationInfo();
    }
}
