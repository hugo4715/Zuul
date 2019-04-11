
/**
 * A class representing a command
 */
public class Command {
    private String commandWord;
    private String secondWord;

    /**
     * Create a new command
     *
     * @param commandWord The first command word
     * @param secondWord  The second command word
     */
    public Command(final String commandWord, final String secondWord) {
        this.commandWord = commandWord;
        this.secondWord = secondWord;
    }

    /**
     * Get the first command word
     *
     * @return The first Command word
     */
    public String getCommandWord() {
        return this.commandWord;
    }

    /**
     * Get the second command word
     *
     * @return The second Command word
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

    /**
     * Get if the command is unknown
     *
     * @return true if the command is unknown (no first word)
     */
    public boolean isUnknown() {
        return this.commandWord == null;
    }
}
