package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.Player;

public class CommandAlea extends Command {
    protected CommandAlea(GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(Player player) {
        if(engine.isTesting()){

            long seed = System.currentTimeMillis();//random value
            if(hasSecondWord()){
                try {
                    seed = Long.parseLong(getSecondWord());
                }catch (NumberFormatException e){
                    gui.println(getSecondWord() + " is not a number");
                }
            }

            engine.setRandomSeed(seed);
            gui.println("Updated seed to " + seed);
        }else{
            gui.println("This command can only be used in test files!");
        }
    }
}
