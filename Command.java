 
/**
 * A class representing a command
 */
public class Command
{
    private String aCommandWord;
    private String aSecondWord;

    /**
     * Create a new command
     * @param pCommandWord The first command word
     * @param pSecondWord The second command word
     */
    public Command(final String pCommandWord, final String pSecondWord) {
        this.aCommandWord = pCommandWord;
        this.aSecondWord = pSecondWord;
    }

    /**
     * Get the first command word
     * @return The first Command word
     */
    public String getCommandWord() {
        return this.aCommandWord;
    }

    /**
     * Get the second command word
     * @return The second Command word
     */
    public String getSecondWord() {
        return this.aSecondWord;
    }

    /**
     * Get if the command has a second word
     * @return true if the command has a second word
     */
    public boolean hasSecondWord(){
        return this.aSecondWord != null;
    }
    
    /**
     * Get if the command is unknown
     * @return true if the command is unknown (no first word)
     */
    public boolean isUnknown(){
        return this.aCommandWord == null;
    }
} // Command
