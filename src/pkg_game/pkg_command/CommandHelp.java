package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.Player;

public class CommandHelp extends Command {
    protected CommandHelp(final GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(final Player player) {
        engine.printHelp();
    }
}
