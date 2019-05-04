package pkg_game.pkg_command;

import pkg_game.GameEngine;
import pkg_game.Player;
import pkg_game.UserInterface;

/**
 * A class representing a command
 */
public abstract class Command {
    protected final GameEngine engine;
    protected UserInterface gui;

    private String secondWord;

    protected Command(GameEngine engine) {
        this.engine = engine;
    }

    public void setGui(UserInterface gui) {
        this.gui = gui;
    }

    public void setSecondWord(String secondWord) {
        this.secondWord = secondWord;
    }

    /**
     * Get the second command word
     *
     * @return The second pkg_game.pkg_command.Command word
     */
    public String getSecondWord() {
        return this.secondWord;
    }

    /**
     * Get if the command has a second word
     *
     * @return true if the command has a second word
     */
    public boolean hasSecondWord() {
        return this.secondWord != null;
    }

    public abstract void execute(final Player player);
}
